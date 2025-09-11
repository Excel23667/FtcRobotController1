package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@Disabled
@TeleOp
public class IfPractice extends OpMode {

    @Override
    public void init() {

    }

    @Override
    public void loop() {
     /*   boolean aButton = gamepad1.a;//Press True; Depress False
        //returns true or false
        if(aButton){
            telemetry.addData("A Button","Pressed");
        }
        else {
            telemetry.addData("A Button","Not Pressed");
        }
        telemetry.addData("A Button",aButton); */

        double motorSpeed = -gamepad1.left_stick_y;
        /*if (lefty < 0){
            telemetry.addData("Left Stick","is Negative");
        } else if (lefty==0) {
            telemetry.addData("Left Stick","is 0");
        } else {
            telemetry.addData("Left Stick","is Positive");
        }*/

        /*if (lefty < 0.1 && lefty > -0.1){
            telemetry.addData("Left Stick","In Dead Zone");
        }*/

        if (!gamepad1.a){
            motorSpeed *= 0.5;
        }

        telemetry.addData("Left Stick Value",motorSpeed);

    }
}
/*
AND - && - if (lefty < 0.5 && lefty > 0)
OR - || - if (lefty < 0 || righty < 0)
NOT - ! - if (!clawClosed)
 */