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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import ftc8564lib.*;

@Autonomous(name="TestAutonomous", group="Autonomous")
public class TestAutonomous extends LinearOpMode implements DriveBase.AbortTrigger {

    Robot robot;

    private int numParticles = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this,true);
        waitForStart();
        robot.driveBase.resetHeading();

        robot.driveBase.drivePID(6, false, null);
        robot.driveBase.spinPID(40);
        robot.driveBase.drivePID(35, false, null);
        // Loads and fires numParticles
        for(int i = 0; i < numParticles; i++)
        {
            robot.shooter.shootSequenceAuto(true);
            robot.shooter.waitForShootAuto();
            robot.shooter.shootSequenceAuto(true);
            robot.shooter.waitForShootAuto();
            robot.shooter.shootSequenceAuto(true);
            if(i != numParticles-1) robot.shooter.waitForShootAuto();
        }
        robot.driveBase.spinPID(35);
        robot.driveBase.drivePID(38, false, null);
        //robot.driveBase.spinPID(13);                    // 2.6 in away from wall
        //robot.driveBase.drivePID(5, false, null);
        robot.driveBase.spinPID(0);
        //robot.driveBase.drivePID(-22, false, this);
        robot.driveBase.drivePID(-15, false, this);

        //At First Beacon
        robot.driveBase.sleep(0.2);
        if(robot.beaconPush.beaconColorIsAlliance(LockdownAutonomous.Alliance.RED_ALLIANCE))
        {
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.beaconPush.pushBeacon(true);
            robot.beaconPush.waitUntilPressed();
            robot.driveBase.drivePID(30, false, null);
            robot.driveBase.spinPID(-2);
            robot.driveBase.drivePID(22, true, this);
        } else {
            robot.driveBase.drivePID(7, false, null);
            robot.driveBase.sleep(0.2);
            if(robot.beaconPush.beaconColorIsAlliance(LockdownAutonomous.Alliance.RED_ALLIANCE))
            {
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
                robot.beaconPush.pushBeacon(true);
                robot.beaconPush.waitUntilPressed();
            }
            robot.driveBase.drivePID(23, false, null);
            robot.driveBase.spinPID(-2);
            robot.driveBase.drivePID(22, true, this);
        }

        robot.shooter.resetMotors();
        robot.beaconPush.resetRacks();
        robot.pulleySystem.resetMotors();
        robot.driveBase.resetMotors();
        robot.driveBase.resetPIDDrive();

    }

    @Override
    public boolean shouldAbort() { return robot.odsLeft.getRawLightDetected() >= 0.6 || robot.odsRight.getRawLightDetected() >= 0.6; }

}
