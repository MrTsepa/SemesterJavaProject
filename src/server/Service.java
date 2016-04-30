package server;

import java.io.BufferedInputStream;
import java.io.*;

import world.*;

public class Service {
    World ourWorld = new World();
    public int id = 0;
    public int nextId() {return id++;}
    
    public void serve(InputStream inputStream, OutputStream outputStream) throws IOException{
        // настраиваем потоки
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
         
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
        
        //записываем новый мир и отправляем его
        //ourWorld = (World) inObject;
        objectOutputStream.writeObject(ourWorld);
        objectOutputStream.writeObject(nextId());
        
    }
    
}

