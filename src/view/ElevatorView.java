package view;

import domain.Const;
import domain.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 电梯内部的视图
 */
public class ElevatorView extends JPanel
{
    JButton openDoor=new JButton("◁▷");//开门按钮
    JButton closeDoor=new JButton("▷◁");//关门按钮
    JButton alarm=new JButton("∎");//报警按钮
    JButton buttonFloor[]=new JButton[Const.FLOOR];//每层楼按钮

    //返回电梯视图
    public JPanel getElevatorView(int index)
    {
        JLabel label=new JLabel("   电梯 "+(index+1));//指明是哪个电梯
        //将电梯楼层按钮加入到电梯视图中
        for (int i = Const.FLOOR-1; i >= 0 ; --i)
        {
            buttonFloor[i] = new JButton("" + i);
            this.add(buttonFloor[i]);

            JButton temp=buttonFloor[i];
            temp.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    temp.setBackground(Color.red);
                }
            });
        }

        closeDoor.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                closeDoor.setBackground(Color.red);
            }
        });

        openDoor.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                openDoor.setBackground(Color.red);
            }
        });

        alarm.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                alarm.setBackground(Color.red);
            }
        });


        this.add(alarm);
        this.add(openDoor);
        this.add(closeDoor);
        this.add(label);

        //设置电梯内部按钮布局方式
        this.setLayout(new GridLayout(8,3));
        this.setBackground(Color.gray);
        return this;
    }
}
