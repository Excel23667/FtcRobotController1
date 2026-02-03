package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous
public class SampleAuto extends OpMode {
    private DcMotor intake;
    private DcMotorEx shooterRight;
    private DcMotorEx shooterLeft;
    private DcMotor turret;
    private Servo hood;
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
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState{
        //Start-End
        DRIVE_STARTPOS,
        SHOOT_PRELOAD

    }
    PathState pathState;

    private final Pose startPose = new Pose(109.90654205607474,134.1308411214953,Math.toRadians(0));
    private final Pose shootPose = new Pose(95.55140186915885,96.22429906542051,Math.toRadians(0));

    private PathChain driveStartPosShootPos;
    public void buildPaths(){
        driveStartPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(),shootPose.getHeading())
                .build();
    }

    public void statePathUpdate(){
        switch (pathState){
            case DRIVE_STARTPOS:
                follower.followPath(driveStartPosShootPos,true);
                setPathState(PathState.SHOOT_PRELOAD);
                break;
            case SHOOT_PRELOAD:
                //is follower done its path
                if (!follower.isBusy()){
                    hood.setPosition(1);
                    shooterRight.setVelocity(1400);
                    shooterLeft.setVelocity(1400);
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
        hood.setPosition(1);
        limelight3A.start();

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
        telemetry.addData("path state", pathState.toString());
        telemetry.addData("x",follower.getPose().getX());
        telemetry.addData("y",follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
    }

}
