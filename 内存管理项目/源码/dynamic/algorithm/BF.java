package dynamic.algorithm;

import dynamic.domain.FreeItem;
import dynamic.domain.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BF
{
    public static void bf(List<FreeItem> freeList, List<FreeItem> alreadyList, Task task)
    {
        //根据最佳适应算法,则应该从容量最小的开始找起,找到第一个满足的
        //实际算法中,则采用了从按地址递增的空闲分区链中,先筛选出符合条件的空闲分区,再找到最小的一个元素
        Optional<FreeItem> min = freeList.stream()
                .filter((FreeItem freeItem) -> freeItem.getLength() >= task.getSize())
                .min((o1, o2) -> o1.getLength() - o2.getLength());
        FreeItem freeItem = min.get();
        if (freeItem.getLength() > task.getSize())
        {
            //往已分配分区表中存放
            alreadyList.add(new FreeItem(freeItem.getStartAddress(), task.getSize(), task.getName()));

            //该空闲分区能容得下该作业.且是大于的,此时需要把该空闲分区的始址与长度进行相应的修改
            freeItem.setStartAddress(freeItem.getStartAddress() + task.getSize());
            freeItem.setLength(freeItem.getLength() - task.getSize());
            //处理完了请求 不再继续寻找
        } else if (freeItem.getLength() == task.getSize())
        {
            //往已分配分区表中存放
            alreadyList.add(new FreeItem(freeItem.getStartAddress(), task.getSize(), task.getName()));

            //正好占满,只需要移除该空闲分区即可
            freeList.remove(freeItem);
        }
    }
}
