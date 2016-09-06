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

package ftc8564;

/**
 * Created by Owner on 2/3/2016.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class DriveBase {

    LinearOpMode opMode;

    final static int ENCODER_CPR = 1120;     //Encoder Counts per Revolution
    final static double GEAR_RATIO = 1;      //Gear Ratio
    final static int WHEEL_DIAMETER = 2;     //Diameter of the wheel in inches
    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;

    private DcMotor motorRight;
    private DcMotor motorLeft;
    private TouchSensor touchSensor;
    private GyroSensor G;
    private double zeroOffset = 0.0;
    private double deadband = 0.0;
    private double gyroHeading;
    private long lastTime;
    private ElapsedTime mRunTime = new ElapsedTime();
    private ElapsedTime mClock = new ElapsedTime();

    public DriveBase(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        motorRight = opMode.hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = opMode.hardwareMap.dcMotor.get("motorLeft");
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        G = opMode.hardwareMap.gyroSensor.get("G");
        touchSensor = opMode.hardwareMap.touchSensor.get("TS");
        mRunTime.reset();
        calibrateGyro();
    }

    public void tankDrive(float leftPower, float rightPower) throws InterruptedException {
        leftPower = Range.clip(leftPower, -1, 1);
        rightPower = Range.clip(rightPower, -1, 1);
        leftPower = (float) scaleInput(leftPower);
        rightPower = (float) scaleInput(rightPower);
        motorLeft.setPower(leftPower);
        motorRight.setPower(rightPower);
    }

    public void driveForward(double DISTANCE, double power) throws InterruptedException {
        double FRACTION;
        double delayTime = 0.0;
        normalSpeed();
        mRunTime.reset();
        if (power == 0.5) {
            FRACTION = 0.095;
        } else if (power == 0.2) {
            FRACTION = 0.35;
        } else {
            FRACTION = 0.08;
        }
        delayTime = FRACTION * DISTANCE;
        double ROTATIONS = DISTANCE / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        int LeftTarget = (int) COUNTS + getMotorPosition(motorLeft);
        int RightTarget = (int) COUNTS + getMotorPosition(motorRight);
        motorLeft.setTargetPosition(LeftTarget);
        motorRight.setTargetPosition(RightTarget);
        motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(power);
        motorRight.setPower(power);
        mRunTime.startTime();
        while (mRunTime.time() < delayTime) {
            if (touchSensor.isPressed()) {
                break;
            }
            opMode.waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
    }

    public void driveForwardNoTouch(double DISTANCE, double power) throws InterruptedException {
        double FRACTION;
        double delayTime = 0.0;
        normalSpeed();
        mRunTime.reset();
        if (power == 0.5) {
            FRACTION = 0.095;
        } else if (power == 0.2) {
            FRACTION = 0.35;
        } else {
            FRACTION = 0.08;
        }
        delayTime = FRACTION * DISTANCE;
        double ROTATIONS = DISTANCE / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        int LeftTarget = (int) COUNTS + getMotorPosition(motorLeft);
        int RightTarget = (int) COUNTS + getMotorPosition(motorRight);
        motorLeft.setTargetPosition(LeftTarget);
        motorRight.setTargetPosition(RightTarget);
        motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(power);
        motorRight.setPower(power);
        mRunTime.startTime();
        while (mRunTime.time() < delayTime) {
            opMode.waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
    }

    public void driveBackward(double DISTANCE, double power) throws InterruptedException {
        double FRACTION;
        double delayTime = 0.0;
        normalSpeed();
        mRunTime.reset();
        if (power == 0.5) {
            FRACTION = 0.095;
        } else if (power == 0.2) {
            FRACTION = 0.35;
        } else {
            FRACTION = 0.08;
        }
        delayTime = FRACTION * DISTANCE;
        double ROTATIONS = DISTANCE / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        int LeftTarget = -(int) COUNTS + getMotorPosition(motorLeft);
        int RightTarget = -(int) COUNTS + getMotorPosition(motorRight);
        motorLeft.setTargetPosition(LeftTarget);
        motorRight.setTargetPosition(RightTarget);
        motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(power);
        motorRight.setPower(power);
        mRunTime.startTime();
        while (mRunTime.time() < delayTime) {
            if (touchSensor.isPressed()) {
                break;
            }
            opMode.waitOneFullHardwareCycle();
        }
    }

    public void driveBackwardNoTouch(double DISTANCE, double power) throws InterruptedException {
        double FRACTION;
        double delayTime = 0.0;
        normalSpeed();
        mRunTime.reset();
        if (power == 0.5) {
            FRACTION = 0.095;
        } else if (power == 0.2) {
            FRACTION = 0.35;
        } else {
            FRACTION = 0.08;
        }
        delayTime = FRACTION * DISTANCE;
        double ROTATIONS = DISTANCE / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        int LeftTarget = -(int) COUNTS + getMotorPosition(motorLeft);
        int RightTarget = -(int) COUNTS + getMotorPosition(motorRight);
        motorLeft.setTargetPosition(LeftTarget);
        motorRight.setTargetPosition(RightTarget);
        motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(power);
        motorRight.setPower(power);
        mRunTime.startTime();
        while (mRunTime.time() < delayTime) {
            opMode.waitOneFullHardwareCycle();
        }
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
        motorLeft.setPower(leftPower);
        motorRight.setPower(rightPower);
        opMode.waitOneFullHardwareCycle();
        lastTime = System.currentTimeMillis();
        gyroHeading = 0.0;
        opMode.waitOneFullHardwareCycle();
        while (Math.abs(gyroHeading) <= Math.abs(degrees)) {
            integrateGyro();
            opMode.waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
        opMode.waitOneFullHardwareCycle();
    }

    public void pivotGyro(double degrees, double power) throws InterruptedException {
        constantSpeed();
        double leftPower, rightPower;
        if (degrees < 0.0) {
            leftPower = 0.0;
            rightPower = power;
        } else {
            leftPower = power;
            rightPower = 0.0;
        }
        motorLeft.setPower(leftPower);
        motorRight.setPower(rightPower);
        opMode.waitOneFullHardwareCycle();
        lastTime = System.currentTimeMillis();
        gyroHeading = 0.0;
        opMode.waitOneFullHardwareCycle();
        while (Math.abs(gyroHeading) <= Math.abs(degrees)) {
            integrateGyro();
            opMode.waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
        opMode.waitOneFullHardwareCycle();
    }

    public void pivotLeftGyro(double degrees, double power) throws InterruptedException {
        constantSpeed();
        double leftPower, rightPower;
        if (degrees < 0.0) {
            leftPower = -power;
            rightPower = 0.0;
        } else {
            leftPower = 0.0;
            rightPower = -power;
        }
        motorLeft.setPower(leftPower);
        motorRight.setPower(rightPower);
        opMode.waitOneFullHardwareCycle();
        lastTime = System.currentTimeMillis();
        gyroHeading = 0.0;
        opMode.waitOneFullHardwareCycle();
        while (Math.abs(gyroHeading) <= Math.abs(degrees)) {
            integrateGyro();
            opMode.waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
        opMode.waitOneFullHardwareCycle();
    }

    public void resetEncoders() throws InterruptedException {
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        opMode.waitOneFullHardwareCycle();
    }

    public void normalSpeed() throws InterruptedException {
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        opMode.waitOneFullHardwareCycle();
        resetEncoders();
    }

    public void constantSpeed() throws InterruptedException {
        motorLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        opMode.waitOneFullHardwareCycle();
    }

    public void calibrateGyro() throws InterruptedException {
        double value = G.getRotation();
        double minValue = value;
        double maxValue = value;
        double sum = 0.0;
        for (int i = 0; i < 50; i++) {
            value = G.getRotation();
            sum += value;
            if (value < minValue) minValue = value;
            if (value > maxValue) maxValue = value;
            waitTime(0.02);
        }
        zeroOffset = sum / 50.0;
        deadband = maxValue - minValue;
    }

    public void integrateGyro() throws InterruptedException {
        long currTime = System.currentTimeMillis();
        double value = G.getRotation() - zeroOffset;
        if (Math.abs(value) < deadband) value = 0.0;
        gyroHeading += value * (currTime - lastTime) / 1000.0;
        lastTime = currTime;
        opMode.waitOneFullHardwareCycle();
    }

    public void mountainCode() throws InterruptedException {
        motorLeft.setPower(1);
        motorRight.setPower(1);
        opMode.waitOneFullHardwareCycle();
    }

    public int getMotorPosition(DcMotor motor) throws InterruptedException {
        if (motor.getController().getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {
            return motor.getCurrentPosition();
        } else {
            motor.getController().setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
            while (motor.getController().getMotorControllerDeviceMode() != DcMotorController.DeviceMode.READ_ONLY) {
                opMode.waitOneFullHardwareCycle();
            }
            int currPosition = motor.getCurrentPosition();
            motor.getController().setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
            while (motor.getController().getMotorControllerDeviceMode() != DcMotorController.DeviceMode.WRITE_ONLY) {
                opMode.waitOneFullHardwareCycle();
            }
            return currPosition;
        }
    }

    public void waitTime(double time) throws InterruptedException {
        mClock.reset();
        mClock.startTime();
        while (mClock.time() <= time) {
            opMode.waitOneFullHardwareCycle();
        }
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
