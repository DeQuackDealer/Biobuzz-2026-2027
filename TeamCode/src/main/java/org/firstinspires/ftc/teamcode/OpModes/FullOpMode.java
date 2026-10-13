package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name="FullOpMode", group="Linear OpMode")
public class FullOpMode extends LinearOpMode {
    Robot robot;

    @Override
    public void runOpMode() {
        robot = new Robot(hardwareMap, gamepad1, gamepad2);

        waitForStart();
        while (opModeIsActive()) {
            robot.driveWithGamepad1();
            robot.driveWithGamepad2();
        }
    }
}
