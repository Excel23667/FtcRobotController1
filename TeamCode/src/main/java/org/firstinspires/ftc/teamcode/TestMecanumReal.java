package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp

public class TestMecanumReal extends OpMode {

    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor BackLeft;
    private DcMotor FrontRight;
    private DcMotor Shooter;
    private DcMotor Intake;
    float y;
    double x;
    double rx;
    double denominator;

    @Override
    public void init() {

        FrontLeft = hardwareMap.get(DcMotor.class, "Front Right");
        BackRight = hardwareMap.get(DcMotor.class, "Front Left");
        BackLeft = hardwareMap.get(DcMotor.class, "Back Right");
        FrontRight = hardwareMap.get(DcMotor.class, "Back Left");
        Shooter = hardwareMap.get(DcMotor.class, "Shooter");
        Intake = hardwareMap.get(DcMotor.class, "Intake");

        FrontLeft.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.FORWARD);
        Shooter.setDirection(DcMotor.Direction.REVERSE);
        Intake.setDirection(DcMotor.Direction.REVERSE);

        //double minustrig =

    }

    @Override
    public void loop() {
        //double minustrig = gamepad1.left_trigger *0.6;

        telemetry.addData("Left Stick X",gamepad1.left_stick_x);
        telemetry.addData("Right Stick X",gamepad1.right_stick_y);
        telemetry.addData("Right Stick Y",gamepad1.right_stick_y);
        telemetry.update();
        y = -gamepad1.right_stick_y * 1;
        x = -gamepad1.right_stick_x * 1.1 * -1;
        rx = gamepad1.left_stick_x * 0.6 * 1;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);
        if (gamepad1.a){
            Shooter.setPower(0.6 - (gamepad1.left_trigger*0.6));
        } else if (gamepad1.b) {
            Shooter.setPower(0);
        }

        Intake.setPower(gamepad1.right_trigger*0.75);

    }
}
