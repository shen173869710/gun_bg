package com.auto.di.guan.socket;

import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UdpClient {
    /**
     * single instance UdpClient
     * */
    private static UdpClient mSocketClient = null;
    private UdpClient(){}
    public static UdpClient getInstance(){
        if(mSocketClient == null){
            synchronized (UdpClient.class) {
                mSocketClient = new UdpClient();
            }
        }
        return mSocketClient;
    }


    String TAG_log = "Socket";
    private DatagramSocket mSocket;
    private DatagramPacket sendPacket;    //发送
    private DatagramPacket receivePacket; //接受

    private SocketThread mSocketThread;
    private boolean isStop = false;//thread flag


    /**
     * 128 - 数据按照最长接收，一次性
     * */
    private class SocketThread extends Thread {

        private String ip;
        private int port;
        public SocketThread(String ip, int port){
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            LogUtils.e(TAG_log,"SocketThread start ");
            super.run();
            //connect ...
            try {
                if (mSocket != null) {
                    mSocket.close();
                    mSocket = null;
                }
                InetAddress ipAddress = InetAddress.getByName(ip);
                mSocket = new DatagramSocket();
                mSocket.connect(ipAddress, port); //连接
                //设置timeout
                //mSocket.setSoTimeout(3000);
                LogUtils.e(TAG_log,"udp connect = "+isConnect());
                if(isConnect()){
                    isStop = false;
                    EventBus.getDefault().post(new SocketEvent(1));
                }
                /* 此处这样做没什么意义不大，真正的socket未连接还是靠心跳发送，等待服务端回应比较好，一段时间内未回应，则socket未连接成功 */
                else{
                    EventBus.getDefault().post(new SocketEvent(-1));
                    LogUtils.e(TAG_log,"SocketThread connect fail");
                    return;
                }

            }
            catch (IOException e) {
                EventBus.getDefault().post(new SocketEvent(-1));
                LogUtils.e(TAG_log,"SocketThread connect io exception = "+e.getMessage());
                e.printStackTrace();
                return;
            }
            LogUtils.e(TAG_log,"SocketThread connect over ");
            //发送一次，否则不发送则收不到，不知道为啥。。。
//            sendByteCmd(new byte[]{00},-1);//send once
            //read ...
            while (isConnect() && !isStop && !isInterrupted()) {
                LogUtils.e(TAG_log, "开始监听");
                int size;
                try {
                    byte[] preBuffer = new byte[4 * 1024];//预存buffer
                    receivePacket = new DatagramPacket(preBuffer, preBuffer.length);
                    mSocket.receive(receivePacket);
                    if (receivePacket.getData() == null) {
                        LogUtils.e(TAG_log, "接收的数据为空");
                    }
                    size = receivePacket.getLength();     //此为获取后的有效长度，一次最多读64k，预存小的话可能分包
                    LogUtils.e(TAG_log, "pre data size = "+receivePacket.getData().length + ", value data size = "+size);
                    byte[] dataBuffer = Arrays.copyOf(preBuffer, size);
                    if (size > 0) {
                        EventBus.getDefault().post(new SocketEvent(1, new String(dataBuffer)));
                    }
                    LogUtils.e(TAG_log, "SocketThread read listening");
                }
                catch (IOException e) {
                    LogUtils.e(TAG_log,"SocketThread read io exception = "+e.getMessage());
                    e.printStackTrace();
                    return;
                }
            }
        }
    }



    //==============================socket connect============================
    private String ip;
    private int port;
    /**
     * connect socket in thread
     * Exception : android.os.NetworkOnMainThreadException
     * */
    public void connect(String ip, int port){
        this.ip = ip;
        this.port = port;
        mSocketThread = new SocketThread(ip, port);
        mSocketThread.start();
    }

    /**
     * socket is connect
     * */
    public boolean isConnect(){
        boolean flag = false;
        if (mSocket != null) {
            flag = mSocket.isConnected();
        }
        return flag;
    }

    /**
     * socket disconnect
     * */
    public void disconnect() {
        isStop = true;
        if (mSocket != null) {
            mSocket.close();
            mSocket = null;
        }
        if (mSocketThread != null) {
            mSocketThread.interrupt();//not intime destory thread,so need a flag
        }
    }

    /**
     * send byte[] cmd
     * Exception : android.os.NetworkOnMainThreadException
     * */
    public void sendByteCmd(final byte[] mBuffer,int requestCode) {
        this.requestCode = requestCode;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress ipAddress = InetAddress.getByName(ip);
                    sendPacket = new DatagramPacket(mBuffer, mBuffer.length, ipAddress, port);
                    mSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    /**
     * socket response data listener
     * */
    private OnDataReceiveListener onDataReceiveListener = null;
    private int requestCode = -1;
    public interface OnDataReceiveListener {
        public void onConnectSuccess();
        public void onConnectFail();
        public void onDataReceive(byte[] buffer, int size, int requestCode);
    }
    public void setOnDataReceiveListener(
            OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }


}
