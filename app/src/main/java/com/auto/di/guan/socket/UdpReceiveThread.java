package com.auto.di.guan.socket;

import android.text.TextUtils;

import com.auto.di.guan.utils.LogUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *    bzt 20000   0            0      385    000   0
 *
 *               水泵编号       0待机  电压    电流  错误信息 0 正常
 *                             1启动                       1 无法连接
 *                             2运行                       2 过流保护
 *                             3停车                       3. 其他
 *                             4故障
 *
 *                   0    不查询
 *                   1 3  1分钟
 *                   2    10分钟
 */
public class UdpReceiveThread extends Thread
{
    private DatagramSocket sendSocket;
    private InetAddress serverAddr;
    @Override
    public void run()
    {
        try
        {
            sendSocket = new DatagramSocket(SocketEntiy.PORT);
            serverAddr = InetAddress.getByName(SocketEntiy.IP);
            while(true)
            {
                byte[] inBuf= new byte[40];
                DatagramPacket inPacket=new DatagramPacket(inBuf,inBuf.length);
                sendSocket.receive(inPacket);
                if(!inPacket.getAddress().equals(serverAddr)){
                    continue;
                }
                LogUtils.e("接收到的数据", new String(inPacket.getData()).trim());
                String data = new String(inPacket.getData()).trim();
                if (!TextUtils.isEmpty(data)) {
                    SocketEntiy.prare(data);
                }

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
