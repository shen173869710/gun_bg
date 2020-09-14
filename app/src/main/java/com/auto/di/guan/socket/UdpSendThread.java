package com.auto.di.guan.socket;

import com.auto.di.guan.utils.LogUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
 *   UDP数据发送线程
 * */
public class UdpSendThread extends Thread
{
    private DatagramSocket sendSocket;
    private InetAddress serverAddr;
    private byte[] buf;
    public UdpSendThread(byte[] cmd) {
        this.buf = cmd;
    }
    @Override
    public void run()
    {
        try
        {
            LogUtils.e("UdpSendThread", "发送消息------>"+new String(buf));
            // 创建DatagramSocket对象，使用随机端口
            sendSocket = new DatagramSocket();
            serverAddr = InetAddress.getByName(SocketEntiy.IP);
            DatagramPacket outPacket = new DatagramPacket(buf, buf.length,serverAddr, SocketEntiy.PORT);
            sendSocket.send(outPacket);
            sendSocket.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}