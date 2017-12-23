/*
 * Lockdown Framework Library
 * Copyright (c) 2017 Lockdown Team 8564 (lockdown8564.weebly.com)
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

import hallib.HalUtil;

public class Lift {

    private DcMotor lift;
    private LinearOpMode opMode;

    /**
     * Lift is a constructor
     *
     * @param opMode is the opMode in the main program
     */
    public Lift(LinearOpMode opMode){
        this.opMode = opMode;
        lift = opMode.hardwareMap.dcMotor.get("liftleft");
    }

    /**
     * up sets the lift to a certain position
     *
     * @param liftPosition is the desired position
     */
    public void up (int liftPosition){
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (liftPosition == 0) {
            lift.setTargetPosition(1);
        }
        else if (liftPosition == 1) {
            lift.setTargetPosition(2);
        }
        else if (liftPosition == 2) {
            lift.setTargetPosition(3);
        }
        else if (liftPosition == 3) {
            lift.setTargetPosition(4);
        }
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(.75);
	// note the below line will cause a hang because of synchronous execution. best to use a state machine
        while (lift.isBusy()){}
        lift.setPower(0);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }



    public void liftglyph(){
        HalUtil.sleep(400);
        lift.setPower(1);
        HalUtil.sleep(300);
        lift.setPower(0);
    }

}

