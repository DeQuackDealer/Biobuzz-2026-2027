package org.firstinspires.ftc.teamcode.subsystems.AutomationStuff.Code;

import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Constants.PedroConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.firstinspires.ftc.teamcode.subsystems.Constants.AutomationConstants.*;

public class AutoIntake {
    public enum State { IDLE, SEARCHING, READY, TIMED_OUT }

    private static final double[] TANGENT_SCALES = {1.4, 1.0, 0.7};
    private static final int CANDIDATE_LIMIT = 24;
    private static final int DRAW_SAMPLES = 20;
    private static final String PATH_COLOR = "#2688d8";
    private static final String BALL_COLOR = "#ffa927";
    private static final String TARGET_COLOR = "#d32c20";

    private static class Track {
        private Pose pose;
        private int hits;
        private double lastSeen;
        private Track(Pose pose, double now) { this.pose = pose; hits = 1; lastSeen = now; }
    }

    private final Limelight3A limelight;
    private final FieldManager panels;
    private final ElapsedTime searchTimer = new ElapsedTime();
    private final ElapsedTime clock = new ElapsedTime();
    private final List<Track> tracks = new ArrayList<>();
    private final List<Pose> detectedBalls = new ArrayList<>();
    private final List<Pose> plannedRoute = new ArrayList<>();
    private State state = State.IDLE;
    private PathChain plan;
    private boolean detectionsChanged;

    public AutoIntake(HardwareMap hardwareMap) {
        this.limelight = hardwareMap.get(Limelight3A.class, LIMELIGHT_NAME);
        this.limelight.pipelineSwitch(BALL_PIPELINE);
        this.limelight.setPollRateHz(LIMELIGHT_POLL_RATE_HZ);
        this.panels = PanelsField.INSTANCE.getField();
        this.panels.setOffsets(PanelsField.INSTANCE.getPresets().getPEDRO_PATHING());
    }

    public void search() {
        reset();
        limelight.start();
        setState(State.SEARCHING);
    }

    public void cancel() {
        reset();
        limelight.pause();
        setState(State.IDLE);
    }

    //should run every loop cuz when the balls move, itll rebuild cuz they bounce
    public void update(Pose robotPose) {
        if (state != State.SEARCHING && state != State.READY) return;
        readDetections(robotPose);
        if (detectionsChanged) plan = plan(robotPose, detectedBalls, plannedRoute);
        if (plan != null) setState(State.READY);
        else if (state == State.READY) setState(State.SEARCHING);
        else if (searchTimer.seconds() > SEARCH_TIMEOUT) {
            limelight.pause();
            setState(State.TIMED_OUT);
        }
    }

    public State getState() { return state; }
    public List<Pose> getDetectedBalls() { return detectedBalls; }
    public List<Pose> getPlannedRoute() { return plannedRoute; }
    public PathChain getPlan() { return plan; }

    //draws the palns and directions onto panels, for debugging and aura hehehehe
    public void draw(Pose robotPose) {
        if (plan != null) {
            panels.setStyle(PanelsField.INSTANCE.getTRANSPARENT(), PATH_COLOR, 1);
            for (int i = 0; i < plan.size(); i++) {
                Path segment = plan.getPath(i);
                Pose previous = segment.getPose(0);
                for (int j = 1; j <= DRAW_SAMPLES; j++) {
                    Pose point = segment.getPose((double) j / DRAW_SAMPLES);
                    panels.moveCursor(previous.getX(), previous.getY());
                    panels.line(point.getX(), point.getY());
                    previous = point;
                }
            }
        }
        panels.setStyle(BALL_COLOR, BALL_COLOR, 0.5);
        for (Pose ball : detectedBalls) {
            panels.moveCursor(ball.getX(), ball.getY());
            panels.circle(BALL_DIAMETER / 2);
        }
        panels.setStyle(PanelsField.INSTANCE.getTRANSPARENT(), TARGET_COLOR, 1);
        for (int i = 1; i < plannedRoute.size(); i++) {
            panels.moveCursor(plannedRoute.get(i).getX(), plannedRoute.get(i).getY());
            panels.circle(BALL_DIAMETER);
        }
        if (robotPose != null) {
            panels.setStyle(PanelsField.INSTANCE.getTRANSPARENT(), PanelsField.INSTANCE.getWHITE(), 1);
            panels.moveCursor(robotPose.getX(), robotPose.getY());
            panels.circle(ROBOT_RADIUS);
            panels.moveCursor(robotPose.getX(), robotPose.getY());
            panels.line(robotPose.getX() + ROBOT_RADIUS * Math.cos(robotPose.getHeading()),
                    robotPose.getY() + ROBOT_RADIUS * Math.sin(robotPose.getHeading()));
        }
        panels.update();
    }

    private void setState(State target) {
        if (state != target) searchTimer.reset();
        state = target;
    }

