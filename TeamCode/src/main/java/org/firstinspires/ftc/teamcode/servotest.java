package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
@Disabled
@TeleOp
public class servotest extends OpMode {
    private CRServo lolServo;
    @Override
    public void init() {
        lolServo = hardwareMap.get(CRServo.class,"lolServo");
    }

    @Override
    public void loop() {
        lolServo.setPower(1);
    }
}
