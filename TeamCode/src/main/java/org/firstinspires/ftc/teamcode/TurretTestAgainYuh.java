package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@Disabled
public class TurretTestAgainYuh extends LinearOpMode {

    private DcMotor turret;
    double BaringYuh;
   // private DigitalChannel leftLimit;
    //private DigitalChannel rightLimit;

    VisionPortal visionPortal;
    AprilTagProcessor tagProcessor;



    @Override
    public void runOpMode() throws InterruptedException {

        turret = hardwareMap.get(DcMotor.class, "Turret");
        // Create AprilTag processor
        tagProcessor = new AprilTagProcessor.Builder().build();

        // Create VisionPortal with the webcam and processor
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(tagProcessor)
                .build();


        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setDirection(DcMotorSimple.Direction.REVERSE);

      //  leftLimit = hardwareMap.get(DigitalChannel.class, "leftLimit1");
      //  leftLimit.setMode(DigitalChannel.Mode.INPUT);
      //  rightLimit = hardwareMap.get(DigitalChannel.class, "rightLimit1");
      //  rightLimit.setMode(DigitalChannel.Mode.INPUT);

        telemetry.addLine("Init complete");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            List<AprilTagDetection> detections = tagProcessor.getDetections();

            if (detections.size() > 0) {
                AprilTagDetection tag = detections.get(0);

                double yaw = tag.ftcPose.yaw;          // Tag rotation
                double bearing = tag.ftcPose.bearing;  // Horizontal offset from center

                telemetry.addData("Tag ID", tag.id);
                telemetry.addData("Yaw (deg)", yaw);
                telemetry.addData("Horizontal Offset (deg)", bearing);
                if (bearing<12 && bearing >-12){
                    turret.setPower(0);
                }else {
                    turret.setPower(0.06*bearing);
                }
                BaringYuh=bearing;
            } else {
                telemetry.addLine("No tag detected");
                if (turret.getCurrentPosition() <= -1550){
                    turret.setPower(0.4);
                    BaringYuh = 0.01;
                } else if (turret.getCurrentPosition() >= 1160) {
                    turret.setPower(-0.4);
                    BaringYuh = -0.01;
                } else {
                    if (BaringYuh> 0){
                        turret.setPower(0.4);
                    } else if (BaringYuh<0) {
                        turret.setPower(-0.4);
                    }else {
                        turret.setPower(0);
                    }
                }

            telemetry.update();
            telemetry.addData("Bearing",BaringYuh);
        }

    }
}}
