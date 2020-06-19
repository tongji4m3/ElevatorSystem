package view;

import controller.Controller;
import controller.IOController;
import model.Const;
import model.FCB;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import static sun.plugin2.os.windows.FLASHWINFO.size;

/*
命令行窗口的操作与展示
 */
public class View
{
    private Controller controller;
    private IOController ioController;

    public View()
    {
        controller = new Controller();
        ioController = new IOController(controller);

        //从本地磁盘加载上一次退出时文件系统的数据
        ioController.load();
        System.out.println("数据加载成功!");

        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.println("-------------欢迎来到文件管理系统(--help获取帮助文档)------------");
        while (true)
        {
            System.out.print("[root@localhost " + controller.getCurrentDir().getFileAddressName() + "]#");
            command = scanner.nextLine();
            //startWith最好放equals后面,不然匹配优先级可能有问题
            if (command.equals("exit")) {exit();return;}
            else if (command.equals("--help")) help();
            else if (command.equals("pwd")) pwd();
            else if (command.equals("ls")) ls();
            else if (command.equals("openFiles")) openFiles();
            else if (command.equals("dirBlock")) dirBlock();
            else if (command.equals("fileBlock")) fileBlock();
            else if (command.equals("format")) format();
            else if (command.startsWith("cd")) cd(command);
            else if (command.startsWith("mkdir")) create(command,Const.DIR);
            else if (command.startsWith("rm")) rm(command);
            else if (command.startsWith("touch")) create(command,Const.FILE);
            else if (command.startsWith("open")) open(command);
            else if (command.startsWith("close")) close(command);
            else if (command.startsWith("vim")) write(command);
            else if (command.startsWith("cat")) read(command);
            else if (command.startsWith("delete")) delete(command);
            else fault();

        }
    }

    private void exit()
    {
        ioController.save();
        System.out.println("内容已保存");
    }

    private void format()
    {
        controller = new Controller();
        ioController = new IOController(controller);
    }

    private void help()
    {
        System.out.println("-------------帮助文档---------------");
        System.out.println("退出: exit");
        System.out.println("查看帮助: --help");
        System.out.println("格式化: format");
        System.out.println("显示目录: pwd");
        System.out.println("更改当前目录: cd fileName");
        System.out.println("创建子目录: mkdir fileName");
        System.out.println("删除子目录: rm fileName");
        System.out.println("列出当前目录下的所有文件: ls");
        System.out.println("创建文件: touch fileName");
        System.out.println("打开文件: open fileName");
        System.out.println("关闭文件: close fileName");
        System.out.println("写文件: vim fileName");
        System.out.println("读文件: cat fileName");
        System.out.println("删除文件: delete fileName");
        System.out.println("查看目录区信息: dirBlock");
        System.out.println("查看文件区信息: fileBlock");
        System.out.println("查看打开文件表: openFiles");
        System.out.println("------------------------------------");
    }

    private void fault()
    {
        System.out.println("指令错误,可以输入--help查看帮助文档");
    }

    //关键的公共方法,负责从指令中获取合法的文件/目录的FCB
    private FCB getCurrentFCB(String command)
    {
        //如果写法错误,则显示fault()
        String[] strings = command.split(" ");
        if (strings.length != 2)
        {
            fault();
            return null;
        }
        String address = strings[1];
        FCB current = null;
        if (address.startsWith("/"))
        {
            current = controller.getDirArea()[0];//根目录
            String[] paths = address.split("/");
            for (int i = 1; i < paths.length; i++)
            {
                current = current.getChildren(paths[i]);
                if (current == null) return null;
            }
        }
        else
        {
            current = controller.getCurrentDir().getChildren(address);
        }
        return current;
    }

    //校验目录中第二个参数是否是合法的文件名或者目录名
    public boolean checkFileName(String fileName)
    {
        String regEx = "[\\w\\.]+";
        //使用正则表达式验证是否合法
        return Pattern.compile(regEx).matcher(fileName).matches();
    }

