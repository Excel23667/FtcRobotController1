package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
@Disabled
@TeleOp
public class TurretTestDoubleYuh extends OpMode {

    private CRServo turret;
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    @Override
    public void init() {
        turret = hardwareMap.get(CRServo.class, "turret");

        aprilTag = new AprilTagProcessor.Builder().build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")) // Logitech C270
                .addProcessor(aprilTag)
                .build();

    }

    @Override
    public void loop() {
        List<AprilTagDetection> detections = aprilTag.getDetections();

        if (detections.size()>0){
            AprilTagDetection tag =detections.get(0);
            double yawDeg = tag.ftcPose.yaw;

            telemetry.addData("Tag Seen", true);
            telemetry.addData("Tag ID", tag.id);
            telemetry.addData("Tag Name", tag.metadata != null ? tag.metadata.name : "Unknown");
            telemetry.addData("Yaw (deg)",yawDeg);
        }else {
            telemetry.addLine("No Tag Detected");
        }
        telemetry.update();
    }
}
