package view;

import control.ElevatorOutViewControl;
import control.ElevatorsControl;
import domain.Const;

import javax.swing.*;
import java.awt.*;

/**
 * 总的视图展示
 */
public class View extends JFrame
{
    public View(ElevatorsControl control)
    {
        //创建包含5个电梯视图的电梯视图群 并且设置位置与布局方式
        JPanel elevatorViews = new JPanel();
        elevatorViews.setLayout(new FlowLayout(FlowLayout.LEADING,20,20));
        elevatorViews.setBounds(Const.ELEVATOR_VIEWS_LEFT,Const.ELEVATOR_VIEWS_TOP,
                Const.ELEVATOR_VIEWS_WIDTH,Const.ELEVATOR_VIEWS_HEIGHT);
        
        //将电梯视图加入电梯群视图
        for (int i = 0; i < Const.ELEVATORS_COUNT; i++)
        {
            elevatorViews.add(new ElevatorView().getElevatorView(i));
        }

        //楼层群
        JPanel floorViews = new JPanel();
        floorViews.setLayout(new FlowLayout(FlowLayout.LEADING,1,1));
        floorViews.setBounds(Const.FLOOR_VIEWS_LEFT,Const.FLOOR_VIEWS_TOP,
                Const.FLOOR_VIEWS_WIDTH,Const.FLOOR_VIEWS_HEIGHT);

        ElevatorOutViewControl elevatorOutViewControl = new ElevatorOutViewControl(control);
        new Thread(elevatorOutViewControl).start();
        for (int i = Const.FLOOR-1; i >= 0 ; --i)
        {
            JLabel label;//if是为了控制格式对齐
            if (i >= 9) label = new JLabel("楼层" + (i + 1) + ":");//指明是哪层楼
            else label = new JLabel("楼层  " + (i + 1) + ":");//指明是哪层楼
            floorViews.add(label);

            for (int j = 0; j < Const.ELEVATORS_COUNT; j++)
            {
                floorViews.add(elevatorOutViewControl.getElevatorOutViews()[i][j].getFloorView());
            }
        }

        this.add(elevatorViews);
        this.add(floorViews);

        this.setLayout(null);
        this.setTitle("电梯系统");
        this.setSize(Const.VIEW_WIDTH,Const.VIEW_HEIGHT);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
