package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
@Disabled
@TeleOp(name="turretTestYuh")
public class turretTestYuh extends OpMode {

    private CRServo turret;
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    // PID for controlling turret power
    private SimplePID pid = new SimplePID(0.5, 0.0, 0.002); // higher kP for high-gear

    // Virtual position to limit turret rotation (0–1)
    private double virtualPos = 0.5; // start centered
    private final double MIN_POS = 0.15;
    private final double MAX_POS = 0.85;

    @Override
    public void init() {

        turret = hardwareMap.get(CRServo.class, "turret");

        aprilTag = new AprilTagProcessor.Builder().build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")) // Logitech C270
                .addProcessor(aprilTag)
                .build();

        telemetry.addLine("Initialized!");
    }

    @Override
    public void loop() {

        AprilTagDetection tag = aprilTag.getDetections().isEmpty()
                ? null : aprilTag.getDetections().get(0);

        if (tag != null) {

            double error = tag.ftcPose.yaw;

            // PID output determines servo power
            double output = pid.update(error);

            // Scale output for high-gear servo
            output *= 0.08; // adjust for responsiveness

            // Update virtual position
            virtualPos += output;
            virtualPos = Range.clip(virtualPos, MIN_POS, MAX_POS);

            // Stop CRServo if hitting limits
            if ((virtualPos <= MIN_POS && output < 0) ||
                    (virtualPos >= MAX_POS && output > 0)) {
                output = 0;
            }

            turret.setPower(output);

            // Telemetry
            telemetry.addData("Tag Seen", true);
            telemetry.addData("Tag ID", tag.id);
            telemetry.addData("Tag Name", tag.metadata != null ? tag.metadata.name : "Unknown");
            telemetry.addData("Yaw Error (deg)", error);
            telemetry.addData("PID Output", output);
            telemetry.addData("Virtual Position", virtualPos);

        } else {
            turret.setPower(0);
            telemetry.addData("Tag Seen", false);
        }

        telemetry.update();
    }
}

// ----------------------------
// SIMPLE PID CLASS
// ----------------------------
class SimplePID {

    private double kP, kI, kD;
    private double integral = 0;
    private double lastError = 0;

    public SimplePID(double p, double i, double d) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
    }

    public double update(double error) {
        integral += error;
        double derivative = error - lastError;
        lastError = error;

        return (kP * error) + (kI * integral) + (kD * derivative);
    }
}
