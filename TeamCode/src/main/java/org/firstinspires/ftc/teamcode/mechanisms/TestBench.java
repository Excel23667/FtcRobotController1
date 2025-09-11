package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TestBench {
    private DigitalChannel touchSensor; //touchSensorOnClaw

    public void init(HardwareMap hwMap){
        touchSensor = hwMap.get(DigitalChannel.class,"touch");
        touchSensor.setMode(DigitalChannel.Mode.INPUT);

    }

    public boolean getTouchSenorState(){
        return touchSensor.getState();
    }
}
