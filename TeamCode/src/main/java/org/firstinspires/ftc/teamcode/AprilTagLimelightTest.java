package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@TeleOp

public class AprilTagLimelightTest extends LinearOpMode {

    double offset;
    int  cticks;
    private Limelight3A limelight3A;
    private DcMotor turret;
    static final double kP =0.02;
    static final double kI =0.0;
    static final double kD =0.02;
    static final double TURRET_MIN = -300;
    static final double TURRET_MAX = 160;
    static final double AIM_TOLERANCE = 1.0;
    double lastError =0;
    double integralSum = 0;

    @Override
    public void runOpMode() {

        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(8);

        turret = hardwareMap.get(DcMotor.class,"Turret");
        turret.setDirection(DcMotor.Direction.REVERSE);

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        limelight3A.start();

        waitForStart();

        while (opModeIsActive()){

            LLResult llResult = limelight3A.getLatestResult();
            if (llResult != null && llResult.isValid()){
                offset = llResult.getTx();
                Pose3D botpose = llResult.getBotpose();
                telemetry.addData("Tx",llResult.getTx());
                telemetry.addData("Ty",llResult.getTy());
                telemetry.addData("Ta",llResult.getTa());
                telemetry.addData("BotPose",botpose.toString());
                telemetry.addData("Yaw", botpose.getOrientation().getYaw());
                telemetry.update();
                double error = llResult.getTx();
                integralSum += error;
                double derivative = error - lastError;

                double output = kP * error + kI * integralSum + kD * derivative;

                lastError = error;

                if (turret.getCurrentPosition() > TURRET_MIN && turret.getCurrentPosition() < TURRET_MAX){
                    turret.setPower(output);
                }else {
                    turret.setPower(0);
                }

            }else {
                turret.setPower(0);
            }

            cticks = turret.getCurrentPosition();

            if (cticks <= -350){
                turret.setTargetPosition(87);
                turret.setPower(0.1);
                while (opModeIsActive() && turret.isBusy()){
                    llResult = limelight3A.getLatestResult();
                    if (llResult != null && llResult.isValid()){
                        offset = llResult.getTx();
                        Pose3D botpose = llResult.getBotpose();
                        telemetry.addData("Tx",llResult.getTx());
                        telemetry.addData("Ty",llResult.getTy());
                        telemetry.addData("Ta",llResult.getTa());
                        telemetry.addData("BotPose",botpose.toString());
                        telemetry.addData("Yaw", botpose.getOrientation().getYaw());
                        telemetry.update();
                        turret.setPower(offset * 0.035);
                        break;
                    }
                    idle();
                }
                turret.setPower(0);
            } else if (cticks >= 87) {
                turret.setTargetPosition(-350);
                turret.setPower(-0.1);
                while (opModeIsActive() && turret.isBusy()){
                    llResult = limelight3A.getLatestResult();
                    if (llResult != null && llResult.isValid()){
                        offset = llResult.getTx();
                        Pose3D botpose = llResult.getBotpose();
                        telemetry.addData("Tx",llResult.getTx());
                        telemetry.addData("Ty",llResult.getTy());
                        telemetry.addData("Ta",llResult.getTa());
                        telemetry.addData("BotPose",botpose.toString());
                        telemetry.addData("Yaw", botpose.getOrientation().getYaw());
                        telemetry.update();
                        turret.setPower(offset * 0.035);
                        break;
                    }
                    idle();
                }
                turret.setPower(0);
            }
        }
    }
}
