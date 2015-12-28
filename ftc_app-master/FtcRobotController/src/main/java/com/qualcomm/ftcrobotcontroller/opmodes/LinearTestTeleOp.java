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
    ElapsedTime time;

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

        waitForStart();

        while(opModeIsActive()) {
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
                HookArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                HookArm.setTargetPosition(getMotorPosition(HookArm) + 20);
                HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                HookArm.setPower(-0.1);
            } else
            if(gamepad2.right_bumper) {
                HookArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
                HookArm.setTargetPosition(getMotorPosition(HookArm) - 20);
                HookArm.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                HookArm.setPower(0.1);
            }
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

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.11, 0.13, 0.15, 0.18, 0.24,
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
}
