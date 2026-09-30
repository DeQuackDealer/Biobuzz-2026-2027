package org.firstinspires.ftc.teamcode.subsystems.Shooting;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;
import org.firstinspires.ftc.teamcode.subsystems.Constants.ShooterConstants;

public class Turret {
    DcMotorEx turretMotor;

    final boolean REVERSETURRET = true;

    private double targetHeading = 0;
    private boolean holdingHeading = false;

    public Turret (HardwareMap map){
        turretMotor = map.get(DcMotorEx.class, HardwareConstants.turretMotor);

        turretMotor.setDirection(REVERSETURRET ? DcMotorEx.Direction.REVERSE : DcMotorEx.Direction.FORWARD);

    }

    // Manual controls cancel any active turnToHeading target
    public void rightTurret(){holdingHeading = false; turretMotor.setPower(ShooterConstants.TURRET_SPEED);}
    public void leftTurret() {holdingHeading = false; turretMotor.setPower(-ShooterConstants.TURRET_SPEED);}
    public void stop() {holdingHeading = false; turretMotor.setPower(0);}
    public double getTurretHeading() {
        return (turretMotor.getCurrentPosition() / ShooterConstants.TURRET_TICKS_PER_REV / ShooterConstants.TURRET_GEAR_RATIO) * 360;
    }

    // Sets the target heading; update() must be called every loop to actually drive the turret there
    public void turnToHeading(double target_heading) {
        targetHeading = target_heading;
        holdingHeading = true;
        update();
    }

    public boolean atTargetHeading() {
        return Math.abs(targetHeading - getTurretHeading()) <= ShooterConstants.TURRET_TOLERANCE;
    }

    public void update() {
        if (!holdingHeading) return;

        double error = targetHeading - getTurretHeading();

        if (Math.abs(error) > ShooterConstants.TURRET_TOLERANCE) {
            double power = (error * ShooterConstants.TURRET_P) + (Math.signum(error) * ShooterConstants.TURRET_F);
            turretMotor.setPower(Math.max(-1, Math.min(1, power)));
        } else {
            turretMotor.setPower(0);
        }
    }
}
