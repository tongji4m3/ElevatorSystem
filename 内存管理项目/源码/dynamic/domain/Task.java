package dynamic.domain;

import java.util.LinkedList;
import java.util.List;

public class Task
{
    private String name;//作业名字
    private boolean isFree;//是否是释放 true释放,false申请
    private int size;//申请或者释放的大小

    @Override
    public String toString()
    {
        String operator = isFree ? "释放" : "申请";
        return name + "    " + operator + "    " + size + "K";
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isFree()
    {
        return isFree;
    }

    public void setFree(boolean free)
    {
        isFree = free;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public Task(String name, boolean isFree, int size)
    {
        this.name = name;
        this.isFree = isFree;
        this.size = size;
    }

    //具体的请求序列
    public static List<Task> getTask()
    {
        List<Task> list = new LinkedList<>();
        list.add(new Task("作业1", false, 130));
        list.add(new Task("作业2", false, 60));
        list.add(new Task("作业3", false, 100));
        list.add(new Task("作业2", true, 60));
        list.add(new Task("作业4", false, 200));
        list.add(new Task("作业3", true, 100));
        list.add(new Task("作业1", true, 130));
        list.add(new Task("作业5", false, 140));
        list.add(new Task("作业6", false, 60));
        list.add(new Task("作业7", false, 50));
        list.add(new Task("作业6", true, 60));
        return list;
    }
}
