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
 * Created by Owner on 2/7/2016.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import ftc8564.Robot;

public class LockdownTeleOp extends LinearOpMode {

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        robot.driveBase.constantSpeed();
        waitForStart();
        while (opModeIsActive()) {
            // Drive Train command
            robot.driveBase.tankDrive(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
            // Tape Measure and Winch motor
            if (-gamepad2.left_stick_y > 0) {
                robot.hanger.runUP();
            } else if (-gamepad2.left_stick_y < 0) {
                robot.hanger.runDOWN();
            } else {
                robot.hanger.stop();
            }
            // Mechanism that controls tape measure and winch angle
            if (gamepad2.left_bumper) {
                robot.hanger.hookArm50();
            } else if (gamepad2.right_bumper) {
                robot.hanger.hookArm150();
            } else if (gamepad2.left_trigger != 0) {
                robot.hanger.hookArm135();
            } else if (gamepad2.right_trigger != 0) {
                robot.hanger.hookArm0();
            } else {
                robot.hanger.steadyHookArm();
            }
            // Second Arm to latch on pull-up bar
            if (gamepad1.left_bumper) {
                robot.climberDeployer.dP0();
            } else if (gamepad1.right_bumper) {
                robot.climberDeployer.dP250();
            } else if (gamepad1.left_trigger == 1) {
                robot.climberDeployer.dP700();
            } else if (gamepad1.right_trigger == 1) {
                robot.climberDeployer.dP950();
            } else {
                robot.climberDeployer.steadydP();
            }
            // All Clear Signal
            if (gamepad1.dpad_up) {
                robot.hanger.upAllClear();
            } else if (gamepad1.dpad_down) {
                robot.hanger.downAllClear();
            } else {
                robot.hanger.noAllClear();
            }
            //Flipper
            if (gamepad1.dpad_left) {
                robot.climberDeployer.setFlipper();
            }
            if (gamepad1.dpad_right) {
                robot.climberDeployer.stopFlipper();
            }
            //Climber Box
            if (gamepad2.dpad_up) {
                robot.climberDeployer.tZero();
            }
            if (gamepad2.dpad_right) {
                robot.climberDeployer.tPrep();
            }
            if (gamepad2.dpad_down) {
                robot.climberDeployer.tDrop();
            }
            if (gamepad2.dpad_left) {
                robot.climberDeployer.tHighUp();
            }
            if (gamepad2.x) {
                robot.climberDeployer.tiltBoxLeft();
            } else if (gamepad2.b) {
                robot.climberDeployer.tiltBoxRight();
            } else {
                robot.climberDeployer.centerBox();
            }
            // Zipline Climbers
            if (gamepad1.x) {
                robot.hanger.raiseZipline();
            } else if (gamepad1.b) {
                robot.hanger.lowerZipline();
            } else {
                robot.hanger.zeroZipline();
            }
        } // end of if gamepad1.back not pressed

    }
}