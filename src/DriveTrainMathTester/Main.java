package DriveTrainMathTester;

public class Main {
    private static Xbox xboxController=new Xbox();
    public static void main(String[] args)
    {
        test(0,0);
        test(1,1);
        test(-1,-1);
        test(1,0);
        test(0, 1);
        test(-1,0);
        test(0,-1);
        test(0.5,0.5);
        test(-0.5,0.5);

    }

    public static void test(double x, double y)
    {
        xboxController.setPos(x, y);
        printCurrent();
    }

    public static void printCurrent()
    {
        System.out.println("XBox Values: ("+xboxController.getX()+", "+xboxController.getY()+")");
        System.out.println("Drivetrain motors: ("+getDriveSpeed(0)+", "+getDriveSpeed(1)+")");
        System.out.println();
    }



    public static double getDriveSpeed(int side)
    {
        //scale times 1/Math.max(xBoxController.getX(), invert());
        if(xboxController.getX()>0 && side==1)
        {
            return invert()*scale();
        }
        return Math.copySign(xboxController.getX()*scale(), xboxController.getY());
    }

    public static double invert()
    {
        return Math.copySign(currentMaxDist()-Math.abs(xboxController.getX()), xboxController.getX());
    }

    //returns the value used to scale each value: distance from center times (maxDist divided by higher value in order to scale to 1:x ratio)
    //Always >=0
    public static double scale()
    {
        return dist() * (currentMaxDist() / higherXVal());
    }

    //Returns distance from (0, 0) of the controller
    public static double dist()
    {
        return Math.sqrt(Math.pow(xboxController.getX(), 2) + Math.pow(xboxController.getY(), 2));
    }

    public static double currentMaxDist()
    {
        return Math.sqrt(1-Math.pow(xboxController.getY(), 2));
    }

    public static double higherXVal()
    {
        return Math.max(Math.abs(xboxController.getX()), Math.abs(invert()));
    }
}