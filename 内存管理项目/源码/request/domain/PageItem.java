package request.domain;

public class PageItem
{
    private int pageNumber;//页号
    private int memoryNumber;//内存块号 -1表示不在内存,0-3表示在内存块中
    private boolean status;//是否调入内存
    private int visited;//访问字段 可以记录上次访问时间,实现LRU算法

    public PageItem(int pageNumber)
    {
        this.pageNumber = pageNumber;
        memoryNumber = -1;//初始都不在内存中
        status = false;//不在内存中
        visited = -1;
    }

    public int getPageNumber()
    {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public int getMemoryNumber()
    {
        return memoryNumber;
    }

    public void setMemoryNumber(int memoryNumber)
    {
        this.memoryNumber = memoryNumber;
    }

    public boolean isStatus()
    {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public int getVisited()
    {
        return visited;
    }

    public void setVisited(int visited)
    {
        this.visited = visited;
    }
}
