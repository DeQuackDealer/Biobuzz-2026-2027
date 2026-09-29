package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;
import org.firstinspires.ftc.teamcode.subsystems.LocalisationControl.Localisation;
import org.firstinspires.ftc.teamcode.subsystems.Shooting.Shooter;

import com.arcrobotics.ftclib.util.Direction;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
@TeleOp(name="ShoooterCalib", group="Linear OpMode")
public class ShooterCalibration extends LinearOpMode {
    Shooter shooter;
    DcMotorEx shooterMotor;
    Gamepad gamepad1;
    public void init(HardwareMap hMap, Gamepad gamepad1) {
        shooterMotor = hMap.get(DcMotorEx.class, HardwareConstants.shooterMotor);
        this.gamepad1 = gamepad1;
    }
    @Override
    public void runOpMode() {
        double power = 0.5;
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.aWasPressed()) {
                power = power - 0.05;
            }
            if (gamepad1.bWasPressed()) {
                power = power + 0.05;
            }
            if (gamepad1.xWasPressed()) {
                shooterMotor.setPower(power);
            }
            sleep(30);
            telemetry.addData('Power:', power);
            telemetry.update();
        }
    }

}