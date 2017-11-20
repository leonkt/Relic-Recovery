package ftc8564lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import hallib.HalUtil;
import ftc8564opMode.LockdownAutonomous;


/**
 * Created by margaretli on 11/8/17.
 */

public class JewelArm {
    ColorSensor colorSensor;
    Servo colorServo;
    CRServo crServo;
    LinearOpMode opMode;
    ElapsedTime mClock = new ElapsedTime();

    double servoForward = -.1;
    double servoBackward = .4;
    double servoRest = 0;
    private boolean blueAlliance = true;

    private enum Color{
        RED,
        BLUE,
        OTHER
    }

    public JewelArm (LinearOpMode opMode)
    {
        this.opMode = opMode;
        colorSensor = opMode.hardwareMap.colorSensor.get("colorSensor");
        colorServo = opMode.hardwareMap.servo.get("colorServo");
        crServo = opMode.hardwareMap.crservo.get("crServo");
        mClock.reset();
    }

    public void pushJewels (boolean blueAlliance){
        colorSensor.enableLed(true);
        if(blueAlliance){
            if(getColor(LockdownAutonomous.Alliance.BLUE_ALLIANCE) == Color.BLUE){
                colorServo.setPosition(0);
                HalUtil.sleep(500);
            }
            else if (getColor(LockdownAutonomous.Alliance.BLUE_ALLIANCE) == Color.RED){
                colorServo.setPosition(1);
                HalUtil.sleep(500);
            }
            else {
                colorServo.setPosition(.4);
            }

        }
        if(!blueAlliance) {
            if (getColor(LockdownAutonomous.Alliance.BLUE_ALLIANCE) == Color.BLUE) {
                colorServo.setPosition(1);
                HalUtil.sleep(500);
            } else if (getColor(LockdownAutonomous.Alliance.BLUE_ALLIANCE) == Color.RED) {
                colorServo.setPosition(0);
                HalUtil.sleep(500);
            } else {
                colorServo.setPosition(.4);
            }
        }
        colorSensor.enableLed(false);
    }

    public void armDown() {
        crServo.setPower(servoForward);
        HalUtil.sleep(1000);
        crServo.setPower(servoRest);
    }

    public void armUp(){
        crServo.setPower(servoBackward);
        HalUtil.sleep(500);
        colorServo.setPosition(0.4);
        crServo.setPower(servoBackward);
        HalUtil.sleep(1500);
        crServo.setPower(servoRest);
    }

    public Color getColor (LockdownAutonomous.Alliance alliance){
        if(LockdownAutonomous.Alliance.RED_ALLIANCE == alliance)
        {
            if(colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green())
            {
                return Color.RED;
            } else if(colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green())
            {
                return Color.BLUE;
            }
        } else if(LockdownAutonomous.Alliance.BLUE_ALLIANCE == alliance)
        {
            if(colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green())
            {
                return Color.RED;
            } else if(colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green())
            {
                return Color.BLUE;
            }
        }
        return Color.OTHER;

    }

    public void resetServo()
    {
        colorServo.setPosition(0.4);
        crServo.setPower(servoRest);
        HalUtil.sleep(500);
        colorSensor.enableLed(false);
    }

}