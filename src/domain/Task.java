package domain;

/**
 * 存储电梯要执行的任务
 *
 * 电梯外:14楼,上
 * 电梯内:在14楼,去15楼
 */
public class Task
{
    private int upDown;
    private int from;//注意from内部是从0开始计数的
    private int to;//如果是电梯外,暂时还没有to,所以外面按,默认为-1

    public int getUpDown()
    {
        return upDown;
    }

    public void setUpDown(int upDown)
    {
        this.upDown = upDown;
    }

    public int getFrom()
    {
        return from;
    }

    public void setFrom(int from)
    {
        this.from = from;
    }

    public int getTo()
    {
        return to;
    }

    public void setTo(int to)
    {
        this.to = to;
    }

    @Override
    public String toString()
    {
        return "Task{" +
                "upDown=" + upDown +
                ", from=" + from +
                ", to=" + to +
                '}';
    }

    public Task(int upDown, int from, int to)
    {
        this.upDown = upDown;
        this.from = from;
        this.to = to;
    }

    public Task()
    {
    }
}
