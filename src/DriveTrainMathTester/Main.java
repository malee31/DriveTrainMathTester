package DriveTrainMathTester;

public class Main {
    private static int mode=0;
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
        test(0.5,0.35);
        test(-0.35,0.5);

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
        switch(mode)
        {
            case 0:
                //scale times 1/Math.max(xBoxController.getX(), invert());
                if (xboxController.getX() < 0 && side == 0) {
                    return invert0() * scale0();
                }
                return Math.copySign(xboxController.getX() * scale0(), xboxController.getY());
            default:
                System.out.println("Invalid speed mode");
                return 0;
        }
    }

    public static double invert0()
    {
        return Math.copySign(currentMaxDist()-Math.abs(xboxController.getX()), xboxController.getX());
    }

    //returns the value used to scale each value: distance from center times (maxDist divided by higher value in order to scale to 1:x ratio)
    //Always >=0
    public static double scale0()

    {
        return dist() * (currentMaxDist() / higherXVal0());
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

    public static double higherXVal0()
    {
        return Math.max(Math.abs(xboxController.getX()), Math.abs(invert0()));
    }
}