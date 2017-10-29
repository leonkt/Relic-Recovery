package ftc8564opMode;

/**
 * Created by ACtheGreat on 2017/10/27.
 */
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
/*import com.qualcomm.robotcore.eventloop.opmode.Disabled;
 */
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;

@TeleOp(name = "Debug: TeleOp", group = "Debug")
/*@Disabled*/

public class TeleopTest extends OpMode{
    /**
    * TeleopTest.java is created by A.C.G. for team LockDown #8564.
    * (c) Copyright 20xx CA Works. All Rights reserved.
    *
    * This is a debug teleop program which introduces the basic movement of the robot.
    * Later the definitions will be moved to a hardware class.
    *
    */


    private ElapsedTime runtime = new ElapsedTime();

    /*object creation (will move to a hardware library)*/
    /*Drive Wheels (Motors)*/
    DcMotor motorleft;
    DcMotor motorright;
    /*clamp motor*/
    Servo clampleft;
    Servo clampright;
    /*lift motors*/
    DcMotor liftleft;
    DcMotor liftright;
    /*intake motors*/
    DcMotor intakeleft;
    DcMotor intakeright;
    /*
    * Special values definition:
    * reverseFactor: multiplying with this to reverse the directon of the motor
    * threshold: this is the common threshold for regular uses.
    * Warning: while using threshold, there must be a reset statement to set it to 0
    *
    */
    private int reverseFactor = -1;
    private double threshold = 0.1;


    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");
        /* hardware mapping */
        /*Drive Wheels (Motors)*/
        motorleft = hardwareMap.dcMotor.get("left");
        motorright = hardwareMap.dcMotor.get("right");
        /*clamp motor*/
        clampleft = hardwareMap.servo.get("clampleft");
        clampright = hardwareMap.servo.get("clampright");
        /*lift motors*/
        liftleft = hardwareMap.dcMotor.get("liftleft");
        liftright = hardwareMap.dcMotor.get("liftright");
        /*intake motors*/
        intakeleft = hardwareMap.dcMotor.get("intakeleft");
        intakeright = hardwareMap.dcMotor.get("intakeright");


    }

  /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    @Override
    public void init_loop() {
        /*sets servo position */
        clampleft.setPosition(0);
        /*clampright.setPosition(90);*/

    }

    /*
     * This method will be called ONCE when start is pressed
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void start() {

        /*clampleft.setPosition(20);*/
        /*clampright.setPosition(75);*/
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     *
     * Use this.time to specify timing.
     *
     */
    @Override
    public void loop() {

        /*
         * main code body
         * These codes will be ran in a loop continuously.
         * Main actions are in here, as well as updates.
         *
         */

        runtime.reset();

        /*
        if(gamepad1.left_stick_y >0.1){
            motorleft.setPower(gamepad1.left_stick_y);
            motorright.setPower(-gamepad1.left_stick_y);
        }
        else if (gamepad1.left_stick_y <-0.1){
            motorleft.setPower(-gamepad1.left_stick_y);
            motorright.setPower(gamepad1.left_stick_y);
        }
        else{
            motorleft.setPower(gamepad1.left_stick_y);
            motorright.setPower(gamepad1.left_stick_y);
        }
        */
        /* Driving
        *  The previously commented out ones are the implementation of single joystick drive.
        *  This implementation below is tank drive.
        *
        *  absolute values are used to measure threshold
        *  while the main value assignment is not modified.
        *  In here, all LEFT motors will be REVERSED
        *
        *  Player 1 controls: left/right joystick up/down ->
        *  corresponding side wheels move forward/backward
        *
        */
        if (abs(gamepad1.left_stick_y) > threshold){
            motorleft.setPower(gamepad1.left_stick_y *reverseFactor);
        }
        else{
                motorleft.setPower(0 );
        }
        if (abs(gamepad1.right_stick_y) > threshold){
            motorright.setPower(gamepad1.right_stick_y );
        }
        else {
            motorright.setPower(0);
        }


        /* Lifting
        *  The lift is done by moving two motors simultaniously.
        *  Additional movements (in case one motor overspun, etc will be added soon.
        *
        *  WARNING: fragile lift.
        *  Clamp system coming soon.
        *
        * Player 2 controls: Right Joystick up/down -> lift up/down
        *
        */
        if (abs(gamepad2.right_stick_y) > threshold){
            liftleft.setPower(gamepad2.right_stick_y * reverseFactor);
            liftright.setPower(gamepad2.right_stick_y);
        }
        else{
            liftleft.setPower(0);
            liftright.setPower(0);
        }

        /*
        * Intake - Intake portion.
        *
        * if both left and right bumper are both pushed above threshold -> both sides intake at 100%
        * if only left/right pushed -> corresponding side goes 100%, while the other side lowers
        * if nothing pushed -> stopped
        *
        * Player 1 controls: left trigger and Right Trigger
        */

        if (abs(gamepad1.left_trigger) > 0.6 && abs(gamepad1.right_trigger) > 0.6){
            intakeleft.setPower(1 );
            intakeright.setPower(1 * reverseFactor);
        }
        else if (abs(gamepad1.left_trigger) > 0.6) {
            intakeleft.setPower(1);
            intakeright.setPower(0.6 * -1);
        }
        else if (abs(gamepad1.right_trigger) > 0.6) {
            intakeleft.setPower(0.6);
            intakeright.setPower(1 * reverseFactor);
        }
        else if (gamepad1.left_bumper){
            intakeleft.setPower(1);
            intakeright.setPower(-1*reverseFactor);
        }
        else if (gamepad1.right_bumper){
            intakeleft.setPower(-1);
            intakeright.setPower(1 * reverseFactor);
        }
        else {
            intakeleft.setPower(0);
            intakeright.setPower(0 * reverseFactor);
        }
        /*
        *
        * Intake -> slant cube adjustment
        * By using bumpers, the corresponded bumper side will go IN while the other side will go OUT
        *
        * Player 1 Control: Left bumper/Right Bumper: corresponding side rolls in while other goes
        * reverse
        */
        
       
        
        



        telemetry.addData("Status", "Run Time: " + runtime.toString());

    }
}
