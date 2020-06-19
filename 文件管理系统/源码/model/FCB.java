package model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/*
FCB实体类
即一个个文件目录项
 */
public class FCB
{
    private String fileName;//文件名称 类似 local
    private String fileAddressName;//文件路径名称 类似/usr/local
    private int fileType;//文件类型
    private int fileBlockIndex;//文件的物理块地址 目录和文件存储在不同的位置
    private int fileLength;//文件的物理块长度,即占用了几个物理块
    private List<FCB> children;//如果文件类型是目录,就有他自己的目录
    private FCB parent;//他的上一级目录 为了cd ../

    public FCB(String fileName, String fileAddressName, int fileType, int fileAddress, int fileLength, FCB parent)
    {
        this.fileName = fileName;
        this.fileAddressName = fileAddressName;
        this.fileType = fileType;
        this.fileBlockIndex = fileAddress;
        this.fileLength = fileLength;
        this.parent = parent;
        //如果是目录,那么就有子目录
        if (fileType == Const.DIR) children = new LinkedList<>();
    }

    //从自己的目录下返回特定的文件
    public FCB getChildren(String childName)
    {
        for (FCB child : children)
        {
            if (child.getFileName().equals(childName)) return child;
        }
        return null;
    }

    //通过文件名从目录下删除某个PCB
    public boolean remove(String fileAddress)
    {
        for (int i = 0; i < children.size(); i++)
        {
            if (children.get(i).getFileName().equals(fileAddress))
            {
                children.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        String stringFileType = (fileType == Const.DIR ? "dir" : "file");
        String stringBlockIndex = (fileType == Const.DIR ? "(dir)" : "(file)") + fileBlockIndex;
        return String.format("%6s%8s%10s%8d", fileName, stringFileType,stringBlockIndex,fileLength);
    }

    public FCB getParent()
    {
        return parent;
    }

    public void setParent(FCB parent)
    {
        this.parent = parent;
    }

    public void setChildren(List<FCB> children)
    {
        this.children = children;
    }

    public List<FCB> getChildren()
    {
        return children;
    }

    public void setChildren(FCB children)
    {
        this.children.add(children);
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileAddressName()
    {
        return fileAddressName;
    }

    public void setFileAddressName(String fileAddressName)
    {
        this.fileAddressName = fileAddressName;
    }

    public int getFileType()
    {
        return fileType;
    }

    public void setFileType(int fileType)
    {
        this.fileType = fileType;
    }

    public int getFileBlockIndex()
    {
        return fileBlockIndex;
    }

    public void setFileBlockIndex(int fileBlockIndex)
    {
        this.fileBlockIndex = fileBlockIndex;
    }

    public int getFileLength()
    {
        return fileLength;
    }

    public void setFileLength(int fileLength)
    {
        this.fileLength = fileLength;
    }
}
