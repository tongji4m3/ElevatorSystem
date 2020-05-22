package dynamic.algorithm;

import dynamic.domain.FreeItem;
import dynamic.domain.Task;

import java.util.LinkedList;
import java.util.List;

public class FF
{
    //首次适应算法
    public static void ff(List<FreeItem> freeList, List<FreeItem> alreadyList, Task task)
    {
        //根据首次适应算法,则从低地址找起,找到第一个满足的
        for (int i = 0; i < freeList.size(); i++)
        {
            FreeItem freeItem = freeList.get(i);

            if (freeItem.getLength() > task.getSize())
            {
                //往已分配分区表中存放
                alreadyList.add(new FreeItem(freeItem.getStartAddress(), task.getSize(), task.getName()));

                //该空闲分区能容得下该作业.且是大于的,此时需要把该空闲分区的始址与长度进行相应的修改
                freeItem.setStartAddress(freeItem.getStartAddress() + task.getSize());
                freeItem.setLength(freeItem.getLength() - task.getSize());
                //处理完了请求 不再继续寻找
                break;
            } else if (freeItem.getLength() == task.getSize())
            {
                //往已分配分区表中存放
                alreadyList.add(new FreeItem(freeItem.getStartAddress(), task.getSize(), task.getName()));

                //正好占满,只需要移除该空闲分区即可
                freeList.remove(i);
                break;
            }
        }
    }
}
