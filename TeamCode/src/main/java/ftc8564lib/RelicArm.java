package ftc8564lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by margaretli on 12/16/17.
 */

public class RelicArm {
    public DcMotor arm;
    public DcMotor armextension;
    Servo longer;
    Servo armgripper;
    Servo claw;
    LinearOpMode opMode;

    public RelicArm(LinearOpMode opMode){
        arm = opMode.hardwareMap.dcMotor.get("arm");
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armextension = opMode.hardwareMap.dcMotor.get("binch");
        armextension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        longer = opMode.hardwareMap.servo.get("longer");
        claw = opMode.hardwareMap.servo.get("claw");
    }

    public void open(){
        claw.setPosition(.7);
    }

    public void close(){
        claw.setPosition(0);
    }

    public void extend(){
        longer.setPosition(.8);
    }

    public void retract(){
        longer.setPosition(0);
    }


    public void armExtension (double power){
        armextension.setPower(power);
    }

    public void moveArm (double power){
        arm.setPower(power * 0.3);
    }
}
