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

    public enum Strategy {
        DO_NOTHING,
        ONE_BEACON,
        TWO_BEACON,
        CORNER_VORTEX,
        SHOOT_BALL,
        DEFENSE
    }

    public enum StartPosition {
        CLOSE,
        FAR
    }

    private Alliance alliance = Alliance.RED_ALLIANCE;
    private Strategy strategy = Strategy.DO_NOTHING;
    private StartPosition startPosition = StartPosition.CLOSE;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this,true);
        doMenus();
        waitForStart();

        switch (strategy) {
            case ONE_BEACON:
                runOneBeacon();
                break;
            case TWO_BEACON:
                runTwoBeacon();
                break;
            case SHOOT_BALL:
                runShootBall();
                break;
            case CORNER_VORTEX:
                runCornerVortex();
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

    private void runOneBeacon() throws InterruptedException {
        robot.beaconPush.detectBeaconColor(alliance);
    }

    private void runTwoBeacon() throws InterruptedException {
        robot.driveBase.drivePID(10, this);
    }

    private void runShootBall() throws InterruptedException {
        robot.driveBase.spinPID(-10);
    }

    private void runCornerVortex() throws InterruptedException {
        robot.driveBase.drivePID(robot.beaconPush.detectBeaconColor(alliance) ? 0 : 10, null);
    }

    private void runDefense() throws InterruptedException {

    }

    private void runDoNothing() throws InterruptedException {
        mClock.reset();
        mClock.startTime();
        while (mClock.time() <= 29.9) {
        }
    }

    private void runCleanUp() throws InterruptedException {
        robot.shooter.resetMotors();
        robot.PulleySystem.resetMotors();
        robot.driveBase.resetMotors();
        robot.driveBase.resetPIDDrive();
    }

    @Override
    public boolean shouldAbort() { return robot.odsLeft.getRawLightDetected() >= 1.0 || robot.odsRight.getRawLightDetected() >= 1.0; }

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
        FtcChoiceMenu startPosMenu = new FtcChoiceMenu("Start position:", allianceMenu, this);
        FtcChoiceMenu strategyMenu = new FtcChoiceMenu("Strategy:", startPosMenu, this);

        allianceMenu.addChoice("Red", Alliance.RED_ALLIANCE, startPosMenu);
        allianceMenu.addChoice("Blue", Alliance.BLUE_ALLIANCE, startPosMenu);

        startPosMenu.addChoice("Close", StartPosition.CLOSE, strategyMenu);
        startPosMenu.addChoice("Far", StartPosition.FAR, strategyMenu);

        strategyMenu.addChoice("Do Nothing", Strategy.DO_NOTHING);
        strategyMenu.addChoice("One Beacon", Strategy.ONE_BEACON);
        strategyMenu.addChoice("Two Beacon", Strategy.TWO_BEACON);
        strategyMenu.addChoice("Shoot Ball", Strategy.SHOOT_BALL);
        strategyMenu.addChoice("Corner Vortex", Strategy.CORNER_VORTEX);
        strategyMenu.addChoice("Defense", Strategy.DEFENSE);

        FtcMenu.walkMenuTree(allianceMenu);
        alliance = (Alliance) allianceMenu.getCurrentChoiceObject();
        strategy = (Strategy) strategyMenu.getCurrentChoiceObject();
    }

}