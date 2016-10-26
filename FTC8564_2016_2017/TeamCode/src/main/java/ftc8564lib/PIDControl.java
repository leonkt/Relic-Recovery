/*
 * Copyright (c) 2015 Titan Robotics Club (http://www.titanrobotics.com)
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

import hallib.*;

/*
*   PID Control is a way to minimize error by using a formula called Proportion, Integral, Derivative.
*   Overtime, the robot will make large errors by not being extremely accurate when going to the target value.
*   In order to fix this, it's possible to minimize the error by adjusting the power through proportion then integral and then derivative.
*   Each one respectively are ok in reducing error. The Proportion method drastically affects the error so it needs a lot of fine tuning.
*   The Integral and Derivative method are fine tuning methods and are combined to create an effective way of reducing error.
*   These three methods are used for current time processing. A fourth method called feedforward sort of acts to predict the next error and adjust for it.
*/

public class PIDControl {

    private HalDashboard dashboard;
    private double kP;
    private double kI;
    private double kD;
    private double kF;
    private double tolerance;
    private double settlingTime;
    private PidInput pidInput;

    private boolean inverted = false;
    private boolean absSetPoint = false;
    private boolean noOscillation = false;
    private double minInput = 0.0;
    private double maxInput = 0.0;
    private double minOutput = -1.0;
    private double maxOutput = 1.0;

    private double prevError = 0.0;
    private double totalError = 0.0;
    private double settlingStartTime = 0.0;
    private double setPoint = 0.0;
    private double output = 0.0;

    public interface PidInput
    {
        double getInput(PIDControl pidCtrl);
    }

    // Constructor
    public PIDControl(double kP, double kI, double kD, double kF, double tolerance, double settlingTime, PidInput pidInput) {
        dashboard = Robot.getDashboard();
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.tolerance = tolerance;
        this.settlingTime = settlingTime;
        this.pidInput = pidInput;
    }

    public void displayPidInfo(int lineNum) {
        dashboard.displayPrintf(lineNum, "Target=%.1f, Input=%.1f, Error=%.1f", setPoint, pidInput.getInput(this), prevError);
        dashboard.displayPrintf(lineNum + 1, "minOutput=%.1f, Output=%.1f, maxOutput=%.1f", minOutput, output, maxOutput);
        System.out.printf("Target=%.1f, Input=%.1f, Error=%.1f", setPoint, pidInput.getInput(this), prevError);
        System.out.println();
        System.out.printf("minOutput=%.1f, Output=%.1f, maxOutput=%.1f", minOutput, output, maxOutput);
    }

    //Inverts information if needed
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    //Sets distance in ticks
    public void setTarget(double target) {
        double input = pidInput.getInput(this);
        setPoint = target;
        if (!absSetPoint) {
            setPoint += input;
        }

        if (maxInput > minInput) {
            if (setPoint > maxInput) {
                setPoint = maxInput;
            } else if (setPoint < minInput) {
                setPoint = minInput;
            }
        }

        prevError = setPoint - input;
        if (inverted) {
            prevError = -prevError;
        }
        totalError = 0.0;
        settlingStartTime = HalUtil.getCurrentTime();
    }

    //Returns previous error
    public double getError() {
        return prevError;
    }

    public void reset() {
        prevError = 0.0;
        totalError = 0.0;
        setPoint = 0.0;
        output = 0.0;
    }

    //Determines whether or not the robot is on track
    public boolean isOnTarget() {

        boolean onTarget = false;

        if (noOscillation) {
            if (Math.abs(prevError) <= tolerance) {
                onTarget = true;
            }
        } else if (Math.abs(prevError) > tolerance) {
            settlingStartTime = HalUtil.getCurrentTime();
        } else if (HalUtil.getCurrentTime() >= settlingStartTime + settlingTime) {
            onTarget = true;
        }

        return onTarget;
    }

    //Calculates the power output for driving forward
    public double getPowerOutput() {

        double error = setPoint - pidInput.getInput(this);
        if (inverted) {
            error = -error;
        }

        if (kI != 0.0) {
            double potentialGain = (totalError + error) * kI;
            if (potentialGain >= maxOutput) {
                totalError = maxOutput / kI;
            } else if (potentialGain > minOutput) {
                totalError += error;
            } else {
                totalError = minOutput / kI;
            }
        }

        output = kP * error + kI * totalError + kD * (error - prevError) + kF * setPoint;

        prevError = error;
        if (output > maxOutput) {
            output = maxOutput;
        } else if (output < minOutput) {
            output = minOutput;
        }

        return output;
    }

}