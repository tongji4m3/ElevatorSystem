package controller;

import model.Block;
import model.FCB;
import model.Const;

import java.util.*;


/*
全局的控制器
 */
public class Controller
{
    private FCB currentDir;//当前目录项
    private  FCB[] dirArea;//目录区
    private  Block[] FileArea;//文件区
    private int[] FAT;//显示连接的文件分配表FAT,对应着物理块号,以及下一块地址
    private boolean[][] dirBits;//目录的空闲分区管理,位示图法
    private boolean[][] bits;//空闲分区管理采用位示图法
    private Map<String,Integer> openFiles;//打开文件表 (文件路径,文件的第一个物理块号)

    public Controller()
    {
        bits = new boolean[Const.N][Const.N];//初始化位示图都为未分配状态
        dirBits = new boolean[Const.DIR_N][Const.DIR_N];//初始化位示图都为未分配状态

        //FAT中,-1代表结束,其他数字代表下一个物理块指向,所以初始化-2
        FAT=new int[Const.BLOCK_COUNT];
        Arrays.fill(FAT, -2);

        //目录区初始化
        dirArea = new FCB[Const.DIR_BLOCK_COUNT];
        FileArea = new Block[Const.BLOCK_COUNT];
        for (int i = 0; i < FileArea.length; i++)
        {
            FileArea[i] = new Block(i);
        }
        //打开文件表初始化
        openFiles = new HashMap<>();

        int dirIndex = getFreeDirBlock();
        if(dirIndex==-1)
        {
            System.out.println("无可分配空闲磁盘块");
            throw new RuntimeException();
        }

        //初始化当前路径为根路径 且为目录,占用目录区的一个物理块0
        currentDir = new FCB("","/",
                Const.DIR, dirIndex, 1,null);
        dirArea[dirIndex] = currentDir;
    }

    public void allocFAT(List<Integer> blocks)
    {

        for (int i = 0; i < blocks.size()-1; i++)
        {
            FAT[blocks.get(i)] = blocks.get(i + 1);
        }
        FAT[blocks.get(blocks.size() - 1)] = -1;
    }

    public void freeFAT(int index)
    {
        while(true)
        {
            if(FAT[index]==-1)
            {
                FAT[index] = -2;
                break;
            }
            int temp = FAT[index];
            FAT[index]=-2;
            index = temp;
        }
    }

    public int getFreeDirBlock()
    {
        for (int i = 0; i < dirBits.length; i++)
        {
            for (int j = 0; j < dirBits[0].length; j++)
            {
                //取一个未分配的
                if(!dirBits[i][j])
                {
                    dirBits[i][j] = true;//变成已分配
                    return dirBits.length * i + j;//返回目录块的索引
                }
            }
        }
        return -1;
    }
    //清空目录磁盘块的位示图
    public void setFreeDirBlock(int dirIndex)
    {
        int i=dirIndex/dirBits.length;
        int j = dirIndex%dirBits.length;
        dirBits[i][j] = false;
    }

    //清空某个目录磁盘块
    public void freeDirArea(int dirIndex)
    {
        dirArea[dirIndex] = null;
    }

    //在位示图分配了
    public List<Integer> getFreeFileBlock(int size)
    {
        List<Integer> result = new LinkedList<>();

        for (int i = 0; i < bits.length; i++)
        {
            for (int j = 0; j < bits[0].length; j++)
            {
                if(size==0) return result;//把该分配的分配好了
                //取一个未分配的
                if(!bits[i][j])
                {
                    --size;//分配出了一个
                    bits[i][j] = true;//变成已分配
                    result.add(bits.length * i + j);
                }
            }
        }
        return result;
    }
    public void setFreeFileBlock(int fileIndex)
    {
        int i=fileIndex/bits.length;
        int j = fileIndex%bits.length;
        bits[i][j] = false;
    }

    public void addOpenFiles(String address,Integer blockIndex)
    {
        openFiles.put(address, blockIndex);
    }

    public void removeOpenFiles(String address)
    {
        openFiles.remove(address);
    }


    public Map<String, Integer> getOpenFiles()
    {
        return openFiles;
    }

    public void setOpenFiles(Map<String, Integer> openFiles)
    {
        this.openFiles = openFiles;
    }

    public Block[] getFileArea()
    {
        return FileArea;
    }

    public void setFileArea(Block[] fileArea)
    {
        FileArea = fileArea;
    }

    public boolean[][] getDirBits()
    {
        return dirBits;
    }

    public void setDirBits(boolean[][] dirBits)
    {
        this.dirBits = dirBits;
    }

    public FCB[] getDirArea()
    {
        return dirArea;
    }

    public int[] getFAT()
    {
        return FAT;
    }

    public boolean[][] getBits()
    {
        return bits;
    }

    public void setCurrentDir(FCB currentDir)
    {
        this.currentDir = currentDir;
    }

    public void setDirArea(FCB[] dirArea)
    {
        this.dirArea = dirArea;
    }

    public void setFAT(int[] FAT)
    {
        this.FAT = FAT;
    }

    public void setBits(boolean[][] bits)
    {
        this.bits = bits;
    }

    public FCB getCurrentDir()
    {
        return currentDir;
    }
}
