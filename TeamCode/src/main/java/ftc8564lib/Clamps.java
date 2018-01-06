package ftc8564lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import hallib.HalUtil;

/**
 * Created by margaretli on 11/10/17.
 */

public class Clamps {
    Servo winch;
    Servo lefthug;
    Servo righthug;
    LinearOpMode opMode;

    private double openL = 1;
    private double openR = 0;
    private double closeL = .6;
    private double closeR = .4;
    private double gripL = .39;
    private double gripR = .6;

    public Clamps (LinearOpMode opMode) {
        this.opMode = opMode;
        lefthug = opMode.hardwareMap.servo.get("lefthug");
        righthug = opMode.hardwareMap.servo.get("righthug");
        winch = opMode.hardwareMap.servo.get("winch");

    }

    public void open(){
        lefthug.setPosition(openL);
        righthug.setPosition(openR);
    }

    public void close(){
        lefthug.setPosition(closeL);
        righthug.setPosition(closeR);
    }

    public void grip(){
        lefthug.setPosition(gripL);
        righthug.setPosition(gripR);
    }

    public void grabglyph(){
        close();
        winch.setPosition(1);
        HalUtil.sleep(4000);
        grip();
        winch.setPosition(0);
        HalUtil.sleep(4300);
        winch.setPosition(.5);

    }
}
