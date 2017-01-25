/*
 * Copyright (c) 2016 Titan Robotics Club (http://www.titanrobotics.com)
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

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import hallib.HalUtil;

public class FtcAnalogGyro
{

    private double voltPerDegPerSec;
    private AnalogInput gyro;
    private int numAxes = 1;
    private int numCalSamples = 100;
    private int calInterval = 10; //in msec
    private double zeroOffset;
    private double deadband;
    private double heading;

    /**
     * Constructor: Creates an instance of the object.
     *
     * @param hardwareMap specifies the global hardware map.
     * @param instanceName specifies the instance name.
     * @param voltPerDegPerSec specifies the rotation rate scale.
     */
    public FtcAnalogGyro(HardwareMap hardwareMap, String instanceName, double voltPerDegPerSec)
    {
        this.voltPerDegPerSec = voltPerDegPerSec;
        gyro = hardwareMap.analogInput.get(instanceName);
    }

    public void calibrate()
    {
        double minValues;
        double maxValues;
        double sums = 0.0;

        minValues = maxValues = gyro.getVoltage();

        for (int n = 0; n < numCalSamples; n++)
        {
            for (int i = 0; i < numAxes; i++)
            {
                double value = gyro.getVoltage();
                sums += value;

                if (value < minValues)
                {
                    minValues = value;
                }
                else if (value > maxValues)
                {
                    maxValues = value;
                }
            }
            HalUtil.sleep(calInterval);
        }
        zeroOffset = sums/numCalSamples;
        deadband = maxValues - minValues;
    }

    private double applyDeadband(double value)
    {
        return Math.abs(value) >= deadband ? value: 0.0;
    }

    public void resetZAxisIntegrator()
    {
        heading = 0;
    }

    public double getRawZData()
    {
        //
        // Analog gyro supports only rotation rate.
        //
        return gyro.getVoltage()/voltPerDegPerSec;
    }

    public void integrationTask()
    {
        double prevTime = HalUtil.getCurrentTime();
        double value = applyDeadband(getRawZData() - zeroOffset);
        heading += value * (HalUtil.getCurrentTime() - prevTime);
    }

    public double getIntegratedZValue()
    {
        return heading;
    }

}