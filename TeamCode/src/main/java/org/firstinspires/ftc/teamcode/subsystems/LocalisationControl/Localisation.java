package org.firstinspires.ftc.teamcode.subsystems.LocalisationControl;
import com.pedropathing.*;
import com.pedropathing.follower.Follower;

public class Localisation {
    private double xPos = 0;
    private double yPos = 0;
    private double xVel = 0;
    private double yVel = 0;
    private double heading = 0;
    private double bGoalX = 84; // placeholder values
    private double GoalY = 82; // placeholder values
    private double rGoalX = 58; // placeholder values
    private double goalYDiff = 30;
    private Follower follower;
    private boolean isBlue = true; // goal bool
    private boolean isTipped = true; // goal bool

    public Localisation(Follower F1, boolean isGoalBlue){
        follower = F1;
        isBlue = isGoalBlue;
    }
    public void update(){
    //code
        follower.update();

        heading = follower.getPose().getHeading();

        xVel = follower.getVelocity().getXComponent();
        yVel = follower.getVelocity().getYComponent();

        xPos = follower.getPose().getX();
        yPos = follower.getPose().getY();
    }
    public double getRX(){
        return xPos;
    }
    public double getRY(){
        return yPos;
    }
    public double getRXS(){
        return xVel;
    }
    public double getRYS(){
        return yVel;
    }
    public double getRH(){
        return heading;
    }
    public double getGD(){
        //MAKE CODE TO CALCULATE AND RETURN GOAL DISTANCE!!!! (depending on goal ofc)
        if (isBlue && isTipped) {
            return Math.hypot(Math.abs(bGoalX - xPos), Math.abs((GoalY - goalYDiff) - yPos));
        } else if (isBlue && !isTipped) {
            return Math.hypot(Math.abs(bGoalX - xPos), Math.abs(GoalY - yPos));
        } else if (!isBlue && isTipped) {
            return Math.hypot(Math.abs(rGoalX - xPos), Math.abs((GoalY - goalYDiff) - yPos));
        } else {
            return Math.hypot(Math.abs(rGoalX - xPos), Math.abs(GoalY - yPos));
        }

    }
    public double getGDX(){
        if (isBlue) {
            return bGoalX;
        } else {
            return rGoalX;
        }
    }
    public double getGDY(){
        if (isTipped) {
            return GoalY - goalYDiff;
        } else {
            return GoalY;
        }
    }
}