    //对应一个不存在的目录或者文件,根据层级建立出来 其中参数指明了是文件还是目录
    private void create(String command, int type)
    {
        //如果写法错误,则显示fault()
        String[] strings = command.split(" ");
        if (strings.length != 2)
        {
            fault();
            return;
        }
        String address = strings[1];
        FCB current = null;
        if (address.startsWith("/"))
        {
            current = controller.getDirArea()[0];//根目录
            StringBuilder parentAddress = new StringBuilder("");//从根路径开始
            String[] paths = address.split("/");
            for (int i = 1; i < paths.length; i++)
            {
                if(!checkFileName(paths[i]))
                {
                    System.out.println("文件名不合法");
                    return;
                }
                parentAddress.append("/");
                parentAddress.append(paths[i]);

                FCB children = current.getChildren(paths[i]);
                if (children == null)
                {
                    //不存在,要新建 而且如果不是最后一级,或者建立的是目录,则统一建立目录
                    if (i != paths.length - 1 || type == Const.DIR)
                    {
                        int freeDirBlock = controller.getFreeDirBlock();
                        children = new FCB(
                                paths[i], parentAddress.toString(),
                                Const.DIR, freeDirBlock, 1, current);
                        controller.getDirArea()[freeDirBlock] = children;
                    }
                    else
                    {
                        //文件初始时只分配一个块 位示图
                        List<Integer> blocks = controller.getFreeFileBlock(1);
                        //在FAT中分配
                        controller.allocFAT(blocks);
                        children = new FCB(
                                paths[i], parentAddress.toString(),
                                Const.FILE, blocks.get(0), 1, current);
                    }

                    //在FCB中分配
                    current.setChildren(children);
                }
                current = children;
            }
        }
        else
        {

            String parentAddress = controller.getCurrentDir().getFileAddressName();
            if (parentAddress.equals("/")) parentAddress = "";//防止根路径的/的影响
            String fileAddress = strings[1];
            //已经存在了一个文件
            if (controller.getCurrentDir().getChildren(fileAddress) != null)  return;
            if (type == Const.DIR)
            {
                int freeDirBlock = controller.getFreeDirBlock();
                FCB childrenDir = new FCB(
                        fileAddress, parentAddress + "/" + fileAddress,
                        Const.DIR, freeDirBlock, 1, controller.getCurrentDir());
                controller.getCurrentDir().setChildren(childrenDir);
                controller.getDirArea()[freeDirBlock] = childrenDir;
                System.out.println("已创建");
            }
            else
            {
                //文件初始时只分配一个块 位示图
                List<Integer> blocks = controller.getFreeFileBlock(1);
                //在FAT中分配
                controller.allocFAT(blocks);

                //在FCB中分配
                FCB childrenFile = new FCB(
                        fileAddress, parentAddress + "/" + fileAddress,
                        Const.FILE, blocks.get(0), 1, controller.getCurrentDir());
                controller.getCurrentDir().setChildren(childrenFile);
                System.out.println("已创建");
            }
        }
    }

    private void delete(String command)
    {
        FCB currentFCB = getCurrentFCB(command);
        if (currentFCB == null || currentFCB.getFileType() != Const.FILE)
        {
            System.out.println("删除的文件不存在");
            return;
        }
        close(command);//先在打开文件表中删除
        int index = currentFCB.getFileBlockIndex();
        controller.setFreeFileBlock(index);//位示图移除 磁盘块删除
        currentFCB.getParent().remove(currentFCB.getFileName());//FCB目录中移除
        controller.freeFAT(index);//FAT回收
    }

