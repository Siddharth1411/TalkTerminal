import java.net.*;
import java.io.*;

class Server{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Server(){
        try {
            server = new ServerSocket(5600); //server is listening to port 5600
            System.out.println("server ready to accept connection");
            System.out.println("waiting.....");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        }
        catch(Exception e) {
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
                    System.out.println("Client has ended the chat");
                    socket.close();
                    break;
                }
                System.out.println("Client: " + incomingMsg);
            }
           } catch (Exception e) {
            // e.printStackTrace();
            // System.out.println("Server Connection closed (reading)");
           }
        //    System.out.println("Connection closed")
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
            e.printStackTrace();
           }
        };
        new Thread(r2).start();
    }
    public static void main(String args[]){
        System.out.println("Server class is working");
        new Server(); //its reference not stored anywhere so becomes eligible for garbage collection
        //it is used to create ServerSocket object
        //why not create ServerSocket object in the main function itself why only constructor?
    }
}