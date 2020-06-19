package controller;

import com.sun.xml.internal.txw2.TXW;
import model.Const;
import model.FCB;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class IOController
{
    private Controller controller;

    public IOController(Controller controller)
    {
        this.controller = controller;
    }

    public void write(FCB currentFCB, String text) throws UnsupportedEncodingException
    {
        int index = currentFCB.getFileBlockIndex();
        int length = currentFCB.getFileLength();
        ByteBuffer byteBuffer = ByteBuffer.allocate(Const.BLOCK_SIZE);//分配大小
        byteBuffer.put(text.getBytes());
        controller.getFileArea()[index].setData(byteBuffer);
    }

    public String read(FCB currentFCB)
    {
        int index = currentFCB.getFileBlockIndex();
        ByteBuffer data = controller.getFileArea()[index].getData();
        if (data == null) return "";
        return new String(data.array());
    }

    //将文件系统保存再磁盘中
    public void save()
    {
        saveDirBits();
        saveBits();
        saveFAT();
        saveDirArea();
        saveFileArea();
    }

    public void load()
    {
        loadDirBits();
        loadBits();
        loadFAT();
        loadDirArea();
        loadFileArea();
    }

    private void saveFileArea()
    {
        File fileArea = new File("fileArea");
        try (FileWriter writer = new FileWriter(fileArea);
             BufferedWriter out = new BufferedWriter(writer)
        )
        {
            boolean[][] bits = controller.getBits();
            for (int i = 0; i < bits.length; i++)
            {
                for (int j = 0; j < bits[0].length; j++)
                {
                    // 开头有//说明是编号那一行
                    if (bits[i][j])//说明这个块有内容的,需要备份
                    {
                        ByteBuffer data = controller.getFileArea()[bits.length * i + j].getData();
                        if (data != null)
                        {
                            out.write(new String(data.array()));
                            out.write("\n/#"+(bits.length * i + j)+"\n");
                        }
                    }
                }
            }
            out.flush();
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }

    private void loadFileArea()
    {
        String pathname = "fileArea";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        )
        {
            List<String> PCBList = new LinkedList<>();
            String line;
            int index = 0;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                // 开头有//说明是编号那一行
                if(line.startsWith("/#"))
                {
                    index = Integer.parseInt(line.substring(2));
                    ByteBuffer byteBuffer = ByteBuffer.allocate(Const.BLOCK_SIZE);//分配大小
                    byteBuffer.put(builder.toString().getBytes());
                    controller.getFileArea()[index].setData(byteBuffer);
                    builder = new StringBuilder();
                }
                else
                {
                    builder.append(line);
                }
            }
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }

    private void saveDirArea()
    {
        File dirArea = new File("dirArea");
        try (FileWriter writer = new FileWriter(dirArea);
             BufferedWriter out = new BufferedWriter(writer)
        )
        {
            //按层级存储目录区
            FCB current = controller.getDirArea()[0];//根目录
            int height = 0;//层级
            recursiveSaveDirArea(current, height, out);
            out.flush();
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }

    private void recursiveSaveDirArea(FCB current, int height, BufferedWriter out)
    {
        try
        {
            out.write(height + " " + current.getFileName() + " " + current.getFileAddressName() + " "
                    + current.getFileType() + " " + current.getFileBlockIndex() + " " + current.getFileLength() + "\n");
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
        if (current.getFileType() != Const.DIR) return;
        for (int i = 0; i < current.getChildren().size(); i++)
        {
            recursiveSaveDirArea(current.getChildren().get(i), height + 1, out);
        }
    }

    private void loadDirArea()
    {
        String pathname = "dirArea";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        )
        {
            List<String> PCBList = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null)
            {
                PCBList.add(line);
            }
            FCB current = controller.getDirArea()[0];//根目录
            for (int i = 1; i < PCBList.size(); i++)
            {
                String[] pcb = PCBList.get(i).split(" ");
                //如果不是上一个的子目录,则要回头找到他的父目录
                //比如层级0,1,2,3,2,3,4 则第二个2的父目录不是前面的3,而是前面的1
                if (Integer.parseInt(pcb[0]) <= Integer.parseInt(PCBList.get(i - 1).split(" ")[0]))
                {
                    for (int j = i; j >= 0; --j)
                    {
                        String[] temp = PCBList.get(j).split(" ");
                        if (Integer.parseInt(temp[0]) < Integer.parseInt(pcb[0]))
                        {
                            current = controller.getDirArea()[0];//根目录
                            String[] paths = temp[2].split("/");
                            for (int k = 1; k < paths.length; k++)
                            {
                                current = current.getChildren(paths[k]);
                            }
                            break;
                        }
                    }
                }
                FCB children = new FCB(pcb[1], pcb[2], Integer.parseInt(pcb[3]),
                        Integer.parseInt(pcb[4]), Integer.parseInt(pcb[5]), current);
                current.setChildren(children);
                current = children;
            }
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }

    private void saveFAT()
    {
        File FAT = new File("FAT");
        try (FileWriter writer = new FileWriter(FAT);
             BufferedWriter out = new BufferedWriter(writer)
        )
        {
            for (int i = 0; i < controller.getFAT().length; i++)
            {
                out.write(controller.getFAT()[i] + " ");
            }
            out.flush();
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }

    private void loadFAT()
    {
        String pathname = "FAT";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        )
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                // 一次读入一行数据
                String[] dirBits = line.split(" ");
                for (int i = 0; i < dirBits.length; i++)
                {
                    controller.getFAT()[i] = Integer.parseInt(dirBits[i]);
                }
            }
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }


    private void saveDirBits()
    {
        File dirBits = new File("dirBits");
        try (FileWriter writer = new FileWriter(dirBits);
             BufferedWriter out = new BufferedWriter(writer)
        )
        {
            for (int i = 0; i < controller.getDirBits().length; i++)
            {
                for (int j = 0; j < controller.getDirBits()[0].length; j++)
                {
                    out.write(controller.getDirBits()[i][j] + " ");
                }
                out.write("\n");
            }
            out.flush();
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }

    private void loadDirBits()
    {
        String pathname = "dirBits";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        )
        {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null)
            {
                // 一次读入一行数据
                String[] dirBits = line.split(" ");
                for (int j = 0; j < dirBits.length; j++)
                {
                    if (dirBits[j].equals("true")) controller.getDirBits()[i][j] = true;
                    else controller.getDirBits()[i][j] = false;
                }
                ++i;
            }
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }

    //保存文件位示图
    private void saveBits()
    {
        File bits = new File("bits");
        try (FileWriter writer = new FileWriter(bits);
             BufferedWriter out = new BufferedWriter(writer)
        )
        {
            for (int i = 0; i < controller.getBits().length; i++)
            {
                for (int j = 0; j < controller.getBits()[0].length; j++)
                {
                    out.write(controller.getBits()[i][j] + " ");
                }
                out.write("\n");
            }
            out.flush();
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }

    private void loadBits()
    {
        String pathname = "bits";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        )
        {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null)
            {
                // 一次读入一行数据
                String[] bits = line.split(" ");
                for (int j = 0; j < bits.length; j++)
                {
                    if (bits[j].equals("false")) controller.getBits()[i][j] = false;
                    else controller.getBits()[i][j] = true;
                }
                ++i;
            }
        }
        catch (IOException e)
        {
            //进入这说明不存在文件,第一次启动,算是正常的
        }
    }
}
