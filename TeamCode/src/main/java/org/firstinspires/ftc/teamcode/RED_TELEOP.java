package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@TeleOp
public class RED_TELEOP extends LinearOpMode {

    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor BackLeft;
    private DcMotor FrontRight;
    private DcMotor intake;
    private DcMotorEx shooterRight;
    private DcMotorEx shooterLeft;
    private DcMotor turret;
    private Servo hood;
    private Servo stopper;
    double y;
    double x;
    double rx;
    double denominator;
    double offset;
    int  cticks;
    private Limelight3A limelight3A;
    static final double kP =0.014;
    static final double kI =0;
    static final double kD =0.015;
    static final double TURRET_RIGHT_NEGATIVE= -85;
    static final double TURRET_LEFT_POSITIVE = 600;
    static final double AIM_TOLERANCE = 1.0;
    double lastError = 0;
    double integralSum = 0;
    PIDFCoefficients shooterPIDF = new PIDFCoefficients(1.5,0,0,14.2);

    @Override
    public void runOpMode() throws InterruptedException {

        FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        BackRight = hardwareMap.get(DcMotor.class, "Back Right");
        BackLeft = hardwareMap.get(DcMotor.class, "Back Left");
        FrontRight = hardwareMap.get(DcMotor.class, "Front Right");
        shooterLeft = hardwareMap.get(DcMotorEx.class, "ShooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "ShooterRight");
        intake = hardwareMap.get(DcMotor.class, "Intake");
        hood = hardwareMap.get(Servo.class, "Hood");
        stopper = hardwareMap.get(Servo.class, "Stopper");

        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterRight.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(8);

        turret = hardwareMap.get(DcMotor.class,"Turret");
        turret.setDirection(DcMotor.Direction.REVERSE);

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);
        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);

        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        hood.setPosition(0.5);
        stopper.setPosition(0.17);

        limelight3A.start();



        waitForStart();



