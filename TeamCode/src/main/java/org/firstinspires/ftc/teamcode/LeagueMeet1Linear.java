package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import android.graphics.Color;
import com.qualcomm.robotcore.hardware.ColorSensor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

import java.util.Objects;

@TeleOp

public class LeagueMeet1Linear extends LinearOpMode {

    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor BackLeft;
    private DcMotor FrontRight;
    private DcMotor Intake;
    private DcMotor Shooter;
    private DcMotor Spindex;
    private Servo Piston;
   // private CRServo Turret;
    private ColorSensor color;
    private DigitalChannel limitSwitch;
    double y;
    double x;
    double rx;
    double denominator;
    private final float[] hsv = new float[3];
    private float hue = 0;
    String colorRead = "empty";
    int ticksCount = 0;
    String pos1 = "empty";
    String pos2 = "empty";
    String pos3 = "empty";
    String Lpos1 = "empty";
    String Lpos2 = "empty";
    String Lpos3 = "empty";
    int LticksCount = 500;
    int i;

    @Override
    public void runOpMode() {

        FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        BackRight = hardwareMap.get(DcMotor.class, "Back Right");
        BackLeft = hardwareMap.get(DcMotor.class, "Back Left");
        FrontRight = hardwareMap.get(DcMotor.class, "Front Right");
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Shooter = hardwareMap.get(DcMotor.class, "Shooter");
        Spindex = hardwareMap.get(DcMotor.class, "Spindex");
        Piston = hardwareMap.get(Servo.class, "Piston");
        //Turret = hardwareMap.get(CRServo.class, "Turret");
        color = hardwareMap.get(ColorSensor.class, "color");

        limitSwitch = hardwareMap.get(DigitalChannel.class, "leftLimit1");
        limitSwitch.setMode(DigitalChannel.Mode.INPUT);

        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        Intake.setDirection(DcMotor.Direction.REVERSE);
        Shooter.setDirection(DcMotor.Direction.FORWARD);
        Spindex.setDirection(DcMotor.Direction.FORWARD);

        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Spindex.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Piston.setPosition(0.1);






        waitForStart();


        while (opModeIsActive()){
            if (gamepad1.left_bumper){
                y = gamepad1.right_stick_y * 1*0.75*0.5;
                x = -gamepad1.right_stick_x * 1.1 * -0.75*0.5;
                rx = gamepad1.left_stick_x * 0.6 * -0.9*0.7;
            }else {
                y = gamepad1.right_stick_y * 1 * 0.75;
                x = -gamepad1.right_stick_x * 1.1 * -0.75;
                rx = gamepad1.left_stick_x * 0.6 * -0.9;
            }
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
            BackLeft.setPower((y + x + rx) / denominator);
            FrontLeft.setPower(((y - x) + rx) / denominator);
            BackRight.setPower(((y - x) - rx) / denominator);
            FrontRight.setPower(((y + x) - rx) / denominator);

            Spindex.setPower(gamepad2.left_stick_x*0.5);
            Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));

