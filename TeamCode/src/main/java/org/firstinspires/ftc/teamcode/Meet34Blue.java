package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@TeleOp(name = "BlueMeet4")
public class Meet34Blue extends LinearOpMode {

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
    static final double kP =0.008;
    static final double kI =0.000001;
    static final double kD =0.0021;
    static final double TURRET_MIN = -150;
    static final double TURRET_MAX = 290;
    static final double AIM_TOLERANCE = 1.0;
    double lastError = 0;
    double integralSum = 0;

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
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(7);

        turret = hardwareMap.get(DcMotor.class,"Turret");
        turret.setDirection(DcMotor.Direction.REVERSE);

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        hood.setPosition(0.65);
        stopper.setPosition(0.4);

        limelight3A.start();



        waitForStart();



        while (opModeIsActive()) {

            if (gamepad1.left_bumper) {
                y = -gamepad1.right_stick_y * 1 * 0.75 * 0.5;
                x = gamepad1.right_stick_x * 1.1 * -0.75 * 0.5;
                rx = -gamepad1.left_stick_x * 0.6 * -0.9 * 0.7;
            } else {
                y = -gamepad1.right_stick_y * 1 * 0.75;
                x = gamepad1.right_stick_x * 1.1 * -0.75;
                rx = -gamepad1.left_stick_x * 0.6 * -0.9;
            }
            denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
            BackLeft.setPower((y + x + rx) / denominator);
            FrontLeft.setPower(((y - x) + rx) / denominator);
            BackRight.setPower(((y - x) - rx) / denominator);
            FrontRight.setPower(((y + x) - rx) / denominator);

            telemetry.addData("Left Stick X", gamepad1.left_stick_x);
            telemetry.addData("Right Stick X", gamepad1.right_stick_y);
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

                if (turret.getCurrentPosition() >= 210){
                    turret.setPower(0);
                    turret.setTargetPosition(1);
                    turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    turret.setPower(-0.7);
                    while (turret.isBusy()){
                        y = -gamepad1.right_stick_y * 1 * 0.75;
                        x = gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = -gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));
                    }
                    turret.setPower(0);
                    turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (turret.getCurrentPosition() <= -150) {
                    turret.setPower(0);
                    turret.setTargetPosition(1);
                    turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    turret.setPower(0.7);
                    while (turret.isBusy()){
                        y = -gamepad1.right_stick_y * 1 * 0.75;
                        x = gamepad1.right_stick_x * 1.1 * -0.75;
                        rx = -gamepad1.left_stick_x * 0.6 * -0.9;
                        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                        BackLeft.setPower((y + x + rx) / denominator);
                        FrontLeft.setPower(((y - x) + rx) / denominator);
                        BackRight.setPower(((y - x) - rx) / denominator);
                        FrontRight.setPower(((y + x) - rx) / denominator);
                        intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));
                    }
                    turret.setPower(0);
                    turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else if (turret.getCurrentPosition() > -210 && turret.getCurrentPosition() < 150) {
                    if(llResult.getTx() <0.5 && llResult.getTx()>-0.5){
                        turret.setPower(0);
                    }else {
                        turret.setPower(output);
                    }
                } else {
                    turret.setPower(0);
                }

            }
            if (turret.getCurrentPosition() >= 210){
                turret.setPower(0);
                turret.setTargetPosition(1);
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turret.setPower(-0.7);
                while (turret.isBusy()){
                    y = -gamepad1.right_stick_y * 1 * 0.75;
                    x = gamepad1.right_stick_x * 1.1 * -0.75;
                    rx = -gamepad1.left_stick_x * 0.6 * -0.9;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    intake.setPower((gamepad1.right_trigger * 1) - (gamepad1.left_trigger * 1));
                }
                turret.setPower(0);
                turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else if (turret.getCurrentPosition() <= -150) {
                turret.setPower(0);
                turret.setTargetPosition(1);
                turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                turret.setPower(0.7);
                while (turret.isBusy()){
                    y = -gamepad1.right_stick_y * 1 * 0.75;
                    x = gamepad1.right_stick_x * 1.1 * -0.75;
                    rx = -gamepad1.left_stick_x * 0.6 * -0.9;
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
                shooterRight.setVelocity(0);
                shooterLeft.setVelocity(0);
            }
            if (gamepad1.y){
                shooterRight.setVelocity(2075);
                shooterLeft.setVelocity(2075);
                hood.setPosition(0.9);
            }
            if (gamepad1.b){
                shooterRight.setVelocity(1650);
                shooterLeft.setVelocity(1650);
                hood.setPosition(0.65);
            }
        }
    }
}
