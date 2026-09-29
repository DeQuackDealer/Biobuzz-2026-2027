package org.firstinspires.ftc.teamcode.subsystems.AutomationStuff.Paths;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

public class REDAUTO {

    private final Pose start = new Pose(56, 8, Math.toRadians(90));
    private final Pose path1 = new Pose(10, 8, Math.toRadians(180));
    private final Pose point2 = new Pose(56, 115, Math.toRadians(-90));
    private final Pose point2Control1 = new Pose(8.4, 83.1521, Math.toRadians(0));
    private final Pose point3 = new Pose(47, 138, Math.toRadians(111.3706));
    private final Pose point4 = new Pose(56, 115, Math.toRadians(-90));
    private final Pose point5 = new Pose(56, 45, Math.toRadians(-90));
    private final Pose point6 = new Pose(56, 25, Math.toRadians(90));
    private final Pose point7 = new Pose(5, 106, Math.toRadians(-167.323));
    private final Pose point7Control1 = new Pose(89.2385, 125.4154, Math.toRadians(0));

    public Path path1() {
        Path path = new Path(new BezierLine(start, path1));
        path.setConstantHeadingInterpolation(path1.getHeading());
        return path;
    }

    public Path path2() {
        Path path = new Path(BezierCurve.through(path1, point2Control1, point2));
        path.setConstantHeadingInterpolation(point2.getHeading());
        return path;
    }

    public Path path3() {
        Path path = new Path(new BezierLine(point2, point3));
        path.setTangentHeadingInterpolation();
        return path;
    }

    public Path path4() {
        Path path = new Path(new BezierLine(point3, point4));
        path.setConstantHeadingInterpolation(point4.getHeading());
        return path;
    }

    public Path path5() {
        Path path = new Path(new BezierLine(point4, point5));
        path.setTangentHeadingInterpolation();
        return path;
    }

    public Path path6() {
        Path path = new Path(new BezierLine(point5, point6));
        path.setConstantHeadingInterpolation(point6.getHeading());
        return path;
    }

    public Path path7() {
        Path path = new Path(BezierCurve.through(point6, point7Control1, point7));
        path.setTangentHeadingInterpolation();
        return path;
    }
}
