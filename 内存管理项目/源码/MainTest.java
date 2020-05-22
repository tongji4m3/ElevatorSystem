import dynamic.algorithm.FitAlgorithm;
import request.algorithm.RequestPage;

import java.util.Scanner;

public class MainTest
{
    public static void main(String[] args)
    {
        System.out.println("请输入要进行的操作:(1表示选择首次适应算法 2表示选择最佳适应算法 3表示进行请求调页存储管理方式模拟)");
        int choose = 0;
        Scanner scanner = new Scanner(System.in);
        choose = scanner.nextInt();
        //choose==1,选择首次适应算法 choose==2,选择最佳适应算法
        //choose==3,请求请求调页存储管理方式模拟
        if (choose == 1 || choose==2)
        {
            FitAlgorithm.start(choose);
        }
        else if(choose==3)
        {
            RequestPage.start();
        }

        System.out.println();
        System.out.println("按1结束程序");
        int stop = -1;
        while(stop!=1)
        {
            stop=scanner.nextInt();
        }
    }
}