    private void write(String command)
    {
        //vim不管文件存不存在,都会创建出来
        FCB currentFCB = getCurrentFCB(command);
        //没有文件就创建一个文件!
        if (currentFCB == null || currentFCB.getFileType() != Const.FILE) create(command,Const.FILE);
        currentFCB = getCurrentFCB(command);

        //读写都会加入到打开文件表里面
        open(command);

        //暂时保存输入要写的信息
        System.out.println("--------将覆盖之前信息, :wq退出编辑----------");
        Scanner scanner = new Scanner(System.in);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine())
        {
            String temp = scanner.nextLine();
            if (temp.equals(":wq")) break;
            builder.append(temp);
            builder.append("\n");
        }
        System.out.println("--------------输入结束---------------");
        //往磁盘块中写信息
        try
        {
            ioController.write(currentFCB, builder.toString());
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    private void read(String command)
    {
        //读写都会加入到打开文件表里面
        open(command);
        FCB currentFCB = getCurrentFCB(command);
        if (currentFCB == null || currentFCB.getFileType() != Const.FILE)
        {
            System.out.println("文件名不存在");
            return;
        }
        System.out.println("-----------文件内容:-------------");
        System.out.println(ioController.read(currentFCB));
        System.out.println("-----------文本结束-------------");
    }

    private void close(String command)
    {
        FCB currentFCB = getCurrentFCB(command);
        if (currentFCB == null || currentFCB.getFileType() != Const.FILE) return;
        System.out.println("已经将该文件移除出打开文件表");
        controller.removeOpenFiles(currentFCB.getFileAddressName());
    }

    private void open(String command)
    {
        FCB current = getCurrentFCB(command);
        if (current == null || current.getFileType() != Const.FILE)
        {
            System.out.println("文件不存在");
            return;
        }
        controller.addOpenFiles(current.getFileAddressName(), current.getFileBlockIndex());
        System.out.println("已经加入打开文件表");
    }

    private void rm(String command)
    {
        FCB currentFCB = getCurrentFCB(command);
        if (currentFCB == null || currentFCB.getFileType() != Const.DIR)
        {
            System.out.println("目录不存在");
            return;
        }
        //将它下面的也递归删除掉
        rmRecursive(currentFCB);
    }

    private void rmRecursive(FCB fcb)
    {
        List<FCB> children = fcb.getChildren();
        for (int i = 0; i < children.size(); i++)
        {
            FCB currentFCB = children.get(i);
            //如果是目录并且还有子目录,就继续删除
            if(currentFCB.getFileType()==Const.DIR && currentFCB.getChildren().size()!=0)
            {
                rmRecursive(children.get(i));
            }
            //根据是目录还是文件 删除该项
            if(currentFCB.getFileType()==Const.DIR)
            {
                int index = currentFCB.getFileBlockIndex();
                currentFCB.getParent().remove(currentFCB.getFileName());//在FCB中删除
                controller.setFreeDirBlock(index);//在目录的位示图中删除
                controller.freeDirArea(index);//在目录磁盘块中删除
            }
            else
            {
                //调用删除文件接口删除
                delete("delete "+currentFCB.getFileName());
            }
        }
        //删除它本身
        int index = fcb.getFileBlockIndex();
        fcb.getParent().remove(fcb.getFileName());//在FCB中删除
        controller.setFreeDirBlock(index);//在目录的位示图中删除
        controller.freeDirArea(index);//在目录磁盘块中删除
    }

    private void cd(String command)
    {
        //如果写法错误,则显示fault()
        String[] strings = command.split(" ");
        if (strings.length != 2)
        {
            fault();
            return;
        }
        String address = strings[1];
        if (address.equals("..") || address.equals("../"))
        {
            if (controller.getCurrentDir().getParent() == null)
            {
                //说明是/
                controller.setCurrentDir(controller.getDirArea()[0]);
            }
            else
            {
                controller.setCurrentDir(controller.getCurrentDir().getParent());
            }
        }
        else if (address.equals(".") || address.equals("./"))
        {
            //当前目录 不用改变
        }
        else
        {
            FCB current = getCurrentFCB(command);
            if (current == null || current.getFileType() != Const.DIR)
            {
                System.out.println("目录不存在,可以输入--help查看帮助文档");
            }
            else
            {
                controller.setCurrentDir(current);
            }
        }
    }

    private void ls()
    {
        List<FCB> children = controller.getCurrentDir().getChildren();
        System.out.println("  文件名 文件类型 物理块地址 物理块长度");
        for (FCB child : children)
        {
            System.out.println(child);
        }
        System.out.println();
    }

    public void pwd()
    {
        System.out.println(controller.getCurrentDir().getFileAddressName());
    }

    private void openFiles()
    {
        Map<String, Integer> openFiles = controller.getOpenFiles();
        System.out.println("文件路径  文件存储的物理块号");
        for (String key : openFiles.keySet())
        {
            System.out.println(key + "        " + openFiles.get(key));
        }
    }

    private void fileBlock()
    {
        boolean[][] bits = controller.getBits();
        System.out.println("文件区磁盘块使用情况:");
        for (int i = 0; i < bits.length; i++)
        {
            for (int j = 0; j < bits[0].length; j++)
            {
                if (bits[i][j])
                {
                    System.out.println("第" + (i * bits.length + j) + "磁盘块已分配");
                }
            }
        }
    }

    private void dirBlock()
    {
        boolean[][] dirBits = controller.getDirBits();
        System.out.println("目录区磁盘块使用情况:");
        for (int i = 0; i < dirBits.length; i++)
        {
            for (int j = 0; j < dirBits[0].length; j++)
            {
                if (dirBits[i][j])
                {
                    System.out.println("第" + (i * dirBits.length + j) + "磁盘块已分配");
                }
            }
        }
    }
}
