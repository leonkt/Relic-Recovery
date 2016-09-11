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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class DriveBase {

    LinearOpMode opMode;

    final static int ENCODER_CPR = 1120;     //Encoder Counts per Revolution
    final static double GEAR_RATIO = 1;      //Gear Ratio
    final static int WHEEL_DIAMETER = 2;     //Diameter of the wheel in inches
    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;

    private DcMotor leftMotorBack, leftMotorFront, rightMotorFront, rightMotorBack;
    private GyroSensor gyroSensor;
    private ElapsedTime mRunTime = new ElapsedTime();

    public DriveBase(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        rightMotorFront = opMode.hardwareMap.dcMotor.get("rightMotorFront");
        rightMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorBack = opMode.hardwareMap.dcMotor.get("rightMotorBack");
        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorFront = opMode.hardwareMap.dcMotor.get("leftMotorFront");
        leftMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotorBack = opMode.hardwareMap.dcMotor.get("leftMotorBack");
        leftMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        gyroSensor = opMode.hardwareMap.gyroSensor.get("gyroSensor");
        mRunTime.reset();
        gyroSensor.calibrate();
    }

    public void driveForward(double distance, double power) throws InterruptedException {
        double ROTATIONS = distance / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        int leftTargetFront = (int) COUNTS + leftMotorFront.getCurrentPosition();
        int rightTargetFront = (int) COUNTS + rightMotorFront.getCurrentPosition();
        int leftTargetBack = (int) COUNTS + leftMotorBack.getCurrentPosition();
        int rightTargetBack = (int) COUNTS + rightMotorBack.getCurrentPosition();
        leftMotorFront.setTargetPosition(leftTargetFront);
        rightMotorFront.setTargetPosition(rightTargetFront);
        leftMotorBack.setTargetPosition(leftTargetBack);
        rightMotorBack.setTargetPosition(rightTargetBack);
        leftMotorFront.setPower(power);
        rightMotorFront.setPower(power);
        leftMotorBack.setPower(power);
        rightMotorBack.setPower(power);
        while(leftMotorFront.isBusy() || rightMotorFront.isBusy() || leftMotorBack.isBusy() || rightMotorBack.isBusy()) {
            if(!leftMotorFront.isBusy()) {
                leftMotorFront.setPower(0);
            }
            if(!rightMotorFront.isBusy()) {
                rightMotorFront.setPower(0);
            }
            if(!leftMotorBack.isBusy()) {
                leftMotorBack.setPower(0);
            }
            if(!rightMotorBack.isBusy()) {
                rightMotorBack.setPower(0);
            }
        }
        resetMotors();
    }

    public void tankDrive(float leftPower, float rightPower) throws InterruptedException {
        leftPower = Range.clip(leftPower, -1, 1);
        rightPower = Range.clip(rightPower, -1, 1);
        leftPower = (float) scaleInput(leftPower);
        rightPower = (float) scaleInput(rightPower);
        leftMotorFront.setPower(leftPower);
        rightMotorFront.setPower(rightPower);
        leftMotorBack.setPower(leftPower);
        rightMotorBack.setPower(rightPower);
    }

    public void resetMotors() throws InterruptedException {
        leftMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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

}
