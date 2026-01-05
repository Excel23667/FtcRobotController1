package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp(name="Limelight AprilTag Tracker", group="Sensor")
public class limelightTest extends OpMode {

    Limelight3A limelight;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        // AprilTag pipeline
        limelight.pipelineSwitch(0);

        // Start Limelight data polling
        limelight.start();
    }

    @Override
    public void loop() {

        LLResult result = limelight.getLatestResult();

        if (!result.isValid()) {
            telemetry.addLine("No data from Limelight");
            telemetry.update();
            return;
        }

        // Read AprilTag results
        List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();

        if (tags.isEmpty()) {
            telemetry.addLine("No AprilTags detected");
            telemetry.update();
            return;
        }

        LLResultTypes.FiducialResult tag = tags.get(0);

        int id = tag.getFiducialId();
        double yaw = tag.getTargetYPixels();   // ← this works only if config is correct
        /*double pitch = tag.getTargetPitch();
        double distance = tag.getTargetZ(); // in inches
        double xError = tag.getTargetX();   // ← your “bearing/x-line error”

        telemetry.addData("Tag ID", id);
        telemetry.addData("Yaw (deg)", yaw);
        telemetry.addData("Pitch (deg)", pitch);
        telemetry.addData("Distance Z (in)", distance);
        telemetry.addData("X Error (in)", xError);
        telemetry.update();*/
    }
}
