package com.example.chat.task;

import android.app.Activity;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class AppClientEndpoint extends WebSocketClient {
    private static final String TAG = "AppClientEndpoint";
    private Activity mAct; // 活动实例
    private OnRespListener mListener; // 消息应答监听器

    public AppClientEndpoint(Activity act, OnRespListener listener, URI serverUri) {
        super(serverUri); // 调用 WebSocketClient 的构造函数
        mAct = act;
        mListener = listener;
    }

    // 向服务器发送请求报文
    public void sendRequest(String req) {
        Log.d(TAG, "发送请求报文：" + req);
        try {
            if (isOpen()) { // 检查连接是否打开
                send(req); // 发送文本消息
            } else {
                Log.w(TAG, "WebSocket 未连接，无法发送消息");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 连接成功后调用
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d(TAG, "成功创建连接");
    }

    // 收到服务端消息时调用
    @Override
    public void onMessage(String message) {
        Log.d(TAG, "WebSocket服务端返回：" + message);
        if (mListener != null) {
            mAct.runOnUiThread(() -> mListener.receiveResponse(message));
        }
    }

    // 连接关闭时调用
    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d(TAG, "连接关闭，code=" + code + ", reason=" + reason + ", remote=" + remote);
    }

    // 收到服务端错误时调用
    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "WebSocket 错误：" + ex.getMessage());
        ex.printStackTrace();
    }

    // 定义 WebSocket 应答的监听器接口
    public interface OnRespListener {
        void receiveResponse(String resp);
    }
}