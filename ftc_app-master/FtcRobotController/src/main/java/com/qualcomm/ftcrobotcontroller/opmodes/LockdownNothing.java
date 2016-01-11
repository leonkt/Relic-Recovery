package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Owner on 12/5/2015.
 */
public class LockdownNothing extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException
    {
        waitOneFullHardwareCycle();
        waitForStart();
        waitOneFullHardwareCycle();
    }
}
