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
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.locks.Lock;

import ftc8564opMode.LockdownAutonomous;
import hallib.HalUtil;

public class BeaconPush {

    ColorSensor redColorSensor, blueColorSensor;
    CRServo redRack, blueRack;
    LinearOpMode opMode;
    STATE state;

    static final double BUTTON_PUSHER_RETRACT_POSITION = 1;
    static final double BUTTON_PUSHER_EXTEND_POSITION = -1;
    static final double BUTTON_PUSHER_REST_POSITION = 0;
    private double endTime;

    private ElapsedTime mClock = new ElapsedTime();

    private enum Color {
        RED,
        BLUE,
        OTHER
    }

    private enum STATE
    {
        EXTENDING,
        RETRACTING,
        EXTENDED,
        RETRACTED
    }

    public BeaconPush(LinearOpMode opMode)
    {
        this.opMode = opMode;
        redColorSensor = opMode.hardwareMap.colorSensor.get("colorSensor");
        redColorSensor.enableLed(false);
        blueColorSensor = opMode.hardwareMap.colorSensor.get("colorSensor1");
        blueColorSensor.enableLed(false);
        redRack = opMode.hardwareMap.crservo.get("rack");
        redRack.setPower(0.1);
        blueRack = opMode.hardwareMap.crservo.get("rack1");
        state = STATE.RETRACTED;
        mClock.reset();
    }

    public boolean beaconColorIsAlliance(LockdownAutonomous.Alliance alliance)
    {
        return (alliance == LockdownAutonomous.Alliance.RED_ALLIANCE && getColor(alliance) == Color.RED) || (alliance == LockdownAutonomous.Alliance.BLUE_ALLIANCE && getColor(alliance) == Color.BLUE);
    }

    public void pushBeacon(boolean redAllianceRack)
    {
        setButtonPusherExtendPosition(redAllianceRack);
        setButtonPusherRetractPosition(redAllianceRack);
    }

    private void setButtonPusherExtendPosition(boolean redAllianceRack)
    {
        // Assuming pusher state is RETRACTED
        if (state == STATE.RETRACTED && redAllianceRack)
        {
            redRack.setPower(BUTTON_PUSHER_EXTEND_POSITION);
            endTime = HalUtil.getCurrentTime() + 1;
            changeState(STATE.EXTENDING);
        } else if(state == STATE.RETRACTED)
        {
            blueRack.setPower(BUTTON_PUSHER_RETRACT_POSITION);
            endTime = HalUtil.getCurrentTime() + 0.8;
            changeState(STATE.EXTENDING);
        }
    }
    private void setButtonPusherRetractPosition(boolean redAllianceRack)
    {
        // Assuming pusher state is EXTENDED
        if (state == STATE.EXTENDED && redAllianceRack)
        {
            redRack.setPower(BUTTON_PUSHER_RETRACT_POSITION);
            endTime = HalUtil.getCurrentTime() + 1;
            changeState(STATE.RETRACTING);
        } else if(state == STATE.EXTENDED)
        {
            blueRack.setPower(BUTTON_PUSHER_EXTEND_POSITION);
            endTime = HalUtil.getCurrentTime() + 0.8;
            changeState(STATE.RETRACTING);
        }
    }
    public void buttonPusherTask()
    {
        // If we are extending or retracting, check if the endTime to see if we are done.
        if ((state == STATE.EXTENDING || state == STATE.RETRACTING) && HalUtil.getCurrentTime() >= endTime)
        {
            redRack.setPower(0.1);
            blueRack.setPower(BUTTON_PUSHER_REST_POSITION);
            changeState(state == STATE.EXTENDING ? STATE.EXTENDED: STATE.RETRACTED);
        }
    }

    private Color getColor(LockdownAutonomous.Alliance alliance)
    {
        if(LockdownAutonomous.Alliance.RED_ALLIANCE == alliance)
        {
            if(redColorSensor.red() > redColorSensor.blue() && redColorSensor.red() > redColorSensor.green())
            {
                return Color.RED;
            } else if(redColorSensor.blue() > redColorSensor.red() && redColorSensor.blue() > redColorSensor.green())
            {
                return Color.BLUE;
            } else {
                return Color.OTHER;
            }
        } else if(LockdownAutonomous.Alliance.BLUE_ALLIANCE == alliance)
        {
            if(redColorSensor.red() > redColorSensor.blue() && redColorSensor.red() > redColorSensor.green())
            {
                return Color.RED;
            } else if(redColorSensor.blue() > redColorSensor.red() && redColorSensor.blue() > redColorSensor.green())
            {
                return Color.BLUE;
            } else {
                return Color.OTHER;
            }
        }
        return Color.OTHER;
    }

    private void changeState(STATE newState)
    {
        state = newState;
    }

}
