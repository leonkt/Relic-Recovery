package com.qualcomm.ftcrobotcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * BeaconSeeker Code. I tried to minmize changes to FtcRobotControllerActivity. Only 2 lines are changed there.
 * This routine simply over-rides the Android life-cycle routines and inserts itself as needed.
 * Of course the layout had to be changed too to include the camera preview.
 * This code can be turned off at run time to improve performance if needed. See turnOffBeaconSeeker().
 * Access mBeaconCenterPointPixels to get beacon point.
 */
public class BeaconSeekerActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String  TAG              = "BeaconSeekerActivity";
    private static CameraBridgeViewBase mOpenCvCameraView;
    private ColorBlobDetector    mDetectorPink;
    private ColorBlobDetector    mDetectorBlue;
    private Scalar               mBlobColorRgbaPink;
    private Scalar               mBlobColorRgbaBlue;
    private Mat mRgba;
    private Mat mRgbaT;
    private Mat mRgbaRotated;
    static public Point mBeaconCenterPointPixels;
    static public Point mBeaconCenterPointPercent;
    private Scalar CONTOUR_COLOR;

    String         _configPath = "/sdcard/FIRST/calibration.txt";
    String _configFileVersion = "Version1";     // One word! no spaces. Using Scanner to read....

    public enum BeaconSeekerStateEnum { On, Off }

    private static BeaconSeekerStateEnum mBeaconSeekerState = BeaconSeekerStateEnum.Off;
    protected String mBeaconSeekerStatusMessage = "";

    public static void enableBeaconSeeker() {
        mBeaconSeekerState = mBeaconSeekerState.On;
        mOpenCvCameraView.enableView();
        mOpenCvCameraView.enableFpsMeter();
    }
    public static void disableBeaconSeeker() {
        mBeaconSeekerState = mBeaconSeekerState.Off;
        mOpenCvCameraView.disableView();
        mOpenCvCameraView.disableFpsMeter();
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    protected void toggleBeaconSeekerOnOff() {
        mBeaconSeekerState = mBeaconSeekerState == BeaconSeekerStateEnum.Off ? BeaconSeekerStateEnum.On : BeaconSeekerStateEnum.Off;
    }
    public void showToastMessage(String message) {
        final Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }
    public void onCameraViewStarted(int width, int height) {
        mRgbaT = new Mat();
        mRgbaRotated = new Mat();
        CONTOUR_COLOR = new Scalar(0,255,0,255);
        mDetectorPink = new ColorBlobDetector();
        mDetectorBlue = new ColorBlobDetector();
        if (!readConfig(_configPath)) {
            showToastMessage("Can't find BlobDetector config file, guessing at best pink and blue.");
            setPinkColor(new Scalar(152.0, 61.0, 81.0, 255.0), true);
            setBlueColor(new Scalar(14.0, 52.0, 76.0, 255.0), true);
        }
    }
    public void onCameraViewStopped() {
//    mRgba.release();
    }
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;
        List<MatOfPoint> contours;

        if (mBeaconSeekerState == BeaconSeekerStateEnum.Off)
            return inputFrame.gray();

        mRgba = inputFrame.rgba();

        mDetectorPink.process(mRgba);
        contours = mDetectorPink.getContours();
        Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);
        Point redCenter = drawRectangleAroundContours(mRgba, contours, new Scalar(255, 0, 0));

        mDetectorBlue.process(mRgba);
        contours = mDetectorBlue.getContours();
        Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);
        Point blueCenter = drawRectangleAroundContours(mRgba, contours,  new Scalar(0, 0, 255));

        if ((blueCenter.x == 0.0 && blueCenter.y == 0.0) || (redCenter.x == 0.0 && redCenter.y == 0.0))
            mBeaconCenterPointPixels = new Point(0.0, 0.0);
        else
            mBeaconCenterPointPixels = new Point((redCenter.x + blueCenter.x)/2.0, (redCenter.y + blueCenter.y)/2.0);
        mBeaconCenterPointPercent = new Point(mBeaconCenterPointPixels.x/mRgba.width(), mBeaconCenterPointPixels.y/mRgba.height());
        Imgproc.circle(mRgba, mBeaconCenterPointPixels, 10, new Scalar(255,255,100), 10);

        // OpenCv defaults to landscape on preview. This code rotates 90 degrees to portrait
        Mat tempMat = mRgba.t();
        Core.flip(tempMat, mRgbaT, 1);
        Imgproc.resize(mRgbaT, mRgbaRotated, mRgba.size());
        tempMat.release();
        return mRgbaRotated;
    }
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "-------- in onCreate() ----------");
        super.onCreate(savedInstanceState);

    }
    protected void setCameraView() {    // call at end of onCreate
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.color_blob_detection_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }
    @Override
    protected void onResume() {
        Log.i(TAG, "-------- in onResume() ----------");
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onPause() {
        Log.i(TAG, "-------- in onPause() ----------");
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "-------- in onDestroy() ----------");
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "-------- in onStop() ----------");
        super.onStop();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    private boolean readConfig(String filePath) {
        try
        {
            if (mDetectorPink == null || mDetectorBlue == null) return false;

            Scanner in = new Scanner(new FileReader(filePath));
            String version = in.next();
            if (!version.equals(_configFileVersion)) return false;
            setPinkColor(new Scalar(Double.parseDouble(in.next()), Double.parseDouble(in.next()), Double.parseDouble(in.next())), false);
            setBlueColor(new Scalar(Double.parseDouble(in.next()), Double.parseDouble(in.next()), Double.parseDouble(in.next())), false);
            in.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void setPinkColor(Scalar color, boolean persist) {
        mBlobColorRgbaPink = color;
        Scalar mBlobColorHsv = convertScalarRgba2Hsv(mBlobColorRgbaPink);
        mDetectorPink.setHsvColor(mBlobColorHsv);
    }
    private void setBlueColor(Scalar color, boolean persist) {
        mBlobColorRgbaBlue = color;
        Scalar mBlobColorHsv = convertScalarRgba2Hsv(mBlobColorRgbaBlue);
        mDetectorBlue.setHsvColor(mBlobColorHsv);
    }
    private Scalar convertScalarRgba2Hsv(Scalar rgbColor) {
        Mat pointMatRgba = new Mat(1, 1, CvType.CV_8UC3, rgbColor);
        Mat pointMatHsv = new Mat();
        Imgproc.cvtColor(pointMatRgba, pointMatHsv, Imgproc.COLOR_RGB2HSV_FULL, 4);

        return new Scalar(pointMatHsv.get(0, 0));
    }
    private Point drawRectangleAroundContours(Mat image, List<MatOfPoint> contours, Scalar color) {
        //For each contour found
        MatOfPoint2f approxCurve = new MatOfPoint2f();

        double maxArea = 0;
        Rect maxRect = null;
        for (int i=0; i<contours.size(); i++)
        {
            //Convert contours(i) from MatOfPoint to MatOfPoint2f
            MatOfPoint2f contour2f = new MatOfPoint2f( contours.get(i).toArray() );
            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint( approxCurve.toArray() );

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(points);
            if (rect.area() > maxArea) {
                maxArea = rect.area();
                maxRect = rect;
            }

            // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), color);
            Imgproc.rectangle(image, new Point(rect.x-1, rect.y-1), new Point(rect.x+1 + rect.width, rect.y+1 + rect.height), color);
            Imgproc.rectangle(image, new Point(rect.x-2, rect.y-2), new Point(rect.x+2 + rect.width, rect.y+2 + rect.height), color);
        }
        if (maxRect == null)
            return new Point(0.0,0.0);
        return new Point(maxRect.x + maxRect.width/2.0, maxRect.y + maxRect.height/2.0);
    }

}