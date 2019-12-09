package DriveTrainMathTester;

public class Xbox
{
    private double[] pos=new double[]{0, 0};
    public Xbox(){}
    public void setPos(double x, double y)
    {
        pos[0] = x;
        pos[1] = y;
    }

    public double getX()
    {
        return pos[0];
    }

    public double getY()
    {
        return pos[1];
    }
}