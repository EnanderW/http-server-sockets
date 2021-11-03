package me.code;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {

        ServerSocket socket = new ServerSocket(8080);

        while (true) {

            Socket client = socket.accept();


            new Thread(new Client(client)).start();
        }


    }

}
