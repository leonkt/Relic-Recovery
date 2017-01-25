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
    private double[] zeroOffsets;
    private double[] deadbands;

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
        zeroOffsets = new double[1];
        deadbands = new double[1];
    }

    public void isCalibrating()
    {

    }

    public void calibrate()
    {
        double[] minValues = new double[numAxes];
        double[] maxValues = new double[numAxes];
        double[] sums = new double[numAxes];

        for (int i = 0; i < numAxes; i++)
        {
            minValues[i] = maxValues[i] = gyro.getVoltage();
            sums[i] = 0.0;
        }

        for (int n = 0; n < numCalSamples; n++)
        {
            for (int i = 0; i < numAxes; i++)
            {
                double value = gyro.getVoltage();
                sums[i] += value;

                if (value < minValues[i])
                {
                    minValues[i] = value;
                }
                else if (value > maxValues[i])
                {
                    maxValues[i] = value;
                }
            }
            HalUtil.sleep(calInterval);
        }

        for (int i = 0; i < numAxes; i++)
        {
            zeroOffsets[i] = sums[i]/numCalSamples;
            deadbands[i] = maxValues[i] - minValues[i];
        }
    }

    public void resetZAxisIntegrator()
    {

    }

    private double getCalibratedData(int index, double data)
    {
        return applyDeadband(data - zeroOffsets[index], deadbands[index]);
    }

    private double applyDeadband(double value, double deadband)
    {
        return Math.abs(value) >= deadband ? value: 0.0;
    }

    public double getRawZData()
    {
        //
        // Analog gyro supports only rotation rate.
        //
        return gyro.getVoltage()/voltPerDegPerSec;
    }

    public double getIntegratedZValue()
    {
        return 0;
    }

    public double getZRotationRate()
    {
        return 0;
    }

}