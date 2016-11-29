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
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import hallib.HalDashboard;

public class DriveBase implements PIDControl.PidInput {

    private LinearOpMode opMode;
    private PIDControl pidControl, pidControlTurn;

    private final static double SCALE = (144.5/12556.5);    // INCHES_PER_COUNT

    private DcMotor leftMotor, rightMotor;
    private ModernRoboticsI2cGyro gyroSensor;
    private HalDashboard dashboard;

    public interface AbortTrigger
    {
        boolean shouldAbort();
    }

    public DriveBase(LinearOpMode opMode, boolean auto) throws InterruptedException {
        this.opMode = opMode;
        rightMotor = opMode.hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor = opMode.hardwareMap.dcMotor.get("leftMotor");
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gyroSensor = (ModernRoboticsI2cGyro)opMode.hardwareMap.gyroSensor.get("gyro");
        ElapsedTime mRunTime = new ElapsedTime();
        mRunTime.reset();
        dashboard = Robot.getDashboard();
        if(auto)
        {
            gyroSensor.calibrate();
            dashboard.displayPrintf(0, "Gyro : Calibrating");
            while(gyroSensor.isCalibrating()) {
                opMode.idle();
            }
            dashboard.displayPrintf(1, "Gyro : Done Calibrating");
        }
        //Sets up PID Drive: kP, kI, kD, kF, Tolerance, Settling Time
        pidControl = new PIDControl(0.03,0,0.001,0,2.0,0.2,this);
        pidControlTurn = new PIDControl(0.014,0,0.02,0,2.0,0.2,this);
        dashboard.clearDisplay();
    }

    //Input distance in inches and power with decimal to hundredth place
    public void drivePID(double distance, AbortTrigger abortTrigger) throws InterruptedException {
        pidControl.setTarget(distance);
        pidControlTurn.setTarget(0.0);
        while (!pidControl.isOnTarget() || !pidControlTurn.isOnTarget()) {
            if (abortTrigger != null && abortTrigger.shouldAbort()) {
                break;
            }
            double drivePower = pidControl.getPowerOutput();
            double turnPower = pidControlTurn.getPowerOutput();
            leftMotor.setPower(drivePower + turnPower);
            rightMotor.setPower(drivePower - turnPower);
            opMode.idle();
        }
        resetMotors();
        resetPIDDrive();
    }

    public void spinPID(double degrees) throws InterruptedException {
        pidControlTurn.setTarget(degrees);
        while (!pidControlTurn.isOnTarget()) {
            double outputPower = pidControlTurn.getPowerOutput();
            leftMotor.setPower(outputPower);
            rightMotor.setPower(-outputPower);
            pidControlTurn.displayPidInfo(5);
            opMode.idle();
        }
        resetMotors();
        resetPIDDrive();
    }

    public void tankDrive(float leftPower, float rightPower) throws InterruptedException {
        leftPower = Range.clip(leftPower, -1, 1);
        rightPower = Range.clip(rightPower, -1, 1);
        leftPower = (float) scalePower(leftPower);
        rightPower = (float) scalePower(rightPower);
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }

    public void resetMotors() throws InterruptedException {
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void resetPIDDrive() {
        pidControl.reset();
        pidControlTurn.reset();
    }

    public void noEncoders()
    {
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private double scalePower(double dVal)
    {
        return -(Math.signum(dVal) * ((Math.pow(dVal, 4) * (.9)) + .1));
    }

    @Override
    public double getInput(PIDControl pidCtrl)
    {
        double input = 0.0;
        if (pidCtrl == pidControl)
        {
            input = (leftMotor.getCurrentPosition() + rightMotor.getCurrentPosition())*SCALE/2.0;
        }
        else if (pidCtrl == pidControlTurn)
        {
            input = gyroSensor.getIntegratedZValue();
        }
        return input;
    }

}
