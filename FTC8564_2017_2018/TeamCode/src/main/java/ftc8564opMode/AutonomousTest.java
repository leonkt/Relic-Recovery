/*
 * Lockdown Framework Library
 * Copyright (c) 2015 Lockdown Team 8564 (lockdown8564.weebly.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ftc8564opMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import ftc8564lib.DriveBase;
import ftc8564lib.Robot;
import ftclib.FtcChoiceMenu;
import ftclib.FtcMenu;

@Autonomous(name="LockdownAutonomous", group="Autonomous")
@Disabled
public class AutonomousTest extends LinearOpMode implements FtcMenu.MenuButtons, DriveBase.AbortTrigger {

    Robot robot;
    ColorSensor colorSensor;
    private ElapsedTime mClock = new ElapsedTime();

    public enum Alliance {
        BLUE_ALLIANCE,
        RED_ALLIANCE
    }

    public enum Alliance_Position {
        BLUE_RIGHT,
        BLUE_LEFT,
        RED_RIGHT,
        RED_LEFT
    }

    public enum position {
        RIGHT,
        LEFT,
        CENTER

    }

    public static final String TAG = "Vuforia VuMark Sample";

    OpenGLMatrix lastLocation = null;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    VuforiaLocalizer vuforia;


    private Alliance_Position alliance = Alliance_Position.BLUE_RIGHT;

    private position glyphPosition = position.CENTER;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this,true);// need this to run menu test
        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        doMenus();
        waitForStart();

        // robot.driveBase.resetHeading();

        if (alliance == Alliance_Position.BLUE_RIGHT) {
            //servo.setPostion(?)
            if (colorSensor.red() < colorSensor.blue()) {
                //crserveo.setPosition(0)
            }
            else {
                //crservo.setPosition(1)
            }
            //servo.setPosition(?)




            ;
        } else if (alliance == Alliance_Position.BLUE_LEFT) {
            ;
        } else if (alliance == Alliance_Position.RED_RIGHT) {
            ;
        } else if (alliance == Alliance_Position.RED_LEFT) {
            ;
        }
        runCleanUp();
        telemetry.clearAll();
        telemetry.update();
        sleep(7000); // sleep 7s

    }

    private void readPictographs() throws InterruptedException {

        //public static final String TAG = "Vuforia VuMark Sample";

        //OpenGLMatrix lastLocation = null;

        /**
         * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
         * localization engine.
         */
        //VuforiaLocalizer vuforia;

        //@Override public void runOpMode() {

        /*
         * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

            // OR...  Do Not Activate the Camera Monitor View, to save power
            // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        /*
         * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the Vuforia web site
         * and paste it in to your code onthe next line, between the double quotes.
         */
            parameters.vuforiaLicenseKey = "AUgbT0j/////AAAAGTj0axLoDUvmo1PWo4YoBxkeKIG99PQSTrgFoQVymuq7TMc3OGQozLZo6d+dLt0EiQwR87XKW4aXt/7BZr95khAoylxKP02Vh5PmZbp18YDa5g9gWiYUGUvHqLUbNgMKotFhGgE9noRvWbiP2RxgOCy+HoT39NaFXiLiH69cLGbCzpz1tzuvRPce/EVkWBBomcS2yC/hl1hTlBjvzTHN1lKMv59s9gYhC69DNODQeNg2JMOv3ggMlRDTjOpbNZUZAQkHfqS/2w0W8d0+krzVBD129juhL2r6u4mWVhXFq2FOZoUTezbkaFTxKCRadl3v5ot5aPmuEU4mSFtmrw15J6R9XtRAg/U8/I1k7zpRpVTg";

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

            /**
             * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
             * in this data set: all three of the VuMarks in the game were created from this one template,
             * but differ in their instance id information.
             * @see VuMarkInstanceId
             */
            VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
            VuforiaTrackable relicTemplate = relicTrackables.get(0);
            relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

            telemetry.addData(">", "Press Play to start");
            telemetry.update();
            waitForStart();

            relicTrackables.activate();

            while (opModeIsActive()) {

                /**
                 * See if any of the instances of {@link relicTemplate} are currently visible.
                 * {@link RelicRecoveryVuMark} is an enum which can have the following values:
                 * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
                 * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
                 */
                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                    telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
                    OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                    telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                    if (pose != null) {
                        VectorF trans = pose.getTranslation();
                        Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                        // Extract the X, Y, and Z components of the offset of the target relative to the robot
                        double tX = trans.get(0);
                        double tY = trans.get(1);
                        double tZ = trans.get(2);

                        // Extract the rotational components of the target relative to the robot
                        double rX = rot.firstAngle;
                        double rY = rot.secondAngle;
                        double rZ = rot.thirdAngle;
                    }
                }
                else {
                    telemetry.addData("VuMark", "not visible");
                }

                telemetry.update();
            }
        }

        String format(OpenGLMatrix transformationMatrix) {
            return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
        }


    private void runCleanUp() throws InterruptedException {
        //robot.shooter.resetMotors();
        //robot.beaconPush.resetRacks();
        //robot.pulleySystem.resetMotors();
        //robot.driveBase.resetMotors();
        //robot.driveBase.resetPIDDrive();
    }

    @Override
    public boolean shouldAbort() { return true; }

    @Override
    public boolean isMenuUpButton() {
        return gamepad1.dpad_up;
    }

    @Override
    public boolean isMenuDownButton() {
        return gamepad1.dpad_down;
    }

    @Override
    public boolean isMenuEnterButton() {
        return gamepad1.dpad_right;
    }

    @Override
    public boolean isMenuBackButton() {
        return gamepad1.dpad_left;
    }

    private void doMenus() throws InterruptedException {
        FtcChoiceMenu alliancePositionMenu = new FtcChoiceMenu("Alliance Position:", null, this);

        alliancePositionMenu.addChoice("BLUE RIGHT", Alliance_Position.BLUE_RIGHT);
        alliancePositionMenu.addChoice("BLUE LEFT", Alliance_Position.BLUE_LEFT);
        alliancePositionMenu.addChoice("RED RIGHT", Alliance_Position.RED_RIGHT);
        alliancePositionMenu.addChoice("RED LEFT", Alliance_Position.RED_LEFT);

        FtcMenu.walkMenuTree(alliancePositionMenu);
        alliance = (Alliance_Position) alliancePositionMenu.getCurrentChoiceObject();
    }

}