            Color.RGBToHSV(
                    color.red() * 8,
                    color.green() * 8,
                    color.blue() * 8,
                    hsv
            );
            hue = hsv[0];
            telemetry.addData("Hue", hue);
            if (hue > 200 && hue < 235) {
                telemetry.addLine(">>> PURPLE detected!");
                colorRead = "purple";
            } else if (hue > 150 && hue < 156) {
                telemetry.addLine(">>> GREEN detected!");
                colorRead = "green";
            } else {
                telemetry.addLine("Unknown color");
                colorRead = "empty";
            }


//auto-intake
            if (!colorRead.equals("empty")) {
                if (!pos1.equals("empty") && !pos2.equals("empty") && !pos3.equals("empty")) {

                } else {
                    if (ticksCount == 0) {
                        pos1 = colorRead;
                        colorRead = "empty";
                        ticksCount = 2000;
                        Spindex.setTargetPosition(ticksCount);
                        Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        Spindex.setPower(1);
                        while (opModeIsActive() && Spindex.isBusy()) {
                            y = gamepad1.right_stick_y * 1 * 0.75;
                            x = -gamepad1.right_stick_x * 1.1 * -0.75;
                            rx = gamepad1.left_stick_x * 0.6 * -0.9;
                            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                            BackLeft.setPower((y + x + rx) / denominator);
                            FrontLeft.setPower(((y - x) + rx) / denominator);
                            BackRight.setPower(((y - x) - rx) / denominator);
                            FrontRight.setPower(((y + x) - rx) / denominator);
                            Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                            idle();
                        }
                        Spindex.setPower(0);
                        Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    } else if (ticksCount == 1000) {
                        pos2 = colorRead;
                        colorRead = "empty";
                        ticksCount = 0;
                        Spindex.setTargetPosition(ticksCount);
                        Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        Spindex.setPower(1);
                        while (Spindex.isBusy()) {
                            y = gamepad1.right_stick_y * 1 * 0.75;
                            x = -gamepad1.right_stick_x * 1.1 * -0.75;
                            rx = gamepad1.left_stick_x * 0.6 * -0.9;
                            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                            BackLeft.setPower((y + x + rx) / denominator);
                            FrontLeft.setPower(((y - x) + rx) / denominator);
                            BackRight.setPower(((y - x) - rx) / denominator);
                            FrontRight.setPower(((y + x) - rx) / denominator);
                            Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                            idle();
                        }
                        Spindex.setPower(0);
                        Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    } else if (ticksCount == 2000) {
                        pos3 = colorRead;
                        colorRead = "empty";
                        ticksCount = 1000;
                        Spindex.setTargetPosition(ticksCount);
                        Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        Spindex.setPower(1);
                        while (Spindex.isBusy()) {
                            y = gamepad1.right_stick_y * 1 * 0.75;
                            x = -gamepad1.right_stick_x * 1.1 * -0.75;
                            rx = gamepad1.left_stick_x * 0.6 * -0.9;
                            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                            BackLeft.setPower((y + x + rx) / denominator);
                            FrontLeft.setPower(((y - x) + rx) / denominator);
                            BackRight.setPower(((y - x) - rx) / denominator);
                            FrontRight.setPower(((y + x) - rx) / denominator);
                            Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                            idle();
                        }
                        Spindex.setPower(0);
                        Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    }
                }
            }

