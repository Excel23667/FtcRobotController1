package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@Autonomous(name = "RedFront4")
public class Meet4RedFront extends LinearOpMode {

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
        limelight3A.pipelineSwitch(8);

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


        y = -0.4;
        x = 0.05;
        rx =0;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);
        sleep(1800);
        BackLeft.setPower(0);
        FrontLeft.setPower(0);
        BackRight.setPower(0);
        FrontRight.setPower(0);

        shooterRight.setVelocity(1650);
        shooterLeft.setVelocity(1650);
        sleep(3000);

        intake.setPower(1);
        sleep(600);
        intake.setPower(0);
        sleep(2000);

        intake.setPower(1);
        sleep(800);
        intake.setPower(0);
        sleep(2000);

        intake.setPower(1);
        sleep(1000);
        intake.setPower(0);
        shooterRight.setVelocity(0);
        shooterLeft.setVelocity(0);

        y = 0;
        x = 0;
        rx =0.2;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);
        sleep(1000);
        BackLeft.setPower(0);
        FrontLeft.setPower(0);
        BackRight.setPower(0);
        FrontRight.setPower(0);

        y = 0.4;
        x = 0;
        rx =0;
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        BackLeft.setPower((y + x + rx) / denominator);
        FrontLeft.setPower(((y - x) + rx) / denominator);
        BackRight.setPower(((y - x) - rx) / denominator);
        FrontRight.setPower(((y + x) - rx) / denominator);
        intake.setPower(0.6);
        sleep(2000);
        BackLeft.setPower(0);
        FrontLeft.setPower(0);
        BackRight.setPower(0);
        FrontRight.setPower(0);
        intake.setPower(0);



    }
}
