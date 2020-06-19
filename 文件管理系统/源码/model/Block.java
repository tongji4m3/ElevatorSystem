package model;

import java.nio.ByteBuffer;

/*
物理块的实体类
 */
public class Block
{
    private int id; // id为该块的编号
    private ByteBuffer data; // 该块存储的数据

    public Block(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public ByteBuffer getData()
    {
        return data;
    }

    public void setData(ByteBuffer data)
    {
        this.data = data;
    }
}
