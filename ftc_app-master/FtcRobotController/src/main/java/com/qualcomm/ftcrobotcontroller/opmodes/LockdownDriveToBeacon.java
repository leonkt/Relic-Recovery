package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by Owner on 12/27/2015.
 */
public class LockdownDriveToBeacon extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor TM;
    DcMotor W;
    DcMotor HookArm;
    DcMotor dP;
    Servo T;
    Servo C;
    GyroSensor G;
    DcMotorController mc1;
    DcMotorController mc2;
    DcMotorController mc3;
    ServoController sc1;
    private long lastTime;
    private double zeroOffset;
    private double gyroHeading;

    final static int ENCODER_CPR = 1120;     //Encoder Counts per Revolution
    final static double GEAR_RATIO = 2;      //Gear Ratio
    final static int WHEEL_DIAMETER = 4;     //Diameter of the wheel in inches

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
        HookArm = hardwareMap.dcMotor.get("HookArm");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        dP = hardwareMap.dcMotor.get("Dustpan");
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        dP.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        dP.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        calibrateGyro();

        waitForStart();

        driveBackward(30.0, 4500, 0.5);
        turnGyro(-45.0, 0.5);
        driveBackward(65.0, 7500, 0.7);
        raisedP();
        turnGyro(-45.0, 0.5);
        tDrop();
    }

    public void driveBackward(double DISTANCE, int x, double power) throws InterruptedException {
        normalSpeed();
        resetEncoders();
        waitOneFullHardwareCycle();
        double ROTATIONS = DISTANCE / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        motorLeft.setTargetPosition(-(int) COUNTS);
        motorRight.setTargetPosition(-(int) COUNTS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(-power);
        motorRight.setPower(-power);
        sleep(x);
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
    }

    public void driveStraight(double DISTANCE, int x) throws InterruptedException {
        double ROTATIONS = DISTANCE / CIRCUMFERENCE;
        double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        motorLeft.setTargetPosition((int) COUNTS);
        motorRight.setTargetPosition((int) COUNTS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorLeft.setPower(0.3);
        motorRight.setPower(0.3);
        sleep(x);
    }

    public void spin(String a, int i) throws InterruptedException {
        if(a.equals("Left")) {
            motorLeft.setPower(-0.5);
            motorRight.setPower(0.5);
        }
        if(a.equals("Right")) {
            motorLeft.setPower(0.5);
            motorRight.setPower(-0.5);
        }
        sleep(i);
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

    public void turn(String a, int i) throws InterruptedException {
        if(a.equals("Left")) {
            motorLeft.setPower(-0.5);
            motorRight.setPower(0.5);
        }
        if(a.equals("Right")) {
            motorLeft.setPower(0.5);
            motorRight.setPower(-0.5);
        }
        sleep(i);
    }

    public void turnGyro(double degrees, double power) throws InterruptedException {
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
        gyroHeading = 0.0;
        lastTime = System.currentTimeMillis();
        while (Math.abs(gyroHeading) < Math.abs(degrees))
        {
            motorLeft.setPower(leftPower);
            motorRight.setPower(rightPower);
            integrateGyro();
            waitForNextHardwareCycle();
        }
        motorLeft.setPower(0.0);
        motorRight.setPower(0.0);
        sleep(20);
    }

    public void curve(String a, int i) throws InterruptedException {
        if(a.equals("Left")) {
            motorLeft.setPower(0.4);
            motorRight.setPower(0.7);
        }
        if(a.equals("Right")) {
            motorLeft.setPower(0.7);
            motorRight.setPower(0.4);
        }
        sleep(i);
    }

    public void tDrop() throws InterruptedException {
        T.setPosition(0.1);
    }

    public void raisedP() throws InterruptedException {
        dP.setTargetPosition(300);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(-0.05);
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
