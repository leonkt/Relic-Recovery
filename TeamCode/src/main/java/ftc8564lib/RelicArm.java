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
    Servo gripperextension;
    CRServo armgripper;
    LinearOpMode opMode;

    public RelicArm(LinearOpMode opMode){
        arm = opMode.hardwareMap.dcMotor.get("arm");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armextension = opMode.hardwareMap.dcMotor.get("armextension");
        armextension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        gripperextension = opMode.hardwareMap.servo.get("gripperextension");
        armgripper = opMode.hardwareMap.crservo.get("armgripper");
    }

    public void open(){
        armgripper.setPower(0.5);
    }

    public void close(){
        armgripper.setPower(-0.5);
    }

    public void extend(){
        gripperextension.setPosition(gripperextension.getPosition() + 0.05);
    }

    public void retract(){
        gripperextension.setPosition(gripperextension.getPosition() - 0.05);
    }

    public void pressure(){
        if (arm.getCurrentPosition() < -300){
            arm.setPower(0.2);
        }
        else if (arm.getCurrentPosition() < -400){
            arm.setPower(0.3);
        }
        else if (arm.getCurrentPosition() < -500){
            arm.setPower(0.4);
        }
        else {
            arm.setPower(0);
        }
    }

    public void armExtension (double power){
        armextension.setPower(power);
    }

    public void moveArm (double power){
        arm.setPower(power * 0.3);
    }
}
