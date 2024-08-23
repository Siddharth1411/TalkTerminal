import java.net.*;
import java.io.*;
public class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Client(){
        try {
            System.out.println("sending request to server");
            socket = new Socket("127.0.0.1", 5600); //127.0.0.1 is localhost
            System.out.println("connection established");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        startReading();
        startWriting();
    }

    void startReading(){
        //thread for reading
        Runnable r1 = () -> {
            System.out.println("reading started");
           try {
            while(true){
                String incomingMsg = br.readLine();
                if(incomingMsg.equals("exit")){
                    System.out.println("Server has ended the chat");
                    break;
                }
                System.out.println("Server: " + incomingMsg);
            }
           } catch (Exception e) {
            // e.printStackTrace();
            // System.out.println("Client Connection Closed (reading)");
           }
        };

        new Thread(r1).start();
    }

    void startWriting(){
        Runnable r2 = () -> {
            System.out.println("writing started");
           try {
            while(!socket.isClosed()){
                BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
                String content = b.readLine();
                out.println(content);
                out.flush();
                if(content.equals("exit")){
                    socket.close();
                    break;
                }
            }
            System.out.println("Connection Closed");
           } catch (Exception e) {
            // e.printStackTrace();
            // System.out.println("Connection closed");
           }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("Client is working");
        new Client();
    }
}
