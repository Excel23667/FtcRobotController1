package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@Disabled
@TeleOp
public class MotorTest extends OpMode {

    private DcMotor heheMotor;
    private CRServo lolServo;
    public boolean tof;

    //boolean trig = gamepad1.a;


    @Override
    public void init() {
        heheMotor = hardwareMap.get(DcMotor.class,"heheMotor");
        lolServo = hardwareMap.get(CRServo.class,"lolServo");
        heheMotor.setDirection(DcMotor.Direction.FORWARD);
        lolServo.setDirection(CRServo.Direction.FORWARD);
        boolean tof = false;

    }

    @Override
    public void loop() {
        boolean trig = gamepad1.right_bumper;
        double rightsticky = gamepad1.right_stick_y;
        double leftsticky = gamepad1.left_stick_y;
        boolean aB = gamepad1.a;

        heheMotor.setPower(0);
        lolServo.setPower(0);
        telemetry.addData("TOF Value",tof);
        /*
        heheMotor.setPower(0);

        lolServo.setPower(leftsticky);
        telemetry.addData("Right Bumper", trig);
        */
        telemetry.addData("Right Stick Y", rightsticky);
        /*
        telemetry.addData("Left Stick Y",leftsticky);
        if (trig){
            heheMotor.setPower(rightsticky);
            lolServo.setPower(0);
        }
        */
        if (aB){
            if (!tof){
                tof = true;
                telemetry.addData("TOF","TRUE");
            } else if (tof) {
                tof = false;
                telemetry.addData("TOF","FALSE");
            }
        }
        if (tof){
            while (tof){
                lolServo.setPower(rightsticky);
                heheMotor.setPower(0);
                telemetry.addData("Servo","Activated");
                if (aB){
                    tof = false;
                    telemetry.addData("TOF","FALSE");
                }
            }
        } else if (!tof) {
            while (!tof){
                heheMotor.setPower(rightsticky);
                lolServo.setPower(0);
                telemetry.addData("Motor","Activated");
                if (aB){
                    tof = true;
                    telemetry.addData("TOF","TRUE");
                }
            }
        }
    }
}