        while (opModeIsActive()) {

            if (gamepad1.left_bumper) {
                y = -gamepad1.right_stick_y * 1 * 0.75 * 0.5;
                x = gamepad1.right_stick_x * 1.1 * -0.75 * 0.5;
                rx = -gamepad1.left_stick_x * 0.6 * -0.9 * 0.7;
            } else {
                y = -gamepad1.right_stick_y * 1 * 1;
                x = gamepad1.right_stick_x * 1.1 * -1;
                rx = -gamepad1.left_stick_x * -0.6;
            }
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
            BackLeft.setPower((y + x + rx) / denominator);
            FrontLeft.setPower(((y - x) + rx) / denominator);
            BackRight.setPower(((y - x) - rx) / denominator);
            FrontRight.setPower(((y + x) - rx) / denominator);

            telemetry.addData("Left Stick X", gamepad1.left_stick_x);
            telemetry.addData("Right Stick X", gamepad1.right_stick_x);
            telemetry.addData("Right Stick Y", gamepad1.right_stick_y);
            telemetry.addData("Tick",turret.getCurrentPosition());
            telemetry.update();

            intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));

            LLResult llResult = limelight3A.getLatestResult();
            if (llResult != null && llResult.isValid()) {
                offset = llResult.getTx();
                Pose3D botpose = llResult.getBotpose();
                telemetry.addData("Tx", llResult.getTx());
                telemetry.addData("Ty", llResult.getTy());
                telemetry.addData("Ta", llResult.getTa());
                telemetry.addData("BotPose", botpose.toString());
                telemetry.addData("Yaw", botpose.getOrientation().getYaw());
                telemetry.update();
                double error = llResult.getTx();
                integralSum += error;
                double derivative = error - lastError;

                double output = kP * error + kI * integralSum + kD * derivative;

                lastError = error;

                if (turret.getCurrentPosition() >= TURRET_LEFT_POSITIVE){
                    turret.setPower(0);
                    turret.setTargetPosition((int) (TURRET_LEFT_POSITIVE-10));
                    turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    turret.setPower(-0.7);
                    while (turret.isBusy()){
                        y = -gamepad1.right_stick_y * 1 * 1;
                        x = gamepad1.right_stick_x * 1.1 * -1;
                        rx = -gamepad1.left_stick_x * 0.6 * -1;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));
                    }
                    turret.setPower(0);
                    turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (turret.getCurrentPosition() <= TURRET_RIGHT_NEGATIVE) {
                    turret.setPower(0);
                    turret.setTargetPosition((int) (TURRET_RIGHT_NEGATIVE+10));
                    turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    turret.setPower(0.7);
                    while (turret.isBusy()){
                        y = -gamepad1.right_stick_y * 1 * 1;
                        x = gamepad1.right_stick_x * 1.1 * -1;
                        rx = -gamepad1.left_stick_x * 0.6 * -1;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));
                    }
                    turret.setPower(0);
                    turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (turret.getCurrentPosition() > TURRET_RIGHT_NEGATIVE && turret.getCurrentPosition() < TURRET_LEFT_POSITIVE) {
                    if(llResult.getTx() <0.5 && llResult.getTx()>-0.5){
                        turret.setPower(0);
                    }else {
                        turret.setPower(output);
                    }
                } else {
                    turret.setPower(0);
                }

            }//else {
                //turret.setPower(0);
          //  }
            if (turret.getCurrentPosition() >= TURRET_LEFT_POSITIVE){
                turret.setPower(0);
                turret.setTargetPosition((int) (TURRET_LEFT_POSITIVE-10));
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turret.setPower(-0.7);
                while (turret.isBusy()){
                    y = -gamepad1.right_stick_y * 1 * 1;
                    x = gamepad1.right_stick_x * 1.1 * -1;
                    rx = -gamepad1.left_stick_x * 0.6 * -1;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));
                }
                turret.setPower(0);
                turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else if (turret.getCurrentPosition() <= TURRET_RIGHT_NEGATIVE) {
                turret.setPower(0);
                turret.setTargetPosition((int) (TURRET_RIGHT_NEGATIVE+10));
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turret.setPower(0.7);
                while (turret.isBusy()){
                    y = -gamepad1.right_stick_y * 1 * 1;
                    x = gamepad1.right_stick_x * 1.1 * -1;
                    rx = -gamepad1.left_stick_x * 0.6 * -1;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));
                }
                turret.setPower(0);
                turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            if (gamepad1.x) {
              //  shooterRight.setVelocity(0);
              //  shooterLeft.setVelocity(0);
                stopper.setPosition(0.17);
            }
            if (gamepad2.x) {
                shooterRight.setVelocity(0);
                shooterLeft.setVelocity(0);
                stopper.setPosition(0.17);
            }
            if (gamepad1.a){
                stopper.setPosition(0.17);
                shooterRight.setVelocity(0);
                shooterLeft.setVelocity(0);
            }
            if (gamepad1.y){
                shooterRight.setVelocity(2100);
                shooterLeft.setVelocity(2100);
                hood.setPosition(0.9);
                stopper.setPosition(0.05);
            }
            if (gamepad1.b){
                shooterRight.setVelocity(1600);
                shooterLeft.setVelocity(1600);
                hood.setPosition(0.6);
                stopper.setPosition(0.05);
            }
            if (gamepad1.right_bumper){
                shooterRight.setVelocity(1600);
                shooterLeft.setVelocity(1600);
                hood.setPosition(0.6);
                stopper.setPosition(0.05);
            }
            if(gamepad1.left_stick_button){
                turret.setPower(0);
                turret.setTargetPosition(350);
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                if (turret.getCurrentPosition()<0){
                    turret.setPower(-0.5);
                }else {
                    turret.setPower(0.5);
                }
                while (turret.isBusy()){
                    y = -gamepad1.right_stick_y * 1 * 1;
                    x = gamepad1.right_stick_x * 1.1 * -1;
                    rx = -gamepad1.left_stick_x * 0.6 * -1;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));
                }
                turret.setPower(0);
                turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            if (gamepad1.right_stick_button){
                turret.setPower(0);
                turret.setTargetPosition(0);
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                if (turret.getCurrentPosition()<0){
                    turret.setPower(-0.5);
                }else {
                    turret.setPower(0.5);
                }
                while (turret.isBusy()){
                    y = -gamepad1.right_stick_y * 1 * 1;
                    x = gamepad1.right_stick_x * 1.1 * -1;
                    rx = -gamepad1.left_stick_x * 0.6 * -1;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));
                }
                turret.setPower(0);
                turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }
    }
}
