package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

import java.util.Objects;
@Disabled
@Autonomous(name = "RedAuto")
public class LeagueMeet1AutoFront extends LinearOpMode {

    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor BackLeft;
    private DcMotor FrontRight;
    private DcMotor Spindex;
    private Servo Piston;
    private DcMotor Shooter;
    double y;
    double x;
    double rx;
    double denominator;
    int ticksCount = 0;

    @Override
    public void runOpMode() {
        FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        BackRight = hardwareMap.get(DcMotor.class, "Back Right");
        BackLeft = hardwareMap.get(DcMotor.class, "Back Left");
        FrontRight = hardwareMap.get(DcMotor.class, "Front Right");
        Shooter = hardwareMap.get(DcMotor.class, "Shooter");
        Spindex = hardwareMap.get(DcMotor.class, "Spindex");
        Piston = hardwareMap.get(Servo.class, "Piston");

        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        Spindex.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Piston.setPosition(0.1);

        waitForStart();

        y = 0.4;
        x = 0;
        rx =0;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);
        sleep(1400);
        y = 0;
        x = 0;
        rx =0;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);

        Shooter.setPower(0.57);
        sleep(5000);
        ticksCount = 1500;
        Spindex.setTargetPosition(ticksCount);
        Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Spindex.setPower(1);
        while (Spindex.isBusy()){
        }
        Spindex.setPower(0);
        Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Piston.setPosition(1.0);
        sleep(200);
        Piston.setPosition(0.1);
        Shooter.setPower(1);
        sleep(200);
        Shooter.setPower(0.57);

        sleep(5000);
        ticksCount = 2500;
        Spindex.setTargetPosition(ticksCount);
        Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Spindex.setPower(1);
        while (Spindex.isBusy()){
        }
        Spindex.setPower(0);
        Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Piston.setPosition(1.0);
        sleep(200);
        Piston.setPosition(0.1);
        Shooter.setPower(1);
        sleep(200);
        Shooter.setPower(0.57);

        sleep(5000);
        ticksCount = 500;
        Spindex.setTargetPosition(ticksCount);
        Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Spindex.setPower(1);
        while (Spindex.isBusy()){
        }
        Spindex.setPower(0);
        Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Piston.setPosition(1.0);
        sleep(200);
        Piston.setPosition(0.1);

        Shooter.setPower(0);
        ticksCount = 0;
        Spindex.setTargetPosition(ticksCount);
        Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Spindex.setPower(1);
        while (Spindex.isBusy()){
        }
        Spindex.setPower(0);
        Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        y = 0;
        x = 0.75;
        rx =0;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);
        sleep(1000);
        y = 0;
        x = 0;
        rx =0;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);

    }

}
