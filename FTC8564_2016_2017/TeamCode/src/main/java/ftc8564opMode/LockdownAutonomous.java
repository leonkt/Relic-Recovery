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
        ONE_BEACON,
        SIXTY_POINT,
        HUNDRED_POINT,
        CORNER_VORTEX,
        CAP_BALL,
        SHOOT_BALL,
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
            case SIXTY_POINT:
                runSixtyPoint();
                break;
            case ONE_BEACON:
                runOneBeacon();
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
            case CAP_BALL:
                runCapBall();
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

    private void runSixtyPoint() throws InterruptedException {
        robot.driveBase.drivePID(25, null);
        robot.driveBase.spinPID(30);
        robot.driveBase.drivePID(55, null);
        robot.driveBase.spinPID(60);
        robot.driveBase.drivePID(22, null);
        robot.driveBase.drivePID(-5, null);
        robot.driveBase.spinPID(90);
        robot.driveBase.drivePID(-20, this);
    }

    private void runOneBeacon() throws InterruptedException {

    }

    private void runHundredPoint() throws InterruptedException {
        robot.driveBase.drivePID(15, null);
        robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 45 : -45);
        robot.driveBase.drivePID(20, null);
        robot.shooter.shootBall(alliance == Alliance.RED_ALLIANCE ? -1 : 1);
    }

    private void runShootBall() throws InterruptedException {
        robot.driveBase.spinPID(15);
    }

    private void runCornerVortex() throws InterruptedException {
        robot.driveBase.spinPID(90);
    }

    private void runCapBall() throws InterruptedException {
        robot.driveBase.drivePID(25, null);
        robot.driveBase.spinPID(alliance == Alliance.RED_ALLIANCE ? 45 : -45);
        robot.driveBase.drivePID(40, null);
    }

    private void runDefense() throws InterruptedException {
        robot.driveBase.spinPID(55);
    }

    private void runDoNothing() throws InterruptedException {
        mClock.reset();
        mClock.startTime();
        while (mClock.time() <= 29.9) {
        }
    }

    private void runCleanUp() throws InterruptedException {
        robot.shooter.resetMotors();
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
        strategyMenu.addChoice("One Beacon: Close", Strategy.ONE_BEACON);
        strategyMenu.addChoice("2 Beacons: Close", Strategy.SIXTY_POINT);
        strategyMenu.addChoice("2 Shot + 2 Beacons + Park: Close", Strategy.HUNDRED_POINT);
        strategyMenu.addChoice("2 Shot + Cap Ball: Far", Strategy.SHOOT_BALL);
        strategyMenu.addChoice("Corner Vortex: Close", Strategy.CORNER_VORTEX);
        strategyMenu.addChoice("Cap Ball: Tile Seam", Strategy.CAP_BALL);
        strategyMenu.addChoice("Defense", Strategy.DEFENSE);

        FtcMenu.walkMenuTree(allianceMenu);
        alliance = (Alliance) allianceMenu.getCurrentChoiceObject();
        strategy = (Strategy) strategyMenu.getCurrentChoiceObject();
    }

}