    private void reset() {
        plan = null;
        tracks.clear();
        detectedBalls.clear();
        plannedRoute.clear();
        detectionsChanged = false;
    }

    //gets the latest ll frame and turns it into a field
    private void readDetections(Pose robotPose) {
        double now = clock.seconds();
        LLResult result = limelight.getLatestResult();
        if (robotPose != null && result != null && result.isValid()) {
            for (LLResultTypes.DetectorResult target : result.getDetectorResults()) {
                if (target == null) continue;
                if (BALL_CLASS_NAME != null && !BALL_CLASS_NAME.isEmpty() && !BALL_CLASS_NAME.equalsIgnoreCase(target.getClassName())) continue;
                Pose ball = cameraTargetToField(target.getTargetXDegrees(), target.getTargetYDegrees(), robotPose);
                if (ball != null && insideField(ball)) trackBall(ball, now);
            }
        }
        for (int i = tracks.size() - 1; i >= 0; i--) if (now - tracks.get(i).lastSeen > PATH_TIMEOUT) tracks.remove(i);
        publishTracks();
    }

    private static boolean insideField(Pose pose) {
        return pose.getX() >= FIELD_MIN_X && pose.getX() <= FIELD_MAX_X && pose.getY() >= FIELD_MIN_Y && pose.getY() <= FIELD_MAX_Y;
    }

    //matches a detection to a nearby track and eases it toward the new reading, else starts a new track
    private void trackBall(Pose ball, double now) {
        Track nearest = null;
        double nearestDistance = PATH_MATCH_RADIUS;
        for (Track track : tracks) {
            double distance = track.pose.distanceFrom(ball);
            if (distance <= nearestDistance) { nearest = track; nearestDistance = distance; }
        }
        if (nearest == null) { tracks.add(new Track(ball, now)); return; }
        nearest.pose = new Pose(nearest.pose.getX() + (ball.getX() - nearest.pose.getX()) * PATH_SMOOTHING,
                nearest.pose.getY() + (ball.getY() - nearest.pose.getY()) * PATH_SMOOTHING);
        nearest.lastSeen = now;
        nearest.hits++;
    }

    //promotes tracks seen enough times into the ball list, flags when the planner needs to rerun
    private void publishTracks() {
        List<Pose> confirmed = new ArrayList<>(tracks.size());
        for (Track track : tracks) if (track.hits >= PATH_MIN_HITS) confirmed.add(track.pose);
        detectionsChanged = confirmed.size() != detectedBalls.size();
        for (int i = 0; !detectionsChanged && i < confirmed.size(); i++) if (confirmed.get(i).distanceFrom(detectedBalls.get(i)) > PATH_MOVE_EPSILON) detectionsChanged = true;
        detectedBalls.clear();
        detectedBalls.addAll(confirmed);
    }

    //turns a ll frame into a field pos via a ray
    static Pose cameraTargetToField(double tx, double ty, Pose robotPose) {
        double yaw = Math.toRadians(tx);
        double pitch = Math.toRadians(ty);
        if (Math.abs(yaw) > CAMERA_HFOV / 2 || Math.abs(pitch) > CAMERA_VFOV / 2) return null;
        double right = Math.tan(yaw);
        double up = Math.tan(pitch);
        double axial = Math.cos(CAMERA_PITCH) - up * Math.sin(CAMERA_PITCH);
        double vertical = Math.sin(CAMERA_PITCH) + up * Math.cos(CAMERA_PITCH);
        double height = BALL_HEIGHT - CAMERA_HEIGHT;
        if (height * vertical <= 0) return null;
        double scale = height / vertical;
        double range = axial * scale;
        if (range <= 0) return null;
        double forward = CAMERA_FORWARD + range;
        double left = CAMERA_LEFT - right * scale;
        double heading = robotPose.getHeading();
        return new Pose(robotPose.getX() + forward * Math.cos(heading) - left * Math.sin(heading),
                robotPose.getY() + forward * Math.sin(heading) + left * Math.cos(heading));
    }

