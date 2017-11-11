package ftc8564lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import hallib.HalUtil;
import ftc8564opMode.LockdownAutonomous;


/**
 * Created by margaretli on 11/8/17.
 */

public class JewelArm {
    ColorSensor colorSensor;
    Servo colorServo;
    Servo crServo;
    LinearOpMode opMode;
    ElapsedTime mClock = new ElapsedTime();

    double servoForward = 1;
    double servoBackward = 0;
    double servoRest = .5;

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
        crServo = opMode.hardwareMap.servo.get("crServo");
        mClock.reset();
    }

    public void pushJewels (boolean blueAlliance){
        if(true){
            if(getColor(LockdownAutonomous.Alliance.BLUE_ALLIANCE) == Color.BLUE){
                colorServo.setPosition(1);
            }
            else if (getColor(LockdownAutonomous.Alliance.BLUE_ALLIANCE) == Color.RED){
                colorServo.setPosition(-1);
            }
            else {
                colorServo.setPosition(0);
            }

        }
        if(false) {
            if (getColor(LockdownAutonomous.Alliance.BLUE_ALLIANCE) == Color.BLUE) {
                colorServo.setPosition(-1);
            } else if (getColor(LockdownAutonomous.Alliance.BLUE_ALLIANCE) == Color.RED) {
                colorServo.setPosition(1);
            } else {
                colorServo.setPosition(0);
            }
        }
    }

    public void armDown() {
        crServo.setPosition(servoForward);
        HalUtil.sleep(500);
        crServo.setPosition(servoRest);
    }

    public void armUp(){
        crServo.setPosition(servoBackward);
        HalUtil.sleep(500);
        crServo.setPosition(servoRest);
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

}