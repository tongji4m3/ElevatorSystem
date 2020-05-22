package request.domain;

import java.util.Random;

/*
根据ppt的方式,得到指令序列
通过伪随机数,每次都得到同样的一组随机数,便于测试与复现
 */
public class InstructionSequence
{
    public static int[] getSequence()
    {
        //该作业有320条指令 所以指令地址应该是[0,320)
        //因为还要执行m+1,所以第m条指令应该在[0,319)之间
        int[] sequence = new int[320];//一共执行320条指令
        Random random = new Random(3);
        //m m+1
        sequence[0]=random.nextInt(319);//[0-319)
        sequence[1] = sequence[0] + 1;
        for (int i = 2; i+3 < sequence.length; i+=4)
        {
            //跳转到[0,m-1)  m1,m1+1
            int last = random.nextInt(sequence[i-2]-1);
            sequence[i] = last;
            sequence[i + 1] = last + 1;

            //为了不越界,m2应该在[m1+2,319)之间,这样m2+1才不会越界
            //所以未[0,317-m1)+m1+2
            last = random.nextInt(317-sequence[i]) + sequence[i]+2;
            sequence[i + 2] = last;
            sequence[i + 3] = last + 1;
        }
        /*
        最后两个序列没有赋值
        就随意赋予320条指令中任意的连续两处位置
         */
        int hi = sequence.length - 1;
        sequence[hi - 1] = random.nextInt(319);
        sequence[hi] = sequence[hi - 1] + 1;
        return sequence;
    }

}
