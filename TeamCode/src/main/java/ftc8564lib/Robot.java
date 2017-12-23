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

package ftc8564lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import hallib.HalDashboard;

public class Robot {

    private static HalDashboard dashboard = null;
    public DriveBase driveBase = null;
    public JewelArm jewelArm = null;
    public VuMark VuMark = null;
    public Clamps clamps = null;
    public Lift lift = null;
    public Intake intake = null;
    public RelicArm relicArm = null;

    public Robot(LinearOpMode opMode, boolean auto) throws InterruptedException {
        dashboard = new HalDashboard(opMode.telemetry);
        jewelArm = new JewelArm(opMode);
        driveBase = new DriveBase(opMode);
        clamps = new Clamps(opMode);
        VuMark = new VuMark(opMode);
        lift = new Lift(opMode);
        intake = new Intake(opMode);
        relicArm = new RelicArm(opMode);
    }

    public static HalDashboard getDashboard() {
        return dashboard;
    }

}
