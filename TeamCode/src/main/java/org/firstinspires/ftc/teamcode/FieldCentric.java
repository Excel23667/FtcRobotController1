package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@TeleOp
@Disabled
public class FieldCentric extends OpMode {
    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor BackLeft;
    private DcMotor FrontRight;
    double y;
    double x;
    double rx;
    double denominator;
    // Declare IMU
    BNO055IMU imu;

    @Override
    public void init() {

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS; // Or DEGREES if preferred
        // Configure other parameters based on your Rev Hub orientation
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new org.firstinspires.ftc.robotcore.external.navigation.Position(), new org.firstinspires.ftc.robotcore.external.navigation.Velocity(), 1000); // Optional, for position tracking
        imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS); // Get initial heading

        FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        BackRight = hardwareMap.get(DcMotor.class, "Back Right");
        BackLeft = hardwareMap.get(DcMotor.class, "Back Left");
        FrontRight = hardwareMap.get(DcMotor.class, "Front Right");

        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.FORWARD);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        y = -gamepad1.right_stick_y * 1*0.75;
        x = -gamepad1.left_stick_x * 1.1 * -0.75;
        rx = gamepad1.right_stick_x * 0.6 * -0.9;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);
    }
}
