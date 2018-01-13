package ftc8564opMode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;

/**
 * Created by margaretli on 1/5/18.
 *
 * DEMO FOR SANTA BARBARA
 *
 * LITERALLY ONLY PUTS PRESSURE ON JEWEL ARM AND DRIVES
 */

@TeleOp(name = "Demo", group = "TeleOp")
public class DemoTeleOp extends OpMode {

    //for time and stuff
    private ElapsedTime runtime = new ElapsedTime();

    //object creation !!
    private DcMotor left;
    private DcMotor right;
    private CRServo crServo;
    private Servo colorservo;

    //variablesssss
    private double slow = 1;

    @Override
    public void init(){
        //mapping motors
        left = hardwareMap.dcMotor.get("left");
        right  = hardwareMap.dcMotor.get("right");
        crServo = hardwareMap.crservo.get("crServo");
        colorservo=hardwareMap.servo.get("colorServo");
    }
    @Override
    public void start() {
        crServo.setPower(.1);
        colorservo.setPosition(0.5);
    }

    @Override
    public void loop() {
        runtime.reset();

        //drivetrain
        if (abs(gamepad1.left_stick_y) > .2) {
            left.setPower(-gamepad1.left_stick_y * slow);
        }
        else {
            left.setPower(0);
        }
        if (abs(gamepad1.right_stick_y) > .2) {
            right.setPower(gamepad1.right_stick_y * slow);
        }
        else {
            right.setPower(0);
        }

        //slow mode toggle
        if (gamepad1.b) {
            slow = .5;
        } else if (gamepad1.a) {
            slow = 1;
        }

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();
    }
}
