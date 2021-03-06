package com.example.asus.lantalk.constant;

import com.example.asus.lantalk.R;

/**
 * Created by asus on 18-5-8.
 */

public class Constant {

    //端口号
    public static final int SERVER_MSG_PORT = 3333;
    public static final int SERVER_FILE_PORT = 4444;
    public static final int SERVER_MULTI_PORT = 3344;

    //服务的通信传参
    public static final String ACTION_SEND_FILE = "com.example.android.wifidirect.SEND_FILE";
    public static final String ACTION_SEND_MSG = "com.example.android.wifidirect.SEND_MSG";

    //
    public static final String SEND_PEER_BEAN = "send";
    public static final String SEND_PEER_NAME = "name";
    public static final String SNED_PEER_PICTURE = "picture";

    //消息的类别
    public static final int MINEMSG = 0;
    public static final int PEERMSG = 1;
    public static final int MINEFILE = 2;
    public static final int PEERFILE = 3;

    public static final String SERVICE_RECEIVER = "scan";//扫描后台广播的接收


    public static final int sCHOOSEALBUM = 0;
    public static final int sOPENWIFI = 1;

    //RecyclerView 的数据标识
    public static final int sITEM = 0;
    public static final int sEMPTY = 1;


    //图片发送的状态

    public static final int sSENDING  = 0;
    public static final int sSUCCESS = 1;
    public static final int sERROR = 2;

    //TTL
    public static final int sTTL = 255;




    public static final int[] PROFILEPHOTO = {R.drawable.iv_head_1, R.drawable.iv_head_2, R.drawable.iv_head_3,
            R.drawable.iv_head_4, R.drawable.iv_head_5, R.drawable.iv_head_6,
            R.drawable.iv_head_7, R.drawable.iv_head_8, R.drawable.iv_head_9,
            R.drawable.iv_head_10, R.drawable.iv_head_11, R.drawable.iv_head_12,};


}
