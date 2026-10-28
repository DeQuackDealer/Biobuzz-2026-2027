package org.firstinspires.ftc.teamcode.subsystems.Constants;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class HardwareConstants {
    //shooter/turret stuff
    public static String shooterMotor = "shooterMotor";
    public static String turretMotor = "turret1";
    public static double turret_no_power = 0.0;
    //drivebase
    public static boolean flreverse = false;
    public static boolean blreverse = true;
    public static boolean frreverse = true;
    public static boolean brreverse = false;
    public static String frmotor = "fr";
    public static String blmotor = "bl";
    public static String flmotor = "fl";
    public static String brmotor = "br";
    //intake/transfer stuff
    public static String intake = "intakeMotor";
    public static double intakePower = 0.8;
    public static String intakeDirection = "REVERSE";
    public static String rightFlower = "rightFlower";
    public static String leftFlower = "leftFlower";
    public static double flowerDownPos = 1;
    public static double flowerUpPos = 0;
    public static String transfer = "transferMotor";
    public static boolean transferReverse = false;
    public static int TURRET_LIMIT = 540;
}
