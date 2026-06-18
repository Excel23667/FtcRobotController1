package org.firstinspires.ftc.teamcode.OldCode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
@Disabled
@Autonomous(name = "RED LONG CORNER",group = "RED")
public class RED_LONG_CORNER extends OpMode {
    private DcMotor intake;
    private DcMotorEx shooterRight;
    private DcMotorEx shooterLeft;
    private DcMotor turret;
    private Servo hood;
    private Servo stopper;
    /*double offset;
    int  cticks;
    private Limelight3A limelight3A;
    static final double kP =0.008;
    static final double kI =0.000001;
    static final double kD =0.0021;
    static final double TURRET_MIN = -150;
    static final double TURRET_MAX = 210;
    static final double AIM_TOLERANCE = 1.0;
    double lastError = 0;
    double integralSum = 0;*/
    //double output;
    private Follower follower;
    private Timer pathTimer, opModeTimer;
    ElapsedTime stateTimer = new ElapsedTime();
    PIDFCoefficients shooterPIDF = new PIDFCoefficients(1.5,0,0,14.2);
    double output;

    public enum PathState{
        //Start-End
        DRIVE_STARTPOS,
        SHOOTER_WARMUP_PRE,
        SHOOTING_PRE1,
        WARMUP_PRE2,
        SHOOTING_PRE2,
        WARMUP_PRE3,
        SHOOTING_PRE3,
        DRIVE_SHOOT_TO_PRE_COLLECT1,
        DRIVE_COLLECT1,
        DRIVE_COLLECT1_TO_SHOOT,
        REVERSE_BALL_1,
        SHOOTER_WARMUP_1BALLS,
        SHOOTING_BALL1_1,
        WARMUP_BALL1_2,
        SHOOTING_BALL1_2,
        WARMUP_BALL1_3,
        SHOOTING_BALL1_3,
        DRIVE_SHOOT_TO_LEAVE,
        RESET_TURRET

    }
    PathState pathState;

    private final Pose startPose = new Pose(81,11.757009345794401,Math.toRadians(90));
    private final Pose shootPose = new Pose(86.57943925233647,18.26168224299067,Math.toRadians(67));
    private final Pose preBallCollect1 = new Pose(137,22.8,Math.toRadians(-58));
    private final Pose ballCollect1 = new Pose(137,5.700934579439238,Math.toRadians(-58));
    private final Pose leavePose = new Pose(118.65420560747667,14.775700934579435,Math.toRadians(90));

