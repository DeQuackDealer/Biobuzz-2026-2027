package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;

public class Drivebase {
    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;
    public Drivebase(HardwareMap map) {

        frontLeftMotor = map.get(DcMotor.class, HardwareConstants.flmotor);
        backLeftMotor = map.get(DcMotor.class, HardwareConstants.blmotor);
        frontRightMotor = map.get(DcMotor.class,HardwareConstants.frmotor);
        backRightMotor = map.get(DcMotor.class, HardwareConstants.brmotor);

        frontLeftMotor.setDirection(HardwareConstants.flreverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(HardwareConstants.blreverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(HardwareConstants.frreverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(HardwareConstants.brreverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
    }

    public void drive(double forward, double strafe, double turn) {
        double frontLeftSpeed = forward - turn - strafe;
        double backLeftSpeed = forward - turn + strafe;
        double frontRightSpeed = forward + turn + strafe;
        double backRightSpeed = forward + turn - strafe;

        frontLeftMotor.setDirection(HardwareConstants.flreverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(HardwareConstants.blreverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(HardwareConstants.frreverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(HardwareConstants.brreverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);

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