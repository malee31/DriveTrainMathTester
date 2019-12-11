package DriveTrainMathTester;

public class Main {
    private static int mode=1;
    private static Xbox xboxController=new Xbox();
    public static void main(String[] args)
    {
        for(int i=0; i<=360; i+=10)
        {
            double radAngle=Math.toRadians(i);
            double x=Math.cos(radAngle);
            double y=Math.sin(radAngle);
            System.out.print(i + "degrees: ");
            test(x, y);
        }
    }

    private static double findComponent(double length, double component1)
    {
        return Math.sqrt(Math.pow(length, 2) - Math.pow(component1, 2));
    }

    private static void test(double x, double y)
    {
        xboxController.setPos(x, y);
        printCurrent();
    }

    private static void printCurrent()
    {
        System.out.println("XBox Values: ("+xboxController.getX()+", "+xboxController.getY()+")");
        System.out.println("Drivetrain motors: ("+getDriveSpeed(0)+", "+getDriveSpeed(1)+")");
        System.out.println();
    }

    private static double getDriveSpeed(int side)
    {
        switch(mode)
        {
            case 0: //v1_Fail
                //scale times 1/Math.max(xBoxController.getX(), invert());
                if (xboxController.getX() < 0 && side == 0) {
                    return invert0() * scale0();
                }
                return Math.copySign(xboxController.getX() * scale0(), xboxController.getY());
            case 1:
                //gets quadrant joystick is currently in (1, 2, 3, 4)
                int quadrant=getQuadrant();
                //reference angle
                double refAngle=getRefAngle();
                //getting default result for right side quadrant 1
                double result=refAngle / (Math.PI / 2) - 1; //Gives range of -1 to 1 CCW
                if(side == 1 && (quadrant == 2 || quadrant == 4))
                {
                    result = 1;
                }
                return Math.copySign(dist() * result, xboxController.getY()); //scale end result
            default:
                System.out.println("Invalid drive mode");
                return 0;
        }
    }

    //gets current quadrant of joystick
    private static int getQuadrant()
    {
        if(xboxController.getX() >= 0)
        {
            if(xboxController.getY() > 0)
            {
                return 1;
            }
            return 4;
        }
        else if(xboxController.getY() > 0)
        {
            return 2;
        }
        return 3;
    }

    //returns reference angle of Joystick from 0 to Pi/2
    private static double getRefAngle()
    {
        return Math.abs(Math.atan(xboxController.getY() / xboxController.getX()));
    }

    //Returns distance from (0, 0) of the controller. Used to scale end values
    private static double dist()
    {
        return Math.sqrt(Math.pow(xboxController.getX(), 2) + Math.pow(xboxController.getY(), 2));
    }



    //dumped from v1_Fail
    private static double invert0()
    {
        return Math.copySign(currentMaxDist()-Math.abs(xboxController.getX()), xboxController.getX());
    }

    //returns the value used to scale each value: distance from center times (maxDist divided by higher value in order to scale to 1:x ratio)
    //Always >=0
    private static double scale0()
    {
        return dist() * (currentMaxDist() / higherXVal0());
    }

    private static double currentMaxDist()
    {
        return Math.sqrt(1-Math.pow(xboxController.getY(), 2));
    }

    private static double higherXVal0()
    {
        return Math.max(Math.abs(xboxController.getX()), Math.abs(invert0()));
    }
}