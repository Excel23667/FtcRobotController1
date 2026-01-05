package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@Disabled
@TeleOp
public class GamepadPractice extends OpMode {

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        //runs 50 times a second
        double forwardSpeed = -gamepad1.left_stick_y/2.0;
        double difXY = Math.abs(gamepad1.left_stick_x - gamepad1.right_stick_x);
        double sumTrig = gamepad1.right_trigger+gamepad1.left_trigger;

        telemetry.addData("LeftStickX",gamepad1.left_stick_x);
        telemetry.addData("LeftStickY",forwardSpeed);
        telemetry.addData("RightStickX",gamepad1.right_stick_x);
        telemetry.addData("RightStickY",gamepad1.right_stick_y);
        telemetry.addData("A Button",gamepad1.a);
        telemetry.addData("B Button",gamepad1.b);
        telemetry.addData("RightTrigger",gamepad1.right_trigger);
        telemetry.addData("Dif of X on Right & X on Left",difXY);
        telemetry.addData("SumOfTriggers",sumTrig);

        /*
        add telemetry for right joystick
        add telemtry for B button
        add telemetry data to report differenece between x on left joy stick and x on right joystick
        add telemtry data to report the sum of both rear triggers
         */
    }
}
