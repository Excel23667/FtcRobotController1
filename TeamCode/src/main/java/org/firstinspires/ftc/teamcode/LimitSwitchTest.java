package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp
public class LimitSwitchTest extends OpMode {

    private DigitalChannel limitSwitch;

    @Override
    public void init() {
        // Initialize limit switch on D7
        limitSwitch = hardwareMap.get(DigitalChannel.class, "limitSwitch1");
        limitSwitch.setMode(DigitalChannel.Mode.INPUT);

        telemetry.addData("Status", "Initialized - Ready to Test");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Check if limit switch is pressed
        if (limitSwitch.getState() == false) { // pressed
            telemetry.addData("Limit Switch", "Pressed!");
        } else {
            telemetry.addData("Limit Switch", "Not Pressed");
        }

        telemetry.update();
    }
}
