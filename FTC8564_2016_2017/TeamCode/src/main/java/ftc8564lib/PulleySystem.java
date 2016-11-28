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
import com.qualcomm.robotcore.util.Range;

import hallib.HalDashboard;
import hallib.HalUtil;

public class PulleySystem {

    LinearOpMode opMode;
    DcMotor leftPulley;
    DcMotor rightPulley;
    DcMotor leftArm;
    DcMotor rightArm;
    HalDashboard dashboard;

    private static final double LIFT_SYNC_KP = 0.005;               //this value needs to be tuned
    private static final double LIFT_POSITION_TOLERANCE = 25; //this value needs to be tuned; in ticks
    private static final double SCALE = (48/9336);  //  INCHES_PER_COUNT; needs to be tuned
    private static final int MIN_DISTANCE = 0;
    private static final int MAX_DISTANCE = 9336;
    private double prevTarget = 0.0;

    State state;

    public enum State {
        NO_PRESSURE,
        PRESSURE
    }

    public PulleySystem(LinearOpMode opMode) {
        this.opMode = opMode;
        dashboard = Robot.getDashboard();
        leftPulley = opMode.hardwareMap.dcMotor.get("leftPulley");
        rightPulley = opMode.hardwareMap.dcMotor.get("rightPulley");
        leftArm = opMode.hardwareMap.dcMotor.get("leftArm");
        rightArm = opMode.hardwareMap.dcMotor.get("rightArm");
        leftPulley.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightPulley.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftPulley.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightPulley.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        state = State.NO_PRESSURE;
    }

    public void applyPressure()
    {
        changeState(State.PRESSURE);
    }

    public void noPressure()
    {
        changeState(State.NO_PRESSURE);
    }

    public void setLeftArm(double power)
    {
        leftArm.setPower(power);
    }

    public void setRightArm(double power)
    {
        rightArm.setPower(power);
    }

    public void restArmMotors()
    {
        if(state == State.PRESSURE)
        {
            leftArm.setPower(0.07);
            rightArm.setPower(-0.07);
        } else {
            leftArm.setPower(0);
            rightArm.setPower(0);
        }
    }

    public void manualControl(double power)
    {
        leftPulley.setPower(power);
        rightPulley.setPower(power);
    }

    public void setSyncMotorPower(double power) throws InterruptedException
    {
        if (power != 0.0)
        {
            int targetPosition = power < 0.0? MAX_DISTANCE: MIN_DISTANCE;
            if (targetPosition != prevTarget)
            {
                leftPulley.setTargetPosition(targetPosition);
                rightPulley.setTargetPosition(targetPosition);
                prevTarget = targetPosition;
            }
            boolean leftOnTarget = Math.abs(targetPosition - leftPulley.getCurrentPosition()) <= LIFT_POSITION_TOLERANCE;
            boolean rightOnTarget = Math.abs(targetPosition - rightPulley.getCurrentPosition()) <= LIFT_POSITION_TOLERANCE;
            if (!leftOnTarget || !rightOnTarget)
            {
                double differentialPower = Range.clip((rightPulley.getCurrentPosition() - leftPulley.getCurrentPosition())*LIFT_SYNC_KP, -1.0, 1.0);
                double leftPower = power + differentialPower;
                double rightPower = power - differentialPower;
                double minPower = Math.min(leftPower, rightPower);
                double maxPower = Math.max(leftPower, rightPower);
                double scale = maxPower > 1.0? 1.0/maxPower: minPower < 1.0? -1.0/minPower: 1.0;
                leftPower *= scale;
                rightPower *= scale;
                leftPulley.setPower(Range.clip(leftPower,-1,1));
                rightPulley.setPower(Range.clip(rightPower,-1,1));
            } else {
                leftPulley.setPower(0.0);
                rightPulley.setPower(0.0);
                prevTarget = 0.0;
            }
        } else {
            leftPulley.setPower(0.0);
            rightPulley.setPower(0.0);
            prevTarget = 0.0;
        }
    }

    private void changeState(State newState)
    {
        state = newState;
    }

    public void resetMotors()
    {
        leftPulley.setPower(0);
        rightPulley.setPower(0);
    }
}
