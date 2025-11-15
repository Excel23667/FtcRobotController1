package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
@Disabled
public class FieldCentricGpt extends LinearOpMode {

    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor BackLeft;
    private DcMotor FrontRight;
    private IMU imu;

    @Override
    public void runOpMode() {
        // ---- Initialize hardware ----
        FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        FrontRight = hardwareMap.get(DcMotor.class, "Front Right");
        BackLeft = hardwareMap.get(DcMotor.class, "Back Left");
        BackRight = hardwareMap.get(DcMotor.class, "Back Right");

        // Motor directions (based on your setup)
        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.FORWARD);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        // ---- Initialize IMU ----
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );
        imu.initialize(parameters);

        // ---- Auto zero heading on init ----
        imu.resetYaw();

        telemetry.addData("Status", "Initialized — waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // ---- Read IMU heading ----
            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // ---- Read controller input ----
            double y = -gamepad1.right_stick_y * 0.75;        // forward/back
            double x = -gamepad1.left_stick_x * 1.1 * -0.75;  // strafe
            double rx = gamepad1.right_stick_x * 0.6 * -0.9;  // rotation

            // ---- Field-centric rotation math ----
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            // ---- Normalize powers ----
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1.0);

            double frontLeftPower  = (rotY - rotX + rx) / denominator;
            double backLeftPower   = (rotY + rotX + rx) / denominator;
            double frontRightPower = (rotY + rotX - rx) / denominator;
            double backRightPower  = (rotY - rotX - rx) / denominator;

            // ---- Set motor power ----
            FrontLeft.setPower(frontLeftPower);
            BackLeft.setPower(backLeftPower);
            FrontRight.setPower(frontRightPower);
            BackRight.setPower(backRightPower);

            // ---- Reset heading manually ----
            if (gamepad1.y) imu.resetYaw();

            // ---- Telemetry ----
            telemetry.addData("Heading (deg)", Math.toDegrees(botHeading));
            telemetry.addData("Heading", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.addData("FL", "%.2f", frontLeftPower);
            telemetry.addData("FR", "%.2f", frontRightPower);
            telemetry.addData("BL", "%.2f", backLeftPower);
            telemetry.addData("BR", "%.2f", backRightPower);
            telemetry.update();
        }
    }
}
