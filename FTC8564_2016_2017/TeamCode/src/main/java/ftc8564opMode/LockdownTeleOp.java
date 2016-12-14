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

package ftc8564opMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import ftc8564lib.Robot;

@TeleOp(name="LockdownTeleOp", group="TeleOp")
public class LockdownTeleOp extends LinearOpMode {

    private Robot robot;
    private boolean inProgress;

    @Override
    public void runOpMode() throws InterruptedException {
        inProgress = false;
        robot = new Robot(this,false);
        robot.driveBase.noEncoders();
        waitForStart();
        while (opModeIsActive()) {
            // Drive Train command
            robot.driveBase.tankDrive(gamepad1.right_stick_y, gamepad1.left_stick_y);
            // Pulley System
            //robot.pulleySystem.setSyncMotorPower(-gamepad2.left_stick_y);
            if(gamepad2.left_bumper)
            {
                robot.pulleySystem.manualControl(gamepad2.left_stick_y, gamepad2.left_stick_y);
            } else {
                robot.pulleySystem.manualControl(gamepad2.left_stick_y, gamepad2.right_stick_y);
            }
            //Beacon Push
            if(gamepad1.right_bumper)
            {
                robot.beaconPush.pushBeacon(true);
            } else if(gamepad1.right_trigger == 1)
            {
                robot.beaconPush.pushBeacon(false);
            }
            robot.beaconPush.buttonPusherTask();
            // Tennis Arm
            if(gamepad1.left_bumper)
            {
                robot.shooter.setTennisArmPower(true);
            } else if(gamepad1.left_trigger == 1)
            {
                robot.shooter.setTennisArmPower(false);
            } else {
                robot.shooter.setTennisArmPower();
            }
            //Shooting Mechanism
            if(gamepad2.x && !inProgress)
            {
                inProgress = true;
                robot.shooter.shootBall(-1);
            } else if(gamepad2.b && !inProgress)
            {
                inProgress = true;
                robot.shooter.shootBall(1);
            } else if(gamepad2.right_stick_y == 0)
            {
                robot.shooter.primeBall(gamepad2.right_stick_x);
            }
            if(!gamepad2.x && !gamepad2.b)
            {
                inProgress = false;
            }
            //Forklift
            if(gamepad2.a)
            {
                robot.pulleySystem.releaseForklift();
            }
        }

        robot.pulleySystem.resetMotors();
        robot.shooter.resetMotors();
        robot.driveBase.resetMotors();
        robot.driveBase.resetPIDDrive();

    }
}