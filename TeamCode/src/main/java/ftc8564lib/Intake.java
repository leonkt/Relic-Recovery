package ftc8564lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by margaretli on 12/16/17.
 */

public class Intake {
    DcMotor hugleft;
    DcMotor hugright;

    LinearOpMode opMode;
    private double rightPower;
    private double leftPower;

    public Intake (LinearOpMode opMode) {
        this.opMode = opMode;
        hugleft = opMode.hardwareMap.dcMotor.get("hugleft");
        hugright = opMode.hardwareMap.dcMotor.get("hugright");
    }

    public void in(double power){
        if (leftPower < .8){
            leftPower = (scalePower(power));
            rightPower = (scalePower(power));
        }
        else{
            leftPower = 0.8;
            rightPower = 0.8;
        }
        hugleft.setPower(leftPower);
        hugright.setPower(-rightPower);
    }

    private double scalePower(double dVal)
    {
        return (Math.signum(dVal) * ((Math.pow(dVal, 2) * (.9)) + .1));
    }

    public void out(){
        hugleft.setPower(-.8);
        hugright.setPower(.8);
    }

    public void stop(){
        hugleft.setPower(0);
        hugright.setPower(0);
    }

}
