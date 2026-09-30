package org.firstinspires.ftc.teamcode.teleop.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;

public class Intake {
    
    //Hardware stuff
    private DcMotorEx intakeMotor;

    // Power, change so it doesnt lowk explode

    public Intake(HardwareMap hardwareMap) {
        //Hardware init
        intakeMotor = hardwareMap.get(DcMotorEx.class, HardwareConstants.intake);
        
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
}