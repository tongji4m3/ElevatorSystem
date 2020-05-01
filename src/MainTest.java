import control.ElevatorsControl;
import view.View;

/**
 * 调用视图
 */
public class MainTest
{
    public static void main(String[] args) throws InterruptedException
    {
        ElevatorsControl control = new ElevatorsControl();
        new Thread(control).start();
        View view = new View(control);//传入控制器
    }

}
