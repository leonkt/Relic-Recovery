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
import com.qualcomm.robotcore.hardware.DcMotor;

public class PulleySystem {

    DcMotor leftPulley;
    DcMotor rightPulley;
    DcMotor leftArm;
    DcMotor rightArm;

    final static int ENCODER_CPR = 1680;     //Encoder Counts per Revolution on NeveRest 60 Motors
    final static int MAX_HEIGHT = 25;        //Max height our pulley system can go
    final static int DISTANCE = MAX_HEIGHT*ENCODER_CPR;

    public PulleySystem(LinearOpMode opMode) {
        leftPulley = opMode.hardwareMap.dcMotor.get("leftPulley");
        rightPulley = opMode.hardwareMap.dcMotor.get("rightPulley");
        //leftPulley.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //rightPulley.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //leftPulley.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //rightPulley.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //leftPulley.setPower(0.8);
        //rightPulley.setPower(0.6);
    }

    public void changePower(double power)
    {
        leftPulley.setPower(power);
        rightPulley.setPower(0.775*power);
    }

    public void setPower(boolean up)
    {
        if(up)
        {
            leftPulley.setTargetPosition(checkPos(1));
            rightPulley.setTargetPosition(leftPulley.getCurrentPosition());
        } else {
            leftPulley.setTargetPosition(checkPos(0));
            rightPulley.setTargetPosition(leftPulley.getCurrentPosition());
        }
    }

    public void setPower()
    {
        leftPulley.setTargetPosition(leftPulley.getCurrentPosition());
        rightPulley.setTargetPosition(leftPulley.getCurrentPosition());
    }

    private int checkPos(int i)
    {
        if(i == 0)
        {
            if(leftPulley.getCurrentPosition() - 200 < 0)
            {
                return 0;
            }
            return leftPulley.getCurrentPosition() - 200;
        } else if(i == 1){
            if(leftPulley.getCurrentPosition() + 200 > DISTANCE)
            {
                return DISTANCE;
            }
            return leftPulley.getCurrentPosition() + 200;
        }
        return leftPulley.getCurrentPosition();
    }

    public void resetMotors()
    {
        leftPulley.setPower(0);
        rightPulley.setPower(0);
    }
}
