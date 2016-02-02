package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Owner on 12/27/2015.
 */
public class LockdownRedAuto extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor TM;
    DcMotor W;
    DcMotor HookArm;
    DcMotor dP;
    Servo T;
    Servo C;
    GyroSensor G;
    TouchSensor touchSensor;
    DcMotorController mc1;
    DcMotorController mc2;
    DcMotorController mc3;
    ServoController sc1;
    private int REV = 0;
    private long lastTime;
    private double zeroOffset;
    private double gyroHeading;
    private ElapsedTime mRunTime = new ElapsedTime();
    private ElapsedTime mTotalTime = new ElapsedTime();

    private enum State {
        STATE_ONE, STATE_TWO, STATE_THREE, STATE_FOUR, STATE_FIVE, STATE_SIX, STATE_RETRACT
    }

    ;
    private State mHookArmState;

    final static int ENCODER_CPR = 1120;     //Encoder Counts per Revolution
    final static double GEAR_RATIO = 1;      //Gear Ratio
    final static int WHEEL_DIAMETER = 2;     //Diameter of the wheel in inches

    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;

    public void runOpMode() throws InterruptedException {
        mc1 = hardwareMap.dcMotorController.get("mc1Dc");
        mc2 = hardwareMap.dcMotorController.get("mc2Dc");
        mc3 = hardwareMap.dcMotorController.get("mc3Dc");
        sc1 = hardwareMap.servoController.get("sc1Sc");
        TM = hardwareMap.dcMotor.get("TM");
        W = hardwareMap.dcMotor.get("W");
        T = hardwareMap.servo.get("T");
        C = hardwareMap.servo.get("C");
        G = hardwareMap.gyroSensor.get("G");
        touchSensor = hardwareMap.touchSensor.get("TS");
        HookArm = hardwareMap.dcMotor.get("HookArm");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        dP = hardwareMap.dcMotor.get("Dustpan");
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        dP.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        dP.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        mRunTime.reset();
        mTotalTime.reset();
        mHookArmState = State.STATE_ONE;
        calibrateGyro();

        waitForStart();

        mTotalTime.startTime();
        sleep(7000);
        pivotGyro(-40.0, 0.5);
        driveForward(64.0, 0.5);
        sleep(100);
        spinGyro(-80.0, 0.5);
        sleep(100);
        driveForward(40.0, 0.5);
        raiseBox();
        raiseHookArm();
        sleep(100);
        while (mHookArmState != State.STATE_RETRACT) {
            switch (mHookArmState) {
                case STATE_ONE:
                    TM.setPower(0.55);
                    W.setPower(getPWRF(REV));
                    sleep(400);
                    newHookState(State.STATE_TWO);
                    REV++;
                    break;
                case STATE_TWO:
                    TM.setPower(0.55);
                    W.setPower(getPWRF(REV));
                    sleep(400);
                    newHookState(State.STATE_THREE);
                    REV++;
                    break;
                case STATE_THREE:
                    TM.setPower(0.55);
                    W.setPower(getPWRF(REV));
                    sleep(450);
                    newHookState(State.STATE_FOUR);
                    REV++;
                    break;
                case STATE_FOUR:
                    TM.setPower(0.50);
                    W.setPower(getPWRF(REV));
                    sleep(450);
                    newHookState(State.STATE_FIVE);
                    REV++;
                    break;
                case STATE_FIVE:
                    TM.setPower(0.50);
                    W.setPower(getPWRF(REV));
                    sleep(450);
                    newHookState(State.STATE_SIX);
                    REV++;
                    break;
                case STATE_SIX:
                    TM.setPower(0.45);
                    W.setPower(getPWRF(REV));
                    sleep(500);
                    newHookState(State.STATE_RETRACT);
                    break;
            }
        }
        TM.setPower(0);
        W.setPower(0);
        latchHookArm();
        sleep(100);
        constantSpeed();
        while (mHookArmState != State.STATE_THREE) {
            switch (mHookArmState) {
                case STATE_FOUR:
                    TM.setPower(-1);
                    W.setPower(-getPWRR(REV));
                    motorRight.setPower(0.9);
                    motorLeft.setPower(0.7);
                    sleep(600);
                    newHookState(State.STATE_THREE);
                    break;
                case STATE_FIVE:
                    TM.setPower(-1);
                    W.setPower(-getPWRR(REV));
                    motorRight.setPower(0.9);
                    motorLeft.setPower(0.7);
                    sleep(500);
                    newHookState(State.STATE_FOUR);
                    break;
                case STATE_SIX:
                    TM.setPower(-getPWRR(REV));
                    W.setPower(-0.8);
                    motorRight.setPower(0.8);
                    motorLeft.setPower(0.7);
                    sleep(800);
                    newHookState(State.STATE_FIVE);
                    break;
                case STATE_RETRACT:
                    motorRight.setPower(0.8);
                    motorLeft.setPower(0.7);
                    sleep(300);
                    newHookState(State.STATE_SIX);
                    break;
            }
            REV--;
        }
        TM.setPower(0.0);
        W.setPower(0.0);
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
        telemetry.addData("Total Time", mTotalTime.time());
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
            waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
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
        waitOneFullHardwareCycle();
        lastTime = System.currentTimeMillis();
        gyroHeading = 0.0;
        waitOneFullHardwareCycle();
        while (Math.abs(gyroHeading) <= Math.abs(degrees)) {
            telemetry.addData("Gyro Heading", gyroHeading);
            integrateGyro();
            waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
        waitOneFullHardwareCycle();
    }

    public void raiseHookArm() throws InterruptedException {
        HookArm.setTargetPosition(70);
        HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        HookArm.setPower(-0.25);
        waitOneFullHardwareCycle();
    }

    public void latchHookArm() throws InterruptedException {
        HookArm.setTargetPosition(50);
        HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        HookArm.setPower(-0.3);
    }

    public void raiseBox() throws InterruptedException {
        T.setPosition(0.4);
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
        waitOneFullHardwareCycle();
        lastTime = System.currentTimeMillis();
        gyroHeading = 0.0;
        waitOneFullHardwareCycle();
        while (Math.abs(gyroHeading) <= Math.abs(degrees)) {
            telemetry.addData("Gyro Heading", gyroHeading);
            integrateGyro();
            waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
        waitOneFullHardwareCycle();
    }

    public void newHookState(State state) throws InterruptedException {
        mHookArmState = state;
        waitOneFullHardwareCycle();
    }

    double getPWRF(int ROTATION_NUMBER) {
        if (ROTATION_NUMBER > 6) {
            ROTATION_NUMBER = 6;
        }
        if (ROTATION_NUMBER < 0) {
            ROTATION_NUMBER = 0;
        }
        double[] array = {0.80, 0.80, 0.85, 0.85, 0.90, 0.90, 1};
        // 13-14 in, 23.5 in, 34 in, 43 in, 52 in , 60 in
        return array[ROTATION_NUMBER];
    }

    double getPWRR(int ROTATION_NUMBER) {
        if (ROTATION_NUMBER > 6) {
            ROTATION_NUMBER = 6;
        }
        if (ROTATION_NUMBER < 0) {
            ROTATION_NUMBER = 0;
        }
        double[] array = {0.70, 0.70, 0.70, 0.70, 0.70, 0.70, 0.50};
        return array[ROTATION_NUMBER];
    }

    public void resetEncoders() throws InterruptedException {
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        waitOneFullHardwareCycle();
    }

    public void normalSpeed() throws InterruptedException {
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitOneFullHardwareCycle();
        resetEncoders();
    }

    public void constantSpeed() throws InterruptedException {
        motorLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        waitOneFullHardwareCycle();
    }

    public void calibrateGyro() throws InterruptedException {
        double sum = 0.0;
        for (int i = 0; i < 50; i++) {
            sum += G.getRotation();
            sleep(20);
        }
        zeroOffset = sum / 50.0;
    }

    public void integrateGyro() throws InterruptedException {
        long currTime = System.currentTimeMillis();
        gyroHeading += (G.getRotation() - zeroOffset) * (currTime - lastTime) / 1000.0;
        lastTime = currTime;
        waitOneFullHardwareCycle();
    }

    public int getMotorPosition(DcMotor motor) throws InterruptedException {
        if (mc1.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {
            return motor.getCurrentPosition();
        } else {
            mc1.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
            while (mc1.getMotorControllerDeviceMode() != DcMotorController.DeviceMode.READ_ONLY) {
                waitOneFullHardwareCycle();
            }
            int currPosition = motor.getCurrentPosition();
            mc1.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
            while (mc1.getMotorControllerDeviceMode() != DcMotorController.DeviceMode.WRITE_ONLY) {
                waitOneFullHardwareCycle();
            }
            return currPosition;
        }

    }
}
