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
    public double getTotHeading() {
        return (turretMotor.getCurrentPosition() / ShooterConstants.TURRET_TICKS_PER_REV / ShooterConstants.TURRET_GEAR_RATIO) * 360;
    }
    public double getCurHeading() {
        double heading = getTotHeading() % 360;
        if (heading < 0) {
            heading += 360;
        }
        return heading;
    }

    public void rightTurret(){
        if (getTotHeading() < HardwareConstants.TURRET_LIMIT_UP) {
            targetHeading += 5;
            holdingHeading = true;
        } else {
            stop();
        }
    }

    public void leftTurret() {
        if (getTotHeading() > HardwareConstants.TURRET_LIMIT_DOWN) {
            targetHeading -= 5;
            holdingHeading = true;
        } else {
            stop();
        }
    }

    public void stop() {
        holdingHeading = false;
        turretMotor.setPower(0);
    }

    public void turnToHeading(double target_heading) {
        double normTarget = target_heading % 360;
        if (normTarget < 0) normTarget += 360;

        double currentAbs = getCurHeading();
        double currentTot = getTotHeading();
        double error = normTarget - currentAbs;
        if (error > 180)  error -= 360;
        if (error < -180) error += 360;
        
        double poTarget = currentTot + error;

        if (poTarget > HardwareConstants.TURRET_LIMIT_UP) {
            targetHeading = poTarget - 360; 
        } else if (poTarget < HardwareConstants.TURRET_LIMIT_DOWN) {
            targetHeading = poTarget + 360;
        } else {
            targetHeading = poTarget;      
        }

        holdingHeading = true;
    }

    public boolean atTargetHeading() {
        return Math.abs(targetHeading - getTotHeading()) <= ShooterConstants.TURRET_TOLERANCE;
    }

    public void update() {
        if (!holdingHeading) return;

        double currentTot = getTotHeading();
        if (currentTot >= HardwareConstants.TURRET_LIMIT_UP || currentTot <= HardwareConstants.TURRET_LIMIT_DOWN) {
            stop();
            return;
        }

        if (atTargetHeading()) {
            turretMotor.setPower(0);
        } else if (currentTot < targetHeading) {
            turretMotor.setPower(ShooterConstants.TURRET_POWER);
        } else if (currentTot > targetHeading) {
            turretMotor.setPower(-ShooterConstants.TURRET_POWER);
        }
    }
}
