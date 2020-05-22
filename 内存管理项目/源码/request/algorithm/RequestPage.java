package request.algorithm;

import request.domain.InstructionSequence;
import request.domain.PageItem;

import java.util.Arrays;

public class RequestPage
{
    public static void start()
    {
        int pageFaultCount = 0;//缺页次数
        int visitCount = 0;//记录访问字段

        PageItem[] pages = new PageItem[32];//该作业共有32页
        for (int i = 0; i < pages.length; i++)
        {
            pages[i] = new PageItem(i);//初始化每个页面
        }
        int[] memory = new int[4];//分配给该作业的4个内存块,记录存储的页号
        //初始化没有页面在内存块中
        Arrays.fill(memory, -1);

        int[] sequence = InstructionSequence.getSequence();
        //对于每条指令的执行,都进行展示
        for (int i = 0; i < sequence.length; i++)
        {
            System.out.println("-----------------------------------");
            System.out.println();
            System.out.println("当前执行的指令为:"+sequence[i]);

            int pageNumber = sequence[i] / 10;//该指令属于哪个页面
            PageItem page = pages[pageNumber];
            if(page.isStatus())//该页面在内存中
            {
                System.out.println("该指令的物理地址为:"+sequence[i]);
                page.setVisited(visitCount++);//记录最近一次访问时间
                System.out.println();
                System.out.println("本次访问没有缺页!");
            }
            else
            {
                ++pageFaultCount;
                boolean isFree = false;//内存块中是否有空余位置
                for (int j = 0; j < memory.length; j++)
                {
                    if(memory[j]==-1)//内存还有空余位置,可直接存放页面
                    {
                        memory[j] = page.getPageNumber();
                        page.setMemoryNumber(j);
                        page.setStatus(true);
                        isFree = true;
                        page.setVisited(visitCount++);//记录最近一次访问时间
                        System.out.println();
                        System.out.println("本次访问缺页,不需要页面置换!");
                        break;
                    }
                }
                if(!isFree)//需要调用LRU置换算法
                {
                    //选取内存块中最近最久未使用的页面
                    int index = 0, oldest = pages[memory[0]].getVisited();
                    for (int j = 1; j < memory.length; j++)
                    {
                        int currentVisited = pages[memory[j]].getVisited();
                        if(oldest>currentVisited)
                        {
                            index = j;
                            oldest = currentVisited;
                        }
                    }
                    //模拟把原来的块调出内存
                    pages[memory[index]].setStatus(false);
                    pages[memory[index]].setMemoryNumber(-1);

                    //index即为需要替换的块
                    memory[index] = page.getPageNumber();
                    page.setMemoryNumber(index);
                    page.setStatus(true);
                    page.setVisited(visitCount++);//记录最近一次访问时间
                    System.out.println();
                    System.out.println("本次访问缺页,并且需要页面置换!");
                }
            }

            System.out.println();
            System.out.println("使用过的页表情况:  ");
            System.out.println("页号 内存块号   状态  访问时间");
            for (int j = 0; j < pages.length; j++)
            {
                if(pages[j].getVisited()>=0)
                {
                    String temp = "";//控制页面
                    if(j/10==0) temp = " ";
                    String temp1 = "";
                    if(pages[j].getMemoryNumber()!=-1)
                        temp1 = " ";
                    String temp3 = "";
                    if(pages[j].isStatus())
                    {
                        temp3 = " ";
                    }
                    System.out.println(temp+j+"     "+temp1+pages[j].getMemoryNumber()+"     "+pages[j].isStatus()+temp3+"    "+pages[j].getVisited());
                }
            }

            System.out.println();
            System.out.println("内存块情况:");
            System.out.println("内存块号 存储页面");
            for (int j = 0; j < memory.length; j++)
            {
                System.out.println("   "+j+"       "+memory[j]);
            }

            System.out.println();
        }
        System.out.println("缺页率为:"+((double)pageFaultCount/320));
    }
}
