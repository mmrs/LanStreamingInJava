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
public class Server {

    private ServerSocket serverSocket;
    Thread msgIn, msgOut, voiceIn, voiceOut;
    Scanner serverMessage = new Scanner(System.in);
    String msg = null;
    AudioProcessor audioProcessor = new AudioProcessor();
    Data data;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //  serverSocket.setSoTimeout(10000);
    }

    public void runServer() {

        // while (true) //  {
        //   {
        try {
            System.out.println("SERVER SITE");
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + ".");
            Socket server = serverSocket.accept();
            System.out.println("Connected to " + server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            msgIn = new Thread(new Runnable() {
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
                }
            });
            msgOut = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {
                        try {
                            msg = serverMessage.nextLine();
                            if (msg.equalsIgnoreCase("exit")) {
                                break;
                            }
                            out.writeUTF("Thank you for connecting to "
                                    + server.getLocalSocketAddress() + "\nGoodbye!");
                            out.writeUTF(msg);
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });

            voiceIn = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(400);
                            audioProcessor.readTargetLine();
                            in.read(audioProcessor.getTargetData(), 0, audioProcessor.getNumBytesRead());
                            System.out.println("Server Voice In:" + audioProcessor.getNumBytesRead());
                            audioProcessor.writeAudio();

                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            voiceOut = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {

                        data = audioProcessor.readTargetLine();
//                        audioProcessor.writeAudio();
                        try {
                            out.write(data.targetData, 0, data.numBytesRead);
                            System.out.println("server : " + data.numBytesRead);
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
//            server.close();
        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            //  break;
        } catch (IOException e) {
            e.printStackTrace();
            //  break;
        }
        //  }
        //  }
    }

    public static void main(String[] args) {
        int port = 1234;
        try {
            Server server = new Server(port);
            server.runServer();
            server.voiceOut.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