    private PathChain driveStartPosShootPos, driveShootToPreBallCollect1, drivePreToBallCollect1,driveCollect1ToShoot,driveShootToLeave;
    public void buildPaths(){
        driveStartPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(),shootPose.getHeading(),0.6)
                .build();
        driveShootToPreBallCollect1 = follower.pathBuilder()
                .addPath(new BezierLine(shootPose,preBallCollect1))
                .setLinearHeadingInterpolation(shootPose.getHeading(),preBallCollect1.getHeading(),0.7)
                .build();
        drivePreToBallCollect1 = follower.pathBuilder()
                .addPath(new BezierLine(preBallCollect1,ballCollect1))
                .setLinearHeadingInterpolation(preBallCollect1.getHeading(),ballCollect1.getHeading())
                .build();
        driveCollect1ToShoot = follower.pathBuilder()
                .addPath(new BezierLine(ballCollect1,shootPose))
                .setLinearHeadingInterpolation(ballCollect1.getHeading(), shootPose.getHeading(),0.6)
                .build();
        driveShootToLeave = follower.pathBuilder()
                .addPath(new BezierLine(shootPose,leavePose))
                .setLinearHeadingInterpolation(shootPose.getHeading(),leavePose.getHeading())
                .build();
    }

    public void statePathUpdate(){
        switch (pathState){
            case DRIVE_STARTPOS:
                follower.followPath(driveStartPosShootPos,true);
                setPathState(PathState.SHOOTER_WARMUP_PRE);
                break;
            case SHOOTER_WARMUP_PRE:
                //is follower done its path
                hood.setPosition(0.8);
                shooterRight.setVelocity(1750);
                shooterLeft.setVelocity(1750);
                stopper.setPosition(1);
                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 4){
                    intake.setPower(1);
                    setPathState(PathState.SHOOTING_PRE1);
                }
                break;
            case SHOOTING_PRE1:
                if (pathTimer.getElapsedTimeSeconds()>0.2){
                    intake.setPower(0);
                    setPathState(PathState.WARMUP_PRE2);
                }
                break;
            case WARMUP_PRE2:
                if (pathTimer.getElapsedTimeSeconds()>1){
                    intake.setPower(1);
                    setPathState(PathState.SHOOTING_PRE2);
                }
                break;
            case SHOOTING_PRE2:
                if (pathTimer.getElapsedTimeSeconds()>0.3){
                    intake.setPower(0);
                    setPathState(PathState.WARMUP_PRE3);
                }
                break;
            case WARMUP_PRE3:
                if (pathTimer.getElapsedTimeSeconds()>1){
                    intake.setPower(1);
                    setPathState(PathState.SHOOTING_PRE3);
                }
                break;
            case SHOOTING_PRE3:
                if (pathTimer.getElapsedTimeSeconds()>1){
                    setPathState(PathState.DRIVE_SHOOT_TO_PRE_COLLECT1);
                }
                break;
            case DRIVE_SHOOT_TO_PRE_COLLECT1:
                stopper.setPosition(0.5);
                follower.setMaxPower(0.65);
                follower.followPath(driveShootToPreBallCollect1,true);
                setPathState(PathState.DRIVE_COLLECT1);
                break;
            case DRIVE_COLLECT1:
                if (!follower.isBusy()){
                    follower.followPath(drivePreToBallCollect1,true);
                    setPathState(PathState.DRIVE_COLLECT1_TO_SHOOT);
                }
                break;
            case DRIVE_COLLECT1_TO_SHOOT:
                if (!follower.isBusy() || pathTimer.getElapsedTimeSeconds() > 6){
                    follower.setMaxPower(1);
                    intake.setPower(0);
                    follower.followPath(driveCollect1ToShoot,true);
                    setPathState(PathState.REVERSE_BALL_1);
                }
                break;
            case REVERSE_BALL_1:
                if (!follower.isBusy()){
                    stopper.setPosition(1);
                    setPathState(PathState.SHOOTER_WARMUP_1BALLS);
                }
                break;
            case SHOOTER_WARMUP_1BALLS:
                //is follower done its path
                if (pathTimer.getElapsedTimeSeconds()>0.2) {
                    intake.setPower(1);
                    setPathState(PathState.SHOOTING_BALL1_1);
                }
                break;
            case SHOOTING_BALL1_1:
                if (pathTimer.getElapsedTimeSeconds()>0.075){
                    intake.setPower(0);
                    setPathState(PathState.WARMUP_BALL1_2);
                }
                break;
            case WARMUP_BALL1_2:
                if (pathTimer.getElapsedTimeSeconds()>1.5){
                    intake.setPower(1);
                    setPathState(PathState.SHOOTING_BALL1_2);
                }
                break;
            case SHOOTING_BALL1_2:
                if (pathTimer.getElapsedTimeSeconds()>0.3){
                    intake.setPower(0);
                    setPathState(PathState.WARMUP_BALL1_3);
                }
                break;
            case WARMUP_BALL1_3:
                if (pathTimer.getElapsedTimeSeconds()>1.5){
                    intake.setPower(1);
                    setPathState(PathState.SHOOTING_BALL1_3);
                }
                break;
            case SHOOTING_BALL1_3:
                if (pathTimer.getElapsedTimeSeconds()>1){
                    shooterRight.setVelocity(0);
                    shooterLeft.setVelocity(0);
                    intake.setPower(0);
                    setPathState(PathState.DRIVE_SHOOT_TO_LEAVE);
                }
                break;
            case DRIVE_SHOOT_TO_LEAVE:
                follower.followPath(driveShootToLeave,true);
                setPathState(PathState.RESET_TURRET);
                break;
            case RESET_TURRET:
                if (!follower.isBusy()){
                    intake.setPower(0);
                    shooterLeft.setVelocity(0);
                    shooterRight.setVelocity(0);
                }
                break;
            default:
                telemetry.addLine("No State Commanded");
                telemetry.update();
        }
    }

    public void  setPathState(PathState newState){
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathState = PathState.DRIVE_STARTPOS;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);

        shooterLeft = hardwareMap.get(DcMotorEx.class, "ShooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "ShooterRight");
        intake = hardwareMap.get(DcMotor.class, "Intake");
        hood = hardwareMap.get(Servo.class, "Hood");
        turret = hardwareMap.get(DcMotor.class,"Turret");
        // limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        //  limelight3A.pipelineSwitch(8);
        turret = hardwareMap.get(DcMotor.class,"Turret");
        stopper = hardwareMap.get(Servo.class, "Stopper");
        turret.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        // turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);
        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);
        hood.setPosition(1);
        stopper.setPosition(1);
        //    limelight3A.start();

       /* turret.setPower(0);
        turret.setTargetPosition(100);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(-0.7);
        while (turret.isBusy()){}
        turret.setPower(0);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/

        buildPaths();
        follower.setPose(startPose);
    }

    public void start(){
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        /*LLResult llResult = limelight3A.getLatestResult();
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

            output = kP * error + kI * integralSum + kD * derivative;

            lastError = error;
        }else if (turret.getCurrentPosition() > -180 && turret.getCurrentPosition() < 160) {
            if(llResult.getTx() <0.5 && llResult.getTx()>-0.5){
                turret.setPower(0);
            }else {
                turret.setPower(output);
            }
        } else {
            turret.setPower(0);
        }*/
        turret.setPower(0);
        telemetry.addData("path state", pathState.toString());
        telemetry.addData("x",follower.getPose().getX());
        telemetry.addData("y",follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("State Timer",stateTimer.seconds());
        telemetry.update();
    }

}
