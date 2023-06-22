package com.example.cliente3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static Socket cliente;

    private static Socket clienteE;

    private static InputStreamReader isr;

    private static PrintWriter printWriter;

    private final String ip = "192.168.1.104";

    String Smessage;

    public EditText chatCaja;

    public EditText input;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatCaja = findViewById(R.id.chatCaja);

        input = findViewById(R.id.input);

        Thread hiloEntrada = new Thread(new ThreadEntrada());

        hiloEntrada.start();

    }

    public void sendText(View view){

         Smessage = input.getText().toString();

        input.setText(" ");

        chatCaja.append("Tu: "+Smessage+"\n");

        Smessage = Smessage + " \n";

        conexionS cs = new conexionS();

        cs.execute();

    }

    class ThreadEntrada implements Runnable{

        Socket entrada;

        ServerSocket Ientrada;

        InputStreamReader input;

        BufferedReader buffer;

        Handler hand = new Handler();

        String mensaje;

        @Override
        public void run() {

            try{

                cliente = new Socket(ip,80);

                isr = new InputStreamReader(cliente.getInputStream());

                BufferedReader buffer = new BufferedReader(isr);

                    while(true){

                         mensaje = buffer.readLine();

                       hand.post(new Runnable() {
                            @Override
                            public void run() {
                                chatCaja.append("Otro cliente: "+mensaje+"\n");
                            }
                        });

                    }


            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

    class conexionS extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            try{

                clienteE = new Socket(ip,80);

                printWriter = new PrintWriter(clienteE.getOutputStream());

                printWriter.write(Smessage);

                printWriter.flush();

                printWriter.close();

            }catch(IOException e){
                Toast.makeText(getBaseContext(),"Error"+e.toString(),Toast.LENGTH_SHORT).show();
            }

            return null;
        }
    }

}