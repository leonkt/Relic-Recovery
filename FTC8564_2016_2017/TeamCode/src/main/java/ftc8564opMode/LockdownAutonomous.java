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

package ftc8564opMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import ftclib.*;
import ftc8564lib.*;

@Autonomous(name="LockdownAutonomous", group="Autonomous")
public class LockdownAutonomous extends LinearOpMode implements FtcMenu.MenuButtons, DriveBase.AbortTrigger {

    Robot robot;
    private ElapsedTime mClock = new ElapsedTime();

    public enum Alliance {
        RED_ALLIANCE,
        BLUE_ALLIANCE
    }

    private enum Strategy {
        SHOOT_BALL,
        SHOOT_DELAY,
        HIGH_RISK,
        DEFENSE
    }

    private enum ParkOption {
        NOTHING,
        CENTER_VORTEX,
        CORNER_VORTEX
    }

    private Alliance alliance = Alliance.RED_ALLIANCE;
    private Strategy strategy = Strategy.DEFENSE;
    private ParkOption parkOption = ParkOption.NOTHING;
    private int numParticles = 2;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this,true);
        doMenus();
        waitForStart();

        robot.driveBase.resetHeading();

        switch (strategy) {
            case HIGH_RISK:
                runHighRisk();
                break;
            case SHOOT_DELAY:
                runShootDelay();
                break;
            case DEFENSE:
                runDefense();
                break;
        }
        runCleanUp();
    }

    private void runHighRisk() throws InterruptedException {
        robot.driveBase.drivePID(13, false, null);
        robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 40 : -40);
        robot.driveBase.drivePID(25, false, null);
        // Loads and fires numParticles
        for(int i = 0; i < numParticles; i++)
        {
            robot.shooter.shootSequenceAuto(alliance == Alliance.RED_ALLIANCE);
            robot.shooter.waitForShootAuto();
            robot.shooter.shootSequenceAuto(alliance == Alliance.RED_ALLIANCE);
            robot.shooter.waitForShootAuto();
            robot.shooter.shootSequenceAuto(alliance == Alliance.RED_ALLIANCE);
            if(i != numParticles-1) robot.shooter.waitForShootAuto();
        }
        robot.driveBase.drivePID(35, false, null);
        robot.driveBase.drivePID(5, true, null);
        robot.driveBase.spinPID(0);
        robot.driveBase.drivePID(-7, true, this);
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(alliance == Alliance.RED_ALLIANCE);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(alliance == Alliance.RED_ALLIANCE);
            robot.beaconPush.waitUntilPressed();
            robot.driveBase.drivePID(50, false, null);
            robot.driveBase.drivePID(10, true, this);
        } else {
            // Needs to check second button
        }
        //Drives to Second Beacon and Aligns with farther button
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(alliance == Alliance.RED_ALLIANCE);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(alliance == Alliance.RED_ALLIANCE);
            robot.beaconPush.waitUntilPressed();
        } else {
            // Needs to check second button
        }
        //Determines Parking Option
        if(parkOption == ParkOption.CENTER_VORTEX)
        {
            robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
            robot.driveBase.drivePID(-10, false, null);
            robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 45 : -45);
            robot.driveBase.drivePID(-55, false, null);
        } else if(parkOption == ParkOption.CORNER_VORTEX)
        {
            robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
            robot.driveBase.drivePID(-20, false, null);
            robot.driveBase.spinPID(0);
            robot.driveBase.drivePID(-70, false , null);
        }
    }

    private void runShootBall() throws InterruptedException {
        //Shoot two balls + Cap Ball; Starting pos: Far
    }

    private void runDefense() throws InterruptedException {
        //Steal opposing alliance: two beacons; Starting pos: far
        mClock.reset();
        mClock.startTime();
        robot.driveBase.drivePID(25, false, null);
        while (mClock.time() <= 10.0) {
        }
    }

    private void runShootDelay() throws InterruptedException {
        mClock.reset();
        mClock.startTime();
        while (mClock.time() <= 15.0) {
        }
        robot.driveBase.drivePID(13, false, null);
        robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 40 : -40);
        robot.driveBase.drivePID(25, false, null);
        // Loads and fires numParticles
        for(int i = 0; i < numParticles; i++)
        {
            robot.shooter.shootSequenceAuto(alliance == Alliance.RED_ALLIANCE);
            robot.shooter.waitForShootAuto();
            robot.shooter.shootSequenceAuto(alliance == Alliance.RED_ALLIANCE);
            robot.shooter.waitForShootAuto();
            robot.shooter.shootSequenceAuto(alliance == Alliance.RED_ALLIANCE);
            if(i != numParticles-1) robot.shooter.waitForShootAuto();
        }
        robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 135 : -135);
        robot.driveBase.drivePID(35, false, null);
    }

    private void runCleanUp() throws InterruptedException {
        robot.shooter.resetMotors();
        robot.beaconPush.resetRacks();
        robot.pulleySystem.resetMotors();
        robot.driveBase.resetMotors();
        robot.driveBase.resetPIDDrive();
    }

    @Override
    public boolean shouldAbort() { return robot.odsLeft.getRawLightDetected() >= 2.5 || robot.odsRight.getRawLightDetected() >= 2.5; }

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
        FtcValueMenu numParticlesMenu = new FtcValueMenu("Shoot Particles:", allianceMenu, this, 0.0, 2.0, 1.0, 2.0, " %.0f");
        FtcChoiceMenu parkOptionMenu = new FtcChoiceMenu("Park Option:", numParticlesMenu, this);
        FtcChoiceMenu strategyMenu = new FtcChoiceMenu("Strategy:", parkOptionMenu, this);

        numParticlesMenu.setChildMenu(parkOptionMenu);

        allianceMenu.addChoice("Red", Alliance.RED_ALLIANCE, numParticlesMenu);
        allianceMenu.addChoice("Blue", Alliance.BLUE_ALLIANCE, numParticlesMenu);

        parkOptionMenu.addChoice("Do Nothing", ParkOption.NOTHING, strategyMenu);
        parkOptionMenu.addChoice("Park Center", ParkOption.CENTER_VORTEX, strategyMenu);
        parkOptionMenu.addChoice("Park Corner", ParkOption.CORNER_VORTEX, strategyMenu);

        strategyMenu.addChoice("100 Point: Close", Strategy.HIGH_RISK);
        strategyMenu.addChoice("2 Shot + 15 Sec Delay + Corner Vortex: Close", Strategy.SHOOT_DELAY);
        strategyMenu.addChoice("2 Shot + Cap Ball: Far", Strategy.SHOOT_BALL);
        strategyMenu.addChoice("Defense: Far", Strategy.DEFENSE);

        FtcMenu.walkMenuTree(allianceMenu);
        alliance = (Alliance) allianceMenu.getCurrentChoiceObject();
        numParticles = (int) numParticlesMenu.getCurrentValue();
        parkOption = (ParkOption) parkOptionMenu.getCurrentChoiceObject();
        strategy = (Strategy) strategyMenu.getCurrentChoiceObject();
    }

}