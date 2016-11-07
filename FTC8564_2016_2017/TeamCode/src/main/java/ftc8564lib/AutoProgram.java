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

package ftc8564lib;

import com.qualcomm.robotcore.util.ElapsedTime;
import ftc8564opMode.LockdownAutonomous;

public class AutoProgram {

    Robot robot;
    LockdownAutonomous.Alliance alliance;
    LockdownAutonomous.Delay delay;
    private ElapsedTime mClock = new ElapsedTime();

    public AutoProgram(LockdownAutonomous.Alliance alliance, Robot robot, LockdownAutonomous.Delay delay) {
        this.alliance = alliance;
        this.robot = robot;
        this.delay = delay;
    }

    public void runOneBeacon() throws InterruptedException {
        runDelay();
        robot.driveBase.driveForwardPID(5,.1);
        robot.driveBase.spinGyroPID(45,0.3);
    }

    public void runTwoBeacon() throws InterruptedException {
        runDelay();
        robot.driveBase.driveForwardPID(5,.1);
        robot.driveBase.spinGyroPID(45,0.3);
    }

    public void runShootBall() throws InterruptedException {
        runDelay();

    }

    public void runCornerVortex() throws InterruptedException {
        runDelay();
        robot.driveBase.driveForwardPID(5,.1);
    }

    public void runDoNothing() throws InterruptedException {
        mClock.reset();
        mClock.startTime();
        while (mClock.time() <= 29.9) {
        }
    }

    public void runCleanUp() throws InterruptedException {
        robot.PulleySystem.resetMotors();
        robot.driveBase.resetMotors();
        robot.driveBase.resetPIDDrive();
    }

    private void runDelay() throws InterruptedException {
        double time = 0;
        if(delay == LockdownAutonomous.Delay.ZERO)
        {
            time = 0;
        } else if(delay == LockdownAutonomous.Delay.FIVE)
        {
            time = 5;
        } else if(delay == LockdownAutonomous.Delay.TEN)
        {
            time = 10;
        }
        mClock.reset();
        mClock.startTime();
        while (mClock.time() <= time) {
        }
    }

}
