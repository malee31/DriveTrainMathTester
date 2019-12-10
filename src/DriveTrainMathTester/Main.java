package DriveTrainMathTester;

public class Main {
    private static int mode=1;
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
                //gives every first quadrant result
                double refAngle=Math.atan(Math.abs(xboxController.getY())/Math.abs(xboxController.getX()));
                double result=dist();
                if(side == 1)
                {
                    result *= (4 * refAngle / Math.PI - 1);
                }
                int quadrant=getQuadrant();
                if(quadrant == 2 || quadrant == 3)
                {
                    if(side == 1)
                    {
                        result=dist();
                    }
                    else
                    {
                        result *= (4 * refAngle / Math.PI - 1);
                    }
                }
                if(quadrant == 3 || quadrant == 4)
                {
                    result *= -1;
                }
                return result;

                /* Code drafting. Refactored above.
                double preProcessLeft= dist();
                double preProcessRight= dist() * (4 * refAngle / Math.PI - 1);
                int quadrant=getQuadrant();
                if(quadrant == 2 || quadrant == 3)
                {
                    double temp=preProcessLeft;
                    preProcessLeft=preProcessRight;
                    preProcessRight=temp;
                }
                if(quadrant == 3 || quadrant == 4)
                {
                    preProcessLeft *= -1;
                    preProcessRight *= -1;
                }*/
            default:
                System.out.println("Invalid speed mode");
                return 0;
        }
    }

    //gets quadrant of joystick
    private static int getQuadrant()
    {
        int quad;
        if(xboxController.getX()>=0)
        {
            quad=1; //or 4
        }
        else
        {
            quad=2; //or 3
        }
        if(xboxController.getY()<=0)
        {
            if(quad==1)
            {
                return 4;
            }
            return 3;
        }
        return quad;
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