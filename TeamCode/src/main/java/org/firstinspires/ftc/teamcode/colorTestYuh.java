package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import android.graphics.Color;

@TeleOp
public class colorTestYuh extends OpMode {

    private ColorSensor colorSensor;

    private final float[] hsv = new float[3];
    private float hue = 0;

    @Override
    public void init() {
        // Initialize the sensor from the hardware map
        colorSensor = hardwareMap.get(ColorSensor.class, "color");

        telemetry.addLine("Color Sensor Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        // Convert RGB to HSV (multiply for better accuracy)
        Color.RGBToHSV(
                colorSensor.red() * 8,
                colorSensor.green() * 8,
                colorSensor.blue() * 8,
                hsv
        );

        hue = hsv[0]; // main color value (0–360)

        telemetry.addData("Red", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue", colorSensor.blue());
        telemetry.addData("Alpha", colorSensor.alpha());
        telemetry.addData("Hue", hue);

        // --- Color detection ---
        if (hue > 200 && hue < 235) {
            telemetry.addLine(">>> PURPLE detected!");
        }
        else if (hue > 125 && hue < 165) {
            telemetry.addLine(">>> GREEN detected!");
        }
        else {
            telemetry.addLine("Unknown color");
        }

        telemetry.update();
    }
}
