package org.firstinspires.ftc.teamcode.subsystems.Shooting;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;
import org.firstinspires.ftc.teamcode.subsystems.Constants.ShooterConstants;

public class Turret {
    private final DcMotorEx turretMotor;
    private final boolean REVERSETURRET = true;

    private double targetHeading = 0;
    private boolean holdingHeading = false;

    public Turret (HardwareMap map){
        turretMotor = map.get(DcMotorEx.class, HardwareConstants.turretMotor);
        turretMotor.setDirection(REVERSETURRET ? DcMotorEx.Direction.REVERSE : DcMotorEx.Direction.FORWARD);
        
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void rightTurret(){
        if (getTurretHeading() < HardwareConstants.TURRET_LIMIT) {
            holdingHeading = false; 
            turretMotor.setPower(ShooterConstants.TURRET_SPEED);
        } else {
            stop();
        }
    }

    public void leftTurret() {
        if (getTurretHeading() > -HardwareConstants.TURRET_LIMIT) {
            holdingHeading = false; 
            turretMotor.setPower(-ShooterConstants.TURRET_SPEED);
        } else {
            stop();
        }
    }

    public void stop() {
        holdingHeading = false; 
        turretMotor.setPower(0);
    }

    public double getTurretHeading() {
        return (turretMotor.getCurrentPosition() / ShooterConstants.TURRET_TICKS_PER_REV / ShooterConstants.TURRET_GEAR_RATIO) * 360;
    }

    public void turnToHeading(double target_heading) {
        targetHeading = target_heading;
        holdingHeading = true;
    }

    public boolean atTargetHeading() {
        return Math.abs(targetHeading - getTurretHeading()) <= ShooterConstants.TURRET_TOLERANCE;
    }

    public void update() {
        if (!holdingHeading) return;
        double currentHeading = getTurretHeading();
        if (atTargetHeading()) {
            turretMotor.setPower(0);
            holdingHeading = true;
        } else if (currentHeading < targetHeading - ShooterConstants.TURRET_TOLERANCE) {
            turretMotor.setPower(ShooterConstants.TURRET_POWER);
        } else if (currentHeading > targetHeading + ShooterConstants.TURRET_TOLERANCE) {
            turretMotor.setPower(-ShooterConstants.TURRET_POWER);
        }
    }

}
