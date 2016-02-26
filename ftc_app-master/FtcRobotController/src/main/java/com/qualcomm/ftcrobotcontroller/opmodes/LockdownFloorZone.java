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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import ftc8564.Robot;

/**
 * Created by Owner on 12/27/2015.
 */
public class LockdownFloorZone extends LinearOpMode {

    public Robot robot;

    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        robot.driveBase.calibrateGyro();

        waitForStart();

        robot.driveBase.driveBackward(35.0, 0.5);
        robot.driveBase.spinGyro(-39.0, 0.5);
        robot.driveBase.driveBackward(61.65, 0.5);
        robot.driveBase.spinGyro(-37.0, 0.5);
        robot.driveBase.waitTime(0.1);
        robot.climberDeployer.raisedP();
        robot.climberDeployer.tPrep();
        robot.driveBase.waitTime(0.1);
        robot.driveBase.driveBackward(13.0, 0.2);
        robot.climberDeployer.lowerdP();
        robot.climberDeployer.tDrop();
        robot.driveBase.waitTime(0.1);
        robot.driveBase.driveForward(12.0, 0.2);
        robot.climberDeployer.zerodP();
        robot.driveBase.spinGyro(-87.0, 0.5);
        robot.driveBase.driveBackward(23.0, 0.5);
    }
}
