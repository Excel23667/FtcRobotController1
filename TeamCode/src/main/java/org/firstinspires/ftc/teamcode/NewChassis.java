package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

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
    private Servo Piston;
    double y;
    double x;
    double rx;
    double denominator;
    boolean intakeFront;


    @Override
    public void init() {

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

        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeFront = true;
        Piston.setPosition(1.0);
    }

    @Override
    public void loop() {
        telemetry.addData("Left Stick X",gamepad1.left_stick_x);
        telemetry.addData("Right Stick X",gamepad1.right_stick_y);
        telemetry.addData("Right Stick Y",gamepad1.right_stick_y);
        telemetry.addData("Intake is front?",intakeFront);
        telemetry.update();
        if (intakeFront){
            y = gamepad1.right_stick_y * 1*0.75;
            x = -gamepad1.left_stick_x * 1.1 * -0.75;
            rx = gamepad1.right_stick_x * 0.6 * -0.9;
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
            BackLeft.setPower((y + x + rx) / denominator);
            FrontLeft.setPower(((y - x) + rx) / denominator);
            BackRight.setPower(((y - x) - rx) / denominator);
            FrontRight.setPower(((y + x) - rx) / denominator);
        } else if (!intakeFront) {
            y = -gamepad1.right_stick_y * 1*0.75;
            x = -gamepad1.left_stick_x * 1.1 * -0.75;
            rx = -gamepad1.right_stick_x * 0.6 * -0.9;
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
            BackLeft.setPower((y + x + rx) / denominator);
            FrontLeft.setPower(((y - x) + rx) / denominator);
            BackRight.setPower(((y - x) - rx) / denominator);
            FrontRight.setPower(((y + x) - rx) / denominator);
        }
        if (gamepad1.y){
            if (intakeFront){
                intakeFront = false;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);

                }
            } else if (!intakeFront) {
                intakeFront = true;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);

                }
            }
        }
        Intake.setPower(gamepad1.right_trigger * 0.6);
        HighIntake.setPower(gamepad1.left_trigger);
        if (gamepad1.a){
            Shooter.setPower( 0.57);
        }
        if (gamepad1.x){
            Shooter.setPower(0);
        }
        if (gamepad1.b){
            Piston.setPosition(0.4);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }
            Piston.setPosition(1.0);
            Shooter.setPower(1);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }
            Shooter.setPower(0.57);
        }
        if (gamepad1.right_bumper){
            Intake.setPower(-0.6);
            Intake.setPower(0);
        }
        if (gamepad1.left_bumper){
            HighIntake.setPower(-0.6);
            HighIntake.setPower(0);
        }
       /* if (gamepad1.b){
            Piston.setPower(-1);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }
            Piston.setPower(1);
            try {
                Thread.sleep(475);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }
            Piston.setPower(0);
        }*/
    }
}
