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

package ftc8564;

/**
 * Created by Owner on 2/4/2016.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ClimberDeployer {

    LinearOpMode opMode;
    private DcMotor dP;
    private Servo T;
    private Servo flipper;
    private Servo boxLeftRight;
    private ElapsedTime mClock = new ElapsedTime();
    private int TARGET_1;

    public ClimberDeployer(LinearOpMode opMode) throws InterruptedException {

        this.opMode = opMode;
        dP = opMode.hardwareMap.dcMotor.get("Dustpan");
        T = opMode.hardwareMap.servo.get("T");
        flipper = opMode.hardwareMap.servo.get("flipper");
        boxLeftRight = opMode.hardwareMap.servo.get("boxLeftRight");
        dP.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        dP.setDirection(DcMotor.Direction.REVERSE);
        dP.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        boxLeftRight.setPosition(0.47);
        T.setPosition(0);
        flipper.setPosition(0);

    }

    public void setFlipper() throws InterruptedException {
        flipper.setPosition(0);
    }

    public void halfFlipper() throws InterruptedException {
        flipper.setPosition(0.75);
    }

    public void stopFlipper() throws InterruptedException {
        flipper.setPosition(0.82);
    }

    public void sweepFlipper() throws InterruptedException {
        stopFlipper();
        waitTime(0.3);
        setFlipper();
    }

    public void tZero() throws InterruptedException {
        T.setPosition(0);
        opMode.waitOneFullHardwareCycle();
    }

    public void tPrep() throws InterruptedException {
        T.setPosition(0.25);
        opMode.waitOneFullHardwareCycle();
    }

    public void tDrop() throws InterruptedException {
        T.setPosition(0.55);
        opMode.waitOneFullHardwareCycle();
    }

    public void tHighUp() throws InterruptedException {
        T.setPosition(0.75);
        opMode.waitOneFullHardwareCycle();
    }

    public void tiltBoxLeft() throws InterruptedException {
        boxLeftRight.setPosition(0.25);
        opMode.waitOneFullHardwareCycle();
    }

    public void tiltBoxRight() throws InterruptedException {
        boxLeftRight.setPosition(0.75);
        opMode.waitOneFullHardwareCycle();
    }

    public void centerBox() throws InterruptedException {
        boxLeftRight.setPosition(0.47);
        opMode.waitOneFullHardwareCycle();
    }

    public void autonomousTiltRight() throws InterruptedException {
        boxLeftRight.setPosition(0.67);
        opMode.waitOneFullHardwareCycle();
    }

    public void autonomousTiltLeft() throws InterruptedException {
        boxLeftRight.setPosition(0.25);
        opMode.waitOneFullHardwareCycle();
    }

    public void raisedP() throws InterruptedException {
        dP.setTargetPosition(150);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(0.09);
        waitTime(0.165);
        dP.setTargetPosition(250);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(0.07);
        waitTime(0.15);
    }

    public void lowerdP() throws InterruptedException {
        dP.setTargetPosition(150);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(-0.08);
        opMode.waitOneFullHardwareCycle();
    }

    public void zerodP() throws InterruptedException {
        dP.setTargetPosition(0);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(-0.1);
        T.setPosition(0);
        opMode.waitOneFullHardwareCycle();
    }

    public void waitTime(double time) throws InterruptedException {
        mClock.reset();
        mClock.startTime();
        while (mClock.time() <= time) {
            opMode.waitOneFullHardwareCycle();
        }
    }

    public void grabClimbers() throws InterruptedException {
        dP.setTargetPosition(40);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(0.09);
        T.setPosition(0.23);
        waitTime(0.1);
    }

    public void dP0() throws InterruptedException {
        dP.setTargetPosition(0);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(0.05);
        setTARGET_1(0);
    }

    public void dP250() throws InterruptedException {
        dP.setTargetPosition(250);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(0.05);
        setTARGET_1(250);
    }

    public void dP650() throws InterruptedException {
        dP.setTargetPosition(650);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(0.05);
    }

    public void dP700() throws InterruptedException {
        dP.setTargetPosition(700);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(0.05);
        setTARGET_1(700);
    }

    public void dP950() throws InterruptedException {
        dP.setTargetPosition(950);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(0.05);
        setTARGET_1(950);
    }

    public void steadydP() throws InterruptedException {
        dP.setTargetPosition(TARGET_1);
        dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        dP.setPower(0.06);
    }

    int setTARGET_1(int i) {
        TARGET_1 = i;
        return TARGET_1;
    }

}
