package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TiltTestFeedback extends LinearOpMode {

    private Servo Tilt;

    @Override
    public void runOpMode() {

        Tilt = hardwareMap.get(Servo.class, "tilt");
        Tilt.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                Tilt.setPosition(0.0);
            }
            if (gamepad1.b) {
                Tilt.setPosition(0.1575);
            }
            if (gamepad1.y) {
                Tilt.setPosition(0.2);
            }
            if (gamepad1.x){
                Tilt.setPosition(1);
            }

            telemetry.addData("Servo Position", Tilt.getPosition());
            telemetry.update();
        }
    }
}