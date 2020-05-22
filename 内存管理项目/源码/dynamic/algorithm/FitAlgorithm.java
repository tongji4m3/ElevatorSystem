package dynamic.algorithm;

import dynamic.domain.FreeItem;
import dynamic.domain.Task;

import java.util.LinkedList;
import java.util.List;

/*
从该类中,根据传入参数的不同,分别调用FF,BF算法
 */
public class FitAlgorithm
{
    //最佳适应算法
    public static void start(int choose)
    {
        List<FreeItem> freeList = new LinkedList<>();//空闲分区链
        freeList.add(new FreeItem(0, 640, "未分配"));//初始有一块640的未分配区间
        List<FreeItem> alreadyList = new LinkedList<>();//已分配分区链
        List<Task> tasks = Task.getTask();//具体的任务要求

        while (tasks.size() != 0)
        {
            System.out.println("---------------------------------------------------------------------");
            System.out.println();
            System.out.println("---当前的请求序列(每次都操作第一个请求):---");
            System.out.println("作业     操作    大小");
            for (Task task : tasks)
            {
                System.out.println(task);
            }
            Task task = tasks.remove(0);//一次处理一个请求

            //根据申请还是释放进行不同的操作
            if (task.isFree())//释放
            {
                //从已分配分区表中得到该作业在内存中的起始地址以及大小
                int startAddress = 0, size = 0;
                for (int i = 0; i < alreadyList.size(); i++)
                {
                    FreeItem freeItem = alreadyList.get(i);

                    if (freeItem.getFlag().equals(task.getName()))
                    {
                        startAddress = freeItem.getStartAddress();
                        size = freeItem.getLength();
                        alreadyList.remove(i);
                        break;
                    }
                }

                boolean isChange = false;//是否修改过
                //在空闲分区表中释放该区域内存
                for (int i = 0; i < freeList.size(); i++)
                {
                    FreeItem freeItem = freeList.get(i);
                    //找到该内存在空闲分区链中应该处于的位置
                    //四种情况,1和后面的连,2和前面的连,3一起连接,4和前后都不连
                    if (startAddress < freeItem.getStartAddress())
                    {
                        //和后面的合并
                        if (startAddress + size == freeItem.getStartAddress())
                        {
                            freeItem.setStartAddress(startAddress);
                            freeItem.setLength(freeItem.getLength() + size);
                            isChange = true;
                        }

                        //如果前面有空闲分区
                        if (i - 1 >= 0)
                        {
                            FreeItem preItem = freeList.get(i - 1);

                            //全合并
                            if (preItem.getStartAddress() + preItem.getLength() == freeItem.getStartAddress())
                            {
                                freeItem.setStartAddress(preItem.getStartAddress());
                                freeItem.setLength(freeItem.getLength() + preItem.getLength());
                                freeList.remove(i - 1);
                                isChange = true;
                            } else if (preItem.getStartAddress() + preItem.getLength() == startAddress)//这合并该个和前面一个
                            {
                                preItem.setLength(preItem.getLength() + size);
                                isChange = true;
                            }
                        }
                        if (isChange) break;
                        //在前和后内存块的中间
                        freeList.add(i, new FreeItem(startAddress, size, "未分配"));
                        isChange = true;
                        break;
                    }
                }

                if (!isChange)//前面都没有修改过,说明了在所有的空闲分区的后面
                {
                    freeList.add(freeList.size(), new FreeItem(startAddress, size, "未分配"));
                }
            }
            else//申请
            {
                //BF,FF只有这里的算法不一样 抽离出方法
                if (choose == 1) FF.ff(freeList, alreadyList, task);
                else if (choose == 2) BF.bf(freeList, alreadyList, task);
            }

            System.out.println();
            System.out.println("---空闲分区链:---");
            System.out.println("始址     长度     标志");
            for (FreeItem freeItem : freeList)
            {
                System.out.println(freeItem);
            }
            System.out.println();
            System.out.println("---已分配分区链:---");
            System.out.println("始址     长度     标志");
            for (FreeItem freeItem : alreadyList)
            {
                System.out.println(freeItem);
            }
            System.out.println();
        }
    }
}
