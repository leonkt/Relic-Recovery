package ftc8564opMode;

/**
 * Created by margaretli on 12/22/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;
import ftclib.*;
import ftc8564lib.*;
import hallib.HalUtil;

@TeleOp(name = "LockdownTeleOp", group = "TeleOp")
@Disabled

public class LockdownTeleOp extends LinearOpMode {

    private Robot robot;
    private boolean relicMode;

    public void runOpMode() throws InterruptedException {
        robot = new Robot(this, false);
        robot.driveBase.noEncoders();
        robot.clamps.openboth();
        robot.jewelArm.resetServo();
        waitForStart();
        while (opModeIsActive()) {
            //drive train
            if (gamepad1.b) {
                robot.driveBase.slowSpeed(true);
            } else if (gamepad1.a) {
                robot.driveBase.slowSpeed(false);
            }
            robot.driveBase.tankDrive(gamepad1.right_stick_y, gamepad1.left_stick_y);
            //lift

            //intake
            if (abs(gamepad1.left_trigger) > 0.6) {
                robot.intake.out();
            } else if (abs(gamepad1.right_trigger) > 0.6) {
                robot.intake.in();
            } else if (gamepad1.left_bumper) {
                robot.intake.rightin();
            } else if (gamepad1.right_bumper) {
                robot.intake.leftin();
            } else {
                robot.intake.stop();
            }
            //relic arm
            if (relicMode) {
                if (abs(gamepad2.left_stick_y) > .6) {
                    robot.relicArm.armExtension(-gamepad2.left_stick_y);
                }else
                    robot.relicArm.armExtension(0);
                if (abs(gamepad2.right_stick_y) > .6) {
                    robot.relicArm.moveArm(-gamepad2.right_stick_y);
                } else {
                    robot.relicArm.pressure();
                }
                if (gamepad2.left_bumper) {
                    robot.relicArm.open();
                }
                if (gamepad2.right_bumper) {
                    robot.relicArm.close();
                }
                if (gamepad2.left_trigger > 0.6) {
                    robot.relicArm.extend();
                }
                if (gamepad2.right_trigger > 0.6) {
                    robot.relicArm.retract();
                }

            }

        }

    }
}
