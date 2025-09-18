package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class Modestbob extends OpMode {

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        double LeftStickX = gamepad1.left_stick_x;
        double RightStickX = gamepad1.right_stick_x;
        double BuyMeSplatoonRaiders = LeftStickX-RightStickX;
        boolean TheAButtonIsBottomOnNontendoLayout = gamepad1.a;
        if (TheAButtonIsBottomOnNontendoLayout == true) {
            telemetry.addData("Difference",BuyMeSplatoonRaiders);
            telemetry.addData("Left Stick",LeftStickX);
            telemetry.addData("Right Stick", RightStickX);
        }
    }
}
