package org.firstinspires.ftc.teamcode.subsystems.Constants;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class AutomationConstants {

    public static int BALL_TARGET_COUNT = 4;

    //ll pipeline
    public static int BALL_PIPELINE = 0;

    //the obj detection name
    public static String BALL_CLASS_NAME = "ball";

    // Route shape

    // total travel budget for one pickup route
    public static double MAX_PATH_LENGTH = 200.0;

    // tightest turn the follower is allowed to be handed, in inches
    public static double MIN_TURN_RADIUS = 4.0;

    // points sampled per curve when checking curvature and field bounds
    public static int CURVE_SAMPLES = 40;

    // how far the robot centre must stay off the walls along a planned route
    public static double PATH_WALL_MARGIN = 6.0;

    // how much longer a curved route may be than the straight one and still win
    public static double CURVE_LENGTH_TOLERANCE = 1.15;

    // most balls the planner will brute force visit orders for
    public static int PERMUTATION_LIMIT = 4;

    // a ball closer than this is treated as already at the robot
    public static double MIN_APPROACH_DISTANCE = 1.0;

    // half of the biggest length
    public static double ROBOT_RADIUS = 9.0;

    public static double FIELD_MIN_X = 0.0;
    public static double FIELD_MIN_Y = 0.0;
    public static double FIELD_MAX_X = 141.5;
    public static double FIELD_MAX_Y = 141.5;

    //intake stuff, in inches
    public static double INTAKE_MOUTH_WIDTH = 13.0;
    public static double BALL_DIAMETER = 2.8;
    public static double INTAKE_CLEARANCE = 1.0;
    public static double INTAKE_WIDTH = INTAKE_MOUTH_WIDTH - BALL_DIAMETER - INTAKE_CLEARANCE;

    //cam as in ll if you couldnt tell
    public static double CAMERA_HEIGHT = 12.0;

    //cam tilt down, might need to change cuz it might be mounted on a servo
    public static double CAMERA_PITCH = Math.toRadians(-20.0);

    //dist from the centre
    public static double CAMERA_FORWARD = 6.0;

    //dist of left from centre
    public static double CAMERA_LEFT = 0.0;

    //ll3a field of view, targets outside this get thrown out
    public static double CAMERA_HFOV = Math.toRadians(54.0);
    public static double CAMERA_VFOV = Math.toRadians(42.0);

    //ball centre height, lowk jst the radius
    public static double BALL_HEIGHT = 1.875;

    //tele update ping
    public static double TELEMETRY_INTERVAL_SECONDS = 0.5;

    //debugging flag, if its true itll print it out to tele
    public static boolean DEBUG = false;

    public static String LIMELIGHT_NAME = "limelight";

    public static int LIMELIGHT_POLL_RATE_HZ = 100;

    public static double SEARCH_TIMEOUT = 1.0;

    //how far apart two frames can be and still be the same ball
    public static double PATH_MATCH_RADIUS = 6.0;

    //how hard a new frame pulls the tracked position, 0 to 1
    public static double PATH_SMOOTHING = 0.4;

    //a tracked ball is dropped if unseen this long
    public static double PATH_TIMEOUT = 0.5;

    //frames a ball needs to be seen for before the planner is allowed to use it
    public static int PATH_MIN_HITS = 3;

    //how far a tracked ball has to move before the sweep is replanned
    public static double PATH_MOVE_EPSILON = 0.5;

    public static double AUTO_SPINUP_POWER = 0.75;

    public static double AUTO_SHOOT_SPINUP_TIMEOUT = 2.0;

    public static double AUTO_SHOOT_FEED_TIME = 1.5;
}
