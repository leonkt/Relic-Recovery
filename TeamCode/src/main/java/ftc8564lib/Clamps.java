package ftc8564lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by margaretli on 11/10/17.
 */

public class Clamps {
    Servo clampleft;
    Servo clampright;
    Servo lefttop;
    Servo righttop;
    LinearOpMode opMode;

    private double openL = 1;
    private double openR = 0;
    private double closeL = .75;
    private double closeR = .25;
    private double gripL = .62;
    private double gripR = .38;
    private double tCloseL = .7;
    private double tCloseR = .3;
    private double tGripL = .575;
    private double tGripR = .425;

    public Clamps (LinearOpMode opMode) {
        this.opMode = opMode;
        clampleft = opMode.hardwareMap.servo.get("clampleft");
        clampright = opMode.hardwareMap.servo.get("clampright");
        lefttop = opMode.hardwareMap.servo.get("lefttop");
        righttop = opMode.hardwareMap.servo.get("righttop");
    }

    public void open(){
        clampleft.setPosition(openL);
        clampright.setPosition(openR);
    }

    public void opentop(){
        lefttop.setPosition(openL);
        righttop.setPosition(openR);
    }

    public void openboth(){
        clampleft.setPosition(openL);
        clampright.setPosition(openR);
        lefttop.setPosition(openL);
        righttop.setPosition(openR);
    }

    public void close(){
        clampleft.setPosition(closeL);
        clampright.setPosition(closeR);
    }

    public void closeboth(){
        clampleft.setPosition(closeL);
        clampright.setPosition(closeR);
        lefttop.setPosition(tCloseL);
        righttop.setPosition(tCloseR);
    }

    public void grip(){
        clampleft.setPosition(gripL);
        clampright.setPosition(gripR);
    }

    public void gripboth(){
        clampleft.setPosition(gripL);
        clampright.setPosition(gripR);
        lefttop.setPosition(tGripL);
        righttop.setPosition(tGripR);
    }

    public void griptop(){
        lefttop.setPosition(tGripL);
        righttop.setPosition(tGripR);
    }
}
