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

public class Hanger {

    LinearOpMode opMode;
    private DriveBase driveBase;
    private DcMotor W;
    private DcMotor TM;
    private DcMotor HookArm;
    private Servo allClear;
    private Servo ziplineServo;
    private int TARGET;
    private int REV;
    private enum State {
        STATE_ONE, STATE_TWO, STATE_THREE, STATE_FOUR, STATE_FIVE, STATE_SIX
    };
    State mHookArmState;
    private ElapsedTime mStateTime = new ElapsedTime();
    private ElapsedTime mClock = new ElapsedTime();

    public Hanger(LinearOpMode opMode, DriveBase driveBase) throws InterruptedException {
        this.opMode = opMode;
        this.driveBase = driveBase;
        W = opMode.hardwareMap.dcMotor.get("W");
        TM = opMode.hardwareMap.dcMotor.get("TM");
        allClear = opMode.hardwareMap.servo.get("allClear");
        HookArm = opMode.hardwareMap.dcMotor.get("HookArm");
        ziplineServo = opMode.hardwareMap.servo.get("C");
        HookArm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        mHookArmState = State.STATE_ONE;
        allClear.setPosition(0.5);
    }

    public void upAllClear() throws InterruptedException {
        allClear.setPosition(0);
    }

    public void downAllClear() throws InterruptedException {
        allClear.setPosition(1);
    }

    public void noAllClear() throws InterruptedException {
        allClear.setPosition(0.5);
    }

    public void runUP() throws InterruptedException {
        if(REV > 5) REV = 5;
        switch(mHookArmState)
        {
            case STATE_ONE:
                TM.setPower(0.6);
                W.setPower(getPWRF(REV));
                waitTime(0.6);
                newHookState(State.STATE_TWO);
                break;
            case STATE_TWO:
                TM.setPower(0.5);
                W.setPower(getPWRF(REV));
                waitTime(0.6);
                newHookState(State.STATE_THREE);
                break;
            case STATE_THREE:
                TM.setPower(0.45);
                W.setPower(getPWRF(REV));
                waitTime(0.6);
                newHookState(State.STATE_FOUR);
                break;
            case STATE_FOUR:
                TM.setPower(0.40);
                W.setPower(getPWRF(REV));
                waitTime(0.6);
                newHookState(State.STATE_FIVE);
                break;
            case STATE_FIVE:
                TM.setPower(0.35);
                W.setPower(getPWRF(REV));
                waitTime(0.6);
                newHookState(State.STATE_SIX);
                break;
            case STATE_SIX:
                TM.setPower(0.25);
                W.setPower(getPWRF(REV));
                waitTime(0.75);
                break;
        }
        REV++;
    }

    public void runDOWN() throws InterruptedException {
        if(REV < 0) REV = 0;
        switch(mHookArmState)
        {
            case STATE_ONE:
                TM.setPower(-1);
                W.setPower(-getPWRR(REV));
                driveBase.mountainCode();
                waitTime(0.55);
                break;
            case STATE_TWO:
                TM.setPower(-1);
                W.setPower(-getPWRR(REV));
                driveBase.mountainCode();
                waitTime(0.55);
                newHookState(State.STATE_TWO);
                break;
            case STATE_THREE:
                TM.setPower(-1);
                W.setPower(-getPWRR(REV));
                driveBase.mountainCode();
                waitTime(0.55);
                newHookState(State.STATE_FOUR);
                break;
            case STATE_FOUR:
                TM.setPower(-1);
                W.setPower(-getPWRR(REV));
                driveBase.mountainCode();
                waitTime(0.55);
                newHookState(State.STATE_THREE);
                break;
            case STATE_FIVE:
                TM.setPower(-1);
                W.setPower(-getPWRR(REV));
                driveBase.mountainCode();
                waitTime(0.55);
                newHookState(State.STATE_FOUR);
                break;
            case STATE_SIX:
                TM.setPower(-getPWRR(REV));
                W.setPower(-1);
                waitTime(0.75);
                newHookState(State.STATE_FIVE);
                break;
        }
        REV--;
    }

    public void hookArm50() throws InterruptedException {
        HookArm.setTargetPosition(47);
        HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        HookArm.setPower(-0.3);
        setTARGET(47);
    }

    public void hookArm150() throws InterruptedException {
        HookArm.setTargetPosition(170);
        HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        HookArm.setPower(-0.3);
        setTARGET(170);
    }

    public void hookArm135() throws InterruptedException {
        HookArm.setTargetPosition(135);
        HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        HookArm.setPower(-0.3);
        setTARGET(135);
    }

    public void hookArm0() throws InterruptedException {
        HookArm.setTargetPosition(0);
        HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        HookArm.setPower(-0.3);
        setTARGET(0);
    }

    public void steadyHookArm() throws InterruptedException {
        HookArm.setTargetPosition(TARGET);
        HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        HookArm.setPower(-0.5);
    }

    public void stop() throws InterruptedException {
        TM.setPower(0.0);
        W.setPower(0.0);
    }

    public void lowerZipline() throws InterruptedException {
        ziplineServo.setPosition(0);
    }

    public void raiseZipline() throws InterruptedException {
        ziplineServo.setPosition(1);
    }

    public void zeroZipline() throws InterruptedException {
        ziplineServo.setPosition(0.5);
    }

    int setTARGET(int i) {
        TARGET = i;
        return TARGET;
    }

    double getPWRF(int ROTATION_NUMBER) {
        if (ROTATION_NUMBER > 6) {
            ROTATION_NUMBER = 6;
        }
        if (ROTATION_NUMBER < 0) {
            ROTATION_NUMBER = 0;
        }
        double[] array = {0.90, 0.95, 0.95, 0.95, 0.95, 1, 1};
        // 13-14 in, 23.5 in, 34 in, 43 in, 52 in , 60 in
        return array[ROTATION_NUMBER];
    }

    double getPWRR(int ROTATION_NUMBER) {
        if (ROTATION_NUMBER > 6) {
            ROTATION_NUMBER = 6;
        }
        if (ROTATION_NUMBER < 0) {
            ROTATION_NUMBER = 0;
        }
        double[] array = {0.70, 0.70, 0.70, 0.70, 0.70, 0.70, 0.50};
        return array[ROTATION_NUMBER];
    }

    void newHookState(State newState) {
        mStateTime.reset();
        mHookArmState = newState;
    }

    public void waitTime(double time) throws InterruptedException {
        mClock.reset();
        mClock.startTime();
        while (mClock.time() <= time) {
            opMode.waitOneFullHardwareCycle();
        }
    }

}
