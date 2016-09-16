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

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import hallib.*;

public class PIDControlTurn {

    public ModernRoboticsI2cGyro gyro;

    private HalDashboard dashboard;
    private double kP;
    private double kI;
    private double kD;
    private double kF;
    private double tolerance;
    private double settlingTime;

    private boolean absSetPoint = false;
    private boolean noOscillation = false;
    private double minInput = 0.0;
    private double maxInput = 0.0;
    private double minOutput = -180.0;
    private double maxOutput = 180.0;

    private double prevError = 0.0;
    private double totalError = 0.0;
    private double settlingStartTime = 0.0;
    private double setPoint = 0.0;
    private double output = 0.0;

    // Constructor
    public PIDControlTurn(double kP, double kI, double kD, double kF, double tolerance, double settlingTime, ModernRoboticsI2cGyro gyro) {
        dashboard = Robot.getDashboard();
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.tolerance = tolerance;
        this.settlingTime = settlingTime;
        this.gyro = gyro;
    }

    //Displays PID Information on a Line Number
    public void displayPidInfo(int lineNum) {
        dashboard.displayPrintf(lineNum, "Target=%.1f, Input=%.1f, Error=%.1f", setPoint, gyro.getIntegratedZValue(), prevError);
        dashboard.displayPrintf(lineNum + 1, "minOutput=%.1f, Output=%.1f, maxOutput=%.1f", minOutput, output, maxOutput);
    }

    //Sets target or heading for the gyro sensor
    public void setTarget(double target) {
        double input = gyro.getIntegratedZValue();
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

    //Determines whether or not robot is on track
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

    //Calculates the power output for turning
    public double getOutput() {

        double error = setPoint - gyro.getIntegratedZValue();
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
