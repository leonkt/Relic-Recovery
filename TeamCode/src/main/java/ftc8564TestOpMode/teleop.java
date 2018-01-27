package ftc8564TestOpMode;

/**
 * Created by ACtheGreat on 2017/10/27.
 */
import android.text.method.Touch;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import hallib.HalUtil;
import static java.lang.Math.abs;

@TeleOp(name = "teleop", group = "TeleOp")
@Disabled

public class teleop extends OpMode {
    /**
     * TeleopTest.java is created by A.C.G. for team LockDown #8564.
     * (c) Copyright 20xx CA Works. All Rights reserved.
     * <p>
     * This is a debug teleop program which introduces the basic movement of the robot.
     * Later the definitions will be moved to a hardware class.
     */


    private ElapsedTime runtime = new ElapsedTime();

    /*object creation (will move to a hardware library)*/
    /*Drive Wheels (Motors)*/
    private DcMotor motorleft;
    private DcMotor motorright;
    /*clamp motor*/
    Servo winch;
    Servo claw;
    Servo lefthug;
    Servo righthug;
    Servo longer;
    /*lift motors*/
    DcMotor lift;
    /*intake motors*/
    DcMotor intakeleft;
    DcMotor intakeright;
    /*stopper*/
    DcMotor arm;
    DcMotor binch;
    TouchSensor touchme;
    //relic arm:
    //DcMotor armextension;
    //Servo gripperextension;
    //CRServo armgripper;
    CRServo crServo;
    Servo colorservo;
    DcMotor kicker;
    ColorSensor colorSensor;

