package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TicksMeasure extends LinearOpMode {

    private DcMotor turret;

    @Override
    public void runOpMode() throws InterruptedException {

        turret = hardwareMap.get(DcMotor.class, "Turret");
        turret.setDirection(DcMotor.Direction.REVERSE);
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while (opModeIsActive()){
            telemetry.addData("Ticks",turret.getCurrentPosition());
            telemetry.update();
        }

    }
}
