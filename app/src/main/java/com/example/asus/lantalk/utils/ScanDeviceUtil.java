package com.example.asus.lantalk.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.asus.lantalk.app.App;
import com.example.asus.lantalk.constant.Constant;
import com.example.asus.lantalk.entity.SocketBean;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import static com.example.asus.lantalk.constant.Constant.SERVER_MSG_PORT;


/**
 * Created by asus on 18-5-8.
 */

public class ScanDeviceUtil {
    private static final String TAG = ScanDeviceUtil.class.getSimpleName();

    /**
     * 核心池大小
     **/
    private static final int CORE_POOL_SIZE = 5;
    /**
     * 线程池最大线程数
     **/
    private static final int MAX_IMUM_POOL_SIZE = 255;

    private static String mDevAddress;// 本机IP地址-完整
    private static String mLocAddress;// 局域网IP地址头,如：192.168.1.
    private static Runtime mRun;// 获取当前运行环境，来执行ping，相当于windows的cmd
    private static Process mProcess = null;// 进程
    private static String mPing = "ping -c 1 -w 3 ";// 其中 -c 1为发送的次数，-w 表示发送后等待响应的时间
    private static List<String> mIpList = new ArrayList<>();// ping成功的IP地址
    private static ThreadPoolExecutor mExecutor;// 线程池对象

    private static Socket mSocket;


    /**
     * TODO<扫描局域网内ip，找到对应服务器>
     *
     * @return void
     */
    public static void scan() {
        mRun = Runtime.getRuntime();
        mDevAddress = getLocAddress();// 获取本机IP地址
        mLocAddress = getLocAddrIndex(mDevAddress);// 获取本地ip前缀
        Log.d(TAG, "开始扫描设备,本机Ip为：" + mDevAddress);

        if (TextUtils.isEmpty(mLocAddress)) {
            Log.e(TAG, "扫描失败，请检查wifi网络");

        }

        /**
         * 1.核心池大小 2.线程池最大线程数 3.表示线程没有任务执行时最多保持多久时间会终止
         * 4.参数keepAliveTime的时间单位，有7种取值,当前为毫秒
         * 5.一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响
         * ，一般来说，这里的阻塞队列有以下几种选择：
         */
        mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_IMUM_POOL_SIZE,
                900, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
                CORE_POOL_SIZE));

        // 新建线程池
        for (int i = 2; i < 255; i++) {
            // 创建256个线程分别去ping
            final int lastAddress = i;// 存放ip最后一位地址 1-255
            Runnable run = new Runnable() {

                @Override
                public void run() {


                    // TODO Auto-generated method stub
                    String ping = mPing + mLocAddress
                            + lastAddress;
                    String currnetIp = mLocAddress + lastAddress;
                    if (mDevAddress.equals(currnetIp)) // 如果与本机IP地址相同,跳过
                        return;

                    try {

                        mProcess = mRun.exec(ping);
                        int result = mProcess.waitFor();
                        Log.d(TAG, "正在扫描的IP地址为：" + currnetIp + "返回值为：" + result);
                        if (result == 0) {
                            Log.d(TAG, "扫描成功,Ip地址为：" + currnetIp);

                            mIpList.add(currnetIp);
                            //扫描到后直接连接
                            connect(currnetIp);
                        } else {
                            // 扫描失败
                            Log.d(TAG, "扫描失败");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "扫描异常" + e.toString());


                    } finally {
                        if (mProcess != null)
                            mProcess.destroy();
                    }
                }
            };

            mExecutor.execute(run);
        }

        mExecutor.shutdown();

        while (true) {
            try {
                if (mExecutor.isTerminated()) {// 扫描结束,开始验证
                    Log.d(TAG, "扫描结束,总共成功扫描到" + mIpList.size() + "个设备.");
                    Log.e(TAG, "scan: "+mIpList.toString() );

                    break;
                }
            } catch (Exception e) {
                // TODO: handle exception

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        mRun.gc();

    }

    /**
     * TODO<销毁正在执行的线程池>
     *
     * @return void
     */
    public static void destory() {
        if (mExecutor != null) {
            mExecutor.shutdownNow();
        }
    }

    /**
     * TODO<获取本地ip地址>
     *
     * @return String
     */
    public static String getLocAddress() {
        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface networks = en.nextElement();
                // 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> address = networks.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (!ip.isLoopbackAddress()
                            && (ip instanceof Inet4Address)) {
                        ipaddress = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("", "获取本地ip地址失败");
            e.printStackTrace();
        }

        Log.i(TAG, "本机IP:" + ipaddress);
        return ipaddress;
    }

    /**
     * TODO<获取本机IP前缀>
     *
     * @param devAddress // 本机IP地址
     * @return String
     */
    private static String getLocAddrIndex(String devAddress) {
        if (!devAddress.equals("")) {
            return devAddress.substring(0, devAddress.lastIndexOf(".") + 1);
        }
        return null;
    }


    public static String getLocalIPAddress() {
        String localIPAddress = "";
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()) {
                    InetAddress address = inetAddressEnumeration.nextElement();
                    if (!address.isLoopbackAddress() && (address instanceof InetAddress)) {
                        localIPAddress = address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return localIPAddress;
    }

    public static void connect(final String ip) {
        final SocketBean socketBean = new SocketBean();
        socketBean.setStatus(Constant.REQUEST);
        socketBean.setTime(TimeUtil.getCurrentTime());
        socketBean.setSendIP(App.sIP);
        socketBean.setSendName(App.sName);
        socketBean.setProfilePicture(App.sProfilePicture);
        socketBean.setReceiveIP(ip);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = new Socket();
                ObjectOutputStream os = null;
                try {
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(socketBean.getReceiveIP(), SERVER_MSG_PORT)), 3000);
                    os = new ObjectOutputStream(socket.getOutputStream());
                    os.writeObject(socketBean);
                    os.flush();
                } catch (IOException e) {

                    Log.e(TAG, "run: "+ip+"3000  端口没开" );
                } finally {
                    if (socket != null) {
                        if (socket.isConnected()) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}