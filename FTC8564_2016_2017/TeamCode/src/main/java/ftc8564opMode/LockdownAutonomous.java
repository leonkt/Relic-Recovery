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
        DO_NOTHING,
        HUNDRED_POINT,
        SHOOT_BALL,
        SHOOT_DELAY,
        HIGH_RISK,
        CORNER_VORTEX,
        CENTER_VORTEX,
        DEFENSE
    }

    private Alliance alliance = Alliance.RED_ALLIANCE;
    private Strategy strategy = Strategy.DO_NOTHING;

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
            case HUNDRED_POINT:
                runHundredPoint();
                break;
            case SHOOT_BALL:
                runShootBall();
                break;
            case CORNER_VORTEX:
                runCornerVortex();
                break;
            case CENTER_VORTEX:
                runCenterVortex();
                break;
            case SHOOT_DELAY:
                runShootDelay();
                break;
            case DEFENSE:
                runDefense();
                break;
            default:
            case DO_NOTHING:
                runDoNothing();
                break;
        }
        runCleanUp();
    }

    private void runHighRisk() throws InterruptedException {
        robot.driveBase.drivePID(13, false, null);
        robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 40 : -40);
        robot.driveBase.drivePID(25, false, null);
        //Loads and Fires a particle
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        //Loads & Fires second ball
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.driveBase.drivePID(35, false, null);
        robot.driveBase.drivePID(5, true, null);
        robot.driveBase.spinPID(0);
        robot.driveBase.drivePID(-5, true, this);
        robot.driveBase.drivePID(-10, true, null);
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.driveBase.drivePID(50, false, null);
            robot.driveBase.drivePID(10, true, this);
        } else {
            robot.driveBase.drivePID(-5, true, null);
            if(robot.beaconPush.beaconColorIsAlliance(alliance))
            {
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.driveBase.drivePID(55, false, null);
                robot.driveBase.drivePID(10, true, this);
            } else {
                robot.driveBase.drivePID(5, true, null);
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.driveBase.drivePID(50, false, null);
                robot.driveBase.drivePID(10, true, this);
            }
        }
        //Drives to Second Beacon and Aligns with farther button
        robot.driveBase.drivePID(-10, true, null);
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            //Goes to Center Vortex
            robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
            robot.driveBase.drivePID(-10, false, null);
            robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 45 : -45);
            robot.driveBase.drivePID(-55, false, null);
        } else {
            robot.driveBase.drivePID(-5, true, null);
            if(robot.beaconPush.beaconColorIsAlliance(alliance))
            {
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                //Goes to Center Vortex
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
                robot.driveBase.drivePID(-10, false, null);
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 50 : -50);
                robot.driveBase.drivePID(-55, false , null);
            } else {
                robot.driveBase.drivePID(5, true, null);
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                //Goes to Center Vortex
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
                robot.driveBase.drivePID(-10, false, null);
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 45 : -45);
                robot.driveBase.drivePID(-60, false, null);
            }
        }
    }

    private void runHundredPoint() throws InterruptedException {
        robot.driveBase.drivePID(13, false, null);
        robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 40 : -40);
        robot.driveBase.drivePID(25, false, null);
        //Loads and Fires a particle
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        //Loads & Fires second ball
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.driveBase.drivePID(35, false, null);
        robot.driveBase.drivePID(5, true, null);
        robot.driveBase.spinPID(0);
        robot.driveBase.drivePID(-5, true, this);
        robot.driveBase.drivePID(-10, true, null);
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.driveBase.drivePID(50, false, null);
            robot.driveBase.drivePID(10, true, this);
        } else {
            robot.driveBase.drivePID(-5, true, null);
            if(robot.beaconPush.beaconColorIsAlliance(alliance))
            {
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.driveBase.drivePID(55, false, null);
                robot.driveBase.drivePID(10, true, this);
            } else {
                robot.driveBase.drivePID(5, true, null);
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.driveBase.drivePID(50, false, null);
                robot.driveBase.drivePID(10, true, this);
            }
        }
        //Drives to Second Beacon and Aligns with farther button
        robot.driveBase.drivePID(-10, true, null);
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            //Goes to Corner Vortex
            robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
            robot.driveBase.drivePID(-20, false, null);
            robot.driveBase.spinPID(0);
            robot.driveBase.drivePID(-70, false , null);
        } else {
            robot.driveBase.drivePID(-5, true, null);
            if(robot.beaconPush.beaconColorIsAlliance(alliance))
            {
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                //Goes to Corner Vortex
                robot.driveBase.spinPID(30);
                robot.driveBase.drivePID(-20, false, null);
                robot.driveBase.spinPID(0);
                robot.driveBase.drivePID(-65, false , null);
            } else {
                robot.driveBase.drivePID(5, true, null);
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                //Goes to Corner Vortex
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
                robot.driveBase.drivePID(-20, false, null);
                robot.driveBase.spinPID(0);
                robot.driveBase.drivePID(-70, false , null);
            }
        }
    }

    private void runCenterVortex() throws InterruptedException {
        robot.driveBase.drivePID(13, false, null);
        robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 40 : -40);
        robot.driveBase.drivePID(60, false, null);
        robot.driveBase.drivePID(5, true, null);
        robot.driveBase.spinPID(0);
        robot.driveBase.drivePID(-5, true, this);
        robot.driveBase.drivePID(-10, true, null);
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.driveBase.drivePID(50, false, null);
            robot.driveBase.drivePID(10, true, this);
        } else {
            robot.driveBase.drivePID(-5, true, null);
            if(robot.beaconPush.beaconColorIsAlliance(alliance))
            {
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.driveBase.drivePID(55, false, null);
                robot.driveBase.drivePID(10, true, this);
            } else {
                robot.driveBase.drivePID(5, true, null);
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.driveBase.drivePID(50, false, null);
                robot.driveBase.drivePID(10, true, this);
            }
        }
        //Drives to Second Beacon and Aligns with farther button
        robot.driveBase.drivePID(-10, true, null);
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            //Goes to Center Vortex
            robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
            robot.driveBase.drivePID(-10, false, null);
            robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 45 : -45);
            robot.driveBase.drivePID(-55, false, null);
        } else {
            robot.driveBase.drivePID(-5, true, null);
            if(robot.beaconPush.beaconColorIsAlliance(alliance))
            {
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                //Goes to Center Vortex
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
                robot.driveBase.drivePID(-10, false, null);
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 50 : -50);
                robot.driveBase.drivePID(-55, false , null);
            } else {
                robot.driveBase.drivePID(5, true, null);
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                //Goes to Center Vortex
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
                robot.driveBase.drivePID(-10, false, null);
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 45 : -45);
                robot.driveBase.drivePID(-55, false, null);
            }
        }
    }

    private void runCornerVortex() throws InterruptedException {
        robot.driveBase.drivePID(13, false, null);
        robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 40 : -40);
        robot.driveBase.drivePID(60, false, null);
        robot.driveBase.drivePID(5, true, null);
        robot.driveBase.spinPID(0);
        robot.driveBase.drivePID(-5, true, this);
        robot.driveBase.drivePID(-10, true, null);
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.driveBase.drivePID(50, false, null);
            robot.driveBase.drivePID(10, true, this);
        } else {
            robot.driveBase.drivePID(-5, true, null);
            if(robot.beaconPush.beaconColorIsAlliance(alliance))
            {
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.driveBase.drivePID(55, false, null);
                robot.driveBase.drivePID(10, true, this);
            } else {
                robot.driveBase.drivePID(5, true, null);
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.driveBase.drivePID(50, false, null);
                robot.driveBase.drivePID(10, true, this);
            }
        }
        //Drives to Second Beacon and Aligns with farther button
        robot.driveBase.drivePID(-10, true, null);
        if(robot.beaconPush.beaconColorIsAlliance(alliance))
        {
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            //Goes to Corner Vortex
            robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
            robot.driveBase.drivePID(-20, false, null);
            robot.driveBase.spinPID(0);
            robot.driveBase.drivePID(-70, false , null);
        } else {
            robot.driveBase.drivePID(-5, true, null);
            if(robot.beaconPush.beaconColorIsAlliance(alliance))
            {
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                //Goes to Corner Vortex
                robot.driveBase.spinPID(30);
                robot.driveBase.drivePID(-20, false, null);
                robot.driveBase.spinPID(0);
                robot.driveBase.drivePID(-65, false , null);
            } else {
                robot.driveBase.drivePID(5, true, null);
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                //Goes to Corner Vortex
                robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 30 : -30);
                robot.driveBase.drivePID(-20, false, null);
                robot.driveBase.spinPID(0);
                robot.driveBase.drivePID(-70, false , null);
            }
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
        robot.driveBase.drivePID(13, false ,null);
        robot.driveBase.spinPID(40);
        robot.driveBase.drivePID(25, false, null);
        //Loads and Fires a particle
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        //Loads & Fires second ball
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.shooter.shootSequenceAuto(true);
        robot.shooter.waitForShootAuto();
        robot.driveBase.spinPID(135);
        robot.driveBase.drivePID(35, false, null);
    }

    private void runDoNothing() throws InterruptedException {
        mClock.reset();
        mClock.startTime();
        while (mClock.time() <= 29.9) {
        }
    }

    private void runCleanUp() throws InterruptedException {
        robot.shooter.resetMotors();
        robot.beaconPush.resetRacks();
        robot.pulleySystem.resetMotors();
        robot.driveBase.resetMotors();
        robot.driveBase.resetPIDDrive();
    }


    @Override
    public boolean shouldAbort() { return robot.odsLeft.getLightDetected() >= 0.2 || robot.odsRight.getLightDetected() >= 0.2; }

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
        strategyMenu.addChoice("2 Shot + 2 Beacons + Cap Ball + Park: Close", Strategy.HIGH_RISK);
        strategyMenu.addChoice("2 Shot + 2 Beacons + Corner Vortex: Close", Strategy.HUNDRED_POINT);
        strategyMenu.addChoice("2 Beacons + Cap Ball + Park: Close", Strategy.CENTER_VORTEX);
        strategyMenu.addChoice("2 Beacons + Corner Vortex: Close", Strategy.CORNER_VORTEX);
        strategyMenu.addChoice("2 Shot + 15 Sec Delay + Corner Vortex: Close", Strategy.SHOOT_DELAY);
        strategyMenu.addChoice("2 Shot + Cap Ball: Far", Strategy.SHOOT_BALL);
        strategyMenu.addChoice("Cap Ball + Defense: Far", Strategy.DEFENSE);

        FtcMenu.walkMenuTree(allianceMenu);
        alliance = (Alliance) allianceMenu.getCurrentChoiceObject();
        strategy = (Strategy) strategyMenu.getCurrentChoiceObject();
    }

}