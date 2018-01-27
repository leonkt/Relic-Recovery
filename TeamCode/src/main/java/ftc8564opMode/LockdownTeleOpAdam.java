package ftc8564opMode;

/**
 * Created by margaretli on 12/22/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import ftc8564lib.Robot;

import static java.lang.Math.abs;

@TeleOp(name = "LockdownTeleOpAdam", group = "TeleOp")
@Disabled

public class LockdownTeleOpAdam extends LinearOpMode {

    private Robot robot;
    private boolean relicMode = false;
    private boolean invertDrive = true;

    public void runOpMode() throws InterruptedException {
        robot = new Robot(this, false);
        robot.driveBase.noEncoders();
        robot.jewelArm.resetServo();
        robot.driveBase.slowSpeed(false);
        waitForStart();
        while (opModeIsActive()) {
            //drive train
            if (gamepad1.x) {
                robot.driveBase.slowSpeed(true);
            }
            else if (gamepad1.y) {
                robot.driveBase.slowSpeed(false);
            }
            if (gamepad1.start && invertDrive){
                invertDrive = false;
            }
            else if (gamepad1.start && !invertDrive){
                invertDrive = true;
            }
            if (!invertDrive){
                robot.driveBase.tankDrive(gamepad1.right_stick_y, gamepad1.left_stick_y);
            }
            else if (invertDrive){
                robot.driveBase.tankDrive(-gamepad1.right_stick_y, -gamepad1.left_stick_y);
            }

            //intake
            if (gamepad1.right_bumper){
                robot.intake.out();
            }
            else if (gamepad1.left_bumper){
                robot.intake.in(.8);
            }
            else{
                robot.intake.stop();
            }
            //relic mode toggle
            if (!relicMode && gamepad2.x){
                relicMode = true;
            }
            else {
                relicMode = false;
            }
            //relic arm
            if (relicMode) {
                robot.relicArm.armExtension(-gamepad2.left_stick_y);
                if (abs(gamepad2.right_stick_y) > .2) {
                    robot.relicArm.moveArm(-gamepad2.right_stick_y);
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
            else {
                //lift
                robot.lift.control(-gamepad2.right_stick_y);
            }

        }

    }
}
