package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@Disabled
@TeleOp
public class QuintonMessier extends OpMode {

    int chicken = 64;
    double wowza = 5.473476834;
    boolean hooray = false;
    int counter = 0;

    double LeftStickX = gamepad1.left_stick_x;
    boolean a = gamepad1.a;

    @Override
    public void init() {
        double hehe = 3.14 / 29;
        counter =1;
        telemetry.addData("Quinton","Is A Monke");
        if(LeftStickX == 1){
            telemetry.addData("WOWZA",counter);
        }
    }

    @Override
    public void loop() {
        telemetry.addData("Mrs. Fehr","Is a Bully");
        if(counter != 34 && counter>62){
            telemetry.addData("MONKE","HEHE");
        } else if (counter > 0 || counter < 99){
            telemetry.addData("Lamar","Jackson");
        }

    }
}
