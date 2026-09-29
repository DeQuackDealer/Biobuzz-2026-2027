package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivebase {
    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;

    final boolean FRONT_LEFT_REVERSE = false;
    final boolean BACK_LEFT_REVERSE = true;
    final boolean FRONT_RIGHT_REVERSE = true;
    final boolean BACK_RIGHT_REVERSE = false;
    public Drivebase(HardwareMap map) {

        frontLeftMotor = map.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor = map.get(DcMotor.class, "backLeftMotor");
        frontRightMotor = map.get(DcMotor.class, "frontRightMotor");
        backRightMotor = map.get(DcMotor.class, "backRightMotor");

        frontLeftMotor.setDirection(FRONT_LEFT_REVERSE ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(BACK_LEFT_REVERSE ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(FRONT_RIGHT_REVERSE ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(BACK_RIGHT_REVERSE ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
    }

    public void drive(double forward, double strafe, double turn) {
        double frontLeftSpeed = forward - turn - strafe;
        double backLeftSpeed = forward - turn + strafe;
        double frontRightSpeed = forward + turn + strafe;
        double backRightSpeed = forward + turn - strafe;

        frontLeftMotor.setDirection(FRONT_LEFT_REVERSE ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(BACK_LEFT_REVERSE ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(FRONT_RIGHT_REVERSE ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(BACK_RIGHT_REVERSE ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);

        double max1 = Math.max(Math.abs(frontLeftSpeed), Math.abs(backLeftSpeed));
        double max2 = Math.max(Math.abs(frontRightSpeed), Math.abs(backRightSpeed));
        double max3 = Math.max(max1, max2);

        if (max3 < 1) {
            max3 = 1;
        }

        frontLeftMotor.setPower(frontLeftSpeed/max3);
        backLeftMotor.setPower(backLeftSpeed/max3);
        frontRightMotor.setPower(frontRightSpeed/max3);
        backRightMotor.setPower(backRightSpeed/max3);
    }

    public void stop() {
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);
    }

    public void test(boolean a, boolean b, boolean x, boolean y) {
        if (a){
            frontLeftMotor.setPower(0.3);
        }
        if (b){
            frontRightMotor.setPower(0.3);
        }
        if (x){
            backLeftMotor.setPower(0.3);
        }
        if (y){
            backRightMotor.setPower(0.3);
        }
    }
}