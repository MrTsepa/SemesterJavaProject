package client_socket;


import org.jsfml.system.Vector2f;
import java.net.*;
import java.io.*;


public class socketConnection {

    
    public static void Connect() throws IOException {
     InetAddress addr = InetAddress.getByName(null);

      try {
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, 8080);
        System.out.println("socket = " + socket);
        
        InputStream sin = socket.getInputStream();
        OutputStream sout = socket.getOutputStream();
        
//        DataInputStream in = new DataInputStream(sin);
//        DataOutputStream out = new DataOutputStream(sout);
//        
        
        //while (true) {
//            out.writeFloat(1);
//            out.writeFloat(1);

            BufferedInputStream in = new BufferedInputStream(sin);
            ObjectInputStream inObject = new ObjectInputStream(in);
             
            World ourWorld = (World) inObject.readObject();
            out.flush(); // заставляем поток закончить передачу данных.
            //competitor = new Vector2f(in.readFloat(),in.readFloat());
         
          System.out.println(competitor.x);
         //   System.out.println(y);
        //}    
      }catch (IOException e) {
        System.out.println("Can't accept");
        System.exit(-1);
      }finally {
         System.out.println("closing...");
         //socket.close();
      }
   }
}
