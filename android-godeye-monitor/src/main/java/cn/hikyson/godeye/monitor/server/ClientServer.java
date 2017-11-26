package cn.hikyson.godeye.monitor.server;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ClientServer implements Runnable {
    private static final String TAG = "AndroidGodEyeServer";

    private final int mPort;

    private boolean mIsRunning;

    private ServerSocket mServerSocket;

    private final RequestHandler mRequestHandler;

    public ClientServer(int port) {
        mRequestHandler = new RequestHandler();
        mPort = port;
    }

    public void start() {
        mIsRunning = true;
        new Thread(this).start();
    }

    public void stop() {
        try {
            mIsRunning = false;
            if (null != mServerSocket) {
                mServerSocket.close();
                mServerSocket = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing the server socket.", e);
        }
    }

    @Override
    public void run() {
        try {
            mServerSocket = new ServerSocket(mPort);
            while (mIsRunning) {
                Socket socket = mServerSocket.accept();
                mRequestHandler.handle(socket);
                socket.close();
            }
        } catch (SocketException e) {
            //服务器关闭
            Log.e(TAG, "socket exception,maybe server closed.", e);
        } catch (IOException e) {
            Log.e(TAG, "Web server error.", e);
        } catch (Throwable ignore) {
            Log.e(TAG, "other exception.", ignore);
        }
    }

    public boolean isRunning() {
        return mIsRunning;
    }
}
