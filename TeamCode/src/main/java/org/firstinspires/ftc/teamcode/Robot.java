package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Constants.PedroConstants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.LocalisationControl.Localisation;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.Shooting.Turret;
import org.firstinspires.ftc.teamcode.subsystems.Shooting.Shooter;
import org.firstinspires.ftc.teamcode.teleop.subsystems.Intake;

public class Robot {
    Drivebase drivebase;
    Transfer transfer;
    Shooter shooter;
    Localisation localisation;
    Intake intake;
    Turret turret;
    Gamepad gamepad1;
    Gamepad gamepad2;

    public Follower follower1;

    public Robot (HardwareMap map, Gamepad gamepad1, Gamepad gamepad2){
        drivebase = new Drivebase(map);
        transfer = new Transfer(map);
        intake = new Intake(map);
        turret = new Turret(map);
        follower1 = PedroConstants.createFollower(map);
        localisation = new Localisation(follower1, true);
        shooter = new Shooter(map, localisation, turret);

        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void driveWithGamepad1() {
        if (abs(gamepad2.left_stick_x + gamepad2.left_stick_y + gamepad2.right_stick_x) < 0.1) {
            double forward = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            boolean controlSpeed = gamepad1.x;

            if (controlSpeed) {
                drivebase.drive(forward * 0.3, strafe * 0.3, turn * 0.3);
            } else {
                drivebase.drive(forward, strafe, turn);
            }
        }
        if (gamepad1.right_trigger > 0.2) {
            shooter.shoot();
        } else {
            shooter.stopShoot(0.2);
        }
        if (gamepad1.leftBumperWasPressed()) turret.leftTurret();
        if (gamepad1.leftBumperWasReleased()) turret.stop();
        if (gamepad1.rightBumperWasPressed()) turret.rightTurret();
        if (gamepad1.rightBumperWasReleased()) turret.stop();
    }
    //imma put all changes on jst drive w g1, too cbz
    public void driveWithGamepad2() {
        double forward = gamepad2.left_stick_y;
        double strafe = gamepad2.left_stick_x;
        double turn = gamepad2.right_stick_x;

        if (abs(forward) > 0.1 || abs(strafe) > 0.1 || abs(turn) > 0.1) {
            drivebase.drive(forward * 0.3, strafe * 0.3, turn * 0.3);
        }
        if (gamepad2.right_trigger > 0.2) {
            shooter.shoot();
        } else {
            shooter.stopShoot(0.2);
        }
    }
}
