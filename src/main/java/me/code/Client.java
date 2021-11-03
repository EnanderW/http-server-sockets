package me.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Client(Socket socket) {
        this.socket = socket;

        try {
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            List<String> request = new ArrayList<>();

            while (!socket.isClosed()) {

                String s = reader.readLine();
                if (s != null)
                    request.add(s);


                if (!request.isEmpty() && s != null && s.isEmpty()) {

                    String contentLength = findHeader("Content-Length", request);
                    if (contentLength != null) {

                        StringBuilder builder = new StringBuilder();

                        int count = Integer.parseInt(contentLength);
                        for (int i = 0; i < count; i++) {
                            builder.append((char) reader.read());
                        }

                        request.add(builder.toString());
                    }

                    request.forEach(System.out::println);

                    writer.println("HTTP/1.1 200 OK");
                    writer.println("Content-Length: 0");
                    writer.println("Connection: close");
                    writer.println();

//                    if (request.get(0).startsWith("POST")) {
//                        writer.println("HTTP/1.1 200 OK");
//                        writer.println("Content-Length: 0");
//                        writer.println("Connection: close");
//                        writer.println();
//                    }

//                    socket.close();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findHeader(String header, List<String> list) {
        for (String s : list) {
            if (s.contains(header))
                return s.split(": ")[1];
        }

        return null;
    }

}
