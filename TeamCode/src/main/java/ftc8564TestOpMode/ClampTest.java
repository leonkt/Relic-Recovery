package ftc8564TestOpMode;

/**
 * Created by margaretli on 2017/10/27.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;

/*import com.qualcomm.robotcore.eventloop.opmode.Disabled;
 */

@TeleOp(name = "ClampTest", group = "LinearOpMode")
/*@Disabled*/

public class ClampTest extends OpMode{
    /**
    * TeleopTest.java is created by A.C.G. for team LockDown #8564.
    * (c) Copyright 20xx CA Works. All Rights reserved.
    *
    * This is a debug teleop program which introduces the basic movement of the robot.
    * Later the definitions will be moved to a hardware class.
    *
    */


    private ElapsedTime runtime = new ElapsedTime();

    /*clamp motor*/
    Servo clampleft;
    Servo clampright;
    private double clamp = -1;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");
        /* hardware mapping */
        clampleft = hardwareMap.servo.get("clampleft");
        clampright = hardwareMap.servo.get("clampright");
    }

  /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    @Override
    public void init_loop() {
        /*sets servo position */
        //clampleft.setPosition(1);
        //clampright.setPosition(0);
        //stopper.setPosition(.5);


    }

    /*
     * This method will be called ONCE when start is pressed
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void start() {
        clampleft.setPosition(0);
        clampright.setPosition(1);

    }

    @Override
    public void loop() {
        runtime.reset();

        if (gamepad2.left_bumper){
            clampleft.setPosition(0);
            clampright.setPosition(1);
        }
        //close clamps
        else if (gamepad2.right_bumper){
            clampleft.setPosition(.25);
            clampright.setPosition(.75);
        }

        if (gamepad2.right_trigger > .6){
            clampleft.setPosition(.325);
            clampright.setPosition(.675);
        }

    }
}
