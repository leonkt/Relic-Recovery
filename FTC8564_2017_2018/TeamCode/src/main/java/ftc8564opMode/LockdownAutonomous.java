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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Position;

import ftc8564lib.DriveBase;
import ftc8564lib.Robot;
import ftclib.FtcChoiceMenu;
import ftclib.FtcMenu;
import ftclib.FtcValueMenu;
import hallib.HalUtil;

@Autonomous(name="LockdownAutonomous", group="Autonomous")
public class LockdownAutonomous extends LinearOpMode implements FtcMenu.MenuButtons, DriveBase.AbortTrigger {

    Robot robot;
    private ElapsedTime mClock = new ElapsedTime();

    public enum Alliance {
        BLUE_ALLIANCE,
        RED_ALLIANCE
    }

    private enum Position { // Which Balancing Stone are we on from perspective of Alliance box
        RIGHT,
        LEFT
    }

    private Alliance alliance = Alliance.BLUE_ALLIANCE;
    private Position positionOption = Position.RIGHT;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this,true); // need this to run menu test
        doMenus();
        waitForStart();

        // robot.driveBase.resetHeading();

        if (alliance == Alliance.BLUE_ALLIANCE && positionOption == Position.LEFT) {
            ;
        } else if (alliance == Alliance.BLUE_ALLIANCE && positionOption == Position.RIGHT) {
            ;
        } else if (alliance == Alliance.RED_ALLIANCE && positionOption == Position.LEFT) {
            ;
        } else if (alliance == Alliance.RED_ALLIANCE && positionOption == Position.RIGHT) {
            ;
        }
        runCleanUp();
        telemetry.clearAll();
        telemetry.addData(">","Selected %s Alliance and %s balancing stone.", alliance == Alliance.BLUE_ALLIANCE ? "BLUE" : "RED",
                          positionOption == Position.RIGHT ? "RIGHT" : "LEFT");
        telemetry.update();
        sleep(7000); // sleep 7s

    }

    private void runCleanUp() throws InterruptedException {
        //robot.shooter.resetMotors();
        //robot.beaconPush.resetRacks();
        //robot.pulleySystem.resetMotors();
        //robot.driveBase.resetMotors();
        //robot.driveBase.resetPIDDrive();
    }

    @Override
    public boolean shouldAbort() { return true; }

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
        FtcChoiceMenu balancingStoneMenu = new FtcChoiceMenu("BalancingStone", allianceMenu, this);

        allianceMenu.addChoice("BLUE", Alliance.BLUE_ALLIANCE, balancingStoneMenu);
        allianceMenu.addChoice("RED", Alliance.RED_ALLIANCE, balancingStoneMenu);

        balancingStoneMenu.addChoice("RIGHT", Position.RIGHT);
        balancingStoneMenu.addChoice("LEFT", Position.LEFT);

        FtcMenu.walkMenuTree(allianceMenu);
        alliance = (Alliance) allianceMenu.getCurrentChoiceObject();
        positionOption = (Position) balancingStoneMenu.getCurrentChoiceObject();
    }

}

