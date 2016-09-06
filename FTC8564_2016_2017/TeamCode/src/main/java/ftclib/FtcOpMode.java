/*
 * Copyright (c) 2015 Titan Robotics Club (http://www.titanrobotics.com)
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

package ftclib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import hallib.HalDashboard;
import hallib.HalUtil;

/**
 * This class implements a cooperative multi-tasking scheduler
 * extending LinearOpMode.
 */
public abstract class FtcOpMode extends LinearOpMode
{
    private static final String moduleName = "FtcOpMode";
    private static final boolean debugEnabled = false;
    private static HalDashboard dashboard = null;
    private static String opModeName = null;

    /**
     * This method is called to initialize the robot. In FTC, this is called when the
     * "Init" button on the Driver Station phone is pressed.
     */
    public abstract void initRobot();

    private final static String OPMODE_AUTO     = "FtcAuto";
    private final static String OPMODE_TELEOP   = "FtcTeleOp";
    private final static String OPMODE_TEST     = "FtcTest";

    private final static long LOOP_PERIOD = 20;
    private static FtcOpMode instance = null;
    private static double startTime = 0.0;
    private static double elapsedTime = 0.0;

    /**
     * Constructor: Creates an instance of the object. It calls the constructor
     * of the LinearOpMode class and saves an instance of this class.
     */
    public FtcOpMode()
    {
        super();
        instance = this;
        //
        // Create task manager. There is only one global instance of task manager.
        //
    }   //FtcOpMode

    /**
     * This method returns the saved instance. This is a static method. So other
     * class can get to this class instance by calling getInstance(). This is very
     * useful for other classes that need to access the public fields such as
     * hardwareMap, gamepad1 and gamepad2.
     *
     * @return save instance of this class.
     */
    public static FtcOpMode getInstance()
    {
        return instance;
    }   //getInstance


    /**
     * This method returns a global dashboard object for accessing the dashboard on the Driver Station.
     *
     * @return dashboard object.
     */
    public static HalDashboard getDashboard()
    {
        return dashboard;
    }   //getDashboard

    /**
     * This method returns the name of the active OpMode.
     *
     * @return active OpMode name.
     */
    public static String getOpModeName()
    {
        return opModeName;
    }   //getOpModeName

    /**
     * This method returns the elapsed time since competition starts.
     * This is the elapsed time after robotInit() is called and after
     * waitForStart() has returned (i.e. The "Play" button is pressed
     * on the Driver Station.
     *
     * @return OpMode start elapsed time in seconds.
     */
    public static double getElapsedTime()
    {
        elapsedTime = HalUtil.getCurrentTime() - startTime;
        return elapsedTime;
    }   //getElapsedTime

}   //class FtcOpMode