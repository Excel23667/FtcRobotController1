package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import java.util.List;

@Disabled
@Autonomous
public class CameraTest extends LinearOpMode {
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    @Override
    public void runOpMode() {
        // Create the AprilTag processor
        aprilTag = new AprilTagProcessor.Builder()
                .build();

        // Create the Vision Portal using the Logitech C270 camera
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();

        telemetry.addLine("AprilTag detection ready. Press Play.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            List<AprilTagDetection> detections = aprilTag.getDetections();

            if (detections.size() > 0) {
                for (AprilTagDetection tag : detections) {
                    telemetry.addData("Detected tag ID", tag.id);
                    telemetry.addData("X", "%.2f", tag.ftcPose.x);
                    telemetry.addData("Y", "%.2f", tag.ftcPose.y);
                    telemetry.addData("Z", "%.2f", tag.ftcPose.z);
                    telemetry.addData("Yaw", "%.2f", tag.ftcPose.yaw);
                    telemetry.addData("Pitch", "%.2f", tag.ftcPose.pitch);
                    telemetry.addData("Roll", "%.2f", tag.ftcPose.roll);
                }
            } else {
                telemetry.addLine("No AprilTags detected");
            }

            telemetry.update();
            sleep(20);
        }

        visionPortal.close();
    }
}