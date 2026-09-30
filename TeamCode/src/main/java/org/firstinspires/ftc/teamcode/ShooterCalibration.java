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

    @Override
    public void runOpMode() {
        // hardwareMap and gamepad1 are already set up by the SDK
        shooterMotor = hardwareMap.get(DcMotorEx.class, HardwareConstants.shooterMotor);

        double power = 0.5;
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.dpadUpWasPressed())   power -= 0.05;
            if (gamepad1.dpadDownWasPressed()) power += 0.05;
            if (gamepad1.xWasPressed())        shooterMotor.setPower(power);

            sleep(30);
            telemetry.addData("Power:", power);
            telemetry.update();
        }
    }

}
