package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp
public class NewChassisWithCameraGPT extends OpMode {

    private DcMotor FrontLeft, BackRight, BackLeft, FrontRight, Intake, Shooter, HighIntake;
    private Servo Piston;
    private boolean intakeFront = true;

    private static final boolean USE_WEBCAM = true;
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    @Override
    public void init() {
        // Initialize AprilTag
        initAprilTag();

        // Initialize hardware
        FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        BackRight = hardwareMap.get(DcMotor.class, "Back Right");
        BackLeft = hardwareMap.get(DcMotor.class, "Back Left");
        FrontRight = hardwareMap.get(DcMotor.class, "Front Right");
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        HighIntake = hardwareMap.get(DcMotor.class, "High Intake");
        Shooter = hardwareMap.get(DcMotor.class, "Shooter");
        Piston = hardwareMap.get(Servo.class, "Piston");

        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.FORWARD);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        Intake.setDirection(DcMotor.Direction.REVERSE);
        HighIntake.setDirection(DcMotor.Direction.REVERSE);
        Shooter.setDirection(DcMotor.Direction.REVERSE);

        for (DcMotor m : new DcMotor[]{FrontLeft, FrontRight, BackLeft, BackRight}) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        Piston.setPosition(1.0);
    }

    @Override
    public void loop() {
        // Vision streaming controls
        if (gamepad1.dpad_down) visionPortal.stopStreaming();
        if (gamepad1.dpad_up) visionPortal.resumeStreaming();

        // Toggle intake direction
        if (gamepad1.y) {
            intakeFront = !intakeFront;
            sleepNonBlocking(300);
        }

        // Mecanum drive math
        double y = (intakeFront ? 1 : -1) * gamepad1.right_stick_y * 0.75;
        double x =gamepad1.left_stick_x * 0.75;
        double rx =(intakeFront ? 1 : -1) * -gamepad1.right_stick_x * 0.6;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        FrontLeft.setPower((y - x + rx) / denominator);
        BackLeft.setPower((y + x + rx) / denominator);
        FrontRight.setPower((y + x - rx) / denominator);
        BackRight.setPower((y - x - rx) / denominator);

        // Intake / shooter controls
        Intake.setPower(gamepad1.right_trigger * 0.6);
        HighIntake.setPower(gamepad1.left_trigger);

        if (gamepad1.a) Shooter.setPower(0.57);
        if (gamepad1.x) Shooter.setPower(0);

        if (gamepad1.b) {
            Piston.setPosition(0.4);
            sleepNonBlocking(600);
            Piston.setPosition(1.0);
            Shooter.setPower(1);
            sleepNonBlocking(600);
            Shooter.setPower(0.57);
        }

        if (gamepad1.right_bumper) Intake.setPower(-0.6);
        if (gamepad1.left_bumper) HighIntake.setPower(-0.6);

        // Telemetry
        telemetryAprilTag();
        telemetry.addData("Intake Front?", intakeFront);
        telemetry.addData("Stick X/Y", "%f / %f", gamepad1.left_stick_x, gamepad1.right_stick_y);
        telemetry.update();
    }

    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder().build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        if (USE_WEBCAM)
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        else
            builder.setCamera(BuiltinCameraDirection.BACK);
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
    }

    private void telemetryAprilTag() {
        List<AprilTagDetection> detections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", detections.size());
        for (AprilTagDetection tag : detections) {
            if (tag.metadata != null) {
                telemetry.addLine(String.format("ID %d | %s", tag.id, tag.metadata.name));
                telemetry.addLine(String.format("XYZ (in) %.1f %.1f %.1f", tag.ftcPose.x, tag.ftcPose.y, tag.ftcPose.z));
            } else {
                telemetry.addLine(String.format("ID %d | Unknown", tag.id));
            }
        }
    }

    // Use a non-blocking delay
    private void sleepNonBlocking(long ms) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < ms) {
            // just yield
        }
    }
    @Override
    public void stop() {
        if (visionPortal != null) visionPortal.close();
    }
}
