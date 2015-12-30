package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.qualcomm.ftcrobotcontroller.CameraPreview;
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
public class LockdownBlueAuto extends LinearOpMode {

    final static int ENCODER_CPR = 1120;
    final static double GEAR_RATIO = 1;
    final static int WHEEL_DIAMETER = 4;
    final static double CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;

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
        public void onPreviewFrame(byte[] data, Camera camera)
        {
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
    public void runOpMode() throws InterruptedException{
        //Init
        DcMotorController mc1 = hardwareMap.dcMotorController.get("mc1Dc");
        DcMotorController mc2 = hardwareMap.dcMotorController.get("mc2Dc");
        DcMotorController mc3 = hardwareMap.dcMotorController.get("mc3Dc");
        ServoController sc = hardwareMap.servoController.get("sc1Sc");
        DcMotor motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        DcMotor motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        Servo leftflipper = hardwareMap.servo.get("lf");
        Servo rightflipper = hardwareMap.servo.get("rf");
        // Arm Motors/Servos
        Servo lazys = hardwareMap.servo.get("lazy");
        Servo dpud = hardwareMap.servo.get("dps");
        DcMotor dp = hardwareMap.dcMotor.get("dpm");
        DcMotor ua = hardwareMap.dcMotor.get("uam");
        DcMotor fnbm = hardwareMap.dcMotor.get("lsm");
        //Setting the power/position to starting position to ensure no misread of values
        leftflipper.setPosition(1);
        rightflipper.setPosition(0.5);
        dpud.setPosition(0.5);
        resetEncoders(motorRight, motorLeft);
        waitOneFullHardwareCycle();
        waitForStart();
        //Start of Autonomous


        waitOneFullHardwareCycle();
    }

    public void resetEncoders(DcMotor motorRight, DcMotor motorLeft)
    {
        motorRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    public void waitForInches(DcMotor motorLeft, DcMotor motorRight, int inches, double power)
    {
        double rotations = inches / CIRCUMFERENCE;
        double counts = ENCODER_CPR * rotations * GEAR_RATIO;
        motorLeft.setTargetPosition((int)counts);
        motorRight.setTargetPosition((int)counts);

        motorLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motorRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

        motorLeft.setPower(power);
        motorRight.setPower(power);
    }

    public void turnLeft(DcMotor motorLeft, DcMotor motorRight, int angle)
    {

    }

    public void turnRight(DcMotor motorLeft, DcMotor motorRight, int angle)
    {

    }

    public void curveLeft(DcMotor motorLeft, DcMotor motorRight, int angle)
    {

    }

    public void curveRight(DcMotor motorLeft, DcMotor motorRight, int angle)
    {

    }
}
