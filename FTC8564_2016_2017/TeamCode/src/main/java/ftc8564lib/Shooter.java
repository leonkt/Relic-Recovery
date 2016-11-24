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

import hallib.HalDashboard;

public class Shooter {

    LinearOpMode opMode;
    DcMotor tennisArm;
    DcMotor highSpeed;
    CRServo lift;
    State state;
    ElapsedTime mClock = new ElapsedTime();
    HalDashboard dashboard;

    public enum State {
        LOADING,
        LIFTING,
        READY,
        FIRING
    }

    public Shooter(LinearOpMode opMode) {
        this.opMode = opMode;
        state = State.LOADING;
        tennisArm = opMode.hardwareMap.dcMotor.get("tennisArm");
        tennisArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tennisArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        tennisArm.setPower(0.5);
        dashboard = Robot.getDashboard();
        mClock.reset();
    }

    public void setTennisArmPower(boolean up)
    {
        if(up)
        {
            tennisArm.setTargetPosition(checkPos(1));
        } else {
            tennisArm.setTargetPosition(checkPos(0));
        }
    }

    public void setTennisArmPower() { tennisArm.setTargetPosition(tennisArm.getCurrentPosition());}

    private int checkPos(int i)
    {
        if(i == 0)
        {
            if(tennisArm.getCurrentPosition() - 100 < 0)
            {
                return 0;
            }
            return tennisArm.getCurrentPosition() - 100;
        } else if(i == 1){
            if(tennisArm.getCurrentPosition() + 100 > 500)
            {
                return 500;
            }
            return tennisArm.getCurrentPosition() + 100;
        }
        return tennisArm.getCurrentPosition();
    }

    public void resetMotors()
    {
        tennisArm.setPower(0);
    }

    private void changeState(State newState)
    {
        state = newState;
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
