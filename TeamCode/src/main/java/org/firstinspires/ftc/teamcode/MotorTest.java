package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class MotorTest extends OpMode {

    private DcMotor Motor;

    //boolean trig = gamepad1.a;


    @Override
    public void init() {
        Motor = hardwareMap.get(DcMotor.class,"heheMotor");
        Motor.setDirection(DcMotor.Direction.FORWARD);

    }

    @Override
    public void loop() {
        boolean trig = gamepad1.right_bumper;
        double rightsticky = gamepad1.right_stick_y;
        Motor.setPower(0);
        telemetry.addData("Right Bumper", trig);
        telemetry.addData("Right Stick Y", rightsticky);
        if (trig){
            Motor.setPower(rightsticky);
        }
    }
}
