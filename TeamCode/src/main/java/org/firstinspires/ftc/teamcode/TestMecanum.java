
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
@Disabled
@TeleOp(name = "TestMecanum (Blocks to Java)")

public class TestMecanum extends LinearOpMode {

    private DcMotor FrontLeft;
    private DcMotor BackRight;
    private DcMotor Backleft;
    private DcMotor FrontRight;

    /**
     * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
     * Comment Blocks show where to place Initialization code (runs once, after touching the
     * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
     * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
     * Stopped).
     */
    @Override
    public void runOpMode() {
        float y;
        double x;
        double rx;
        double denominator;

        FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        BackRight = hardwareMap.get(DcMotor.class, "Back Right");
        Backleft = hardwareMap.get(DcMotor.class, "Back left");
        FrontRight = hardwareMap.get(DcMotor.class, "Front Right");

        // Put initialization blocks here.
        waitForStart();
        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        Backleft.setDirection(DcMotor.Direction.REVERSE);
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                // Put loop blocks here.
                telemetry.addData("Left Stick X",gamepad1.left_stick_x);
                telemetry.addData("Right Stick X",gamepad1.right_stick_y);
                telemetry.addData("Right Stick Y",gamepad1.right_stick_y);
                telemetry.update();
                y = -gamepad1.right_stick_y * 1;
                x = gamepad1.right_stick_x * 1.1 * -1;
                rx = -gamepad1.left_stick_x * 0.6 * 1;
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
                Backleft.setPower((y + x + rx) / denominator);
                FrontLeft.setPower(((y - x) + rx) / denominator);
                BackRight.setPower(((y - x) - rx) / denominator);
                FrontRight.setPower(((y + x) - rx) / denominator);
            }
        }
    }
}