package server;

import java.io.BufferedInputStream;
import java.io.*;

import world.*;

public class Service {
    World ourWorld = new World();
    public int id = 0;
    public int nextId() {return id++;}
    
    public void serve(InputStream i, OutputStream o) throws IOException{
        // настраиваем потоки
        BufferedInputStream in = new BufferedInputStream(i);
        ObjectInputStream inObject = new ObjectInputStream(in);
         
        BufferedOutputStream out = new BufferedOutputStream(o);
        ObjectOutputStream outObject = new ObjectOutputStream(out);
        
        //записываем новый мир и отправляем его
        //ourWorld = (World) inObject;
        outObject.writeObject(ourWorld);
        outObject.writeObject(nextId());
        
    }
    
}

