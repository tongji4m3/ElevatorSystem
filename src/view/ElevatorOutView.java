package view;

import control.ElevatorsControl;
import domain.Elevator;
import domain.Const;

import javax.swing.*;
import java.awt.*;
import java.net.CookieHandler;


public class ElevatorOutView extends JPanel
{
    //因为JPanel用private,再给get方法就会报错
    private ElevatorsControl control;//整个电梯控制类
    private int floorHeight;//楼层高度
    private int index;//哪一个电梯

    public int getIndex()
    {
        return index;
    }

    public JLabel getFloor()
    {
        return floor;
    }

    public JLabel getUpDownButton()
    {
        return upDownButton;
    }

    private JButton upButton=new JButton("∆");//上升按钮
    private JButton downButton=new JButton("∇");//下降按钮

    private JLabel floor=new JLabel();//指示电梯所在楼层
    private JLabel upDownButton=null;//指示电梯目前上升还是下降

    public JButton getUpButton()
    {
        return upButton;
    }

    public JButton getDownButton()
    {
        return downButton;
    }

    public int getFloorHeight()
    {
        return floorHeight;
    }

    public ElevatorOutView(int floorHeight, int index, ElevatorsControl control)
    {
        this.floorHeight = floorHeight;
        this.index = index;
        this.control = control;
    }

    public JPanel getFloorView()
    {
        Elevator elevator = control.getElevators(index);//目前是哪个电梯
        int elevatorHeight=elevator.getLocation()+1;//电梯目前在哪
        if(elevatorHeight>=10) floor.setText(elevatorHeight+"");//指示电梯在哪层楼
        else floor.setText(elevatorHeight+"  ");//指示电梯在哪层楼

        if(elevator.getUpDown()== Const.DOWN) upDownButton=new JLabel("∇");//指示电梯在上升还是下降
        else  if(elevator.getUpDown()== Const.UP)upDownButton=new JLabel("∆");
        else upDownButton=new JLabel("∎ ");


        upButton.setBackground(Color.cyan);
        downButton.setBackground(Color.cyan);
        floor.setForeground(Color.YELLOW);
        upDownButton.setForeground(Color.YELLOW);

        this.add(upButton);
        this.add(downButton);
        this.add(upDownButton);
        this.add(floor);

        this.setBackground(Color.gray);
        return this;
    }
}
