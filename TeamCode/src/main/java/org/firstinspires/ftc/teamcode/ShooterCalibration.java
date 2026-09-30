package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;
import org.firstinspires.ftc.teamcode.subsystems.Constants.PedroConstants;
import org.firstinspires.ftc.teamcode.subsystems.LocalisationControl.Localisation;
import org.firstinspires.ftc.teamcode.subsystems.Shooting.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Shooting.Turret;

import com.arcrobotics.ftclib.util.Direction;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
@TeleOp(name="ShoooterCalib", group="Linear OpMode")
public class ShooterCalibration extends LinearOpMode {
    //Shooter shooter;
    DcMotorSimple shooterMotor;
    Turret turret;
   // Localisation localisation;
   // public Follower follower1;

    @Override
    public void runOpMode() {
      //  follower1 = PedroConstants.createFollower(hardwareMap);
      //  localisation = new Localisation(follower1, true);
       // shooter = new Shooter(hardwareMap, localisation, turret);
        shooterMotor = hardwareMap.get(DcMotorEx.class, HardwareConstants.shooterMotor);
        turret = new Turret(hardwareMap);

        double rpm = 0.75;
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.dpadUpWasPressed())   rpm -= 0.05;
            if (gamepad1.dpadDownWasPressed()) rpm += 0.05;
            if (gamepad1.xWasPressed()) shooterMotor.setPower(rpm);//shooter.shootAtRpm(rpm);
            if (gamepad1.leftBumperWasPressed()) turret.leftTurret();
            if (gamepad1.leftBumperWasReleased()) turret.stop();
            if (gamepad1.aWasPressed()) turret.rightTurret();
            if (gamepad1.aWasReleased()) turret.stop();
            if (gamepad1.bWasPressed()) turret.turnToHeading(0);
            turret.update();
            sleep(30);
            telemetry.addData("rpm:", rpm);
            telemetry.addData("Turret Heading:", turret.getTurretHeading());
            telemetry.update();
        }
    }

}