            if (gamepad2.a){
                Shooter.setPower( 0.6);
            }
            if (gamepad2.x){
                Shooter.setPower(0);
            }
            if (gamepad2.b){
                Piston.setPosition(1.0);
                sleep(200);
                Piston.setPosition(0.1);
                Shooter.setPower(1);
                sleep(200);
                Shooter.setPower(0.57);
            }
            if (gamepad1.dpad_right) {
                if (ticksCount == 0){
                    if (Objects.equals(pos1, "empty")){
                        colorRead = "empty";
                        Color.RGBToHSV(
                                color.red() * 8,
                                color.green() * 8,
                                color.blue() * 8,
                                hsv
                        );
                        hue = hsv[0];
                        if (hue > 200 && hue < 235) {
                            telemetry.addLine(">>> PURPLE detected!");
                            colorRead = "purple";
                        }
                        else if (hue > 150 && hue < 160) {
                            telemetry.addLine(">>> GREEN detected!");
                            colorRead = "green";
                        }
                        else {
                            telemetry.addLine("Unknown color");
                            colorRead = "empty";
                        }
                        pos1 = colorRead;
                    }
                    ticksCount = 2000;
                    Spindex.setTargetPosition(ticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (ticksCount == 1000) {
                    if (Objects.equals(pos2, "empty")){
                        colorRead = "empty";
                        Color.RGBToHSV(
                                color.red() * 8,
                                color.green() * 8,
                                color.blue() * 8,
                                hsv
                        );
                        hue = hsv[0];
                        if (hue > 200 && hue < 235) {
                            telemetry.addLine(">>> PURPLE detected!");
                            colorRead = "purple";
                        }
                        else if (hue > 150 && hue < 160) {
                            telemetry.addLine(">>> GREEN detected!");
                            colorRead = "green";
                        }
                        else {
                            telemetry.addLine("Unknown color");
                            colorRead = "empty";
                        }
                        pos2 = colorRead;
                    }

                    ticksCount = 0;
                    Spindex.setTargetPosition(ticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (ticksCount == 2000) {
                    if (Objects.equals(pos3, "empty")){
                        colorRead = "empty";
                        Color.RGBToHSV(
                                color.red() * 8,
                                color.green() * 8,
                                color.blue() * 8,
                                hsv
                        );
                        hue = hsv[0];
                        if (hue > 200 && hue < 235) {
                            telemetry.addLine(">>> PURPLE detected!");
                            colorRead = "purple";
                        }
                        else if (hue > 150 && hue < 160) {
                            telemetry.addLine(">>> GREEN detected!");
                            colorRead = "green";
                        }
                        else {
                            telemetry.addLine("Unknown color");
                            colorRead = "empty";
                        }
                        pos3 = colorRead;
                    }
                    ticksCount = 1000;
                    Spindex.setTargetPosition(ticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
            }
            if (gamepad1.dpad_left) {
                if (ticksCount == 2000){
                    if (Objects.equals(pos3, "empty")){
                        colorRead = "empty";
                        Color.RGBToHSV(
                                color.red() * 8,
                                color.green() * 8,
                                color.blue() * 8,
                                hsv
                        );
                        hue = hsv[0];
                        if (hue > 200 && hue < 235) {
                            telemetry.addLine(">>> PURPLE detected!");
                            colorRead = "purple";
                        }
                        else if (hue > 150 && hue < 160) {
                            telemetry.addLine(">>> GREEN detected!");
                            colorRead = "green";
                        }
                        else {
                            telemetry.addLine("Unknown color");
                            colorRead = "empty";
                        }
                        pos3 = colorRead;
                    }
                    ticksCount = 0;
                    Spindex.setTargetPosition(ticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }else if(ticksCount == 1000){
                    if (Objects.equals(pos2, "empty")){
                        colorRead = "empty";
                        Color.RGBToHSV(
                                color.red() * 8,
                                color.green() * 8,
                                color.blue() * 8,
                                hsv
                        );
                        hue = hsv[0];
                        if (hue > 200 && hue < 235) {
                            telemetry.addLine(">>> PURPLE detected!");
                            colorRead = "purple";
                        }
                        else if (hue > 150 && hue < 160) {
                            telemetry.addLine(">>> GREEN detected!");
                            colorRead = "green";
                        }
                        else {
                            telemetry.addLine("Unknown color");
                            colorRead = "empty";
                        }
                        pos2 = colorRead;
                    }

                    ticksCount = 2000;
                    Spindex.setTargetPosition(ticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (ticksCount == 0) {
                    if (Objects.equals(pos1, "empty")){
                        colorRead = "empty";
                        Color.RGBToHSV(
                                color.red() * 8,
                                color.green() * 8,
                                color.blue() * 8,
                                hsv
                        );
                        hue = hsv[0];
                        if (hue > 200 && hue < 235) {
                            telemetry.addLine(">>> PURPLE detected!");
                            colorRead = "purple";
                        }
                        else if (hue > 150 && hue < 160) {
                            telemetry.addLine(">>> GREEN detected!");
                            colorRead = "green";
                        }
                        else {
                            telemetry.addLine("Unknown color");
                            colorRead = "empty";
                        }
                        pos1 = colorRead;
                    }

                    ticksCount = 1000;
                    Spindex.setTargetPosition(ticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }


            }
            if (gamepad2.dpad_right){
                if (LticksCount == 500){
                    LticksCount = 2500;
                    Spindex.setTargetPosition(LticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (LticksCount == 1500) {
                    LticksCount = 500;
                    Spindex.setTargetPosition(LticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (LticksCount == 2500) {
                    LticksCount = 500;
                    Spindex.setTargetPosition(LticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
            }
            if (gamepad2.dpad_left) {
                if (LticksCount == 2500){
                    LticksCount = 500;
                    Spindex.setTargetPosition(LticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (LticksCount == 1500) {
                    LticksCount = 2500;
                    Spindex.setTargetPosition(LticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (LticksCount == 500) {
                    LticksCount = 1500;
                    Spindex.setTargetPosition(LticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        Intake.setPower((gamepad1.right_trigger * 0.6)-(gamepad1.left_trigger*0.6));
                        idle();
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
            }
            if (gamepad1.y){
                Shooter.setPower(0.75);
                sleep(2000);
                if (!Objects.equals(pos1, "empty")){
                    Shooter.setPower(0.57);
                    y = 0;
                    x = 0;
                    rx = 0;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    ticksCount = 1500;
                    Spindex.setTargetPosition(ticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75*0.5;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75*0.5;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9*0.7;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    Piston.setPosition(1.0);
                    sleep(200);
                    Piston.setPosition(0.1);
                    Shooter.setPower(1);
                    sleep(200);
                    Shooter.setPower(0.9);
                    y = 0;
                    x = 0;
                    rx = 0;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    pos1 = "empty";
                }
                if (!Objects.equals(pos2, "empty")){
                    Shooter.setPower(0.65);
                    y = 0;
                    x = 0;
                    rx = 0;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    ticksCount = 2500;
                    Spindex.setTargetPosition(ticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75*0.5;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75*0.5;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9*0.7;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    Piston.setPosition(1.0);
                    sleep(200);
                    Piston.setPosition(0.1);
                    Shooter.setPower(1);
                    sleep(200);
                    Shooter.setPower(0.9);
                    y = 0;
                    x = 0;
                    rx = 0;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    pos2 = "empty";
                }
                if (!Objects.equals(pos3, "empty")){
                    Shooter.setPower(0.63);
                    y = 0;
                    x = 0;
                    rx = 0;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    ticksCount = 500;
                    Spindex.setTargetPosition(ticksCount);
                    Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    Spindex.setPower(1);
                    while (Spindex.isBusy()){
                        y = gamepad1.right_stick_y * 1*0.75*0.5;
                        x = -gamepad1.right_stick_x * 1.1 * -0.75*0.5;
                        rx = gamepad1.left_stick_x * 0.6 * -0.9*0.7;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                    }
                    Spindex.setPower(0);
                    Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    Piston.setPosition(1.0);
                    sleep(200);
                    Piston.setPosition(0.1);
                    Shooter.setPower(1);
                    sleep(200);
                    Shooter.setPower(0);
                    y = 0;
                    x = 0;
                    rx = 0;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    pos3 = "empty";
                }
                Shooter.setPower(0);
                ticksCount = 0;
                Spindex.setTargetPosition(ticksCount);
                Spindex.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                Spindex.setPower(1);
                while (Spindex.isBusy()){
                    y = gamepad1.right_stick_y * 1*0.75*0.5;
                    x = -gamepad1.right_stick_x * 1.1 * -0.75*0.5;
                    rx = gamepad1.left_stick_x * 0.6 * -0.9*0.7;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                }
                Spindex.setPower(0);
                Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            if (gamepad1.a){
                pos1= "empty";
                pos2= "empty";
                pos3= "empty";
            }
            if (gamepad1.b){
                pos1= "purple";
                pos2= "purple";
                pos3= "purple";
            }
            if (gamepad1.y){
                Spindex.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                Spindex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            telemetry.addData("Ticks", Spindex.getCurrentPosition());
            telemetry.addData("Predicted Ticks", ticksCount);
            telemetry.addData("LPredicted Ticks", LticksCount);
            telemetry.addData("Position 1",pos1);
            telemetry.addData("Position 2",pos2);
            telemetry.addData("Position 3",pos3);
            telemetry.addData("Left Stick X",gamepad1.left_stick_x);
            telemetry.addData("Right Stick X",gamepad1.right_stick_y);
            telemetry.addData("Right Stick Y",gamepad1.right_stick_y);
            telemetry.addData("Ticks",Spindex.getCurrentPosition());
            telemetry.update();
        }







    }

}
