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
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class DriveBase {

    LinearOpMode opMode;
    PIDControl pidControlLeft, pidControlRight;
    PIDControlTurn pidControlTurn;
    OpticalDistanceSensor odsLeft, odsRight;

    final static int ENCODER_CPR = 1120;     //Encoder Counts per Revolution on NeveRest 40 Motors
    final static double GEAR_RATIO = 1;      //Gear Ratio
    final static int WHEEL_DIAMETER = 2;     //Diameter of the wheel in inches
    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;

    private DcMotor leftMotor, rightMotor;
    private ModernRoboticsI2cGyro gyroSensor;
    private ElapsedTime mRunTime = new ElapsedTime();

    public DriveBase(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        rightMotor = opMode.hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor = opMode.hardwareMap.dcMotor.get("leftMotor");
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        gyroSensor = (ModernRoboticsI2cGyro)opMode.hardwareMap.gyroSensor.get("gyro");
        mRunTime.reset();
        gyroSensor.calibrate();
        while(gyroSensor.isCalibrating()) {
           Thread.sleep(50);
            opMode.idle();
        }
        gyroSensor.resetZAxisIntegrator();
        //Sets up PID Drive
        pidControlLeft = new PIDControl(0.05,0,0,0,1,10, leftMotor);
        pidControlRight = new PIDControl(0.05,0,0,0,1,10, rightMotor);
        pidControlRight.setInverted(true);
        pidControlTurn = new PIDControlTurn(0.05,0,0,0,1,10, gyroSensor);
        odsLeft = opMode.hardwareMap.opticalDistanceSensor.get("odsLeft");
        odsRight = opMode.hardwareMap.opticalDistanceSensor.get("odsRight");
    }

    //Input distance in inches and power with decimal to hundredth place
    public void driveForward(double distance, double power) throws InterruptedException {
        normalSpeed();
        double ROTATIONS = distance / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        int leftTarget = (int) COUNTS + leftMotor.getCurrentPosition();
        int rightTarget = (int) COUNTS + rightMotor.getCurrentPosition();
        leftMotor.setTargetPosition(leftTarget);
        rightMotor.setTargetPosition(rightTarget);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setPower(power);
        rightMotor.setPower(power);
        pidControlLeft.setTarget(COUNTS);
        pidControlRight.setTarget(COUNTS);
        pidControlTurn.setTarget(0.00);
        while (leftMotor.isBusy() || rightMotor.isBusy()) {
            if(!pidControlLeft.isOnTarget() || !pidControlRight.isOnTarget() || !pidControlTurn.isOnTarget()) {
                double leftPower = pidControlLeft.getPowerOutput() + pidControlTurn.getTurnOutput();
                double rightPower = pidControlRight.getPowerOutput() - pidControlTurn.getTurnOutput();
                leftPower = Range.clip(leftPower, -1, 1);
                rightPower = Range.clip(rightPower, -1, 1);
                leftMotor.setPower(leftPower);
                rightMotor.setPower(rightPower);
            }
            opMode.idle();
        }
        resetMotors();
        resetPIDDrive();
    }

    public void driveForwardODS(double distance, double power) throws InterruptedException {
        normalSpeed();
        double ROTATIONS = distance / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        int leftTarget = (int) COUNTS + leftMotor.getCurrentPosition();
        int rightTarget = (int) COUNTS + rightMotor.getCurrentPosition();
        leftMotor.setTargetPosition(leftTarget);
        rightMotor.setTargetPosition(rightTarget);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setPower(power);
        rightMotor.setPower(power);
        pidControlLeft.setTarget(COUNTS);
        pidControlRight.setTarget(COUNTS);
        pidControlTurn.setTarget(0.00);
        while (leftMotor.isBusy() || rightMotor.isBusy()) {
            if(!pidControlLeft.isOnTarget() || !pidControlRight.isOnTarget() || !pidControlTurn.isOnTarget()) {
                double leftPower = pidControlLeft.getPowerOutput() + pidControlTurn.getTurnOutput();
                double rightPower = pidControlRight.getPowerOutput() - pidControlTurn.getTurnOutput();
                leftPower = Range.clip(leftPower, -1, 1);
                rightPower = Range.clip(rightPower, -1, 1);
                leftMotor.setPower(leftPower);
                rightMotor.setPower(rightPower);
            }
            opMode.idle();
        }
        resetMotors();
        resetPIDDrive();
    }

    public void spinGyro(double degrees, double power) throws InterruptedException {
        constantSpeed();
        double leftPower, rightPower;
        if (degrees < 0.0) {
            leftPower = -power;
            rightPower = power;
        } else {
            leftPower = power;
            rightPower = -power;
        }
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
        while (Math.abs(gyroSensor.getHeading()) <= Math.abs(degrees)) {
            opMode.idle();
        }
        leftMotor.setPower(0.0);
        rightMotor.setPower(0.0);
    }

    public void tankDrive(float leftPower, float rightPower) throws InterruptedException {
        leftPower = Range.clip(leftPower, -1, 1);
        rightPower = Range.clip(rightPower, -1, 1);
        leftPower = (float) scaleInput(leftPower);
        rightPower = (float) scaleInput(rightPower);
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }

    public void resetMotors() throws InterruptedException {
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void constantSpeed() throws InterruptedException {
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void normalSpeed() throws InterruptedException {
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.11, 0.13, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.65, 0.72, 0.75, 0.80, 0.85, 0.90, 0.90};
        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        // index should be positive.
        if (index < 0) {
            index = -index;
        }
        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }
        // get value from the array.
        double dScale = 0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }
        // return scaled value.
        return dScale;
    }

    public void resetPIDDrive() {
        pidControlLeft.reset();
        pidControlRight.reset();
        pidControlTurn.reset();
    }

}
