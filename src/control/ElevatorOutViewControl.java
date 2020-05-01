package control;

import domain.Task;
import domain.Const;
import view.ElevatorOutView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElevatorOutViewControl implements Runnable
{
    ElevatorsControl control=null;
    ElevatorOutView[][] elevatorOutViews = new ElevatorOutView[Const.FLOOR][Const.ELEVATORS_COUNT];

    public ElevatorOutView[][] getElevatorOutViews()
    {
        return elevatorOutViews;
    }

    public ElevatorOutViewControl(ElevatorsControl control)
    {
        this.control = control;

        for (int i = 0; i < Const.FLOOR; i++)
        {
            for (int j = 0; j < Const.ELEVATORS_COUNT; j++)
            {
                elevatorOutViews[i][j] = new ElevatorOutView(i, j, control);
            }
        }

        //不能用同一个for循环,因为要实现互联结操作,如果是上面的,可能会操作到还没初始化的
        for (int i = 0; i < Const.FLOOR; i++)
        {
            for (int j = 0; j < Const.ELEVATORS_COUNT; j++)
            {
                //添加事件监听 temp只是为了简化书写
                ElevatorOutView temp = elevatorOutViews[i][j];
                temp.getUpButton().addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        Task task = new Task(Const.UP, temp.getFloorHeight(), -1);
                        control.addTask(task);
                        //同一层的UP都应该亮了
                        for (int k = 0; k < Const.ELEVATORS_COUNT; k++)
                        {
                            elevatorOutViews[temp.getFloorHeight()][k].getUpButton().setBackground(Color.red);
                        }
                    }
                });

                temp.getDownButton().addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        Task task = new Task(Const.DOWN, temp.getFloorHeight(), -1);
                        control.addTask(task);
                        //同一层的down都应该亮了
                        for (int k = 0; k < Const.ELEVATORS_COUNT; k++)
                        {
                            elevatorOutViews[temp.getFloorHeight()][k].getDownButton().setBackground(Color.red);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                //电梯运动时,控制刷新界面
                Thread.sleep(100);
                for (int i = 0; i < Const.FLOOR; i++)
                {
                    for (int j = 0; j < Const.ELEVATORS_COUNT; j++)
                    {
                        ElevatorOutView temp = elevatorOutViews[i][j];
                        temp.getFloor().setText(""+(control.getElevators(temp.getIndex()).getLocation()+1));

                        if(control.getElevators(temp.getIndex()).getUpDown()== Const.DOWN) temp.getUpDownButton().setText("∇");//指示电梯在上升还是下降
                        else if(control.getElevators(temp.getIndex()).getUpDown()== Const.UP) temp.getUpDownButton().setText("∆");
                        else temp.getUpDownButton().setText("∎ ");//设置为静止状态

                        //如果有任意一个电梯到达了该楼层,并且方向和该按钮的方向一样,那么按钮就变回原本的颜色
                        //并且在task中,取消该任务
                        for (int k = 0; k < Const.ELEVATORS_COUNT; k++)
                        {
                            if(control.getElevators(k).getLocation()==temp.getFloorHeight())
                            {
                                //将button改变,并且移除已经失效的任务
                                if(control.getElevators(k).getUpDown()==Const.UP)
                                {
                                    temp.getUpButton().setBackground(Color.cyan);
                                    control.getTasks().removeIf(task -> task.getUpDown() == Const.UP && task.getFrom() == temp.getFloorHeight());
                                }
                                if(control.getElevators(k).getUpDown()==Const.DOWN)
                                {
                                    temp.getDownButton().setBackground(Color.cyan);
                                    control.getTasks().removeIf(task -> task.getUpDown() == Const.DOWN && task.getFrom() == temp.getFloorHeight());
                                }

                            }
                        }
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
