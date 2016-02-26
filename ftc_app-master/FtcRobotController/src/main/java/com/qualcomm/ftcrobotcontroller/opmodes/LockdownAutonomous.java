/*
 * Lockdown Framework Library
 * Copyright (c) 2015 Lockdown Team 8564 (lockdown8564.weebly.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Owner on 2/11/2016.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import ftclib.*;
import hallib.*;
import ftc8564.Robot;

public class LockdownAutonomous extends LinearOpMode implements FtcMenu.MenuButtons {

    Robot robot;
    private ElapsedTime mClock = new ElapsedTime();

    public enum Alliance {
        RED_ALLIANCE,
        BLUE_ALLIANCE
    }

    public enum Strategy {
        DO_NOTHING,
        BEACON_85,
        DRIVE_TO_BEACON,
        FLOOR_ZONE,
        RAMP,
        FAR_RAMP
    }

    private Alliance alliance = Alliance.RED_ALLIANCE;
    private Strategy strategy = Strategy.DO_NOTHING;

    @Override
    public void runOpMode() throws InterruptedException {
        HalDashboard dashboard = new HalDashboard(telemetry);
        robot = new Robot(this);
        doMenus();
        waitForStart();

        switch (strategy) {
            case BEACON_85:
                runBeacon85(alliance);
                break;
            case DRIVE_TO_BEACON:
                runDriveToBeacon(alliance);
                break;
            case FLOOR_ZONE:
                runFloorZone(alliance);
                break;
            case RAMP:
                runRAMP(alliance);
                break;
            case FAR_RAMP:
                runFARRAMP(alliance);
                break;
            default:
            case DO_NOTHING:
                runDoNothing();
                break;
        }
    }

    @Override
    public boolean isMenuUpButton() {
        return gamepad1.dpad_up;
    }

    @Override
    public boolean isMenuDownButton() {
        return gamepad1.dpad_down;
    }

    @Override
    public boolean isMenuEnterButton() {
        return gamepad1.dpad_right;
    }

    @Override
    public boolean isMenuBackButton() {
        return gamepad1.dpad_left;
    }

    private void doMenus() throws InterruptedException {
        FtcChoiceMenu allianceMenu = new FtcChoiceMenu("Alliance:", null, this);
        FtcChoiceMenu strategyMenu = new FtcChoiceMenu("Strategy:", allianceMenu, this);

        allianceMenu.addChoice("Red", Alliance.RED_ALLIANCE, strategyMenu);
        allianceMenu.addChoice("Blue", Alliance.BLUE_ALLIANCE, strategyMenu);

        strategyMenu.addChoice("Do Nothing", Strategy.DO_NOTHING);
        strategyMenu.addChoice("Far Ramp : Further Far", Strategy.FAR_RAMP);
        strategyMenu.addChoice("Beacon 85 : Far", Strategy.BEACON_85);
        strategyMenu.addChoice("Floor Zone : Close", Strategy.FLOOR_ZONE);
        strategyMenu.addChoice("Beacon Ramp : Close To Ramp", Strategy.DRIVE_TO_BEACON);
        strategyMenu.addChoice("Ramp : Close To Ramp", Strategy.RAMP);

        FtcMenu.walkMenuTree(allianceMenu);
        alliance = (Alliance) allianceMenu.getCurrentChoiceObject();
        strategy = (Strategy) strategyMenu.getCurrentChoiceObject();
    }

    private void runFARRAMP(Alliance alliance) throws InterruptedException {
        robot.driveBase.waitTime(10.0);
        robot.driveBase.driveBackward(10.0, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -80.0 : 80.0, 0.5);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 40.5 : 35.0, 0.5);
        robot.driveBase.pivotLeftGyro(alliance == Alliance.RED_ALLIANCE ? 43.5 : -43.0, 0.5);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 17.5 : 19.5, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? 80.0: -80.0, 0.5);
        robot.climberDeployer.stopFlipper();
        robot.climberDeployer.dP250();
        robot.driveBase.waitTime(2.0);
        robot.climberDeployer.tDrop();
        robot.climberDeployer.dP0();
        robot.driveBase.waitTime(2.0);
        robot.climberDeployer.setFlipper();
        robot.driveBase.driveForward(alliance == Alliance.RED_ALLIANCE ? 15.0 : 18.0, 0.5);
        robot.driveBase.driveForward(25.0, 0.7);
    }

    private void runRAMP(Alliance alliance) throws InterruptedException {
        robot.driveBase.waitTime(5.0);
        robot.driveBase.driveBackward(15.5, 0.5);
        robot.driveBase.pivotGyro(alliance == Alliance.RED_ALLIANCE ? -34.0 : 35.0, 0.5);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 18.0 : 23.0, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? 85.0 : -75.0, 0.5);
        robot.climberDeployer.stopFlipper();
        robot.climberDeployer.dP250();
        robot.driveBase.waitTime(2.0);
        robot.climberDeployer.tDrop();
        robot.climberDeployer.dP0();
        robot.driveBase.waitTime(2.0);
        robot.climberDeployer.setFlipper();
        robot.driveBase.driveForward(alliance == Alliance.RED_ALLIANCE ? 15.0 : 20.0, 0.5);
        robot.driveBase.driveForward(alliance == Alliance.RED_ALLIANCE ? 20.0 : 30.0, 0.7);
    }

    private void runBeacon85(Alliance alliance) throws InterruptedException {
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 7.1 : 7.7, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -83.0 : 83.0, 0.3);
        robot.driveBase.waitTime(0.15);
        robot.climberDeployer.stopFlipper();
        robot.driveBase.driveBackward(8.7, 0.2);
        robot.climberDeployer.grabClimbers();
        robot.driveBase.driveForwardNoTouch(21, 0.5);
        robot.climberDeployer.halfFlipper();
        robot.climberDeployer.raisedP();
        robot.driveBase.waitTime(0.15);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? 37.0 : -37.0, 0.2);
        robot.driveBase.driveBackward(48.5, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -5.0 : 4.5, 0.5);
        robot.driveBase.driveBackward(49.0, 0.5);
        robot.driveBase.waitTime(0.2);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? 144.0 : -144.0, 0.5);
        robot.driveBase.waitTime(0.2);
        robot.driveBase.driveForward(alliance == Alliance.RED_ALLIANCE ? 7.5 : 6.5, 0.5);
        robot.climberDeployer.tZero();
        robot.climberDeployer.dP700();
        robot.driveBase.waitTime(3.0);
        if(alliance == Alliance.RED_ALLIANCE) { robot.climberDeployer.autonomousTiltLeft(); } else {robot.climberDeployer.autonomousTiltRight();}
        robot.driveBase.waitTime(3.0);
        robot.climberDeployer.centerBox();
        robot.driveBase.driveBackward(15.0, 0.5);
        robot.driveBase.driveForward(10.0, 0.2);
        robot.climberDeployer.dP0();
        robot.driveBase.waitTime(3.0);
        robot.climberDeployer.setFlipper();
    }

    private void runDriveToBeacon(Alliance alliance) throws InterruptedException {
        robot.driveBase.driveBackward(17.5, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -34.0 : 33.5, 0.5);
        robot.driveBase.waitTime(0.2);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 66.0 : 64.5, 0.5);
        robot.driveBase.waitTime(0.2);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? 124.0 : -124.0, 0.5);
        robot.driveBase.driveForward(alliance == Alliance.RED_ALLIANCE ? 2.6 : 5.0, 0.2);
        robot.climberDeployer.stopFlipper();
        robot.climberDeployer.dP700();
        robot.driveBase.waitTime(3.7);
        if(alliance == Alliance.RED_ALLIANCE) { robot.climberDeployer.autonomousTiltLeft(); } else {robot.climberDeployer.autonomousTiltRight();}
        robot.driveBase.waitTime(1.2);
        robot.climberDeployer.centerBox();
        robot.climberDeployer.tDrop();
        robot.climberDeployer.dP0();
        robot.driveBase.waitTime(1.5);
        robot.climberDeployer.setFlipper();
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? 33.0 : -33.0, 0.5);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 26.0 : 27.0, 0.5);
        robot.driveBase.waitTime(0.2);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -75.0 : 80.0, 0.5);
        robot.driveBase.waitTime(0.2);
        robot.driveBase.driveForward(20.0, 0.5);
        robot.driveBase.driveForward(30.0, 0.7);
    }

    private void runFloorZone(Alliance alliance) throws InterruptedException {
        robot.driveBase.driveBackward(40.0, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -30.6 : 37.5, 0.5);
        robot.driveBase.waitTime(0.1);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 65.0 : 70.0, 0.5);
        robot.driveBase.waitTime(0.1);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? 120.0 : -130.0, 0.5);
        robot.driveBase.driveForward(alliance == Alliance.RED_ALLIANCE ? 9.5 : 10.5, 0.2);
        robot.climberDeployer.stopFlipper();
        robot.climberDeployer.dP700();
        robot.driveBase.waitTime(3.5);
        if(alliance == Alliance.RED_ALLIANCE) { robot.climberDeployer.autonomousTiltRight(); } else {robot.climberDeployer.autonomousTiltLeft();}
        robot.driveBase.waitTime(1.0);
        robot.climberDeployer.centerBox();
        robot.climberDeployer.dP0();
        robot.driveBase.waitTime(3.0);
        robot.driveBase.driveBackward(5.0, 0.5);
        robot.climberDeployer.setFlipper();
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? 80.0 : -80.0, 0.5);
        robot.driveBase.driveBackward(25.0, 0.5);
    }

    private void runDoNothing() throws InterruptedException {
        mClock.reset();
        waitOneFullHardwareCycle();
        mClock.startTime();
        while (mClock.time() <= 29.9) {
            waitOneFullHardwareCycle();
        }
    }
}
