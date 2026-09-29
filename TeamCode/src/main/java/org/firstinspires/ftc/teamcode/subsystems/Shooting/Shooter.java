package org.firstinspires.ftc.teamcode.subsystems.Shooting;

import org.firstinspires.ftc.teamcode.subsystems.LocalisationControl.Localisation;
import org.firstinspires.ftc.teamcode.subsystems.Constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.subsystems.Shooting.Turret;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;

public class Shooter {
    private double aH = 0;
    private double tH = 0;
    private double rH = 0;
    private double rX = 0;
    private double rY = 0;
    private double sX = 0;
    private double sY = 0;
    private double rXS = 0;
    private double rYS = 0;
    private double rXF = 0;
    private double rYF = 0;
    private double bT = 0;
    private double bD = 0;
    private double bS = 0;
    private double bV = 0; //ball velocity at launch
    private double bA = 70; //ball launch angle
    private double bRPM = 0;
    private double bR = 0.036; //radius of flywheel
    private double fD = 0;
    private double shootingSpeed = 0;
    private double rZ = 20; //shooter height
    private double sZ = 77.7; //goal height
    private double lF = 1; //manual correction factor for RPM (due to compression etc.)
    private double aF = 0; //angle to add to correct for alignment issues
    private Localisation odometry;

    Turret turret;
    HardwareConstants hardwareConstants;

    DcMotorEx shooterMotor;

    public void init(Localisation l1, HardwareMap hMap) {
        shooterMotor = hMap.get(DcMotorEx.class, HardwareConstants.shooterMotor);
        odometry = l1;
    }

    public void update() {
        updateVar(odometry);
        updateCalcs();
    }

    public void updateCalcs() {
        bV = Math.sqrt(
                (9.82 * Math.pow((sX - rX), 2))
                        /
                        (((sX - rX) * Math.tan(Math.toRadians(bA)) - (sZ - rZ)) * 2 * Math.pow(Math.cos(Math.toRadians(bA)), 2))
        );
        //bV = (bRPM / 60) * 2 * 3.14159265359 * bR;
        bS = Math.cos(Math.toRadians(bA)) * bV; //bS is horizontal ball speed
        bT = bD / bS;
        rYF = rYS * bT;
        rXF = rXS * bT;
        fD = Math.hypot(sY - rYF, sX - rXF);
        tH = Math.acos(sX - rXF / fD); //angle to shoot into the goal
        aH = tH + aF - rH;
        shootingSpeed = fD / bT; // Move to Shooter subsystem. Horizontal shooting speed.
        bRPM = (shootingSpeed / Math.cos(Math.toRadians(bA))) / 2 * 3.14159265359 * bR * 60 * lF;
    }

    public double getShootingRPM() {
        return bRPM;
    }

    public double getTurretRPM() {
        return ((Math.abs(shooterMotor.getVelocity()) * 60) / ShooterConstants.SHOOTER_TICKS_PER_REV);
    }

    public void updateVar(Localisation odo) {
        rH = odo.getRH();
        rY = odo.getRY();
        rX = odo.getRX();
        rYS = odo.getRYS();
        rXS = odo.getRXS();
        bD = odo.getGD();
        sX = odo.getGDX();
        sY = odo.getGDY();
    }

    public void shoot() {
        //FIRE IN THE HOLLE
        updateVar(odometry);
        updateCalcs();
        double error = bRPM - getTurretRPM();
        turret.turnToAngle(aH);

        if (Math.abs(error) > ShooterConstants.SHOOTER_TOLERANCE) {
            double power = (error * ShooterConstants.SHOOTER_P) + (ShooterConstants.SHOOTER_F);
            shooterMotor.setPower(power);
        } else {
            shooterMotor.setPower(0);
        }
    }
    public void shootAtRpm(double rpm) {
        double error = rpm - getTurretRPM();
        if (Math.abs(error) > ShooterConstants.SHOOTER_TOLERANCE) {
            double power = (error * ShooterConstants.SHOOTER_P) + (ShooterConstants.SHOOTER_F);
            shooterMotor.setPower(power);
        } else {
            shooterMotor.setPower(0);
        }
    }
}