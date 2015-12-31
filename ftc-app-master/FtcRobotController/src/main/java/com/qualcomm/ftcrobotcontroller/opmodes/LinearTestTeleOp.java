package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Owner on 12/27/2015.
 */
public class LinearTestTeleOp extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor TM;
    DcMotor W;
    DcMotor HookArm;
    DcMotorController mc1;
    DcMotorController mc2;
    DcMotorController mc3;
    private enum State {
        STATE_ONCE, STATE_POSITION
    };
    private State mCurrentState;
    public ElapsedTime  mRuntime = new ElapsedTime();   // Time into round.
    private ElapsedTime mStateTime = new ElapsedTime();  // Time into current state
    private int mLeftEncoderTarget;
    private int mRightEncoderTarget;
    private int POS;

    @Override
    public void runOpMode() throws InterruptedException {

        mc1 = hardwareMap.dcMotorController.get("mc1Dc");
        mc2 = hardwareMap.dcMotorController.get("mc2Dc");
        mc3 = hardwareMap.dcMotorController.get("mc3Dc");
        TM = hardwareMap.dcMotor.get("TM");
        W = hardwareMap.dcMotor.get("W");
        HookArm = hardwareMap.dcMotor.get("HookArm");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        HookArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        int REV = 0;

        waitForStart();

        while(opModeIsActive()) {

            mRuntime.reset();

            /* float left = -gamepad1.left_stick_y;
            float right = -gamepad1.right_stick_y;

            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            right = (float)scaleInput(right);
            left =  (float)scaleInput(left);

            if(left < 0)
            {
                motorLeft.setPower(left);
            } else if(left > 0){
                motorLeft.setPower(left);
            } else {
                motorLeft.setPower(0);
            }
            if(right < 0)
            {
                motorRight.setPower(right);
            } else if (right > 0) {
                motorRight.setPower(right);
            } else {
                motorRight.setPower(0);
            }

            float HookArm1 = -gamepad2.left_stick_y;
            float TM1 = -gamepad2.right_stick_y;

            HookArm1 = Range.clip(HookArm1, -1, 1);
            TM1 = Range.clip(TM1, -1, 1);

            //TM1 = (float)scaleInput(TM1);
            float W1 = (float)scaleInput(TM1);

            if(TM1 < 0)
            {
                TM.setPower(0.76 * (W1 + 0.15));
                W.setPower(W1 + 0.15);
            } else if(TM1 > 0) {
                TM.setPower(0.76 * (W1 - 0.15));
                W.setPower(W1 - 0.15);
            } else {
                TM.setPower(0);
                W.setPower(0);
            }*/
            if(gamepad2.left_bumper) {                          // 45 - 50: Top Most Bar in TeleOp but not End Game; 150: Hanging Bar
                //HookArm.setTargetPosition(incrementBy20(HookArm,0));
                //HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                //HookArm.setPower(-0.1);
                //newState(State.STATE_ONCE);
            } else if(gamepad2.right_bumper) {
                //HookArm.setTargetPosition(incrementBy20(HookArm,1));
                //HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                //HookArm.setPower(0.1);
                //newState(State.STATE_ONCE);
            } else {
                //HookArm.setTargetPosition(currentPosition(HookArm));
                //HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                //HookArm.setPower(-0.1);
            }
            if(-gamepad2.left_stick_y > 0) {
                TM.setPower(1);
                W.setPower(getPWR(REV));
                sleep(waitTime(REV));
                REV++;
            } else if(-gamepad2.left_stick_y < 0) {
                TM.setPower(-1);
                W.setPower(-getPWR(REV));
                sleep(waitTime(REV));
                REV--;
            } else {
                TM.setPower(0);
                W.setPower(0);
            }
            telemetry.addData("Rotation Number", REV+1);
        }
        TM.setPower(0);
        W.setPower(0);
        HookArm.setPower(0);
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

    public int getMotorPosition(DcMotor motor) throws InterruptedException
    {
        DcMotorController motorController = motor.getController();
        if (motorController.getMotorControllerDeviceMode()  == DcMotorController.DeviceMode.READ_ONLY)
        {
            return motor.getCurrentPosition();
        }
        else
        {
            motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
            while (motorController.getMotorControllerDeviceMode() != DcMotorController.DeviceMode.READ_ONLY)
            {
                waitForNextHardwareCycle();
            }
            int currPosition = motor.getCurrentPosition();
            motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
            while (motorController.getMotorControllerDeviceMode() != DcMotorController.DeviceMode.WRITE_ONLY)
            {
                waitForNextHardwareCycle();
            }
            return currPosition;
        }
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

    int incrementBy20(DcMotor motor, int i) throws InterruptedException {
        if(i == 0) {
            int TARGET = getMotorPosition(motor) + 20;
            return TARGET;
        }
        int TARGET = getMotorPosition(motor) - 20;
        return TARGET;
    }

    double getPWR(int ROTATION_NUMBER) {
        if(ROTATION_NUMBER > 5) {
            ROTATION_NUMBER = 5;
        }
        if(ROTATION_NUMBER < 0) {
            ROTATION_NUMBER = 0;
        }
        double[] array = {0.75, 0.67, 0.59, 0.51, 0.42, 0.33};
        return array[ROTATION_NUMBER];
    }

    int waitTime(int ROTATION_NUMBER) {
        if(ROTATION_NUMBER > 5) {
            ROTATION_NUMBER = 5;
        }
        if(ROTATION_NUMBER < 0) {
            ROTATION_NUMBER = 0;
        }
        int[] waitarray = {526, 592, 671, 775, 943, 1196};
        return waitarray[ROTATION_NUMBER];
    }

    int currentPosition(DcMotor motor) throws InterruptedException {
        switch(mCurrentState) {
            case STATE_ONCE:
                newState(State.STATE_POSITION);
                POS = getMotorPosition(motor);
                return POS;
            case STATE_POSITION:
                return POS;
        }
        return POS;
    }

    private void newState(State newState)
    {
        // Reset the state time, and then change to next state.
        mStateTime.reset();
        mCurrentState = newState;
    }

    //--------------------------------------------------------------------------
    // setEncoderTarget( LeftEncoder, RightEncoder);
    // Sets Absolute Encoder Position
    //--------------------------------------------------------------------------
    void setEncoderTarget(int leftEncoder, int rightEncoder)
    {
        motorLeft.setTargetPosition(mLeftEncoderTarget = leftEncoder);
        motorRight.setTargetPosition(mRightEncoderTarget = rightEncoder);
    }

    //--------------------------------------------------------------------------
    // addEncoderTarget( LeftEncoder, RightEncoder);
    // Sets relative Encoder Position.  Offset current targets with passed data
    //--------------------------------------------------------------------------
    void addEncoderTarget(int leftEncoder, int rightEncoder)
    {
        motorLeft.setTargetPosition(mLeftEncoderTarget += leftEncoder);
        motorRight.setTargetPosition(mRightEncoderTarget += rightEncoder);
    }

    //--------------------------------------------------------------------------
    // setDrivePower( LeftPower, RightPower);
    //--------------------------------------------------------------------------
    void setDrivePower(double leftPower, double rightPower)
    {
        motorLeft.setPower(Range.clip(leftPower, -1, 1));
        motorRight.setPower(Range.clip(rightPower, -1, 1));
    }

    //--------------------------------------------------------------------------
    // setDriveSpeed( LeftSpeed, RightSpeed);
    //--------------------------------------------------------------------------
    void setDriveSpeed(double leftSpeed, double rightSpeed)
    {
        setDrivePower(leftSpeed, rightSpeed);
    }

    //--------------------------------------------------------------------------
    // runToPosition ()
    // Set both drive motors to encoder servo mode (requires encoders)
    //--------------------------------------------------------------------------
    public void runToPosition()
    {
        setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    //--------------------------------------------------------------------------
    // useConstantSpeed ()
    // Set both drive motors to constant speed (requires encoders)
    //--------------------------------------------------------------------------
    public void useConstantSpeed()
    {
        setDriveMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    //--------------------------------------------------------------------------
    // useConstantPower ()
    // Set both drive motors to constant power (encoders NOT required)
    //--------------------------------------------------------------------------
    public void useConstantPower()
    {
        setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    //--------------------------------------------------------------------------
    // resetDriveEncoders()
    // Reset both drive motor encoders, and clear current encoder targets.
    //--------------------------------------------------------------------------
    public void resetDriveEncoders()
    {
        setEncoderTarget(0, 0);
        setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    //--------------------------------------------------------------------------
    // syncEncoders()
    // Load the current encoder values into the Target Values
    // Essentially synch's the software with the hardware
    //--------------------------------------------------------------------------
    void synchEncoders()
    {
        //	get and set the encoder targets
        mLeftEncoderTarget = motorLeft.getCurrentPosition();
        mRightEncoderTarget = motorRight.getCurrentPosition();
    }

    //--------------------------------------------------------------------------
    // setDriveMode ()
    // Set both drive motors to new mode if they need changing.
    //--------------------------------------------------------------------------
    public void setDriveMode(DcMotorController.RunMode mode)
    {
        // Ensure the motors are in the correct mode.
        if (motorLeft.getMode() != mode)
            motorLeft.setMode(mode);

        if (motorRight.getMode() != mode)
            motorRight.setMode(mode);
    }

    //--------------------------------------------------------------------------
    // getLeftPosition ()
    // Return Left Encoder count
    //--------------------------------------------------------------------------
    int getLeftPosition()
    {
        return motorLeft.getCurrentPosition();
    }

    //--------------------------------------------------------------------------
    // getRightPosition ()
    // Return Right Encoder count
    //--------------------------------------------------------------------------
    int getRightPosition()
    {
        return motorRight.getCurrentPosition();
    }

    //--------------------------------------------------------------------------
    // moveComplete()
    // Return true if motors have both reached the desired encoder target
    //--------------------------------------------------------------------------
    boolean moveComplete()
    {
        //  return (!mLeftMotor.isBusy() && !mRightMotor.isBusy());
        return ((Math.abs(getLeftPosition() - mLeftEncoderTarget) < 10) &&
                (Math.abs(getRightPosition() - mRightEncoderTarget) < 10));
    }

    //--------------------------------------------------------------------------
    // encodersAtZero()
    // Return true if both encoders read zero (or close)
    //--------------------------------------------------------------------------
    boolean encodersAtZero()
    {
        return ((Math.abs(getLeftPosition()) < 5) && (Math.abs(getRightPosition()) < 5));
    }
}
