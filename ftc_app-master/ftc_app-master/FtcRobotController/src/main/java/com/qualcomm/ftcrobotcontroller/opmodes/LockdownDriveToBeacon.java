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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.io.ByteArrayOutputStream;

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
    DcMotorController mc1;
    DcMotorController mc2;
    DcMotorController mc3;
    ServoController sc1;
    private enum State {
        STATE_ONE, STATE_TWO, STATE_THREE, STATE_FOUR, STATE_FIVE, STATE_SIX
    };
    private State mCurrentState;
    private State mHookArmState;
    public ElapsedTime mRuntime = new ElapsedTime();   // Time into round.
    private ElapsedTime mStateTime = new ElapsedTime();  // Time into current state
    private int TARGET;
    private int TARGET_1;

    final static int ENCODER_CPR = 1120;     //Encoder Counts per Revolution
    final static double GEAR_RATIO = 1;      //Gear Ratio
    final static int WHEEL_DIAMETER = 4;     //Diameter of the wheel in inches
    final static int DISTANCE = 15;          //Distance in inches to drive

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

    public void runOpMode() throws InterruptedException {
        mc1 = hardwareMap.dcMotorController.get("mc1Dc");
        mc2 = hardwareMap.dcMotorController.get("mc2Dc");
        mc3 = hardwareMap.dcMotorController.get("mc3Dc");
        sc1 = hardwareMap.servoController.get("sc1Sc");
        TM = hardwareMap.dcMotor.get("TM");
        W = hardwareMap.dcMotor.get("W");
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

        camera = ((FtcRobotControllerActivity)hardwareMap.appContext).camera;
        camera.setPreviewCallback(previewCallback);

        Camera.Parameters parameters = camera.getParameters();
        data = parameters.flatten();

        waitForStart();

        driveToPosition((int) COUNTS);

        //turnLeft();

        //turnRight();

        //telemetry.addData("Camera Color", cameraColor());

    }

    public String cameraColor() {
        String colorString = "";
        for(int i = 0; i < 5; i++) {
            int redValue = 0;
            int blueValue = 0;
            int greenValue = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = image.getPixel(x, y);
                    redValue += red(pixel);
                    blueValue += blue(pixel);
                    greenValue += green(pixel);
                }
            }
            int color = highestColor(redValue, greenValue, blueValue);
            switch (color) {
                case 0:
                    colorString = "RED";
                    break;
                case 1:
                    colorString = "GREEN";
                    break;
                case 2:
                    colorString = "BLUE";
            }
        }
        return colorString;
    }

    public int highestColor(int red, int green, int blue) {
        int[] color = {red,green,blue};
        int value = 0;
        for (int i = 1; i < 3; i++) {
            if (color[value] < color[i]) {
                value = i;
            }
        }
        return value;
    }

    public void driveToPosition(int i) throws InterruptedException {
        while((getMotorPosition(motorLeft) < i) || (getMotorPosition(motorRight) < i)) {
            if (getMotorPosition(motorLeft) < getMotorPosition(motorRight)) {
                motorLeft.setPower(-0.8);
                motorRight.setPower(-0.5);
            } else if (getMotorPosition(motorRight) < getMotorPosition(motorLeft)) {
                motorRight.setPower(-0.8);
                motorLeft.setPower(-0.5);
            } else {
                motorLeft.setPower(-0.6);
                motorRight.setPower(-0.6);
            }
        }
    }

    public void turnRight() throws InterruptedException {
        motorLeft.setPower(0.5);
        motorRight.setPower(-0.5);
        sleep(100);
    }

    public void turnLeft() throws InterruptedException {
        motorLeft.setPower(-0.5);
        motorRight.setPower(0.5);
        sleep(100);
    }

    public void curveLeft() throws InterruptedException {
        motorLeft.setPower(0.4);
        motorRight.setPower(0.7);
        sleep(100);
    }

    public void curveRight() throws InterruptedException {
        motorLeft.setPower(0.7);
        motorRight.setPower(0.4);
        sleep(100);
    }

    public int getMotorPosition(DcMotor motor) throws InterruptedException
    {
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

    int setTARGET_1(int i) {
        TARGET_1 = i;
        return TARGET_1;
    }

    int setTARGET(int i) {
        TARGET = i;
        return TARGET;
    }

    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.11, 0.13, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.65, 0.72, 0.75, 0.80, 0.85, 0.95, 1.00};

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

    double getPWRF(int ROTATION_NUMBER) {
        if(ROTATION_NUMBER > 6) {
            ROTATION_NUMBER = 6;
        }
        if(ROTATION_NUMBER < 0) {
            ROTATION_NUMBER = 0;
        }
        double[] array = {0.90, 1, 1, 1, 1, 1, 1};
        // 13-14 in, 23.5 in, 34 in, 43 in, 52 in , 60 in
        return array[ROTATION_NUMBER];
    }

    double getPWRR(int ROTATION_NUMBER) {
        if(ROTATION_NUMBER > 6) {
            ROTATION_NUMBER = 6;
        }
        if(ROTATION_NUMBER < 0) {
            ROTATION_NUMBER = 0;
        }
        double[] array = {0.70, 0.70, 0.70, 0.70, 0.70, 0.70, 0.50};
        return array[ROTATION_NUMBER];
    }

    private void newState(State newState)
    {
        // Reset the state time, and then change to next state.
        mStateTime.reset();
        mCurrentState = newState;
    }

    private void newHookState(State newState)
    {
        mStateTime.reset();
        mHookArmState = newState;
    }
}
