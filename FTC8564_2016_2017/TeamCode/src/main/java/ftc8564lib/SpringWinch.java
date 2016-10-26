package ftc8564lib;


public class SpringWinch implements PIDControl.PidInput{

    public SpringWinch() throws InterruptedException {

    }

    public double getInput(PIDControl pidCtrl) {
        return 0;
    }
}
