package ftc8564lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by margaretli on 12/16/17.
 */

public class Intake {
    DcMotor intakeleft;
    DcMotor intakeright;
    LinearOpMode opMode;

    public Intake (LinearOpMode opMode) {
        this.opMode = opMode;
        intakeleft = opMode.hardwareMap.dcMotor.get("intakeleft");
        intakeright = opMode.hardwareMap.dcMotor.get("intakeright");
    }

    public void in(){
        intakeleft.setPower(.8);
        intakeright.setPower(-.8);
    }

    public void out(){
        intakeleft.setPower(-.8);
        intakeright.setPower(0.8);
    }

    public void leftin(){
        intakeleft.setPower(.8);
        intakeright.setPower(.8);
    }
    public void rightin(){
        intakeleft.setPower(-.8);
        intakeright.setPower(-.8);
    }
    public void stop(){
        intakeleft.setPower(0);
        intakeright.setPower(0);
    }

}
