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

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import ftclib.*;
import ftc8564lib.*;
import hallib.HalUtil;

@Autonomous(name="LockdownAutonomous", group="Autonomous")
public class LockdownAutonomous extends LinearOpMode implements FtcMenu.MenuButtons{
    private Robot robot;

    private ElapsedTime mClock = new ElapsedTime();

    //different scenerios
    public enum Alliance_Position {
        BLUE_RIGHT,
        BLUE_LEFT,
        RED_RIGHT,
        RED_LEFT
    }

    //defaults to blue right
    private Alliance_Position alliance = Alliance_Position.BLUE_RIGHT;
    //defaults to center vumark
    private RelicRecoveryVuMark vumark = RelicRecoveryVuMark.CENTER;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this, true);// need this to run menu test
        doMenus();
        robot.VuMark.activate();
        waitForStart();
        // robot.driveBase.resetHeading();
        if (alliance == Alliance_Position.BLUE_RIGHT) {
            robot.clamps.down();
            //jewel arm
            robot.jewelArm.resetServo();
            robot.jewelArm.armDown();
            robot.jewelArm.pushJewels(true);
            robot.jewelArm.armUp();
            robot.jewelArm.resetServo();
            robot.clamps.lift();
            //scan and read picture
            robot.VuMark.decodePictograph();
            telemetry.addData("VuMark", "%s visible", robot.VuMark.getCryptoboxKey());
            telemetry.update();
            vumark = (robot.VuMark.getCryptoboxKey());
            //drive to cryptobox and place glyph
            if (vumark == RelicRecoveryVuMark.LEFT) {
                robot.driveBase.drivePID(32, false);
            } else if (vumark == RelicRecoveryVuMark.RIGHT) {
                robot.driveBase.drivePID(48 , false);
            } else {
                robot.driveBase.drivePID(40, false);
            }
            robot.driveBase.spinPID(-90);
            robot.driveBase.drivePID(15, false);
            robot.clamps.open();
            robot.driveBase.drivePID(4, false);
            robot.driveBase.drivePID(-8,false);
        }
        else if (alliance == Alliance_Position.BLUE_LEFT) {
            robot.clamps.down();
            //jewel arm
            robot.jewelArm.resetServo();
            robot.jewelArm.armDown();
            robot.jewelArm.pushJewels(true);
            robot.jewelArm.armUp();
            robot.jewelArm.resetServo();
            robot.clamps.lift();
            //scan and read picture
            robot.VuMark.decodePictograph();
            telemetry.addData("VuMark", "%s visible", robot.VuMark.getCryptoboxKey());
            telemetry.update();
            vumark = (robot.VuMark.getCryptoboxKey());
            //drive to cryptobox and place glyph
            if (vumark == RelicRecoveryVuMark.LEFT) {
                robot.driveBase.drivePID(24, true);
                robot.driveBase.spinPID(15);
            } else if (vumark == RelicRecoveryVuMark.RIGHT) {
                robot.driveBase.drivePID(30, false);
                robot.driveBase.spinPID(90);
                robot.driveBase.drivePID(24, false);
                robot.driveBase.spinPID(-90);
            } else {
                robot.driveBase.drivePID(28, false);
                robot.driveBase.spinPID(90);
                robot.driveBase.drivePID(20, false);
                robot.driveBase.spinPID(-90);
            }
            robot.driveBase.drivePID(11, false);
            robot.clamps.open();
            robot.driveBase.drivePID(-6,false);

        }
        else if (alliance == Alliance_Position.RED_RIGHT) {
            robot.clamps.down();
            //jewel arm
            robot.jewelArm.resetServo();
            robot.jewelArm.armDown();
            robot.jewelArm.pushJewels(true);
            robot.jewelArm.armUp();
            robot.jewelArm.resetServo();
            robot.clamps.lift();
            //scan and read picture
            robot.VuMark.decodePictograph();
            telemetry.addData("VuMark", "%s visible", robot.VuMark.getCryptoboxKey());
            telemetry.update();
            vumark = (robot.VuMark.getCryptoboxKey());
            //drive to cryptobox and place glyph
            if (vumark == RelicRecoveryVuMark.LEFT) {
                robot.driveBase.drivePID(-24, false);
                robot.driveBase.spinPID(165);
            } else if (vumark == RelicRecoveryVuMark.RIGHT) {
                robot.driveBase.drivePID(-26, false);
                robot.driveBase.spinPID(90);
                robot.driveBase.drivePID(23, false);
                robot.driveBase.spinPID(-90);
            } else {
                robot.driveBase.drivePID(-26, false);
                robot.driveBase.spinPID(90);
                robot.driveBase.drivePID(18, false);
                robot.driveBase.spinPID(-90);
            }
            robot.driveBase.drivePID(10, false);
            robot.clamps.open();
            robot.driveBase.drivePID(-6,false);
        }
        else if (alliance == Alliance_Position.RED_LEFT) {
            robot.clamps.down();
            //jewel arm
            robot.jewelArm.resetServo();
            robot.jewelArm.armDown();
            robot.jewelArm.pushJewels(true);
            robot.jewelArm.armUp();
            robot.jewelArm.resetServo();
            robot.clamps.lift();
            //scan and read picture
            robot.VuMark.decodePictograph();
            telemetry.addData("VuMark", "%s visible", robot.VuMark.getCryptoboxKey());
            telemetry.update();
            vumark = (robot.VuMark.getCryptoboxKey());
            //drive to cryptobox and place glyph
            if (vumark == RelicRecoveryVuMark.LEFT) {
                robot.driveBase.drivePID(-48.5, false);
            }
            else if (vumark == RelicRecoveryVuMark.RIGHT){
                robot.driveBase.drivePID(-30, false);
            }
            else {
                robot.driveBase.drivePID(-37, false);
            }
            robot.driveBase.spinPID(-90);
            robot.driveBase.drivePID(15, false);
            robot.clamps.open();
            robot.driveBase.drivePID(-5,false);
        }
        runCleanUp();
        telemetry.clearAll();
        telemetry.update();
        sleep(7000); // sleep 7s
    }

    private void runCleanUp() throws InterruptedException {
        //robot.driveBase.resetMotors();
        //robot.driveBase.resetPIDDrive();
    }

    //menu
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
        FtcChoiceMenu alliancePositionMenu = new FtcChoiceMenu("Alliance Position:", null, this);

        alliancePositionMenu.addChoice("BLUE RIGHT", LockdownAutonomous.Alliance_Position.BLUE_RIGHT);
        alliancePositionMenu.addChoice("BLUE LEFT", LockdownAutonomous.Alliance_Position.BLUE_LEFT);
        alliancePositionMenu.addChoice("RED RIGHT", LockdownAutonomous.Alliance_Position.RED_RIGHT);
        alliancePositionMenu.addChoice("RED LEFT", LockdownAutonomous.Alliance_Position.RED_LEFT);

        FtcMenu.walkMenuTree(alliancePositionMenu);
        alliance = (LockdownAutonomous.Alliance_Position) alliancePositionMenu.getCurrentChoiceObject();
    }

}