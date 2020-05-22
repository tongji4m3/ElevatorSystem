package dynamic.domain;

public class FreeItem
{
    private int StartAddress;//起始地址
    private int length;//长度
    private String flag;//该内存分配情况

    public FreeItem(int startAddress, int length, String flag)
    {
        StartAddress = startAddress;
        this.length = length;
        this.flag = flag;
    }

    @Override
    public String toString()
    {
        String temp = "";//控制格式
        if(StartAddress==0) temp = "  ";
        else if(StartAddress/10>0 && StartAddress/100==0) temp = " ";
        else temp = "";
        String temp2 = "";
        if(length/10>0 && length/100==0) temp2 = " ";
        return StartAddress+"K"+temp+"     "+length+"k    "+temp2+flag;
    }

    public int getStartAddress()
    {
        return StartAddress;
    }

    public void setStartAddress(int startAddress)
    {
        StartAddress = startAddress;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public String getFlag()
    {
        return flag;
    }

    public void setFlag(String flag)
    {
        this.flag = flag;
    }
}
