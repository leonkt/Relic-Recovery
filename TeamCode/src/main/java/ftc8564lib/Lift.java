package ftc8564lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by margaretli on 11/19/17.
 */

public class Lift {
    DcMotor lift;
    LinearOpMode opMode;

    public Lift (LinearOpMode opMode){
        this.opMode = opMode;
        lift = opMode.hardwareMap.dcMotor.get("liftleft");
    }

    public void up (int liftPosition){
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (liftPosition == 0) {
            lift.setTargetPosition(1);
        }
        else if (liftPosition == 1) {
            lift.setTargetPosition(2);
        }
        else if (liftPosition == 2) {
            lift.setTargetPosition(3);
        }
        else if (liftPosition == 3) {
            lift.setTargetPosition(4);
        }
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(.75);
        while (lift.isBusy()){}
        lift.setPower(0);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

