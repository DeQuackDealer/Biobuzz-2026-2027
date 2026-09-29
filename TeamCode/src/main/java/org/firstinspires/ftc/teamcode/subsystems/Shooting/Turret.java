package org.firstinspires.ftc.teamcode.subsystems.Shooting;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.subsystems.Constants.ShooterConstants;

public class Turret {
    DcMotorEx turretMotor;

    final boolean REVERSETURRET = true;

    public Turret (HardwareMap map){
        turretMotor = map.get(DcMotorEx.class, "turret1");

        turretMotor.setDirection(REVERSETURRET ? DcMotorEx.Direction.REVERSE : DcMotorEx.Direction.FORWARD);

    }

    public void rightTurret(boolean rightTurretBool){
        if (rightTurretBool) {
            turretMotor.setPower(1);
        } else {
            turretMotor.setPower(0);
        }
    }

    public void leftTurret(boolean leftTurretBool) {
        if (leftTurretBool) {
            turretMotor.setPower(-1);
        } else {
            turretMotor.setPower(0);
        }
    }
    public double getTurretHeading() {
        return ((turretMotor.getCurrentPosition() / ShooterConstants.TURRET_TICKS_PER_REV) / 2.0) * 360;
    }
    public void turnToAngle(double target_angle) {
        double error = target_angle - getTurretHeading();

        if (Math.abs(error) > ShooterConstants.SHOOTER_TOLERANCE) {
            double power = (error * ShooterConstants.SHOOTER_P) + (ShooterConstants.SHOOTER_F);
            turretMotor.setPower(power);
        } else {
            turretMotor.setPower(0);
        }
    }
}
