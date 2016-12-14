package udpchatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Johnathan Tripp (╯°□°）╯︵ ┻━┻
 */
public class UDPChatClient {

    DatagramSocket socket;
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    boolean running = true;
    
    public UDPChatClient(){}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        UDPChatClient client = new UDPChatClient();
        client.run();
    } 
    
    public void run(){
        Scanner scan = new Scanner(System.in);
        try{
            System.out.println("Please enter the IP address of the server you would like to connect to:");
            String host = scan.nextLine().trim();
            socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(host);
            System.out.println("Connected to " + IPAddress.getHostName());
            byte[] incoming = new byte[1024];
                Thread sendThread = new Thread(new SendMsg(input, socket, IPAddress));
                Thread recvThread = new Thread(new RecvMsg(socket, incoming));
                sendThread.start();
                recvThread.start();
            while(running){
                
                //Uncomment the following lines and comment out the Thread lines to run a different implementation. Do this for the Server as well.
//                String msg = input.readLine();
//                byte[] data = msg.getBytes();
//                DatagramPacket sendPacket = new DatagramPacket(data , data.length , IPAddress , 9876);
//                socket.send(sendPacket);
//                System.out.println("Message Sent."); 
//                if(msg.toLowerCase().trim().equals("quit")){
//                    running = false;
//                    continue;
//                }
//                DatagramPacket incomingPacket = new DatagramPacket(incoming, incoming.length);
//                socket.receive(incomingPacket);
//                String response = new String(incomingPacket.getData());
//                System.out.println("Server: " + response);
//                if(response.toLowerCase().trim().equals("quit")){
//                    running = false;
//                }
            }
            socket.close();
            System.out.println("Connection Ended.");
        
        } catch(UnknownHostException e){
            e.printStackTrace();
        }catch (SocketException e){
            e.printStackTrace();  
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public class SendMsg implements Runnable {

        BufferedReader input;
        DatagramSocket socket;
        InetAddress IPAddress;
        
        public SendMsg(BufferedReader input, DatagramSocket socket, InetAddress IPAddress){
            this.input = input;
            this.socket = socket;
            this.IPAddress = IPAddress;
        }
        
        @Override
        public void run() {
            try{
                String msg = input.readLine();
                byte[] data = msg.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(data , data.length , IPAddress , 9876);
                socket.send(sendPacket);
                System.out.println("Message Sent."); 
                if(msg.toLowerCase().trim().equals("quit")){
                    running = false;
                }
                Thread.sleep(2000);
            } catch(IOException e){
                e.printStackTrace();
            } catch (InterruptedException ex) {
                Logger.getLogger(UDPChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public class RecvMsg implements Runnable {

        DatagramSocket socket;
        byte[] incoming;
        
        public RecvMsg(DatagramSocket socket, byte[] incoming){
            this.socket = socket;
            this.incoming = incoming;
        }
        
        @Override
        public void run() {
            try{
                DatagramPacket incomingPacket = new DatagramPacket(incoming, incoming.length);
                socket.receive(incomingPacket);
                String response = new String(incomingPacket.getData());
                System.out.println("Server: " + response);
                if(response.toLowerCase().trim().equals("quit")){
                    running = false;
                }
                Thread.sleep(2000);
            } catch(IOException e){
                e.printStackTrace();
            } catch (InterruptedException ex) {
                Logger.getLogger(UDPChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
