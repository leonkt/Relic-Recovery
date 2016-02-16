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
    }

    private Alliance alliance = Alliance.RED_ALLIANCE;
    private Strategy strategy = Strategy.BEACON_85;

    @Override
    public void runOpMode() throws InterruptedException {
        HalDashboard dashboard = new HalDashboard(telemetry);
        robot = new Robot(this, true);
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
        strategyMenu.addChoice("Beacon 85", Strategy.BEACON_85);
        strategyMenu.addChoice("Beacon Ramp", Strategy.DRIVE_TO_BEACON);
        strategyMenu.addChoice("Floor Zone", Strategy.FLOOR_ZONE);

        FtcMenu.walkMenuTree(allianceMenu);
        alliance = (Alliance) allianceMenu.getCurrentChoiceObject();
        strategy = (Strategy) strategyMenu.getCurrentChoiceObject();
    }

    private void runBeacon85(Alliance alliance) throws InterruptedException {
        robot.driveBase.driveBackward(7.1, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -80.0 : 83.5, 0.5);
        robot.driveBase.waitTime(0.15);
        robot.driveBase.driveBackward(8.6, 0.2);
        robot.climberDeployer.grabClimbers();
        robot.driveBase.driveForward(21, 0.5);
        robot.driveBase.waitTime(0.15);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? 40.0 : -40.5, 0.5);
        robot.driveBase.driveBackward(47.5, 0.5);
        robot.driveBase.waitTime(alliance == Alliance.RED_ALLIANCE ? 0.06 : 0.05);
        robot.driveBase.driveBackward(49.5, 0.5);
        robot.driveBase.waitTime(0.2);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -40.0 : 39.0, 0.3);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 13.0 : 8.0, 0.5);
        robot.climberDeployer.raisedP();
        robot.climberDeployer.tPrep();
        robot.driveBase.waitTime(0.1);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 15.0 : 17.0, 0.2);
        robot.climberDeployer.lowerdP();
        robot.climberDeployer.tDrop();
        robot.driveBase.waitTime(1.5);
        robot.driveBase.driveForward(15.0, 0.5);
        robot.climberDeployer.zerodP();
    }

    private void runDriveToBeacon(Alliance alliance) throws InterruptedException {
        robot.driveBase.driveBackward(35.0, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -39.0 : 37.5, 0.5);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 61.65 : 60.0, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -37.0 : 37.8, 0.5);
        robot.driveBase.waitTime(0.1);
        robot.climberDeployer.raisedP();
        robot.climberDeployer.tPrep();
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 13.0 : 14.0, 0.2);
        robot.climberDeployer.lowerdP();
        robot.climberDeployer.tDrop();
        robot.driveBase.waitTime(0.1);
        robot.driveBase.driveForward(alliance == Alliance.RED_ALLIANCE ? 10.0 : 9.5, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -90.0 : 90.0, 0.5);
        robot.climberDeployer.zerodP();
        robot.driveBase.driveBackward(23.0, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -25.0 : 25.0, 0.5);
        robot.driveBase.driveBackward(19.0, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -90.0 : 85.0, 0.5);
        robot.driveBase.driveForward(40.0, 0.7);
    }

    private void runFloorZone(Alliance alliance) throws InterruptedException {
        robot.driveBase.driveBackward(35.0, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -39.0 : 37.5, 0.5);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 61.65 : 60.0, 0.5);
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -37.0 : 37.0, 0.5);
        robot.driveBase.waitTime(0.1);
        robot.climberDeployer.raisedP();
        robot.climberDeployer.tPrep();
        robot.driveBase.waitTime(0.1);
        robot.driveBase.driveBackward(alliance == Alliance.RED_ALLIANCE ? 13.0 : 14.0, 0.2);
        robot.climberDeployer.lowerdP();
        robot.climberDeployer.tDrop();
        robot.driveBase.waitTime(0.1);
        robot.driveBase.driveForward(12.0, 0.2);
        robot.climberDeployer.zerodP();
        robot.driveBase.spinGyro(alliance == Alliance.RED_ALLIANCE ? -87.0 : 80.0, 0.5);
        robot.driveBase.driveBackward(23.0, 0.5);
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
