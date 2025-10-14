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
    private DcMotor Intake;
    private DcMotor Shooter;
    private DcMotor HighIntake;
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
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        HighIntake = hardwareMap.get(DcMotor.class, "High Intake");
        Shooter = hardwareMap.get(DcMotor.class, "Shooter");

        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.FORWARD);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        telemetry.addData("Left Stick X",gamepad1.left_stick_x);
        telemetry.addData("Right Stick X",gamepad1.right_stick_y);
        telemetry.addData("Right Stick Y",gamepad1.right_stick_y);
        telemetry.update();
        y = gamepad1.right_stick_y * 1*0.5;
        x = -gamepad1.left_stick_x * 1.1 * -0.5;
        rx = gamepad1.right_stick_x * 0.6 * -0.8;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);
        Intake.setPower(gamepad1.right_trigger * 0.6);
        Shooter.setPower(gamepad1.left_trigger * 0.6);
        if (gamepad1.a){
            HighIntake.setPower(0.6);
        }

    }
}
