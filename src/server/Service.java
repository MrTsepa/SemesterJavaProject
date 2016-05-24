package server;

import java.io.*;
import world.World;
import world.game.events.Event;


public class Service{
    public int id = 0;
    public synchronized int nextId() {
        return id++;
    }
   
    public void sendWorld(OutputStream o, World world) throws IOException{
        ObjectOutputStream outObject = new ObjectOutputStream(o);
        world.playerNumber = nextId();
        outObject.writeObject(world);
    }
    public Event readEvent(InputStream i) throws IOException, ClassNotFoundException{
        ObjectInputStream inObject = new ObjectInputStream(i);
        Event event = (Event) inObject.readObject();
        return event;
    }
    
}

