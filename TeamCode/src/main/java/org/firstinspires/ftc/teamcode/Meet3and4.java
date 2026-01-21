package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp
public class Meet3and4 extends LinearOpMode {

    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor BackLeft;
    private DcMotor FrontRight;
    double y;
    double x;
    double rx;
    double denominator;

    @Override
    public void runOpMode() throws InterruptedException {

        FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        BackRight = hardwareMap.get(DcMotor.class, "Back Right");
        BackLeft = hardwareMap.get(DcMotor.class, "Back Left");
        FrontRight = hardwareMap.get(DcMotor.class, "Front Right");

        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        waitForStart();



        while (opModeIsActive()){
            if (gamepad1.left_bumper){
                y = -gamepad1.right_stick_y * 1*0.75*0.5;
                x = gamepad1.right_stick_x * 1.1 * -0.75*0.5;
                rx = -gamepad1.left_stick_x * 0.6 * -0.9*0.7;
            }else {
                y = -gamepad1.right_stick_y * 1 * 0.75;
                x = gamepad1.right_stick_x * 1.1 * -0.75;
                rx = -gamepad1.left_stick_x * 0.6 * -0.9;
            }
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
            BackLeft.setPower((y + x + rx) / denominator);
            FrontLeft.setPower(((y - x) + rx) / denominator);
            BackRight.setPower(((y - x) - rx) / denominator);
            FrontRight.setPower(((y + x) - rx) / denominator);

            telemetry.addData("Left Stick X",gamepad1.left_stick_x);
            telemetry.addData("Right Stick X",gamepad1.right_stick_y);
            telemetry.addData("Right Stick Y",gamepad1.right_stick_y);
            telemetry.update();
        }

    }
}
