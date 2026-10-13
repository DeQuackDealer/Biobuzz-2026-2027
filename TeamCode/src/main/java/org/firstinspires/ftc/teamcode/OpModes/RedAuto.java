package org.firstinspires.ftc.teamcode.OpModes;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Constants.PedroConstants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Autonomous
public class RedAuto {
    private Follower follower;
    @Override
    public void init(){
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void start() {

    }
    @Override
    public void loop() {

    }
}
