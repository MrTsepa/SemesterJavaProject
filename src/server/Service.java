
import java.io.BufferedInputStream;



import java.io.*;
import org.jsfml.system.Vector2f;
import world.World;
import world.game.objects.Cell;
import world.game.Team;

public class Service {
 
    Cell cell1 = new Cell(new Vector2f(100, 100), 20, Team.Player1);
    Cell cell2 = new Cell(new Vector2f(200, 100), 20, Team.Player1);
    Cell cell3 = new Cell(new Vector2f(100, 200), 20, Team.Player2);
    Cell cell4 = new Cell(new Vector2f(300, 200), 20, Team.Player2);
    Cell cell5 = new Cell(new Vector2f(150, 150), 10, Team.Neutral);
    Cell cell6 = new Cell(new Vector2f(400, 400), 20, Team.Player1);

    world.World world = new world.World(cell1, cell2, cell3, cell4, cell5, cell6);
    
    public int id = 0;
    public int nextId() {return id++;}
    
    public void serve(InputStream i, OutputStream o) throws IOException{
        System.out.println("in serve");
        // настраиваем потоки
        
        //ObjectInputStream inObject = new ObjectInputStream(i);
        System.out.println("Objectstream in created " );
        
        ObjectOutputStream outObject = new ObjectOutputStream(o);
        
        //записываем новый мир и отправляем его
        //ourWorld = (World) inObject;
        System.out.println("Ready to serve" );
        outObject.writeObject(world);
        System.out.println("world sent");
       //s outObject.writeObject(nextId());
        
    }
    
}

