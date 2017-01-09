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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import hallib.HalDashboard;
import hallib.HalUtil;

public class Shooter {

    LinearOpMode opMode;
    DcMotor tennisArm;
    DcMotor highSpeed;
    ElapsedTime mClock = new ElapsedTime();
    HalDashboard dashboard;
    State shooter;

    private double shootTime;

    private enum State {
        HOME,
        LOADED,
        READY,
        FIRED,
        MOVING_HOME,
        LOADING,
        READYING,
        FIRING
    }

    public Shooter(LinearOpMode opMode, boolean auto) {
        this.opMode = opMode;
        tennisArm = opMode.hardwareMap.dcMotor.get("tennisArm");
        tennisArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if(auto)
        {
            tennisArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            tennisArm.setPower(0.3);
        } else {
            tennisArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        highSpeed = opMode.hardwareMap.dcMotor.get("highSpeed");
        highSpeed.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        highSpeed.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter = State.HOME;
        dashboard = Robot.getDashboard();
        mClock.reset();
    }

    public void primeBall(double power)
    {
        highSpeed.setPower(power);
    }

    public void waitForShoot()
    {
        while(shooter == State.READYING || shooter == State.LOADING || shooter == State.FIRING || shooter == State.MOVING_HOME)
        {
            shooterTask();
        }
    }

    public void shootSequence(boolean redSide)
    {
        moveCenter();
        loadBall(redSide);
        setBall(redSide);
        shootBall(redSide);
    }

    private void moveCenter()
    {
        if(shooter == State.FIRED)
        {
            highSpeed.setTargetPosition(0);
            shootTime = HalUtil.getCurrentTime() + 1;
            changeState(State.MOVING_HOME);
        }
    }

    private void loadBall(boolean redSide)
    {
        if(shooter == State.HOME)
        {
            if(redSide)
            {
                highSpeed.setTargetPosition(10);
            } else {
                highSpeed.setTargetPosition(-10);
            }
            shootTime = HalUtil.getCurrentTime() + 1;
            changeState(State.LOADING);
        }
    }

    private void setBall(boolean redSide)
    {
        if(shooter == State.LOADED)
        {
            if(redSide)
            {
                highSpeed.setTargetPosition(100);
            } else {
                highSpeed.setTargetPosition(-100);
            }
            shootTime = HalUtil.getCurrentTime() + 1;
            changeState(State.READYING);
        }
    }

    private void shootBall(boolean redSide)
    {
        if(shooter == State.READY)
        {
            if(redSide)
            {
                highSpeed.setTargetPosition(200);
            } else {
                highSpeed.setTargetPosition(-200);
            }
            shootTime = HalUtil.getCurrentTime() + 1;
            changeState(State.FIRING);
        }
    }

    public void shooterTask()
    {
        if(shooter == State.FIRING && HalUtil.getCurrentTime() >= shootTime)
        {
            changeState(State.FIRED);
        } else if(shooter == State.READYING && HalUtil.getCurrentTime() >= shootTime)
        {
            changeState(State.READY);
        } else if(shooter == State.LOADING && HalUtil.getCurrentTime() >= shootTime)
        {
            changeState(State.LOADED);
        } else if(shooter == State.MOVING_HOME && HalUtil.getCurrentTime() >= shootTime)
        {
            changeState(State.HOME);
        }
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

    private void changeState(State newState)
    {
        shooter = newState;
    }

}
