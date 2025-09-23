package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class Quinolus extends OpMode {

    private DcMotor motor1;
    private CRServo servo1;

    @Override
    public void init() {
        motor1 = hardwareMap.get(DcMotor.class,"motor1");
        servo1 = hardwareMap.get(CRServo.class,"servo1");
        motor1.setDirection(DcMotor.Direction.FORWARD);
        servo1.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double Fish = gamepad1.left_stick_y;

        motor1.setPower(Fish);
    }
}
