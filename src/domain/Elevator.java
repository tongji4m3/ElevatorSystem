package domain;

/**
 * 电梯实体类
 * 一部电梯一个实体
 */
public class Elevator implements Runnable
{
    private int elevatorId;//第几个电梯
    private int location;//当前位置
    private int upDown;//处于UP或者DOWN或者STOP的状态

    public int getElevatorId()
    {
        return elevatorId;
    }

    public void setElevatorId(int elevatorId)
    {
        this.elevatorId = elevatorId;
    }

    public int getLocation()
    {
        return location;
    }

    public void setLocation(int location)
    {
        this.location = location;
    }

    public int getUpDown()
    {
        return upDown;
    }

    public void setUpDown(int upDown)
    {
        this.upDown = upDown;
    }

    public Elevator(int elevatorId)
    {
        this.elevatorId = elevatorId;
        this.location=0;//第一层楼,但是为了符合其他的,从0开始算,只是在显示时,才显示+1的
        this.upDown= Const.STOP;//开始为静止状态
    }

    @Override
    public void run()
    {

    }
}
