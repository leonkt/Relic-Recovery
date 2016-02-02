package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Owner on 12/27/2015.
 */
public class LockdownTeleOp extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor TM;
    DcMotor W;
    DcMotor HookArm;
    DcMotor dP;
    Servo T;
    Servo C;
    DcMotorController mc1;
    DcMotorController mc2;
    DcMotorController mc3;
    ServoController sc1;

    private enum State {
        STATE_ONE, STATE_TWO, STATE_THREE, STATE_FOUR, STATE_FIVE, STATE_SIX
    };

    private State mHookArmState;
    public ElapsedTime mRuntime = new ElapsedTime();   // Time into round.
    private ElapsedTime mStateTime = new ElapsedTime();  // Time into current state
    private int TARGET;
    private int TARGET_1;

    @Override
    public void runOpMode() throws InterruptedException {

        mc1 = hardwareMap.dcMotorController.get("mc1Dc");
        mc2 = hardwareMap.dcMotorController.get("mc2Dc");
        mc3 = hardwareMap.dcMotorController.get("mc3Dc");
        sc1 = hardwareMap.servoController.get("sc1Sc");
        TM = hardwareMap.dcMotor.get("TM");
        W = hardwareMap.dcMotor.get("W");
        HookArm = hardwareMap.dcMotor.get("HookArm");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        dP = hardwareMap.dcMotor.get("Dustpan");
        T = hardwareMap.servo.get("T");
        C = hardwareMap.servo.get("C");
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        dP.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        dP.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        int REV = 0;
        newHookState(State.STATE_ONE);

        waitForStart();

        while (opModeIsActive()) {

            mRuntime.reset();

            float left = -gamepad1.left_stick_y;
            float right = -gamepad1.right_stick_y;

            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            right = (float) scaleInput(right);
            left = (float) scaleInput(left);

            if (left < 0) {
                motorLeft.setPower(left);
            } else if (left > 0) {
                motorLeft.setPower(left);
            } else {
                motorLeft.setPower(0);
            }
            if (right < 0) {
                motorRight.setPower(right);
            } else if (right > 0) {
                motorRight.setPower(right);
            } else {
                motorRight.setPower(0);
            }
            if (gamepad2.left_bumper) {                          // 45 - 50: Top Most Bar in TeleOp but not End Game; 150: Hanging Bar
                HookArm.setTargetPosition(80);
                HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                HookArm.setPower(-0.3);
                setTARGET(47);
            } else if (gamepad2.right_bumper) {
                HookArm.setTargetPosition(170);
                HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                HookArm.setPower(-0.3);
                setTARGET(170);
            } else if (gamepad2.left_trigger != 0) {
                HookArm.setTargetPosition(135);
                HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                HookArm.setPower(-0.3);
                setTARGET(135);
            } else if (gamepad2.right_trigger != 0) {
                HookArm.setTargetPosition(0);
                HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                HookArm.setPower(-0.3);
                setTARGET(0);
            } else {
                HookArm.setTargetPosition(TARGET);
                HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                HookArm.setPower(-0.5);
            }
            if (gamepad2.b) {
                TM.setPower(-0.7);
                sleep(50);
            }
            if (gamepad2.a) {
                W.setPower(-0.7);
                sleep(50);
            }
            if (gamepad2.y) {
                TM.setPower(0.7);
                sleep(50);
            }
            if (gamepad2.x) {
                W.setPower(0.7);
                sleep(50);
            }
            if (gamepad1.left_bumper) {
                dP.setTargetPosition(0);
                dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                dP.setPower(-0.10);
                setTARGET_1(0);
            } else if (gamepad1.right_bumper) {
                dP.setTargetPosition(150);
                dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                dP.setPower(-0.20);
                setTARGET_1(150);
            } else if (gamepad1.left_trigger != 0) {
                dP.setTargetPosition(250);
                dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                dP.setPower(-0.10);
                setTARGET_1(250);
            } else if (gamepad1.right_trigger != 0) {
                dP.setTargetPosition(950);
                dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                dP.setPower(-0.10);
                setTARGET_1(950);
            } else {
                dP.setTargetPosition(TARGET_1);
                dP.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                dP.setPower(-0.15);
            }
            if (-gamepad2.left_stick_y > 0) {
                switch (mHookArmState) {
                    case STATE_ONE:
                        TM.setPower(0.55);
                        W.setPower(getPWRF(REV));
                        sleep(600);
                        newHookState(State.STATE_TWO);
                        break;
                    case STATE_TWO:
                        TM.setPower(0.55);
                        W.setPower(getPWRF(REV));
                        sleep(550);
                        newHookState(State.STATE_THREE);
                        break;
                    case STATE_THREE:
                        TM.setPower(0.50);
                        W.setPower(getPWRF(REV));
                        sleep(550);
                        newHookState(State.STATE_FOUR);
                        break;
                    case STATE_FOUR:
                        TM.setPower(0.45);
                        W.setPower(getPWRF(REV));
                        sleep(550);
                        newHookState(State.STATE_FIVE);
                        break;
                    case STATE_FIVE:
                        TM.setPower(0.45);
                        W.setPower(getPWRF(REV));
                        sleep(550);
                        newHookState(State.STATE_SIX);
                        break;
                    case STATE_SIX:
                        TM.setPower(0.40);
                        W.setPower(getPWRF(REV));
                        sleep(750);
                        break;
                }
                REV++;
            } else if (-gamepad2.left_stick_y < 0) {
                switch (mHookArmState) {
                    case STATE_ONE:
                        TM.setPower(-1);
                        W.setPower(-getPWRR(REV));
                        motorRight.setPower(1);
                        motorLeft.setPower(1);
                        sleep(550);
                        break;
                    case STATE_TWO:
                        TM.setPower(-1);
                        W.setPower(-getPWRR(REV));
                        motorRight.setPower(0.8);
                        motorLeft.setPower(0.7);
                        sleep(550);
                        newHookState(State.STATE_TWO);
                        break;
                    case STATE_THREE:
                        TM.setPower(-1);
                        W.setPower(-getPWRR(REV));
                        motorRight.setPower(0.8);
                        motorLeft.setPower(0.7);
                        sleep(550);
                        newHookState(State.STATE_FOUR);
                        break;
                    case STATE_FOUR:
                        TM.setPower(-1);
                        W.setPower(-getPWRR(REV));
                        motorRight.setPower(0.8);
                        motorLeft.setPower(0.7);
                        sleep(550);
                        newHookState(State.STATE_THREE);
                        break;
                    case STATE_FIVE:
                        TM.setPower(-1);
                        W.setPower(-getPWRR(REV));
                        motorRight.setPower(0.8);
                        motorLeft.setPower(0.7);
                        sleep(550);
                        newHookState(State.STATE_FOUR);
                        break;
                    case STATE_SIX:
                        TM.setPower(-getPWRR(REV));
                        W.setPower(-1);
                        sleep(750);
                        newHookState(State.STATE_FIVE);
                        break;
                }
                REV--;
            } else {
                TM.setPower(0);
                W.setPower(0);
            }
            if (gamepad1.a) {
                C.setPosition(0);
            } else if (gamepad1.b) {
                C.setPosition(1);
            } else {
                C.setPosition(0.5);
            }
            telemetry.addData("HookArm State", mHookArmState);
            if (mHookArmState == State.STATE_SIX) {
                telemetry.addData("HookArm State", "One More Forward");
            }
            if (mHookArmState == State.STATE_ONE) {
                telemetry.addData("HookArm State", "One More Back");
            }
        }
        TM.setPower(0);
        W.setPower(0);
        HookArm.setPower(0);
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

    int setTARGET_1(int i) {
        TARGET_1 = i;
        return TARGET_1;
    }

    int setTARGET(int i) {
        TARGET = i;
        return TARGET;
    }

    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.11, 0.13, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.65, 0.72, 0.75, 0.80, 0.85, 0.95, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

    double getPWRF(int ROTATION_NUMBER) {
        if (ROTATION_NUMBER > 6) {
            ROTATION_NUMBER = 6;
        }
        if (ROTATION_NUMBER < 0) {
            ROTATION_NUMBER = 0;
        }
        double[] array = {0.90, 0.90, 0.95, 1, 1, 1, 1};
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

    private void newHookState(State newState) {
        mStateTime.reset();
        mHookArmState = newState;
    }
}
