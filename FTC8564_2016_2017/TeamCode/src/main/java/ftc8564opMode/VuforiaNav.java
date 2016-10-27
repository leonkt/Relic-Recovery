/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package ftc8564opMode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;
import ftc8564lib.VuforiaLocalizerImplSubclass;
import ftc8564lib.pixelObject;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name="Vuforia Navigation", group ="Autonomous")
public class VuforiaNav extends LinearOpMode {

    @Override
    public void runOpMode() {

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AUgbT0j/////AAAAGTj0axLoDUvmo1PWo4YoBxkeKIG99PQSTrgFoQVymuq7TMc3OGQozLZo6d+dLt0EiQwR87XKW4aXt/7BZr95khAoylxKP02Vh5PmZbp18YDa5g9gWiYUGUvHqLUbNgMKotFhGgE9noRvWbiP2RxgOCy+HoT39NaFXiLiH69cLGbCzpz1tzuvRPce/EVkWBBomcS2yC/hl1hTlBjvzTHN1lKMv59s9gYhC69DNODQeNg2JMOv3ggMlRDTjOpbNZUZAQkHfqS/2w0W8d0+krzVBD129juhL2r6u4mWVhXFq2FOZoUTezbkaFTxKCRadl3v5ot5aPmuEU4mSFtmrw15J6R9XtRAg/U8/I1k7zpRpVTg";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        VuforiaLocalizerImplSubclass vuforia = new VuforiaLocalizerImplSubclass(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);
        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");

        boolean test = false;

        waitForStart();

        beacons.activate();

        while(opModeIsActive()) {

            if(test)
            {
                int color = 0;
                int red = 0;
                int green = 0;
                int blue = 0;
                int xpos = 0;
                int count = 0;

                Bitmap bm = Bitmap.createBitmap(vuforia.rgb.getWidth(), vuforia.rgb.getHeight(), Bitmap.Config.RGB_565);
                bm.copyPixelsFromBuffer(vuforia.rgb.getPixels());

                pixelObject[][] pixel = new pixelObject[vuforia.rgb.getWidth()][vuforia.rgb.getHeight()];
                pixelObject[][] colorPixel = new pixelObject[vuforia.rgb.getWidth()][vuforia.rgb.getHeight()];
                pixelObject beaconRed = new pixelObject(0,0,xpos);
                pixelObject beaconBlue = new pixelObject(1,0,xpos);
                pixelObject beaconOther = new pixelObject(-1,0,xpos);

                for(int height = 0; height < bm.getHeight(); height++)
                {
                    for(int width = 0; width < bm.getWidth(); width++)
                    {
                        color = bm.getPixel(width,height);
                        red = Color.red(color);
                        green = Color.green(color);
                        blue = Color.blue(color);
                        if (red > blue && red > green) {
                            colorPixel[width][height] = beaconRed;
                        } else if (blue > green && blue > red) {
                            colorPixel[width][height] = beaconBlue;
                        } else {
                            colorPixel[width][height] = beaconOther;
                        }
                    }
                }

                for(int height = 0; height < bm.getHeight(); height++) {
                    //int lastcol = 0; // last column with run-legth
                    xpos = 0;
                    for (int width = 0; width < bm.getWidth(); width++) {

                        /*if (width == 0)
                        {
                            pixel[lastcol][height].setCount(1);
                            pixel[lastcol][height].setColor(colorPixel[width][height].getColor());
                            pixel[lastcol][height].setXpos(width);
                        } else {
                            if (pixel[lastcol][height].getColor() == colorPixel[width][height].getColor()) {
                                // increment count
                                pixel[lastcol][height].addCount();
                            } else {
                                // increment lastcol and start new run-length
                                lastcol++;
                                pixel[lastcol][height].setCount(1);
                                pixel[lastcol][height].setColor(colorPixel[width][height].getColor());
                                pixel[lastcol][height].setXpos(width);
                            }
                        }*/

                        int previous = colorPixel[count][height].getColor();
                        if(colorPixel[width][height].getColor() != previous)
                        {
                            pixel[xpos][height] = colorPixel[count][height];
                            count = width;
                            xpos++;
                        }
                        colorPixel[count][height].addCount();
                    }
                }

                for(int height = 0; height < bm.getHeight(); height++) {
                    for (int width = 0; width < bm.getWidth(); width++) {
                        if(pixel[width][height] != null)
                        {
                            telemetry.addData("",pixel[width][height].toString() + " ");
                        } else {
                            telemetry.addData("","-1 ");
                        }
                        telemetry.addLine();
                        telemetry.update();
                    }
                }
            }

            for(VuforiaTrackable beacon : beacons) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beacon.getListener()).getPose();
                if(pose != null) {
                    VectorF translation = pose.getTranslation();
                    telemetry.addData(beacon.getName()+"-Translation", translation);
                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(0), translation.get(1)));
                    telemetry.addData(beacon.getName()+"-Degrees", degreesToTurn);
                    telemetry.addData("Pos", format(pose));

                }
            }
            telemetry.update();
        }

    }

    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
}
