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
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Position;

import ftc8564lib.VuMarkIdentification;
import ftc8564lib.DriveBase;
import ftc8564lib.Robot;
import ftclib.FtcChoiceMenu;
import ftclib.FtcMenu;
import ftclib.FtcValueMenu;
import hallib.HalUtil;

@Autonomous(name="LockdownAutonomous", group="Autonomous")
public class LockdownAutonomous extends LinearOpMode implements FtcMenu.MenuButtons, DriveBase.AbortTrigger {

    Robot robot;
    ColorSensor colorSensor;
    DcMotor motorLeft;
    DcMotor motorRight;
    Servo clampleft;
    Servo clampright;

    private ElapsedTime mClock = new ElapsedTime();

    public enum Alliance {
        BLUE_ALLIANCE,
        RED_ALLIANCE
    }

    private enum AlliancePosition {
        BLUE_RIGHT,
        BLUE_LEFT,
        RED_RIGHT,
        RED_LEFT
    }

    private Alliance alliance = Alliance.BLUE_ALLIANCE;
    private AlliancePosition alliancePosition = AlliancePosition.BLUE_RIGHT;

    RelicRecoveryVuMark curVuMark = RelicRecoveryVuMark.UNKNOWN;
    VuMarkIdentification vuMarkDecode;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new Robot(this,true);// need this to run menu test
        //color sensor
        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        //left motor
        motorLeft = hardwareMap.dcMotor.get("left");
        //right motor
        motorRight = hardwareMap.dcMotor.get("right");
        //left clamp servo
        clampleft = hardwareMap.servo.get("clampleft");
        //right clamp servo
        clampright = hardwareMap.servo.get("clampright");
        // New instance of VuMarkIdentification object
        vuMarkDecode = new VuMarkIdentification();

        doMenus();
        telemetry.clearAll();

        double power = .5;

        clampleft.setPosition(.75);
        clampright.setPosition(.3);

        switch (alliancePosition) {
            case BLUE_RIGHT:
                alliance = Alliance.BLUE_ALLIANCE;
                telemetry.addData("Selected:","BLUE RIGHT");
                break;
            case BLUE_LEFT:
                alliance = Alliance.BLUE_ALLIANCE;
                telemetry.addData("Selected:","BLUE LEFT");
                break;
            case RED_RIGHT:
                alliance = Alliance.RED_ALLIANCE;
                telemetry.addData("Selected:","RED RIGHT");
                break;
            case RED_LEFT:
                alliance = Alliance.RED_ALLIANCE;
                telemetry.addData("Selected:","RED LEFT");
                break;
        }

        telemetry.update();

        waitForStart();

        vuMarkDecode.init(hardwareMap); // calls activate method at end
        vuMarkDecode.decodePictograph();
        curVuMark = vuMarkDecode.getCryptoboxKey();
        vuMarkDecode.fini(); // calls deactivate method on relicTrackables object

        telemetry.addData("Detected:","%s",curVuMark);
        telemetry.update();

        // robot.driveBase.resetHeading();

        // Drop Jewel Arm
        //servo.setPosition(?)

        // Detect Color of Ball
        // Color sensor is facing fwd in direction of Robot
        colorSensor.enableLed(true);

        if (alliance == Alliance.BLUE_ALLIANCE) {
            if(colorSensor.red() > colorSensor.blue()){
                //crservo.setPosition(.3)
            }
            else if (colorSensor.blue() > colorSensor.red()) {
                //crservo.setPosition(.7)
            }
        }
        else if (alliance == Alliance.RED_ALLIANCE) {
            // if color of Jewel facing color sensor is RED, move bck to knock off BLUE
            // else move fwd to knock off BLUE
            if(colorSensor.red() > colorSensor.blue()){
                //crservo.setPosition(.7)
            }
            else if (colorSensor.blue() > colorSensor.red()) {
                //crservo.setPosition(.3)
            }
        }
        sleep(500);
        //crservo.setPosition(.5)
        //servo.setPosition(?)

        switch (alliancePosition) {
            case BLUE_RIGHT:
                //go forward
                motorLeft.setPower(-power);
                motorRight.setPower(power);
                if (curVuMark == RelicRecoveryVuMark.RIGHT) {
                    sleep(2000);
                }
                else if (curVuMark == RelicRecoveryVuMark.CENTER){
                    sleep(2500);
                }
                else {
                    sleep(3000);
                }
                //turn left
                motorRight.setPower(power);
                motorLeft.setPower(power);
                sleep(2000);
                //move forward
                motorLeft.setPower(-power);
                motorRight.setPower(power);
                sleep(500);
                //release block
                clampleft.setPosition(.9);
                clampright.setPosition(.15);
                // Robot is facing left, Cryptobox is on left of robot
                // Need to go forward and then spin 90 to face Cryptobox
                break;
            case BLUE_LEFT:
                //go forward
                motorLeft.setPower(-power);
                motorRight.setPower(power);

                // Robot is facing left, Cryptobox is straight ahead of robot
                // Need to go forward and then face Cryptobox
                break;
            case RED_RIGHT:
                //go backward
                motorRight.setPower(-power);
                motorLeft.setPower(power);
                sleep(2000);
                //turn 180
                motorRight.setPower(power);
                motorLeft.setPower(power);
                sleep(4000);
                // Robot is facing left, Cryptobox is straight behind robot
                // Need to go backward and then spin 180 to face Cryptobox
                break;
            case RED_LEFT:
                //go backward
                motorRight.setPower(-power);
                motorLeft.setPower(power);
                //turn left
                motorLeft.setPower(-power);
                motorRight.setPower(power);
                // Robot is facing left, Cryptobox is behind, on left of robot
                // Need to go backward and then spin 90 to face Cryptobox
                break;
        }

        runCleanUp();
        //telemetry.addData("Sleeping:","7 seconds");
        //telemetry.update();
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
        FtcChoiceMenu alliancePositionMenu = new FtcChoiceMenu("Alliance Position:", null, this);

        alliancePositionMenu.addChoice("BLUE RIGHT", AlliancePosition.BLUE_RIGHT);
        alliancePositionMenu.addChoice("BLUE LEFT", AlliancePosition.BLUE_LEFT);
        alliancePositionMenu.addChoice("RED RIGHT", AlliancePosition.RED_RIGHT);
        alliancePositionMenu.addChoice("RED LEFT", AlliancePosition.RED_LEFT);

        FtcMenu.walkMenuTree(alliancePositionMenu);
        alliancePosition = (AlliancePosition) alliancePositionMenu.getCurrentChoiceObject();
    }

}