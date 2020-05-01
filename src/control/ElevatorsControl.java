package control;

import domain.Elevator;
import domain.Task;
import domain.Const;

import java.util.LinkedList;
import java.util.List;

/**
 * 实际的控制类
 */
public class ElevatorsControl implements Runnable
{
    private Elevator[] elevators=null;

    public Elevator[] getElevators()
    {
        return elevators;
    }

    private List<Task> tasks = new LinkedList<>();

    public List<Task> getTasks()
    {
        return tasks;
    }

    public Elevator getElevators(int i)
    {
        return elevators[i];
    }

    //初始化生成5个电梯
    public ElevatorsControl()
    {
        elevators=new Elevator[Const.ELEVATORS_COUNT];
        for (int i = 0; i < Const.ELEVATORS_COUNT; i++)
        {
            elevators[i] = new Elevator(i);
        }
    }

    public void addTask(Task task)
    {
        tasks.add(task);
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            int runElevators=Math.min(tasks.size(),Const.ELEVATORS_COUNT);
            for (int i = 0; i < runElevators; i++)
            {
                if(elevators[i].getLocation()==Const.FLOOR-1)
                {
                    elevators[i].setUpDown(Const.DOWN);
                }
                else if(elevators[i].getLocation()==0)
                {
                    elevators[i].setUpDown(Const.UP);
                }

                if(elevators[i].getUpDown()==Const.UP)
                    elevators[i].setLocation(elevators[i].getLocation()+1);
                else if(elevators[i].getUpDown()==Const.DOWN)
                    elevators[i].setLocation(elevators[i].getLocation()-1);
            }
        }
    }
}
