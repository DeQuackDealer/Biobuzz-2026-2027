package org.firstinspires.ftc.teamcode.teleop.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    
    //Hardware stuff
    private DcMotorEx intakeMotor;

    // Power, change so it doesnt lowk explode
    private final double INTAKE_POWER = 0.8; 

    public Intake(HardwareMap hardwareMap) {
        //Hardware init
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        
        // Change intake motor if you need to 
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    //what u do to intake
    public void runIntake() {
        intakeMotor.setPower(INTAKE_POWER);
    }
    
    //reversal ig
    public void reverseIntake() {
        intakeMotor.setPower(-INTAKE_POWER);
    }
    
    public void stopIntake() {
        intakeMotor.setPower(0);
    }
}