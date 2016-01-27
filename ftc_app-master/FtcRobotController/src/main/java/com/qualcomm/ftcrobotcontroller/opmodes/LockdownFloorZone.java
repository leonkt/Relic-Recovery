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
public class LockdownFloorZone extends LinearOpMode {

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
    private long lastTime;
    private double zeroOffset;
    private double gyroHeading;
    private ElapsedTime mRunTime = new ElapsedTime();
    private ElapsedTime mTotalTime = new ElapsedTime();

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
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        dP = hardwareMap.dcMotor.get("Dustpan");
        dP.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        dP.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        mRunTime.reset();
        mTotalTime.reset();
        calibrateGyro();

        waitForStart();

        mTotalTime.startTime();
        driveBackward(35.0, 0.5);
        spinGyro(-39.7, 0.5);
        driveBackward(61.65, 0.5);
        spinGyro(-40.0, 0.5);
        sleep(100);
        raisedP();
        tPrep();
        driveBackward(15.0, 0.2);
        lowerdP();
        tDrop();
        sleep(100);
        driveForward(12.0, 0.5);
        zerodP();
        spinGyro(-90.0, 0.5);
        driveBackward(23.0, 0.5);
        telemetry.addData("Total Time", mTotalTime.time());
    }

    public void driveBackward(double DISTANCE, double power) throws InterruptedException {
        double FRACTION;
        double delayTime = 0.0;
        normalSpeed();
        mRunTime.reset();
        if(power == 0.5) {
            FRACTION = 0.095;
        } else if(power == 0.2) {
            FRACTION = 0.35;
        } else {
            FRACTION = 0.075;
        }
        delayTime = FRACTION * DISTANCE;
        double ROTATIONS = DISTANCE / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        int LeftTarget = - (int) COUNTS + getMotorPosition(motorLeft);
        int RightTarget = - (int) COUNTS + getMotorPosition(motorRight);
        motorLeft.setTargetPosition(LeftTarget);
        motorRight.setTargetPosition(RightTarget);
        motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(power);
        motorRight.setPower(power);
        mRunTime.startTime();
        while(mRunTime.time() < delayTime) {
            if(touchSensor.isPressed()) {
                break;
            }
            waitOneFullHardwareCycle();
        }
    }

    public void driveForward(double DISTANCE, double power) throws InterruptedException {
        double FRACTION;
        double delayTime = 0.0;
        normalSpeed();
        mRunTime.reset();
        if(power == 0.5) {
            FRACTION = 0.095;
        } else if(power == 0.2) {
            FRACTION = 0.35;
        }  else {
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
        while(mRunTime.time() < delayTime) {
            waitOneFullHardwareCycle();
        }
    }

    public void spinGyro(double degrees, double power) throws InterruptedException {
        constantSpeed();
        double leftPower, rightPower;
        if (degrees <= 0.0)
        {
            leftPower = -power;
            rightPower = power;
        }
        else
        {
            leftPower = power;
            rightPower = -power;
        }
        motorLeft.setPower(leftPower);
        motorRight.setPower(rightPower);
        lastTime = System.currentTimeMillis();
        gyroHeading = 0.0;
        while (Math.abs(gyroHeading) <= Math.abs(degrees))
        {
            telemetry.addData("Gyro Heading", gyroHeading);
            integrateGyro();
            waitOneFullHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
    }

    public void tPrep() throws InterruptedException {
        T.setPosition(0.25);
        waitOneFullHardwareCycle();
    }

    public void tDrop() throws InterruptedException {
        T.setPosition(0.55);
        waitOneFullHardwareCycle();
    }

    public void raisedP() throws InterruptedException {
        dP.setTargetPosition(150);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(-0.09);
        sleep(165);
        dP.setTargetPosition(250);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(-0.07);
        sleep(150);
    }

    public void lowerdP() throws InterruptedException {
        dP.setTargetPosition(150);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(-0.08);
        waitOneFullHardwareCycle();
    }

    public void zerodP() throws InterruptedException {
        dP.setTargetPosition(0);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(-0.1);
        T.setPosition(0);
        waitOneFullHardwareCycle();
    }

    public void raise45() throws InterruptedException {
        HookArm.setTargetPosition(45);
        HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        HookArm.setPower(-0.3);
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
        for(int i = 0; i < 50; i++) {
            sum += G.getRotation();
            sleep(20);
        }
        zeroOffset = sum/50.0;
    }

    public void integrateGyro() {
        long currTime = System.currentTimeMillis();
        gyroHeading += (G.getRotation() - zeroOffset)*(currTime - lastTime)/1000.0;
        lastTime = currTime;
    }

    public int getMotorPosition(DcMotor motor) throws InterruptedException
    {
        if(mc1.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {
            return motor.getCurrentPosition();
        } else {
            mc1.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
            while (mc1.getMotorControllerDeviceMode() != DcMotorController.DeviceMode.READ_ONLY)
            {
                waitOneFullHardwareCycle();
            }
            int currPosition = motor.getCurrentPosition();
            mc1.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
            while (mc1.getMotorControllerDeviceMode() != DcMotorController.DeviceMode.WRITE_ONLY)
            {
                waitOneFullHardwareCycle();
            }
            return currPosition;
        }

    }
}
