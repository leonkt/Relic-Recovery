package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.qualcomm.ftcrobotcontroller.CameraPreview;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.io.ByteArrayOutputStream;

/**
 * Created by Owner on 12/27/2015.
 */
public class LockdownDriveToBeacon extends LinearOpMode {

    DcMotor rightMotor;
    DcMotor leftMotor;

    final static int ENCODER_CPR = 1120;     //Encoder Counts per Revolution
    final static double GEAR_RATIO = 2;      //Gear Ratio
    final static int WHEEL_DIAMETER = 4;     //Diameter of the wheel in inches
    final static int DISTANCE = 24;          //Distance in inches to drive

    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
    final static double ROTATIONS = DISTANCE / CIRCUMFERENCE;
    final static double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;

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

    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("motorLeft");
        rightMotor = hardwareMap.dcMotor.get("motorRight");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        waitForStart();


    }
}
