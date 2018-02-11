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

package ftc8564TestOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import ftc8564lib.DriveBase;
import ftc8564lib.Robot;
import hallib.HalUtil;

@Autonomous(name="PIDTuning", group="Autonomous")
//@Disabled
public class PIDTuning extends LinearOpMode {

    CRServo thrust;
    Servo kicker;
    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        ElapsedTime runtime = new ElapsedTime();
        thrust = hardwareMap.crservo.get("thrust");
        kicker = hardwareMap.servo.get("kicker");
        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        waitForStart();
        thrust.setPower(1);
        HalUtil.sleep(1500);
        kicker.setPosition(.25);
        HalUtil.sleep(1500);
        colorSensor.enableLed(true);
        if(colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green()){
            kicker.setPosition(0);
            HalUtil.sleep(500);
        }
        else if (colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green()){
            kicker.setPosition(.5);
            HalUtil.sleep(500);
        }
        else {
            kicker.setPosition(.25);
        }
        colorSensor.enableLed(false);
        thrust.setPower(1);
        HalUtil.sleep(1500);
        kicker.setPosition(.8);
        HalUtil.sleep(1500);



    }


}
