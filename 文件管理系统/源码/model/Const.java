package model;

/*
存储model包下的一些公共参数
 */
public class Const
{
    //标识是文件还是目录
    public static final int DIR = 1;
    public static final int FILE = 0;
    //目录区物理块数量
    public static final int DIR_BLOCK_COUNT = 400;
    //目录区的N*N位视图
    public static final int DIR_N = 20;

    //文件区物理块数量
    public static final int BLOCK_COUNT = 10000;
    //物理块容量
    public static final int BLOCK_SIZE = 1024;
    //N*N的位视图
    public static final int N = 100;
}
