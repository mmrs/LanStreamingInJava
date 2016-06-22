/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Siyam
 */
public class Server extends Thread {

    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //  serverSocket.setSoTimeout(10000);
    }

    public void run() {

        Scanner serverMessage = new Scanner(System.in);
        String msg = null;
        while (true) //  {
        {
            try {
                System.out.println("SERVER SITE");
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + ".");
                Socket server = serverSocket.accept();
                System.out.println("Connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                new Thread(new Runnable() {
                    String msg = null;
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(400);
                                msg = in.readUTF();
                                System.out.println("Client: " + msg);
                            } catch (Exception ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                }).start();
                DataOutputStream out
                        = new DataOutputStream(server.getOutputStream());
                while (true) {
                    msg = serverMessage.nextLine();
                    if (msg.equalsIgnoreCase("exit")) {
                        break;
                    }
//                out.writeUTF("Thank you for connecting to "
//              + server.getLocalSocketAddress() + "\nGoodbye!");
                    out.writeUTF(msg);
                }
                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        //  }
    }
    public static void main(String[] args) {
        int port = 1234;
        try {
            Thread t = new Server(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
