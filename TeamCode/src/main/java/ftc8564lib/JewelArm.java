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
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import hallib.HalUtil;
import ftc8564opMode.LockdownAutonomous;

public class JewelArm {

    private ColorSensor colorSensor;
    private Servo colorServo;
    private CRServo crServo;
    private LinearOpMode opMode;
    private ElapsedTime mClock = new ElapsedTime();

    private double servoForward = -.1;
    private double servoBackward = .7;
    private double servoRest = 0;
    private boolean blueAlliance = true;

    public JewelArm (LinearOpMode opMode)
    {
        this.opMode = opMode;
        colorSensor = opMode.hardwareMap.colorSensor.get("colorSensor");
        colorServo = opMode.hardwareMap.servo.get("colorServo");
        crServo = opMode.hardwareMap.crservo.get("crServo");
        mClock.reset();
    }

    public void pushJewels (boolean blueAlliance){
        colorSensor.enableLed(true);
        if(blueAlliance){
            if(colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green()){
                colorServo.setPosition(0);
                HalUtil.sleep(500);
            }
            else if (colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green()){
                colorServo.setPosition(1);
                HalUtil.sleep(500);
            }
            else {
                colorServo.setPosition(.4);
            }
        } else {
            if (colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green()) {
                colorServo.setPosition(1);
                HalUtil.sleep(500);
            } else if (colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green()) {
                colorServo.setPosition(0);
                HalUtil.sleep(500);
            } else {
                colorServo.setPosition(.4);
            }
        }
        colorSensor.enableLed(false);
    }

    public void armDown() {
        crServo.setPower(servoForward);
        HalUtil.sleep(1000);
        crServo.setPower(servoRest);
        HalUtil.sleep(500);
    }

    public void armUp(){
        crServo.setPower(servoBackward);
        HalUtil.sleep(1000);
        colorServo.setPosition(0.45);
        crServo.setPower(servoBackward);
        HalUtil.sleep(3000);
        crServo.setPower(servoRest);
    }

    public void resetServo()
    {
        colorServo.setPosition(0.45);
        colorSensor.enableLed(false);
        crServo.setPower(.1);
    }

}