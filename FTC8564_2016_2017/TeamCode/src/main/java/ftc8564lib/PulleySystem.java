package ftc8564lib;



public class PulleySystem implements PIDControl.PidInput{

    public PulleySystem() throws InterruptedException {

    }

    @Override
    public double getInput(PIDControl pidCtrl) {
        return 0;
    }
}
