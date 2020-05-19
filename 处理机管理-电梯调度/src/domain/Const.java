package domain;

/**
 * 存储一些全局变量,易于修改
 */
public class Const
{
    public static int FLOOR=20;//楼层高度
    public static int ELEVATORS_COUNT=5;//电梯数量
    public static int VIEW_WIDTH=1500;//整个视图宽
    public static int VIEW_HEIGHT=800;//整个视图高

    public static int ELEVATOR_VIEWS_LEFT=10;//电梯视图群距离左边的距离
    public static int ELEVATOR_VIEWS_TOP=10;//电梯视图群距离上边的距离
    public static int ELEVATOR_VIEWS_WIDTH=400;//电梯视图群宽
    public static int ELEVATOR_VIEWS_HEIGHT=700;//电梯视图群高    
    
    public static int FLOOR_VIEWS_LEFT=500;//楼层视图群距离左边的距离
    public static int FLOOR_VIEWS_TOP=10;//楼层视图群距离上边的距离
    public static int FLOOR_VIEWS_WIDTH=750;//楼层视图群宽
    public static int FLOOR_VIEWS_HEIGHT=800;//楼层视图群高

    //-1下降,1上升,0停止
    public static int UP=1;
    public static int DOWN=-1;
    public static int STOP=0;
}
