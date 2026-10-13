package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.AutomationStuff.Code.AutoIntake;
import org.firstinspires.ftc.teamcode.subsystems.AutomationStuff.Paths.REDAUTO;
import org.firstinspires.ftc.teamcode.subsystems.Constants.AutomationConstants;
import org.firstinspires.ftc.teamcode.subsystems.Constants.HardwareConstants;
import org.firstinspires.ftc.teamcode.subsystems.Constants.PedroConstants;
import org.firstinspires.ftc.teamcode.subsystems.Constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.subsystems.LocalisationControl.Localisation;
import org.firstinspires.ftc.teamcode.subsystems.Shooting.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Shooting.Turret;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

@Autonomous(name = "RedAuto", group = "Auto")
public class RedAuto extends OpMode {

    private enum State {
        SHOOT_PRELOAD,      //shoot at start
        TO_PATH1,           //intake runnin
        TO_PATH2,           //flywheel ramping
        SHOOT_PATH2,
        TO_PATH3,           //intake running
        TO_PATH4,           //same as above
        TO_PATH5,           //same as above
        AUTO_INTAKE_SEARCH, //ded ahh self explantory
        AUTO_INTAKE_FOLLOW, //driving on the auto intake path
        TO_PATH6,           //flywheel ramping
        SHOOT_PATH6,
        TO_PATH7,           //intake runnin
        DONE
    }

    private Follower follower;
    private Localisation localisation;
    private Shooter shooter;
    private Turret turret;
    private Transfer transfer;
    private Intake intake;
    private AutoIntake autoIntake;
    private final REDAUTO paths = new REDAUTO();

    private State state = State.SHOOT_PRELOAD;
    private final ElapsedTime shotTimer = new ElapsedTime();
    private boolean feeding = false;

    @Override
    public void init() {
        follower = PedroConstants.createFollower(hardwareMap);
        follower.setStartingPose(paths.getStartPose());
        localisation = new Localisation(follower, false);
        turret = new Turret(hardwareMap);
        shooter = new Shooter(hardwareMap, localisation, turret);
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        autoIntake = new AutoIntake(hardwareMap);
    }

    @Override
    public void start() {
        setState(State.SHOOT_PRELOAD);
    }

    @Override
    public void loop() {
        localisation.update();
        turret.update();
        Pose pose = follower.getPose();

        switch (state) {
            case SHOOT_PRELOAD:
                intake.stopIntake();
                if (runShot()) follow(paths.path1(), State.TO_PATH1);
                break;

            case TO_PATH1:
                intake.runIntake();
                shooter.stopShoot(HardwareConstants.turret_no_power);
                if (!follower.isBusy()) follow(paths.path2(), State.TO_PATH2);
                break;

            case TO_PATH2:
                intake.stopIntake();
                shooter.stopShoot(AutomationConstants.AUTO_SPINUP_POWER);
                if (!follower.isBusy()) setState(State.SHOOT_PATH2);
                break;

            case SHOOT_PATH2:
                if (runShot()) follow(paths.path3(), State.TO_PATH3);
                break;

            case TO_PATH3:
                intake.runIntake();
                shooter.stopShoot(HardwareConstants.turret_no_power);
                if (!follower.isBusy()) follow(paths.path4(), State.TO_PATH4);
                break;

            case TO_PATH4:
                intake.runIntake();
                if (!follower.isBusy()) follow(paths.path5(), State.TO_PATH5);
                break;

            case TO_PATH5:
                intake.runIntake();
                if (!follower.isBusy()) {
                    autoIntake.search();
                    setState(State.AUTO_INTAKE_SEARCH);
                }
                break;

            case AUTO_INTAKE_SEARCH:
                intake.runIntake();
                autoIntake.update(pose);
                if (autoIntake.getState() == AutoIntake.State.READY) {
                    PathChain plan = autoIntake.getPlan();
                    autoIntake.cancel();
                    follower.followPath(plan, true);
                    setState(State.AUTO_INTAKE_FOLLOW);
                } else if (autoIntake.getState() == AutoIntake.State.TIMED_OUT) {
                    autoIntake.cancel();
                    follow(paths.path6(pose), State.TO_PATH6);
                }
                break;

            case AUTO_INTAKE_FOLLOW:
                intake.runIntake();
                if (!follower.isBusy()) follow(paths.path6(pose), State.TO_PATH6);
                break;

            case TO_PATH6:
                intake.stopIntake();
                shooter.stopShoot(AutomationConstants.AUTO_SPINUP_POWER);
                if (!follower.isBusy()) setState(State.SHOOT_PATH6);
                break;

            case SHOOT_PATH6:
                if (runShot()) follow(paths.path7(), State.TO_PATH7);
                break;

            case TO_PATH7:
                intake.runIntake();
                shooter.stopShoot(HardwareConstants.turret_no_power);
                if (!follower.isBusy()) setState(State.DONE);
                break;

            case DONE:
                intake.stopIntake();
                transfer.runTransfer(false);
                shooter.stopShoot(HardwareConstants.turret_no_power);
                break;
        }

        if (AutomationConstants.DEBUG) autoIntake.draw(pose);

        telemetry.addData("State", state);
        telemetry.addData("X", pose.getX());
        telemetry.addData("Y", pose.getY());
        telemetry.addData("Heading", Math.toDegrees(pose.getHeading()));
        telemetry.addData("Shooter target RPM", shooter.getShootingRPM());
        telemetry.addData("Shooter RPM", shooter.getTurretRPM());
        telemetry.addData("Auto intake", autoIntake.getState());
        telemetry.update();
    }

    @Override
    public void stop() {
        autoIntake.cancel();
    }

    private void follow(Path path, State next) {
        follower.followPath(path, true);
        setState(next);
    }

    private void setState(State next) {
        state = next;
        feeding = false;
        shotTimer.reset();
    }

    private boolean runShot() {
        shooter.shoot();
        if (!feeding && (shooterReady() || shotTimer.seconds() > AutomationConstants.AUTO_SHOOT_SPINUP_TIMEOUT)) {
            feeding = true;
            shotTimer.reset();
        }
        transfer.runTransfer(feeding);
        if (feeding && shotTimer.seconds() > AutomationConstants.AUTO_SHOOT_FEED_TIME) {
            transfer.runTransfer(false);
            return true;
        }
        return false;
    }

    private boolean shooterReady() {
        return Math.abs(shooter.getShootingRPM() - shooter.getTurretRPM()) <= ShooterConstants.SHOOTER_TOLERANCE
                && turret.atTargetHeading();
    }
}
