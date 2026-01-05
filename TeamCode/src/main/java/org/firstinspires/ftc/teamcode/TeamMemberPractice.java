package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@Disabled
@TeleOp
public class TeamMemberPractice extends OpMode {
    boolean initDone;
    @Override
    public void init() {
        telemetry.addData("Init",initDone);
        initDone = true;
    }

    double squareInputWithSign(double input){
        double output = input*input;

        if (input<0){
            output *= -1;
        }

        return output;
    }

    @Override
    public void loop() {
        telemetry.addData("Init",initDone);

        double yaxis = gamepad1.left_stick_y;
        telemetry.addData("Left Stick Normal",yaxis);

        yaxis = squareInputWithSign(yaxis);
        telemetry.addData("Left Stick Modified",yaxis);
    }
}
