
import java.io.*;
import world.World;
import world.game.events.Event;


public class Service{
    public int id = 0;
    public synchronized int nextId() {
        return id++;
    }
   
    public void sendWorld(OutputStream o, World world) throws IOException{
        System.out.println("in serve");
        System.out.println("Objectstream in created " );
        ObjectOutputStream outObject = new ObjectOutputStream(o);
        System.out.println("Ready to serve" );
        world.playerNumber = nextId();
        outObject.writeObject(world);
        System.out.println("world sent");
    }
    public Event readEvent(InputStream i) throws IOException, ClassNotFoundException{
        ObjectInputStream inObject = new ObjectInputStream(i);
        Event event = (Event) inObject.readObject();
        System.out.println("Read event in readEvent");
        return event;
    }
    
}

