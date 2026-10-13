package org.firstinspires.ftc.teamcode.teleop.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;

public class Intake {
    
    //Hardware stuff
    private DcMotorEx intakeMotor;
    private Servo leftFlower;
    private Servo rightFlower;

    // Power, change so it doesnt lowk explode

    public Intake(HardwareMap hardwareMap) {
        //Hardware init
        intakeMotor = hardwareMap.get(DcMotorEx.class, HardwareConstants.intake);

        rightFlower = hardwareMap.get(Servo.class, HardwareConstants.rightFlower);
        leftFlower = hardwareMap.get(Servo.class, HardwareConstants.leftFlower);


        // Change intake motor if you need to 
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    //what u do to intake
    public void runIntake() {
        intakeMotor.setPower(HardwareConstants.intakePower);
    }
    
    //reversal ig
    public void reverseIntake() {
        intakeMotor.setPower(-HardwareConstants.intakePower);
    }
    
    public void stopIntake() {
        intakeMotor.setPower(0);
    }
    public void lowerLeftFlower() {leftFlower.setPosition(HardwareConstants.flowerDownPos);}
    public void lowerRightFlower() {rightFlower.setPosition(-HardwareConstants.flowerDownPos);}
    public void flowerDown() {
        rightFlower.setPosition(-HardwareConstants.flowerUpPos);
        leftFlower.setPosition(HardwareConstants.flowerUpPos);
    }
}