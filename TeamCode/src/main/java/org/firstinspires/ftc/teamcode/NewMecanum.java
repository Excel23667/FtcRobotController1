package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp
@Disabled
public class NewMecanum extends OpMode {

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
    public boolean yuh;

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


    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            if (!yuh) {
                yuh = true;
            } else if (yuh) {
                yuh = false;
            }
        }
        if(yuh){
            telemetry.addData("Function", "In B");
            Shooter.setPower(0.6 - (gamepad1.left_trigger * 0.6));
            telemetry.addData("Left Stick X", gamepad1.left_stick_x);
            telemetry.addData("Right Stick X", gamepad1.right_stick_y);
            telemetry.addData("Right Stick Y", gamepad1.right_stick_y);
            telemetry.update();
            y = -gamepad1.right_stick_y * 1;
            x = -gamepad1.right_stick_x * 1.1 * -1;
            rx = gamepad1.left_stick_x * 0.6 * 1;
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
            BackLeft.setPower((y + x + rx) / denominator);
            FrontLeft.setPower(((y - x) + rx) / denominator);
            BackRight.setPower(((y - x) - rx) / denominator);
            FrontRight.setPower(((y + x) - rx) / denominator);
            Intake.setPower(gamepad1.right_trigger * 0.75);
        } else if (!yuh) {
            Shooter.setPower(0);
            telemetry.addData("Left Stick X", gamepad1.left_stick_x);
            telemetry.addData("Right Stick X", gamepad1.right_stick_y);
            telemetry.addData("Right Stick Y", gamepad1.right_stick_y);
            telemetry.update();
            y = -gamepad1.right_stick_y * 1;
            x = -gamepad1.right_stick_x * 1.1 * -1;
            rx = gamepad1.left_stick_x * 0.6 * 1;
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
            BackLeft.setPower((y + x + rx) / denominator);
            FrontLeft.setPower(((y - x) + rx) / denominator);
            BackRight.setPower(((y - x) - rx) / denominator);
            FrontRight.setPower(((y + x) - rx) / denominator);
            Intake.setPower(gamepad1.right_trigger * 0.75);
        }
    }
}
        /*
        if (gamepad1.a){
            telemetry.addData("Function","In A");
            while (gamepad1.b == false){
                telemetry.addData("Function","In B");
                Shooter.setPower(0.6 - (gamepad1.left_trigger*0.6));
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
                Intake.setPower(gamepad1.right_trigger*0.75);
            }
            Shooter.setPower(0);
        }

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
        Intake.setPower(gamepad1.right_trigger*0.75);
        */

/* if (gamepad1.a){

        } else if (gamepad1.b) {
            Shooter.setPower(0);
        } */