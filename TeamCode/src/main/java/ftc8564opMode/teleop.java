package ftc8564opMode;

/**
 * Created by ACtheGreat on 2017/10/27.
 */
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;

@TeleOp(name = "teleop", group = "TeleOp")
//@Disabled

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
    DcMotor motorleft;
    DcMotor motorright;
    /*clamp motor*/
    Servo leftpivot;
    Servo rightpivot;
    Servo lefthug;
    Servo righthug;
    /*lift motors*/
    DcMotor lift;
    /*intake motors*/
    //DcMotor intakeleft;
    //DcMotor intakeright;
    /*stopper*/
    //Servo stopper;
    //relic arm:
    //DcMotor arm;
    //DcMotor armextension;
    //Servo gripperextension;
    //CRServo armgripper;
    //CRServo crServo;
    //Servo colorservo;

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
    private int liftPosition = 0;
    private boolean relicMode = false;
    private boolean grip = false;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");
        /* hardware mapping */
        /*Drive Wheels (Motors)*/
        motorleft = hardwareMap.dcMotor.get("left");
        motorright = hardwareMap.dcMotor.get("right");
        /*lift motors*/
         lift = hardwareMap.dcMotor.get("liftleft");
        /*intake motors*/
        //intakeleft = hardwareMap.dcMotor.get("intakeleft");
        //intakeright = hardwareMap.dcMotor.get("intakeright");
        //arm
        //arm = hardwareMap.dcMotor.get("arm");
        //arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        //crServo = hardwareMap.crservo.get("crServo");
        //colorservo=hardwareMap.servo.get("colorServo");
        leftpivot = hardwareMap.servo.get("leftpivot");
        rightpivot = hardwareMap.servo.get("rightpivot");
        lefthug = hardwareMap.servo.get("lefthug");
        righthug = hardwareMap.servo.get("righthug");


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

        /*clampleft.setPosition(20);*/
        /*clampright.setPosition(75);*/
        lefthug.setPosition(.6);
        righthug.setPosition(.4);
        leftpivot.setPosition(-.8);
        rightpivot.setPosition(.8);
        //stopper.setPosition(.5);
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
        //crServo.setPower(.1);
        //colorservo.setPosition(0.5);

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
        if (abs(gamepad1.left_stick_y) > threshold) {
            motorleft.setPower(gamepad1.left_stick_y * reverseFactor * slow);
        } else {
            motorleft.setPower(0);
        }
        if (abs(gamepad1.right_stick_y) > threshold) {
            motorright.setPower(gamepad1.right_stick_y * slow);
        } else {
            motorright.setPower(0);
        }

        if (gamepad1.b) {
            slow = .5;
        } else if (gamepad1.a) {
            slow = 1;
        }

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
                lift.setPower(gamepad2.right_stick_y * reverseFactor);
            } else {
                lift.setPower(0);
            }
        }
        /*
        else{
            if (abs(gamepad2.left_stick_y) > threshold){
                armextension.setPower(-gamepad2.left_stick_y);
            }
            else{
                armextension.setPower(0);
            }
            if (abs(gamepad2.right_stick_y) > threshold){
                arm.setPower(-gamepad2.right_stick_y * 0.3);
            }
            else{
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


        }
        */

        /*if ((liftPosition == 0) && (gamepad2.right_stick_y > .6)){
            lift.setTargetPosition(200);
            liftPosition = 1;
        }
        else if ((liftPosition == 1) && (gamepad2.right_stick_y > .6)){
            lift.setTargetPosition(400);
            liftPosition = 2;
        }
        else if ((liftPosition == 2) && (gamepad2.right_stick_y > .6)) {
            lift.setTargetPosition(600);
            liftPosition = 3;
        }
        else if ((liftPosition == 3) && (gamepad2.right_stick_y > .6)) {
            lift.setTargetPosition(800);
            liftPosition = 4;
        }
        if ((liftPosition == 1) && (gamepad2.right_stick_y < .4)){
            lift.setTargetPosition(0);
            liftPosition = 0;
        }
        else if ((liftPosition == 2) && (gamepad2.right_stick_y < .4)){
            lift.setTargetPosition(200);
            liftPosition = 1;
        }
        else if ((liftPosition == 3) && (gamepad2.right_stick_y < .4)) {
            lift.setTargetPosition(400);
            liftPosition = 2;
        }
        else if ((liftPosition == 4) && (gamepad2.right_stick_y < .4)) {
            lift.setTargetPosition(600);
            liftPosition = 3;
        }
        */
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
                grip = false;
            }
            //close
            else if (gamepad2.right_bumper) {
                lefthug.setPosition(.6);
                righthug.setPosition(.4);
                grip = false;
            }
            //grip
            if (gamepad2.right_trigger > .6) {
                lefthug.setPosition(.39);
                righthug.setPosition(.6);
                grip = true;

            }
            if (!grip) {
                if (gamepad2.left_stick_y > .2) {
                    leftpivot.setPosition(-.8);
                    rightpivot.setPosition(.8);
                }
                if (gamepad2.left_stick_y < -.2) {
                    leftpivot.setPosition(-.25);
                    rightpivot.setPosition(.25);
                }

            }
            else {
                if (gamepad2.left_stick_y > .2) {
                    leftpivot.setPosition(-.8);
                    rightpivot.setPosition(.8);
                }
                if (gamepad2.left_stick_y < -.2) {
                    leftpivot.setPosition(-.1);
                    rightpivot.setPosition(.1);
                }
        }
        /*
        else{
            if (gamepad2.left_bumper){
                armgripper.setPower(0.5);
            }
            if(gamepad2.right_bumper){
                armgripper.setPower(-0.5);
            }
            if(gamepad2.left_trigger > 0.6){
                gripperextension.setPosition(gripperextension.getPosition() + 0.05);
            }
            if (gamepad2.right_trigger >0.6){
                gripperextension.setPosition(gripperextension.getPosition() - 0.05);
            }

        }
        */
        /*
        * Intake - Intake portion.
        *
        * if both left and right bumper are both pushed above threshold -> both sides intake at 100%
        * if only left/right pushed -> corresponding side goes 100%, while the other side lowers
        * if nothing pushed -> stopped
        *
        * Player 1 controls: left trigger and Right Trigger
        */
/*
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
            intakeright.setPower(0 * reverseFactor);
        }
        */
        /*
        *
        * Intake -> slant cube adjustment
        * By using bumpers, the corresponded bumper side will go IN while the other side will go OUT
        *
        * Player 1 Control: Left bumper/Right Bumper: corresponding side rolls in while other goes
        * reverse
        */

            //stopper
        /*if (gamepad2.dpad_right){
            stopper.setPosition(.58);
        }
        else if (gamepad2.dpad_left){
            stopper.setPosition(.48);
        }
        else {
            stopper.setPosition(.5);
        }
        */

            //arm
            if (gamepad2.x) {
                if (relicMode) {
                    relicMode = false;
                } else {
                    relicMode = true;
                }

            }


            telemetry.addData("Status", "Run Time: " + runtime.toString());

        }
    }
}
