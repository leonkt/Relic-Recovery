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
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class BeaconPush {

    ColorSensor colorSensor;
    CRServo rack;

    static final double BUTTON_PUSHER_RETRACT_POSITION = 1;
    static final double BUTTON_PUSHER_EXTEND_POSITION = -1;
    static final double BUTTON_PUSHER_REST_POSITION = 0;

    private ElapsedTime mClock = new ElapsedTime();

    private enum Color {
        RED,
        BLUE,
        OTHER
    }

    private enum STATE {
        EXTEND,
        RETRACT
    }

    public BeaconPush(LinearOpMode opMode)
    {
        colorSensor = opMode.hardwareMap.colorSensor.get("colorSensor");
        colorSensor.enableLed(false);
        rack = opMode.hardwareMap.crservo.get("rack");
    }

    public void enableLED(boolean on)
    {
        colorSensor.enableLed(on);
    }

    public int redColor()
    {
        return colorSensor.red();
    }

    public int blueColor()
    {
        return colorSensor.blue();
    }

    public int greenColor()
    {
        return colorSensor.green();
    }

    private Color getColor()
    {
       if(colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green())
       {
           return Color.RED;
       } else if(colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green())
       {
           return Color.BLUE;
       } else {
           return Color.OTHER;
       }
    }

    private void waitTime(int i)
    {
        mClock.reset();
        mClock.startTime();
        while(mClock.seconds() < i) {}
    }

}
