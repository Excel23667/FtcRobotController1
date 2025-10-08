package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp

public class NewChassis extends OpMode {

    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor BackLeft;
    private DcMotor FrontRight;
    private DcMotor Shooter;
    private DcMotor Intake;
    double y;
    double x;
    double rx;
    double denominator;

    @Override
    public void init() {

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
        telemetry.addData("Left Stick X",gamepad1.left_stick_x);
        telemetry.addData("Right Stick X",gamepad1.right_stick_y);
        telemetry.addData("Right Stick Y",gamepad1.right_stick_y);
        telemetry.update();
        y = gamepad1.right_stick_y * 1*0.5;
        x = -gamepad1.right_stick_x * 1.1 * -0.5;
        rx = gamepad1.left_stick_x * 0.6 * 0.5;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);
    }
}
