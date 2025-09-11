package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@Disabled
@TeleOp
public class VaribaleTutorial extends OpMode {
    /* data types
    int robotHeight;
    double motorSpeed;
    decimal
    boolean clawClosed;
    yes or no

    robotHeight = 10;
    motorSpeed = 0.5;
    clawClosed = true;
     */

    @Override
    public void init() {
        int teamNumber = 23667;
        double motorSpeed = 0.75;
        boolean clawClosed = true;
        String name = "Nano Ninjas";
        int motorAngle = 67;

        telemetry.addData("Team Number",teamNumber);
        telemetry.addData("Motor Speed",motorSpeed);
        telemetry.addData("Claw Closed",clawClosed);
        telemetry.addData("Name",name);
        telemetry.addData("Motor Angle",motorAngle);
    }

    @Override
    public void loop() {
        /*
        1. change the string variable name to team name
        2. create an integer variable called motor angle between 0-100, display this in your init method.
         */

    }
}
