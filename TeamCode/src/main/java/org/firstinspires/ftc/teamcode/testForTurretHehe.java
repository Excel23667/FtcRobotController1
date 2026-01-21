package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DigitalChannel;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@Disabled
public class testForTurretHehe extends OpMode {
    private CRServo Turret;
    double BaringYuh;
    private DigitalChannel leftLimit;
    private DigitalChannel rightLimit;

    VisionPortal visionPortal;
    AprilTagProcessor tagProcessor;

    @Override
    public void init() {
        Turret = hardwareMap.get(CRServo.class, "Turret");
        // Create AprilTag processor
        tagProcessor = new AprilTagProcessor.Builder().build();

        // Create VisionPortal with the webcam and processor
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(tagProcessor)
                .build();

        leftLimit = hardwareMap.get(DigitalChannel.class, "leftLimit1");
        leftLimit.setMode(DigitalChannel.Mode.INPUT);
        rightLimit = hardwareMap.get(DigitalChannel.class, "rightLimit1");
        rightLimit.setMode(DigitalChannel.Mode.INPUT);

        telemetry.addLine("Init complete");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Get current detections
        List<AprilTagDetection> detections = tagProcessor.getDetections();

        if (detections.size() > 0) {
            AprilTagDetection tag = detections.get(0);

            double yaw = tag.ftcPose.yaw;          // Tag rotation
            double bearing = tag.ftcPose.bearing;  // Horizontal offset from center

            telemetry.addData("Tag ID", tag.id);
            telemetry.addData("Yaw (deg)", yaw);
            telemetry.addData("Horizontal Offset (deg)", bearing);
            /*if(bearing > 5 && bearing <10){
                Turret.setPower(0.25);
            } else if (bearing>10) {
                Turret.setPower(0.5);
            } else if (bearing<-5 && bearing>-10) {
                Turret.setPower(-0.25);
            } else if (bearing<-10) {
                Turret.setPower(-0.5);
            }else {
                Turret.setPower(0);
            }*/
            if (bearing<10 && bearing >-10){
                Turret.setPower(0);
            }else {
                Turret.setPower(0.05*bearing);
            }
            BaringYuh=bearing;
        } else {
            telemetry.addLine("No tag detected");
            if (leftLimit.getState() == false){
                Turret.setPower(-0.15);
                BaringYuh = -0.01;
            } else if (rightLimit.getState() == false) {
                Turret.setPower(0.15);
                BaringYuh = 0.01;
            } else {
                if (BaringYuh> 0){
                    Turret.setPower(0.15);
                } else if (BaringYuh<0) {
                    Turret.setPower(-0.15);
                }else {
                    Turret.setPower(0);
                }
            }

        }

        telemetry.update();
        telemetry.addData("Bearing",BaringYuh);
    }
}
