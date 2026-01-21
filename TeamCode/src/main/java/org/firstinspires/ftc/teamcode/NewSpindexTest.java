package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@Autonomous
public class NewSpindexTest extends LinearOpMode {

    private Servo spindex;

    @Override
    public void runOpMode() throws InterruptedException {
        spindex = hardwareMap.get(Servo.class, "Spindex");

        waitForStart();
        spindex.setPosition(0.167);
        sleep(5000);
        spindex.setPosition(0.5);
        sleep(5000);
        spindex.setPosition(0.833);
        sleep(5000);
        spindex.setPosition(0.333);
        sleep(5000);
        spindex.setPosition(0.667);
        sleep(5000);
        spindex.setPosition(1);
        sleep(5000);

    }
}
