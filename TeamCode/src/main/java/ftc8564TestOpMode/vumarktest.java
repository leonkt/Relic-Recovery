package ftc8564TestOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

import ftc8564lib.Robot;
import ftclib.FtcMenu;

/**
 * Created by margaretli on 11/20/17.
 */
@Autonomous(name="vumarktest", group="TestSamples")
public class vumarktest extends LinearOpMode{
    Robot robot;

    private ElapsedTime mClock = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this, true);// need this to run menu test
        waitForStart();
        while (opModeIsActive()) {

            robot.VuMark.activate();
            robot.VuMark.decodePictograph();
            telemetry.addData("VuMark: ", robot.VuMark.getCryptoboxKey());
            telemetry.update();
            robot.VuMark.deactivate();
        }
    }
}