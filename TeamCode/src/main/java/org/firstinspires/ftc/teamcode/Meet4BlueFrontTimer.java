package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
@Disabled
@Autonomous(name = "BlueFront4Timer")
public class Meet4BlueFrontTimer extends LinearOpMode {

    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor BackLeft;
    private DcMotor FrontRight;
    private DcMotor intake;
    private DcMotorEx shooterRight;
    private DcMotorEx shooterLeft;
    private DcMotor turret;
    private Servo hood;
    //private Servo stopper;
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
    static final double TURRET_MAX = 210;
    static final double AIM_TOLERANCE = 1.0;
    double lastError = 0;
    double integralSum = 0;
    int step = 0;

    ElapsedTime timer = new ElapsedTime();

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
        turret = hardwareMap.get(DcMotor.class,"Turret");

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
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
        //stopper.setPosition(0.4);

        limelight3A.start();



        waitForStart();

        timer.reset();


        while (opModeIsActive()){
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
                turret.setPower(output);

            } else {
                turret.setPower(0);
            }

            if (step == 0){
                y = -0.4;
                x = 0.1;
                rx =0;
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                BackLeft.setPower((y + x + rx) / denominator);
                FrontLeft.setPower(((y - x) + rx) / denominator);
                BackRight.setPower(((y - x) - rx) / denominator);
                FrontRight.setPower(((y + x) - rx) / denominator);
                if (timer.seconds() >= 1.8){
                    BackLeft.setPower(0);
                    FrontLeft.setPower(0);
                    BackRight.setPower(0);
                    FrontRight.setPower(0);
                    shooterRight.setVelocity(1650);
                    shooterLeft.setVelocity(1650);
                    step = 1;
                    timer.reset();
                }
            } else if (step == 1) {

                if (timer.seconds() >= 3){
                    intake.setPower(1);
                    step = 2;
                    timer.reset();
                }
            } else if (step == 2) {

                if (timer.seconds() >= 0.9){
                    intake.setPower(0);
                    step = 3;
                    timer.reset();
                }
            }else if (step == 3) {

                if (timer.seconds() >= 3){
                    intake.setPower(1);
                    step = 4;
                    timer.reset();
                }
            } else if (step == 4) {

                if (timer.seconds() >= 1.2){
                    intake.setPower(0);
                    step = 5;
                    timer.reset();
                }
            }else if (step == 5) {

                if (timer.seconds() >= 3){
                    intake.setPower(1);
                    step = 6;
                    timer.reset();
                }
            } else if (step == 6) {

                if (timer.seconds() >= 1.2){
                    intake.setPower(0);
                    shooterRight.setVelocity(0);
                    shooterLeft.setVelocity(0);
                    y = 0;
                    x = 0.4;
                    rx =0;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    step = 7;
                    timer.reset();
                }
            }else if (step == 7) {

                if (timer.seconds() >= 1){
                    BackLeft.setPower(0);
                    FrontLeft.setPower(0);
                    BackRight.setPower(0);
                    FrontRight.setPower(0);
                    turret.setPower(0);
                    turret.setTargetPosition(1);
                    turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    turret.setPower(0.7);
                    while (turret.isBusy()){

                    }
                    turret.setPower(0);
                }
            }/*else if (step == 8) {

                if (timer.seconds() >= 1){
                    y = 0.4;
                    x = 0;
                    rx =0;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower((y + x + rx) / denominator);
                    FrontLeft.setPower(((y - x) + rx) / denominator);
                    BackRight.setPower(((y - x) - rx) / denominator);
                    FrontRight.setPower(((y + x) - rx) / denominator);
                    intake.setPower(1);
                    step = 9;
                    timer.reset();
                }
            }
            else if (step == 9) {

                if (timer.seconds() >= 3){
                    y = 0.4;
                    x = 0;
                    rx =0;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                    BackLeft.setPower(0);
                    FrontLeft.setPower(0);
                    BackRight.setPower(0);
                    FrontRight.setPower(0);
                    intake.setPower(0);
                    turret.setPower(0);
                    turret.setTargetPosition(1);
                    turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    turret.setPower(0.7);
                    while (turret.isBusy()){

                    }
                    turret.setPower(0);
                }*/
        }

    }

}


