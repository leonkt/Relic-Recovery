package ftc8564opMode;

/**
 * Created by ACtheGreat on 2017/10/27.
 */
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
/*import com.qualcomm.robotcore.eventloop.opmode.Disabled;
 */
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;

@TeleOp(name = "Debug: TeleOp", group = "TeleOp")
/*@Disabled*/

public class TeleopTest extends OpMode{

    private ElapsedTime runtime = new ElapsedTime();
    DcMotor motorleft;
    DcMotor motorright;

    Servo clampleft;
    Servo clampright;

    DcMotor liftleft;
    DcMotor liftright;

    DcMotor intakeleft;
    DcMotor intakeright;
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        motorleft = hardwareMap.dcMotor.get("left");
        motorright = hardwareMap.dcMotor.get("right");


        clampleft = hardwareMap.servo.get("clampleft");
        clampright = hardwareMap.servo.get("clampright");

        liftleft = hardwareMap.dcMotor.get("liftleft");
        liftright = hardwareMap.dcMotor.get("liftright");

        intakeleft = hardwareMap.dcMotor.get("intakeleft");
        intakeright = hardwareMap.dcMotor.get("intakeright");


    }

  /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */

    @Override
    public void init_loop() {
        clampleft.setPosition(0);
        /*clampright.setPosition(90);*/

    }

    /*
     * This method will be called ONCE when start is pressed
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void start() {

        clampleft.setPosition(30);
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

        if (abs(gamepad1.left_stick_y) > 0.1){
            motorleft.setPower(gamepad1.left_stick_y *-1);
        }
        else{
                motorleft.setPower(0 );
        }
        if (abs(gamepad1.right_stick_y) > 0.1){
            motorright.setPower(gamepad1.right_stick_y );
        }
        else {
            motorright.setPower(0);
        }



        if (abs(gamepad2.right_stick_y) > 0.1){
            liftleft.setPower(gamepad2.right_stick_y * -1);
            liftright.setPower(gamepad2.right_stick_y);
        }


        if (abs(gamepad1.left_trigger) > 0.6 && abs(gamepad1.right_trigger) > 0.6){
            intakeleft.setPower(1 );
            intakeright.setPower(1*-1);
        }
        else if (abs(gamepad1.left_trigger) > 0.6) {
            intakeleft.setPower(1);
            intakeright.setPower(0.6 * -1);
        }
        else if (abs(gamepad1.right_trigger) > 0.6) {
            intakeleft.setPower(0.6);
            intakeright.setPower(1 * -1);
        }
        else {
            intakeleft.setPower(0);
            intakeright.setPower(0 * -1);
        }

        if (gamepad1.left_bumper){
            intakeleft.setPower(1);
            intakeright.setPower(-1*-1);
        }

        if (gamepad1.right_bumper){
            intakeleft.setPower(-1);
            intakeright.setPower(1*-1);
        }



        telemetry.addData("Status", "Run Time: " + runtime.toString());
    }
}