    //tries a bunch of sets of balls and returns the best one
    static PathChain plan(Pose robotPose, List<Pose> balls, List<Pose> routeOut) {
        routeOut.clear();
        if (robotPose == null || balls == null || balls.isEmpty()) return null;
        List<Pose> targets = selectTargets(robotPose, balls);
        if (targets.isEmpty()) return null;
        int wanted = Math.min(BALL_TARGET_COUNT, balls.size());
        PathChain best = null;
        List<Pose> bestPoints = null;
        int bestCollected = 0;
        int evaluated = 0;
        double bestLength = Double.POSITIVE_INFINITY;
        int[] order = new int[targets.size()];
        order[0] = 1;
        for (int i = 1, size = targets.size(); i < order.length; i++, size--) order[i] = size;
        for (int index = 0; index < order.length && evaluated < CANDIDATE_LIMIT; index++) {
            int size = order[index];
            for (int mask = (1 << targets.size()) - 1; mask > 0 && evaluated < CANDIDATE_LIMIT; mask--) {
                if (Integer.bitCount(mask) != size) continue;
                List<Pose> points = new ArrayList<>(size + 1);
                points.add(robotPose);
                for (int i = 0; i < targets.size(); i++) if ((mask & 1 << i) != 0) points.add(targets.get(i));
                for (double scale : TANGENT_SCALES) {
                    evaluated++;
                    PathChain candidate = chain(points, scale);
                    int collected = score(candidate, balls);
                    if (collected < 0) continue;
                    double length = candidate.length();
                    if (collected > bestCollected || (collected == bestCollected && length < bestLength)) {
                        best = candidate;
                        bestPoints = points;
                        bestCollected = collected;
                        bestLength = length;
                    }
                    break;
                }
                if (bestCollected >= wanted) break;
            }
            if (bestCollected >= wanted) break;
        }
        if (best == null) return null;
        routeOut.addAll(bestPoints);
        return best;
    }

    //nearest first system up to 4
    private static List<Pose> selectTargets(Pose robotPose, List<Pose> balls) {
        List<Pose> remaining = new ArrayList<>(balls);
        List<Pose> targets = new ArrayList<>();
        Pose current = robotPose;
        while (!remaining.isEmpty() && targets.size() < Math.max(1, BALL_TARGET_COUNT)) {
            int nearest = 0;
            double nearestDistance = Double.POSITIVE_INFINITY;
            for (int i = 0; i < remaining.size(); i++) {
                double distance = remaining.get(i).distanceFrom(current);
                if (distance < nearestDistance) { nearestDistance = distance; nearest = i; }
            }
            Pose next = remaining.remove(nearest);
            if (next.distanceFrom(current) <= MIN_APPROACH_DISTANCE) continue;
            targets.add(next);
            current = next;
        }
        return targets;
    }

    //tangents at each ball so it doesnt it controls how much swing there is
    private static PathChain chain(List<Pose> points, double scale) {
        int size = points.size();
        double[] tangentX = new double[size];
        double[] tangentY = new double[size];
        for (int i = 0; i < size; i++) {
            Pose previous = points.get(Math.max(0, i - 1));
            Pose next = points.get(Math.min(size - 1, i + 1));
            double dx = next.getX() - previous.getX();
            double dy = next.getY() - previous.getY();
            double norm = Math.hypot(dx, dy);
            if (norm == 0) continue;
            double reach = scale * Math.min(i == 0 ? Double.MAX_VALUE : points.get(i).distanceFrom(previous),
                    i == size - 1 ? Double.MAX_VALUE : points.get(i).distanceFrom(next));
            tangentX[i] = dx / norm * reach;
            tangentY[i] = dy / norm * reach;
        }
        Path[] paths = new Path[size - 1];
        for (int i = 0; i + 1 < size; i++) {
            Pose from = points.get(i);
            Pose to = points.get(i + 1);
            Path path = new Path(new BezierCurve(Arrays.asList(from,
                    new Pose(from.getX() + tangentX[i] / 3, from.getY() + tangentY[i] / 3),
                    new Pose(to.getX() - tangentX[i + 1] / 3, to.getY() - tangentY[i + 1] / 3),
                    to)), PedroConstants.pathConstraints);
            path.setTangentHeadingInterpolation();
            paths[i] = path;
        }
        return new PathChain(PedroConstants.pathConstraints, paths);
    }

    //returns how many balls the chain gets
    private static int score(PathChain chain, List<Pose> balls) {
        if (chain.length() > MAX_PATH_LENGTH) return -1;
        double sweepSquared = INTAKE_WIDTH * INTAKE_WIDTH / 4;
        double[] nearestSquared = new double[balls.size()];
        Arrays.fill(nearestSquared, Double.POSITIVE_INFINITY);
        for (int i = 0; i < chain.size(); i++) {
            Path path = chain.getPath(i);
            for (int j = 0; j <= CURVE_SAMPLES; j++) {
                double t = (double) j / CURVE_SAMPLES;
                double curvature = Math.abs(path.getCurvature(t));
                if (curvature * MIN_TURN_RADIUS > 1) return -1;
                Pose point = path.getPose(t);
                if (i > 0 || j > 0) {
                    if (point.getX() < FIELD_MIN_X + PATH_WALL_MARGIN || point.getX() > FIELD_MAX_X - PATH_WALL_MARGIN) return -1;
                    if (point.getY() < FIELD_MIN_Y + PATH_WALL_MARGIN || point.getY() > FIELD_MAX_Y - PATH_WALL_MARGIN) return -1;
                }
                for (int k = 0; k < balls.size(); k++) {
                    nearestSquared[k] = Math.min(nearestSquared[k], point.distSquared(balls.get(k)));
                }
            }
        }
        int collected = 0;
        for (double distance : nearestSquared) if (distance <= sweepSquared) collected++;
        return collected;
    }
}
