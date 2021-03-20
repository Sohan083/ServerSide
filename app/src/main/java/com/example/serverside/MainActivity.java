package com.example.serverside;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ServerSocket serverSocket;
    Socket s;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread() {
            public void run()
            {
                try {
                    serverSocket = new ServerSocket();
                    while (true)
                    {
                        s = serverSocket.accept();
                        inputStreamReader = new InputStreamReader(s.getInputStream());
                        bufferedReader = new BufferedReader(inputStreamReader);
                        msg = bufferedReader.readLine();
                        Log.d("message", msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }


}