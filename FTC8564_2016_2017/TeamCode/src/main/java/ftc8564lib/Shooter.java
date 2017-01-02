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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import hallib.HalDashboard;

public class Shooter {

    LinearOpMode opMode;
    DcMotor tennisArm;
    DcMotor highSpeed;
    ElapsedTime mClock = new ElapsedTime();
    HalDashboard dashboard;

    public Shooter(LinearOpMode opMode) {
        this.opMode = opMode;
        tennisArm = opMode.hardwareMap.dcMotor.get("tennisArm");
        tennisArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tennisArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        highSpeed = opMode.hardwareMap.dcMotor.get("highSpeed");
        highSpeed.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        highSpeed.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        dashboard = Robot.getDashboard();
        mClock.reset();
    }

    public void primeBall(double power)
    {
        highSpeed.setPower(power);
    }

    public void shootBall(double power)
    {
        highSpeed.setPower(power);
        waitTime(0.35);
        highSpeed.setPower(0);
    }

    public void setTennisArmPower(boolean up)
    {
        if(up)
        {
            tennisArm.setPower(0.4);
        } else {
            tennisArm.setPower(-0.4);
        }
    }

    public void setTennisArmPower() { tennisArm.setPower(0);}

    public void resetMotors()
    {
        tennisArm.setPower(0);
    }

    private void waitTime(double i)
    {
        mClock.reset();
        mClock.startTime();
        while(mClock.time() <= i)
        {
            opMode.idle();
        }
    }

}
