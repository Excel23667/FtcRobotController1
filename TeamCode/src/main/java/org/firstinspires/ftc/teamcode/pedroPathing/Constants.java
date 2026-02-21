package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(14.696)
            .forwardZeroPowerAcceleration(67.26425963874878)
            .lateralZeroPowerAcceleration(46.788039320097205)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.045,0,0.0012,0.0425))
            .headingPIDFCoefficients(new PIDFCoefficients(0.35,0, 0.0015,0.08))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.013,0,0.0005,0.6,0.055))
            .centripetalScaling(0.00045)
            ;
    public static PathConstraints pathConstraints = new PathConstraints(0.99,
            100,
            1.8,
            1);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("Front Right")
            .rightRearMotorName("Back Right")
            .leftRearMotorName("Back Left")
            .leftFrontMotorName("Front Left")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .xVelocity(66.62575404850517)
            .yVelocity(50.15650375246063);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(1.142)
            .strafePodX(-7.75)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .pinpointLocalizer(localizerConstants)
                .mecanumDrivetrain(driveConstants)
                .build();
    }
}
