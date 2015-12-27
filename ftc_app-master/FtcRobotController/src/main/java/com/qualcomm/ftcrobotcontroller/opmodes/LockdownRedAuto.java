package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.qualcomm.ftcrobotcontroller.CameraPreview;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.io.ByteArrayOutputStream;

/**
 * Created by Owner on 12/4/2015.
 */
public class LockdownRedAuto extends LinearOpMode {

    static final double ENCODER_CPR = 1120.0; // for us digital encoders or 1120.0 for NeveRest motors.
    final static double GEAR_RATIO = 1;
    final static int DIAMETER = 4;
    static final double INCHES_PER_TICK = (DIAMETER * Math.PI) / ENCODER_CPR;

    private Camera camera;
    public CameraPreview preview;
    public Bitmap image;
    private int width;
    private int height;
    private YuvImage yuvImage = null;
    private int looped = 0;
    private String data;

    private int red(int pixel) {
        return (pixel >> 16) & 0xff;
    }

    private int green(int pixel) {
        return (pixel >> 8) & 0xff;
    }

    private int blue(int pixel) {
        return pixel & 0xff;
    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            width = parameters.getPreviewSize().width;
            height = parameters.getPreviewSize().height;
            yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
            looped += 1;
        }
    };

    private void convertImage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 0, out);
        byte[] imageBytes = out.toByteArray();
        image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //Init
        DcMotorController mc1 = hardwareMap.dcMotorController.get("mc1");
        //DcMotorController mc2 = hardwareMap.dcMotorController.get("mc2Dc");
        //DcMotorController mc3 = hardwareMap.dcMotorController.get("mc3Dc");
        //ServoController sc = hardwareMap.servoController.get("sc1Sc");
        DcMotor motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        DcMotor motorLeft = hardwareMap.dcMotor.get("motorLeft");
        //Servo leftflipper = hardwareMap.servo.get("lf");
        //Servo rightflipper = hardwareMap.servo.get("rf");
        // Arm Motors/Servos
        //Servo lazys = hardwareMap.servo.get("lazy");
        //Servo dpud = hardwareMap.servo.get("dps");
        //DcMotor dp = hardwareMap.dcMotor.get("dpm");
        //DcMotor ua = hardwareMap.dcMotor.get("uam");
        //DcMotor fnbm = hardwareMap.dcMotor.get("lsm");
        //Setting the power/position to starting position to ensure no misread of values
        //leftflipper.setPosition(0.5);
        //rightflipper.setPosition(0.5);
        //dpud.setPosition(0.5);
        waitOneFullHardwareCycle();
        //Start of Autonomous
        waitForStart();
        waitForInches(motorLeft, motorRight, 10, 0.3);
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

    public void resetEncoders(DcMotor motorRight, DcMotor motorLeft) {
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    public int getMotorPosition(DcMotor motor) throws InterruptedException
    {
        DcMotorController motorController = motor.getController();
        if (motorController.getMotorControllerDeviceMode()  == DcMotorController.DeviceMode.READ_ONLY)
        {
            return motor.getCurrentPosition();
        }
        else
        {
            motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
            while (motorController.getMotorControllerDeviceMode() != DcMotorController.DeviceMode.READ_ONLY)
            {
                waitForNextHardwareCycle();
            }
            int currPosition = motor.getCurrentPosition();
            motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
            while (motorController.getMotorControllerDeviceMode() != DcMotorController.DeviceMode.WRITE_ONLY)
            {
                waitForNextHardwareCycle();
            }
            return currPosition;
        }
    }

    public void waitForInches(DcMotor motorLeft, DcMotor motorRight, int inches, double power) throws InterruptedException {
        {
            int encoderValue = (int) (inches / INCHES_PER_TICK);
            int leftMotorTarget = getMotorPosition(motorLeft) + encoderValue;
            int rightMotorTarget = getMotorPosition(motorRight) + encoderValue;

            motorLeft.setTargetPosition(leftMotorTarget);
            motorRight.setTargetPosition(rightMotorTarget);

            motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            waitForNextHardwareCycle();

            power = Math.abs(power);
            motorLeft.setPower(power);
            motorRight.setPower(power);
        }
    }
}
