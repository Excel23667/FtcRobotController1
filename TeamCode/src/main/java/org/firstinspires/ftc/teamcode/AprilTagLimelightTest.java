package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@Disabled

public class AprilTagLimelightTest extends LinearOpMode {

    double offset;
    int  cticks;
    private Limelight3A limelight3A;
    private DcMotor turret;
   // private IMU imu;

    @Override
    public void runOpMode() {

        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(8);

        turret = hardwareMap.get(DcMotor.class,"Turret");
        turret.setDirection(DcMotor.Direction.FORWARD);

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
                turret.setPower(offset * 0.035);
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