    /*
    * Special values definition:
    * reverseFactor: multiplying with this to reverse the directon of the motor
    * threshold: this is the common threshold for regular uses.
    * Warning: while using threshold, there must be a reset statement to set it to 0
    *
    */
    private double threshold = 0.3;
    private double slow = 1;
    private int liftPosition = 0;
    private boolean relicMode = false;
    private boolean betaMode = false;


    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");
        /* hardware mapping */
        /*Drive Wheels (Motors)*/
        motorleft = hardwareMap.dcMotor.get("left");
        motorright = hardwareMap.dcMotor.get("right");
        touchme =  hardwareMap.touchSensor.get("touchme");
        /*lift motors*/
         lift = hardwareMap.dcMotor.get("liftleft");
         lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        /*intake motors*/
        intakeleft = hardwareMap.dcMotor.get("intakeleft");
        intakeright = hardwareMap.dcMotor.get("intakeright");
        //arm
        arm = hardwareMap.dcMotor.get("arm");
        binch = hardwareMap.dcMotor.get("binch");
        longer = hardwareMap.servo.get("longer");
        claw = hardwareMap.servo.get("claw");
        //armextension = hardwareMap.dcMotor.get("armextension");
        /*stopper*/
        //stopper = hardwareMap.servo.get("stopper");
        motorleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //armextension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //relic arm
        //gripperextension = hardwareMap.servo.get("gripperextension");
        //armgripper = hardwareMap.crservo.get("armgripper");
        crServo = hardwareMap.crservo.get("crServo");
        colorservo=hardwareMap.servo.get("colorServo");
        winch = hardwareMap.servo.get("winch");
        lefthug = hardwareMap.servo.get("lefthug");
        righthug = hardwareMap.servo.get("righthug");
        kicker = hardwareMap.dcMotor.get("kicker");
        //kicker.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        colorSensor = hardwareMap.colorSensor.get("colorSensor");


    }

  /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    @Override
    public void init_loop() {

    }

    /*
     * This method will be called ONCE when start is pressed
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void start() {
        lefthug.setPosition(.6);
        righthug.setPosition(.4);
        longer.setPosition(.8);
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
        crServo.setPower(.7);
        colorservo.setPosition(0.5);

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

        if (!betaMode) {
            if (abs(gamepad1.left_stick_y) > threshold) {
                motorleft.setPower(-gamepad1.left_stick_y * slow);
            } else {
                motorleft.setPower(0);
            }
            if (abs(gamepad1.right_stick_y) > threshold) {
                motorright.setPower(gamepad1.right_stick_y * slow);
            } else {
                motorright.setPower(0);
            }


            if (gamepad1.b) {
                slow = .7;
            } else if (gamepad1.a) {
                slow = 1;
            }
        }
        else{

            if(gamepad1.left_stick_y>threshold && gamepad1.left_stick_y<threshold){
                motorleft.setPower(-gamepad1.left_stick_y *slow);
            }
            else if (abs(gamepad1.left_stick_y) > threshold) {
                motorleft.setPower(gamepad1.left_stick_y * slow);
            }
            else {
                motorleft.setPower(0);
            }

            if (gamepad1.left_stick_y<threshold && gamepad1.left_stick_y>threshold){
                motorright.setPower(gamepad1.right_stick_y*slow);
            }
            else if (abs(gamepad1.right_stick_y) > threshold) {
                motorright.setPower(-gamepad1.right_stick_y * slow);
            }
            else {
                motorright.setPower(0);
            }


            if (gamepad1.b) {
                slow = .7;
            } else if (gamepad1.a) {
                slow = 1;
            }
        }

        //Beta: reversed DPad Control / intake kicker




        /* Lifting
        *
        *  Additional movements (in case one motor overspun, etc will be added soon.
        *
        *  WARNING: fragile lift.
        *  Clamp system coming soon.
        *
        * Player 2 controls: Right Joystick up/down -> lift up/down
        *
        */
        if (!relicMode) {
            if ((abs(gamepad2.right_stick_y)) > threshold) {
                lift.setPower(-gamepad2.right_stick_y);
            } else {
                lift.setPower(0);
            }
        }
        else{
            if ((abs(gamepad2.left_stick_y)) > threshold) {
                arm.setPower(gamepad2.left_stick_y * .25);
            } else {
                arm.setPower(0);
            }
            if ((abs(gamepad2.right_stick_y)) > threshold) {
                binch.setPower(-gamepad2.right_stick_y);
            } else {
                binch.setPower(0);
            }
        }


        /* Grips
        * If right bumper is pressed, the grips will open
        * if left bumper is pressed, the grips will close
        * if right trigger is pressed, the grips will tighten to pick up the block
         */
        //open
        if (!relicMode) {
            if (gamepad2.left_bumper) {
                lefthug.setPosition(1);
                righthug.setPosition(0);
            }
            //close
            if (gamepad2.right_bumper) {
                lefthug.setPosition(.6);
                righthug.setPosition(.4);
            }
            //grip
            if (gamepad2.right_trigger > .6) {
                lefthug.setPosition(.29);
                righthug.setPosition(.7);
            }
            /*
            if (gamepad2.left_stick_y > .2) {
                winch.setPower(1);
            }
            else if (gamepad2.left_stick_y < -.2) {
               winch.setPower(-1);
            }
            else {
                winch.setPower(0);
            }
            */
            if (!touchme.isPressed() && gamepad2.left_stick_y > .2 ) {
                winch.setPosition(1);
            }
            else if (gamepad2.left_stick_y < -.2) {
                winch.setPosition(0);
            }
            else {
                winch.setPosition(.5);
            }
        }
        else{
            if(abs(gamepad2.left_trigger) > 0.1){
                longer.setPosition(.8);
            }
            if (abs(gamepad2.right_trigger) > 0.1) {
                longer.setPosition(0);
            }
            if (gamepad2.left_bumper){
                claw.setPosition(0);
            }
            if(gamepad2.right_bumper){
                claw.setPosition(.7);
            }
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
            //ejaculate
        if (abs(gamepad1.left_trigger) > 0.6) {
            intakeleft.setPower(-.8);
            intakeright.setPower(.8);
        }
        //swallow
        else if (abs(gamepad1.right_trigger) > 0.6) {
            intakeleft.setPower(0.8);
            intakeright.setPower(-.8);
        }
        //left fast right slow
        else if (gamepad1.left_bumper) {
            intakeleft.setPower(.8);
            intakeright.setPower(-.4);
        }
        //right fast left slow
        else if (gamepad1.right_bumper) {
            intakeleft.setPower(.4);
            intakeright.setPower(-.8);
        }
        else {
            intakeleft.setPower(0);
            intakeright.setPower(0);
        }

        if (gamepad1.x){
            kicker.setPower(0.2);
        }
        else if (gamepad1.y){
            kicker.setPower(-0.4);
        }
        /*
        else{
            kicker.setPower(0);
        }
        */






            //arm
        if (gamepad2.x) {

            relicMode = true;
            /*
            if (relicMode) {
                relicMode = false;
            }
            else {
                relicMode = true;
            }
            HalUtil.sleep(100);
            */
        }
        if (gamepad2.y){
            relicMode = false;
        }

        //betaMode
        if (gamepad1.right_stick_button){
            betaMode = true;
            /*
            if (betaMode) {
                betaMode = false;
            }
            else {
                betaMode = true;
            }
            HalUtil.sleep(100);
            */
            kicker.setPower(0);
        }
        if (gamepad1.left_stick_button){
            betaMode = false;
            kicker.setPower(0);
        }

        colorSensor.enableLed(false);



        telemetry.addData("Status", "touch " + touchme.isPressed()  );
        telemetry.addData("Debug","relicMode: " +relicMode);
        telemetry.addData("Debug","kicker value: " +kicker.getPower());
        telemetry.addData("Debug","betaMode: "+betaMode);

        telemetry.update();

        }
    }
