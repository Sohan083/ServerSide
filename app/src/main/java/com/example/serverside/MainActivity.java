package com.example.serverside;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.img);
        Thread thread = new Thread() {
            public void run()
            {
                try {
                    serverSocket = new ServerSocket(8080);
                    while (true)
                    {
                        s = serverSocket.accept();
                        /*inputStreamReader = new InputStreamReader(s.getInputStream());
                        bufferedReader = new BufferedReader(inputStreamReader);
                        msg = bufferedReader.readLine();
                        //Log.d("message", String.valueOf(msg.length()));

                         */
                        DataInputStream input;
                        InputStream in = s.getInputStream();
                        input = new DataInputStream(in);
                        byte[] data;//String read = input.readLine();
                        int len= input.readInt();
                        data = new byte[len];
                        if (len > 0) {
                            input.readFully(data,0,data.length);
                        }
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }

    public static Bitmap stringToImage(String string)
    {
        byte[] imgBytes= Base64.decode(string, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);

        return bitmap;
    }

    public static String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        float aspectRatio = bitmap.getWidth() / (float)bitmap.getHeight();
        int width = 1280;
        int height = Math.round(width / aspectRatio);
        bitmap = Bitmap.createScaledBitmap(bitmap,width,height,false);
        bitmap.compress(Bitmap.CompressFormat.JPEG,30,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }




}