package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;

public class Transfer {
    DcMotor transferMotor;

    public Transfer(HardwareMap map) {
        transferMotor = map.get(DcMotor.class, HardwareConstants.transfer);
        transferMotor.setDirection(HardwareConstants.transferReverse ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
    }

    public void runTransfer(boolean transferBool){
        if (transferBool) {
            transferMotor.setPower(1);
        } else {
            transferMotor.setPower(0);
        }
    }

    public void reverseTransfer(boolean reverseTransferBool) {
        if (reverseTransferBool) {
            transferMotor.setPower(-1);
        } else {
            transferMotor.setPower(0);
        }
    }
}
