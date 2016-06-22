/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Siyam
 */
public class Client {
    public void runClient()
   {
      String serverName = "localhost";
      int port = 1234;
      try
      {
          System.out.println("CLIENT SITE");
         // System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         System.out.println("Just connected to " 
		 + client.getRemoteSocketAddress());
         
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         new Thread(new Runnable() {
             @Override
             public void run() {
                 Scanner clinetScanner = new Scanner(System.in);
                 String msg = null;
                 while(clinetScanner.hasNextLine()){
                     try {
                         msg = clinetScanner.nextLine();
                         if(msg.equalsIgnoreCase("exit"))
                             break;
                         out.writeUTF(msg);
                         //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                     } catch (IOException ex) {
                         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                     }
             }
             }
         }).start();
      
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         String msg = null;
         while(true){
             Thread.sleep(400);
             msg = in.readUTF();
             if(msg.equalsIgnoreCase("exit"))
                 break;
         System.out.println("Server: " + msg);    
         }
         client.close();
      }catch(Exception e)
      {
         e.printStackTrace();
      }
   }  
    
    
    public static void main(String args[]){

        new Client().runClient();
    }
}
