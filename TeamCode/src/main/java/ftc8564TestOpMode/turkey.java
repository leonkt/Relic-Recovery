package ftc8564TestOpMode;

/**
 * Created by margaretli on 2017/11/19.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


import static java.lang.Math.abs;

@TeleOp(name = "turkey", group = "Debug")
//@Disabled

public class turkey extends OpMode{
    /*
    FOR TURKEYBOTS
    (demo for outreach)
    */


    private ElapsedTime runtime = new ElapsedTime();

    /*object creation (will move to a hardware library)*/
    /*Drive Wheels (Motors)*/
    DcMotor motorleft;
    DcMotor motorright;
    /*intake motors*/
    DcMotor intakeleft;
    DcMotor intakeright;
    /*stopper*/
    Servo stopper;
    //turkey
    DcMotor turkey;
    /*
    * Special values definition:
    * reverseFactor: multiplying with this to reverse the directon of the motor
    * threshold: this is the common threshold for regular uses.
    * Warning: while using threshold, there must be a reset statement to set it to 0
    *
    */
    private int reverseFactor = -1;
    private double threshold = 0.1;
    private double slow = 1;
    private double turkeyon = 0;
    private double bck = 1;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");
        /* hardware mapping */
        /*Drive Wheels (Motors)*/
        motorleft = hardwareMap.dcMotor.get("left");
        motorright = hardwareMap.dcMotor.get("right");
        /*intake motors*/
        intakeleft = hardwareMap.dcMotor.get("intakeleft");
        intakeright = hardwareMap.dcMotor.get("intakeright");
        /*stopper*/
        stopper = hardwareMap.servo.get("stopper");
        motorleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turkey = hardwareMap.dcMotor.get("turkey");

    }

  /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        runtime.reset();
        //drive
        if (abs(gamepad1.left_stick_y) > threshold){
            motorleft.setPower(gamepad1.left_stick_y *reverseFactor * slow * bck);
        }
        else{
                motorleft.setPower(0);
        }
        if (abs(gamepad1.right_stick_y) > threshold){
            motorright.setPower(gamepad1.right_stick_y * slow * bck);
        }
        else {
            motorright.setPower(0);
        }

        if (gamepad1.b){
            slow = .5;
        }
        else if (gamepad1.a){
            slow = 1;
        }
        if (bck == 1 && gamepad1.y){
            bck = -1;
        }
        else if (bck == -1 && gamepad1.y){
            bck = 1;
        }
        if (abs(gamepad1.left_trigger) > 0.6 && abs(gamepad1.right_trigger) > 0.6){
            intakeleft.setPower(.8);
            intakeright.setPower(.8 * reverseFactor);
        }
        //ejaculate
        else if (abs(gamepad1.left_trigger) > 0.6) {
            intakeleft.setPower(.8 * reverseFactor);
            intakeright.setPower(0.8);
        }
        //swallow
        else if (abs(gamepad1.right_trigger) > 0.6) {
            intakeleft.setPower(0.8);
            intakeright.setPower(.8 * reverseFactor);
        }
        //left out right in
        else if (gamepad1.left_bumper){
            intakeleft.setPower(.8 * reverseFactor);
            intakeright.setPower(.8 * reverseFactor);
        }
        //left in right out
        else if (gamepad1.right_bumper){
            intakeleft.setPower(.8);
            intakeright.setPower(.8);
        }
        else {
            intakeleft.setPower(0);
            intakeright.setPower(0);
        }
        //stopper
        if (gamepad1.dpad_right){
            stopper.setPosition(.58);
        }
        else if (gamepad1.dpad_left){
            stopper.setPosition(.48);
        }
        else {
            stopper.setPosition(.5);
        }
        //TURKEY
        if (gamepad1.dpad_up){
            turkey.setPower(.25);
        }
        else if (gamepad1.dpad_down){
            turkey.setPower(-.25);
        }
        else {
            turkey.setPower(0);
        }

        telemetry.addData("Status", "Run Time: " + runtime.toString());




    }
}
