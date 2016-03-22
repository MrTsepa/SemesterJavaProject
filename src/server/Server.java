package server;

import org.jsfml.system.Vector2f;
import java.io.*;
import java.net.*;
import world.World;
import world.PlayerInfo;


public class Server{
   private static int playerCount = 0;
   static Vector2f player1 = new Vector2f(0,0);
   static Vector2f player2 = new Vector2f(0,0);
   
   //static float  client1_x = 0, client1_y = 50;
   //static float  client2_x = 50, client2_y = 50;
   static World ourWorld = new World();

   
   // Выбираем порт вне пределов 1-1024:
   public static final int PORT = 8080;
   
   public static void main(String[] args) throws IOException, ClassNotFoundException {
       ServerSocket s = new ServerSocket(PORT);
       System.out.println("Started: " + s);
       World.setSelfXY(player1);

       try {
           // Блокирует до тех пор, пока не возникнет соединение:
           Socket socket = s.accept();
           playerCount++;
           System.out.println("Connection accepted: " + socket);

        //Потоки из и в сокет
           InputStream sin = socket.getInputStream();
           OutputStream sout = socket.getOutputStream();

        // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
//        DataInputStream in = new DataInputStream(sin);
//        DataOutputStream out = new DataOutputStream(sout);

        // String line = null;
        while(true) {
             BufferedInputStream in = new BufferedInputStream(sin);
             ObjectInputStream inObject = new ObjectInputStream(in);

             BufferedOutputStream out = new BufferedOutputStream(sout);
             ObjectOutputStream outObject = new ObjectOutputStream(out);

             //World A  = (World) object.readObject();
             outObject.writeObject(ourWorld);
//           client1_x = in.readFloat();
//
//           out.writeFloat(client2_x); // отсылаем клиенту обратно ту самую строку текста.
//           out.writeFloat(client2_y);
//
//
//           out.flush(); // заставляем поток закончить передачу данных.
//           System.out.println("Waiting for the next line...");
//           System.out.println();
        }
      }finally{
          System.out.println("closing...");
          s.close();
      }
      
    }
}
