package DriveTrainMathTester;

public class Main {
    private static Xbox xboxController = new Xbox();
    public static void main(String[] args)
    {
        test();
        debug();
    }

    private static void test()
    {
        for(int i=0; i<=360; i+=45)
        {
            System.out.print(i + " degrees: ");
            xboxController.setPos(Math.cos(Math.toRadians(i)), Math.sin(Math.toRadians(i)));
            printCurrent();
        }
    }

    private static void debug()
    {
        System.out.println("Direction: Clockwise");
        for(int side = 0; side < 2; side++)
        {
            System.out.print((side == 0 ? "Left side: " : "Right side: "));
            for(int i = 0; i <= 360; i += 10)
            {
                xboxController.setPos(Math.cos(Math.toRadians(i)), Math.sin(Math.toRadians(i)));
                System.out.print("(" + Math.toRadians(i) + ", " + getDriveSpeed(side) + ")");
            }
            System.out.println();
        }
        currentlyUp = true;
        System.out.println("\nDirection: Counter Clockwise");
        for(int side = 0; side < 2; side++)
        {
            System.out.print((side == 0 ? "Left side: " : "Right side: "));
            for(int i = 360; i >= 0; i -= 10)
            {
                xboxController.setPos(Math.cos(Math.toRadians(i)), Math.sin(Math.toRadians(i)));
                System.out.print("(" + Math.toRadians(i)+", " + getDriveSpeed(side) + ")");
            }
            System.out.println();
        }
    }

    private static void printCurrent()
    {
        System.out.println("XBox Values: ("+xboxController.getX()+", "+xboxController.getY()+")");
        System.out.println("Drivetrain motors: ("+getDriveSpeed(0)+", "+getDriveSpeed(1)+")");
        System.out.println();
    }

    private static boolean currentlyUp = true;
    private static double getDriveSpeed(int side)
    {
        int quadrant = getQuadrant();
        //insert 180deg mark if statements
        if(Math.abs(xboxController.getY()) <= Math.sin(Math.toRadians(7.5)))
        {
            return dist() * (currentlyUp ? 1 : -1);
        }

        //Quick answers for max throttle and invalid parameters
        if((side == 0 && (quadrant == 1 || quadrant == 4)) || (side == 1 && (quadrant == 2 || quadrant == 3)))
        {
            //returns scaled +1 or -1
            return dist() * Math.copySign(1, xboxController.getY());
        }
        else if(side != 0 && side != 1)
        {
            System.out.println("Invalid port/side number");
            return 0;
        }

        //convert reference angle from either 0->90deg or -90->0deg to all 0->90deg
        double refAngle = Math.abs((getRefAngle() + 90) % 90);

        //default right side value(quadrants 1 and 4)
        double result = refAngle / (Math.PI / 4) - 1; //Gives range of -1 to 1 CCW

        //Left side (quadrants 2 and 3)
        if((side == 0 && quadrant != 2)|| quadrant == 4)
        {
            result *= -1;
        }

        //sets variable on whether up is the direction it is going
        currentlyUp = (quadrant == 1 || quadrant == 2);

        //finalize and scale based on joystick distance from center
        return finalRound(dist() * result); //scale end result. Rounding is just for my own sanity
    }

    //gets current quadrant of joystick quadrants don't include the shared minimum border, only the maximum. Example: Q1 => (0, 90]
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

    private static double finalRound(double roundee)
    {
        double threshold=0.0001;
        if(1-Math.abs(roundee)<threshold)
        {
            return Math.copySign(1, roundee);
        }
        else if(Math.abs(roundee)<threshold)
        {
        return Math.copySign(0, roundee);
        }
        return roundee;
    }
}