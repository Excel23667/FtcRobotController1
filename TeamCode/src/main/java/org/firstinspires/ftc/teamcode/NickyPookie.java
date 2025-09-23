package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class NickyPookie extends OpMode {
    @Override
    public void init() {
        telemetry.addData("I Hate","Quinton");

        int Chicken = 5;
        double Balls = 9.67;
        boolean Tractor = false;


    }

    @Override
    public void loop() {
        double leftStickY = gamepad1.left_stick_y + gamepad1.right_stick_y;
        boolean AButton = gamepad1.a;

        int count = 0;


        telemetry.addData("Nicky","Is a Cutie");
        if (leftStickY == 0 && AButton){
            telemetry.addData("Left Stick","Is STicky");
            count += 1;
        } else if (leftStickY < 0 || AButton) {
            telemetry.addData("Left STick","Is Not STicky");
        }else {
            telemetry.addData("Left STick","Is too sticky");
        }
        if (AButton){

        }

    }
}
