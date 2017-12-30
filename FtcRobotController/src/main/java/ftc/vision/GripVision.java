package ftc.vision;

import android.view.SurfaceView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class GripVision implements CameraBridgeViewBase.CvCameraViewListener2
{

    private static final int NUM_IMAGE_BUFFERS = 2;

    private static final double[] RED_TARGET_HUE = {0.0, 100.0};
    private static final double[] BLUE_TARGET_HUE = {40.0, 160.0};

    private Rect[] targetRects = new Rect[2];
    private GripPipeline gripRedTarget = null;
    private GripPipeline gripBlueTarget = null;
    private boolean videoOutEnabled = false;
    private Mat image;

    public GripVision(CameraBridgeViewBase c)
    {

        c.setVisibility(SurfaceView.VISIBLE);
        c.setCvCameraViewListener(this);

        gripRedTarget = new GripPipeline("RedTarget", RED_TARGET_HUE);
        gripBlueTarget = new GripPipeline("BlueTarget", BLUE_TARGET_HUE);
    }   //GripVision

    /**
     * This method enables/disables the video out stream.
     *
     * @param enabled specifies true to enable video out stream, false to disable.
     */
    public void setVideoOutEnabled(boolean enabled)
    {
        final String funcName = "setVideoOutEnabled";

        videoOutEnabled = enabled;
    }   //setVideoOutEnabled

    public void retrieveTargetRects(Rect[] rects)
    {
        synchronized (targetRects)
        {
            rects[0] = targetRects[0];
            targetRects[0] = null;
            rects[1] = targetRects[1];
            targetRects[1] = null;
        }
    }   //retrieveTargetRects

    private Rect getTargetRect(GripPipeline pipeline, Mat image)
    {
        Rect targetRect = null;
        MatOfKeyPoint detectedTargets;

        pipeline.process(image);
        detectedTargets = pipeline.findBlobsOutput();
        if (detectedTargets != null)
        {
            KeyPoint[] targets = detectedTargets.toArray();
            if (targets.length > 1)
            {
                //HalDashboard.getInstance().displayPrintf(15, "%s: %s", pipeline, targets[0]);
                double radius = targets[0].size/2;
                targetRect = new Rect(
                        (int)(targets[0].pt.x - radius), (int)(targets[0].pt.y - radius),
                        (int)targets[0].size, (int)targets[0].size);
            }
            detectedTargets.release();
        }

        return targetRect;
    }   //getTargetRect

    /**
     * This method is called to detect objects in the acquired image frame.
     *
     * @param image specifies the image to be processed.
     * @param buffers specifies the preallocated buffer to hold the detected targets (not used since no
     *        preallocated buffer required).
     * @return detected objects, null if none detected.
     */
    public Rect[] detectObjects(Mat image, Rect[] buffers)
    {
        //
        // Process the image to detect the targets we are looking for and put them into targetRects.
        //
        synchronized (targetRects)
        {
            targetRects[0] = getTargetRect(gripRedTarget, image);
            targetRects[1] = getTargetRect(gripBlueTarget, image);

            if (videoOutEnabled)
            {
                drawRectangles(image, targetRects, new Scalar(0, 255, 0), 0);
            }
        }

        return targetRects;
    }   //detectObjects

    public void drawRectangles(Mat image, Rect[] detectedObjectRects, Scalar color, int thickness)
    {
        //
        // Overlay a rectangle on each detected object.
        //
        synchronized (image)
        {
            if (detectedObjectRects != null)
            {
                for (Rect r: detectedObjectRects)
                {

                    if(r == null) continue;

                    //
                    // Draw a rectangle around the detected object.
                    //
                    Imgproc.rectangle(
                            image, new Point(r.x, r.y), new Point(r.x + r.width, r.y + r.height), color, thickness);
                }
            }

            // Save image
            this.image = image;
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        detectObjects(inputFrame.rgba(), null);
        if(image != null) return image;
        return inputFrame.rgba();
    }
}   //class GripVision