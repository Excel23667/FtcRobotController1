package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "ITDTeleopCode (aaaTeleop) (24-25)")
@Disabled
public class ITDTeleopCode extends LinearOpMode {

    private DcMotor backleft;
    private DcMotor frontleft;
    private DcMotor RVslide;
    private CRServo Harm;
    private DcMotor frontright;
    private DcMotor backright;
    private DcMotor intake;
    private DcMotor HSlides;
    private CRServo Basket;
    private DcMotor VSlides;

    /**
     * This function is executed when this OpMode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        ElapsedTime runTime;
        double y;
        double x;
        double rx;
        double denominator;

        backleft = hardwareMap.get(DcMotor.class, "backleftAsDcMotor");
        frontleft = hardwareMap.get(DcMotor.class, "frontleftAsDcMotor");
        RVslide = hardwareMap.get(DcMotor.class, "RVslideAsDcMotor");
        Harm = hardwareMap.get(CRServo.class, "HarmAsCRServo");
        frontright = hardwareMap.get(DcMotor.class, "frontrightAsDcMotor");
        backright = hardwareMap.get(DcMotor.class, "backrightAsDcMotor");
        intake = hardwareMap.get(DcMotor.class, "intakeAsDcMotor");
        HSlides = hardwareMap.get(DcMotor.class, "HSlidesAsDcMotor");
        Basket = hardwareMap.get(CRServo.class, "BasketAsCRServo");
        VSlides = hardwareMap.get(DcMotor.class, "VSlidesAsDcMotor");

        runTime = new ElapsedTime();
        backleft.setDirection(DcMotor.Direction.REVERSE);
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        RVslide.setDirection(DcMotor.Direction.REVERSE);
        Harm.setDirection(CRServo.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive()) {
            y = -gamepad1.right_stick_y * 1;
            x = gamepad1.right_stick_x * 1.1 * 1;
            rx = gamepad1.left_stick_x * 0.6 * 1;
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
            frontleft.setPower((y + x + rx) / denominator);
            backleft.setPower(((y - x) + rx) / denominator);
            frontright.setPower(((y - x) - rx) / denominator);
            backright.setPower(((y + x) - rx) / denominator);
            intake.setPower(gamepad2.right_trigger * 1);
            if (gamepad2.left_stick_y < 0) {
                Harm.setPower(gamepad2.left_stick_y);
            }
            if (gamepad2.left_stick_y > 0) {
                Harm.setPower(gamepad2.left_stick_y * 0.25);
            }
            if (gamepad1.right_trigger >= 0.1) {
                while (gamepad1.right_trigger >= 0.1) {
                    telemetry.addLine("Precision Mode : ACTIVATED");
                    telemetry.update();
                    intake.setPower(gamepad2.right_trigger * 1);
                    Harm.setPower(gamepad2.left_stick_y * 1);
                    if (gamepad2.right_bumper) {
                        intake.setPower(-1);
                        runTime.reset();
                        while (opModeIsActive() && runTime.seconds() < 0.01) {
                            telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                            telemetry.update();
                        }
                        intake.setPower(0);
                    }
                    y = -gamepad1.right_stick_y * 0.25;
                    x = gamepad1.right_stick_x * 1.1 * 0.25;
                    rx = gamepad1.left_stick_x * 0.6 * 0.35;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    HSlides.setPower(gamepad2.right_stick_x);
                    Basket.setPower(gamepad2.left_stick_x * 0.65);
                    RVslide.setPower(gamepad2.right_stick_y * 0.8);
                    VSlides.setPower(gamepad2.right_stick_y * 0.8);
                }
            }
            if (gamepad2.right_bumper) {
                intake.setPower(-1);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 0.01) {
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                intake.setPower(0);
            }
            RVslide.setPower(gamepad2.right_stick_y * 0.8);
            VSlides.setPower(gamepad2.right_stick_y * 0.8);
            HSlides.setPower(gamepad2.left_stick_x * -1);
            Basket.setPower(gamepad2.right_stick_x * -0.65);
            telemetry.addData("Path leg 1 seconds elapsed:", gamepad2.left_stick_x);
            if (gamepad2.a) {
                y = -gamepad1.right_stick_y;
                x = gamepad1.right_stick_x * 1.1;
                rx = gamepad1.left_stick_x * 0.6;
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                frontleft.setPower((y + x + rx) / denominator);
                backleft.setPower(((y - x) + rx) / denominator);
                frontright.setPower(((y - x) - rx) / denominator);
                backright.setPower(((y + x) - rx) / denominator);
                Harm.setPower(-1);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 1.8) {
                    y = -gamepad1.right_stick_y;
                    x = gamepad1.right_stick_x * 1.1;
                    rx = gamepad1.left_stick_x * 0.6;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                y = -gamepad1.right_stick_y;
                x = gamepad1.right_stick_x * 1.1;
                rx = gamepad1.left_stick_x * 0.6;
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                frontleft.setPower((y + x + rx) / denominator);
                backleft.setPower(((y - x) + rx) / denominator);
                frontright.setPower(((y - x) - rx) / denominator);
                backright.setPower(((y + x) - rx) / denominator);
                Harm.setPower(0);
                HSlides.setPower(-0.8);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 0.7) {
                    y = -gamepad1.right_stick_y;
                    x = gamepad1.right_stick_x * 1.1;
                    rx = gamepad1.left_stick_x * 0.6;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                y = -gamepad1.right_stick_y;
                x = gamepad1.right_stick_x * 1.1;
                rx = gamepad1.left_stick_x * 0.6;
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                frontleft.setPower((y + x + rx) / denominator);
                backleft.setPower(((y - x) + rx) / denominator);
                frontright.setPower(((y - x) - rx) / denominator);
                backright.setPower(((y + x) - rx) / denominator);
                intake.setPower(-1);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 1) {
                    y = -gamepad1.right_stick_y;
                    x = gamepad1.right_stick_x * 1.1;
                    rx = gamepad1.left_stick_x * 0.6;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                y = -gamepad1.right_stick_y;
                x = gamepad1.right_stick_x * 1.1;
                rx = gamepad1.left_stick_x * 0.6;
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                frontleft.setPower((y + x + rx) / denominator);
                backleft.setPower(((y - x) + rx) / denominator);
                frontright.setPower(((y - x) - rx) / denominator);
                backright.setPower(((y + x) - rx) / denominator);
                HSlides.setPower(0);
                intake.setPower(0);
            }
            if (gamepad1.y) {
                VSlides.setPower(-1);
                RVslide.setPower(-1);
                HSlides.setPower(0.2);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 0.4) {
                    y = -gamepad1.right_stick_y * 1;
                    x = gamepad1.right_stick_x * 1.1 * 1;
                    rx = gamepad1.left_stick_x * 0.6 * 1;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                while (!gamepad1.y) {
                    VSlides.setPower(-0.25);
                    RVslide.setPower(-0.25);
                    y = -gamepad1.right_stick_y * 1;
                    x = gamepad1.right_stick_x * 1.1 * 1;
                    rx = gamepad1.left_stick_x * 0.6 * 1;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    HSlides.setPower(gamepad2.right_stick_x);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                RVslide.setPower(0.15);
                VSlides.setPower(0.15);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 1) {
                    y = -gamepad1.right_stick_y * 1;
                    x = gamepad1.right_stick_x * 1.1 * 1;
                    rx = gamepad1.left_stick_x * 0.6 * 1;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
            }
            if (gamepad1.b) {
                VSlides.setPower(-0.8);
                RVslide.setPower(-0.8);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 1.2) {
                    y = -gamepad1.right_stick_y * 0.25;
                    x = gamepad1.right_stick_x * 1.1 * 0.25;
                    rx = gamepad1.left_stick_x * 0.6 * 0.3;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                while (!gamepad1.b) {
                    VSlides.setPower(-0.25);
                    RVslide.setPower(-0.25);
                    y = -gamepad1.right_stick_y * 0.25;
                    x = gamepad1.right_stick_x * 1.1 * 0.25;
                    rx = gamepad1.left_stick_x * 0.6 * 0.25;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                Basket.setPower(-1);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 0.9) {
                    y = -gamepad1.right_stick_y * 0.25;
                    x = gamepad1.right_stick_x * 1.1 * 0.25;
                    rx = gamepad1.left_stick_x * 0.6 * 0.25;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                Basket.setPower(2);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 0.7) {
                    y = -gamepad1.right_stick_y * 0.25;
                    x = gamepad1.right_stick_x * 1.1 * 0.25;
                    rx = gamepad1.left_stick_x * 0.6 * 0.25;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                Basket.setPower(0);
                VSlides.setPower(0.4);
                RVslide.setPower(0.4);
                runTime.reset();
                while (opModeIsActive() && runTime.seconds() < 1) {
                    y = -gamepad1.right_stick_y;
                    x = gamepad1.right_stick_x * 1.1;
                    rx = gamepad1.left_stick_x * 0.6;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    frontleft.setPower((y + x + rx) / denominator);
                    backleft.setPower(((y - x) + rx) / denominator);
                    frontright.setPower(((y - x) - rx) / denominator);
                    backright.setPower(((y + x) - rx) / denominator);
                    telemetry.addData("Path leg 1 seconds elapsed:", runTime.seconds());
                    telemetry.update();
                }
                VSlides.setPower(0);
                RVslide.setPower(0);
            }
        }
    }
}