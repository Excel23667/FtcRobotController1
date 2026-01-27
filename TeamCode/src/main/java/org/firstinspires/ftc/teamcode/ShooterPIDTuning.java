package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class ShooterPIDTuning extends LinearOpMode {

    public DcMotorEx shooterRight;
    public DcMotorEx shooterLeft;
    public double lowVelocity  = 1400;
    public double highVelocity = 1200;
    double curTargetVelocity = highVelocity;
    double F;
    double P;
    double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
    int stepIndex = 1;

    @Override
    public void runOpMode() throws InterruptedException {

        shooterRight = hardwareMap.get(DcMotorEx.class,"ShooterRight");
        shooterLeft = hardwareMap.get(DcMotorEx.class,"ShooterLeft");

        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P,0,0,F);
        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry.addLine("Init Complete");

        waitForStart();

        while (opModeIsActive()){

            if (gamepad1.yWasPressed()){
                if (curTargetVelocity == highVelocity){
                    curTargetVelocity = lowVelocity;
                }else {
                    curTargetVelocity = highVelocity;
                }
            }

            if (gamepad1.bWasPressed()){
                stepIndex = (stepIndex + 1 ) % stepSizes.length;
            }

            if (gamepad1.dpadLeftWasPressed()){
                F -= stepSizes[stepIndex];
            }

            if (gamepad1.dpadRightWasPressed()){
                F += stepSizes[stepIndex];
            }

            if (gamepad1.dpadUpWasPressed()){
                P += stepSizes[stepIndex];
            }

            if (gamepad1.dpadDownWasPressed()){
                P -= stepSizes[stepIndex];
            }

            pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
            shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

            shooterRight.setVelocity(curTargetVelocity);
            shooterLeft.setVelocity(curTargetVelocity);

            double curVelocityRight = shooterRight.getVelocity();
            double curVelocityLeft= shooterLeft.getVelocity();

            double errorRight = curTargetVelocity - curVelocityRight;
            double errorLeft = curTargetVelocity - curVelocityLeft;

            telemetry.addData("Target Velocity", curTargetVelocity);
            telemetry.addData("Current Velocity Right", "%.2f", curVelocityRight);
            telemetry.addData("Current Velocity Left", "%.2f", curVelocityLeft);
            telemetry.addData("Error Right","%.2f",errorRight);
            telemetry.addData("Error Left","%.2f",errorLeft);
            telemetry.addLine("---------------------------------------------------------");
            telemetry.addData("Tuning P","%.4f (D-Pad U/D)", P);
            telemetry.addData("Tuning F","%.4f (D-Pad L/R)", F);
            telemetry.addData("Step Size", "%.4f (Button)", stepSizes[stepIndex]);

        }

    }
}
