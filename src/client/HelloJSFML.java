package client;

import java.io.IOException;
import java.net.*;
import java.io.*;

import org.jsfml.graphics.*;


import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import utils.Geometry;
import world.UpdateRunnable;
import world.World;
import world.game.Team;
import world.game.objects.Cell;
import world.game.events.*;
import world.game.objects.Tentacle;
import static utils.Geometry.findIntersectionPart;

public class HelloJSFML {

    static RenderWindow window = new RenderWindow();
    static World world;

    static Team clientTeam;

    static class DrawRunnable implements Runnable {
        
        
        @Override
        public void run() {
            window.create(new VideoMode(640, 480), "Tentacle Wars");
            window.setFramerateLimit(30);

            Cell parentCellChosen = null;

            while (window.isOpen()) {
                Cell cellClicked = null;
                Tentacle tentacleClicked = null;
                Float partTentacleClicked = null;
                
                for (Event event : window.pollEvents()) {
                    if (event.type == Event.Type.CLOSED) {
                        window.close();
                    }
                    if (event.type == Event.Type.MOUSE_BUTTON_PRESSED) {
                        Vector2i mousePosition = event.asMouseEvent().position;
                        for (Cell cell : world.cellArray) {
                            if (length(Vector2f.sub(cell.getPosition(), new Vector2f(mousePosition)))
                                    < cell.getRadius()) {
                                cellClicked = cell;
                            }
                        }
                        for (Cell cell : world.cellArray) {
                            for (Tentacle tentacle : cell.tentacleSet) {
                                Float part = findIntersectionPart(new Geometry.Circle(mousePosition, 5),
                                        new Geometry.Line(
                                                Vector2f.add(tentacle.parentCell.getPosition(),
                                                        Vector2f.mul(tentacle.getNormalizedDistanceVector(),
                                                                tentacle.parentCell.getRadius())),
                                                Vector2f.sub(tentacle.targetCell.getPosition(),
                                                        Vector2f.mul(tentacle.getNormalizedDistanceVector(),
                                                                tentacle.targetCell.getRadius()))));
                                if (part != null && part < tentacle.getDistancePart()) {
                                    tentacleClicked = tentacle;
                                    partTentacleClicked = part;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (cellClicked != null) {
                    if (parentCellChosen == null) {
                        //if (cellClicked.getTeam() == clientTeam) {
                            parentCellChosen = cellClicked;
                            parentCellChosen.isClicked = true;
                        //}
                    } else {
                        if (cellClicked == parentCellChosen) {
                            parentCellChosen.isClicked = false;
                            parentCellChosen = null;
                        } else {
                            parentCellChosen.isClicked = false;
                            world.game.events.Event event =
                                    new TentacleCreateEvent(parentCellChosen, cellClicked);
                            try {
                                SocketConnection.writeStream(event);
                                System.out.println("event send");
                            } catch (IOException ex) {
                                System.out.println("event didn't send");
                            }
                            // TODO Dasha send event
                            // пока что будет создавтаься локально
                            parentCellChosen.tentacleSet.add(new Tentacle(parentCellChosen, cellClicked));
                            parentCellChosen = null;
                        }
                    }
                }
                if (tentacleClicked != null && cellClicked == null) {
                    if (tentacleClicked.isConfronted() == false) {
                        if (tentacleClicked.getState() == Tentacle.State.MovingForward) {
                            if (tentacleClicked.getDistancePart() > partTentacleClicked) {
                                world.game.events.Event event =
                                        new TentacleDestroyEvent(tentacleClicked);
                                try {
                                    SocketConnection.writeStream(event);
                                    System.out.println("event send");
                                } catch (IOException ex) {
                                    System.out.println("event didn't send");
                                }
                                // TODO Dasha send event
                                // пока что будет создавтаься локально
                                tentacleClicked.setState(Tentacle.State.IsDestroyed);
                            }
                        }
                        if (tentacleClicked.getState() == Tentacle.State.Still) {
                            world.game.events.Event event =
                                    new TentacleCutEvent(tentacleClicked, partTentacleClicked);
                            try {
                                SocketConnection.writeStream(event);
                                System.out.println("event send");
                            } catch (IOException ex) {
                                System.out.println("event didn't send");
                            }
                            // TODO Dasha send event
                            // пока что будет создавтаься локально
                            tentacleClicked.setState(Tentacle.State.IsCutted);
                            tentacleClicked.setCuttedDistancePart(partTentacleClicked);
                            tentacleClicked.setDistancePart(partTentacleClicked);
                        }
                    }
                    if (tentacleClicked.isConfronted() == true) {
                        if (tentacleClicked.getState() == Tentacle.State.MovingForward) {
                            if (tentacleClicked.getDistancePart() > partTentacleClicked) {
                                world.game.events.Event event =
                                        new TentacleDestroyEvent(tentacleClicked);
                            try {
                                SocketConnection.writeStream(event);
                                System.out.println("event send");
                            } catch (IOException ex) {
                                System.out.println("event didn't send");
                            }
                                tentacleClicked.setState(Tentacle.State.IsDestroyed);
                            }
                        } // немного дублированного кода
                        if (tentacleClicked.getState() == Tentacle.State.IsCollided) {
                            if (tentacleClicked.getDistancePart() > partTentacleClicked) {
                                world.game.events.Event event =
                                        new TentacleDestroyEvent(tentacleClicked);
                                 try {
                                     SocketConnection.writeStream(event);
                                     System.out.println("event send");
                                 } catch (IOException ex) {
                                     System.out.println("event didn't send");
                                 }
                                tentacleClicked.setState(Tentacle.State.IsDestroyed);
                                tentacleClicked.getConfronting().setState(Tentacle.State.MovingForward);
                            }
                        }
                        if (tentacleClicked.getState() == Tentacle.State.Still) {
                            if (tentacleClicked.getDistancePart() > partTentacleClicked) {
                                world.game.events.Event event =
                                        new TentacleDestroyEvent(tentacleClicked);
                                try {
                                    SocketConnection.writeStream(event);
                                    System.out.println("event send");
                                } catch (IOException ex) {
                                    System.out.println("event didn't send");
                                }
                                tentacleClicked.setState(Tentacle.State.IsDestroyed);
                                tentacleClicked.getConfronting().setState(Tentacle.State.MovingForward);
                            }

                        }
                    }
                }

                window.clear(Color.BLACK);
                window.draw(world);
                window.display();

            }
        }

        private float length(Vector2f vector) {
            return (float) Math.sqrt(vector.x*vector.x + vector.y*vector.y);
        }
    }

    
    
    public static class SocketConnection {

        static Socket socket;

        public SocketConnection(String address) throws IOException {
            InetAddress addr = InetAddress.getByName(address);

            try {
                System.out.println("addr = " + addr);
                socket = new Socket(addr, 8080);
                System.out.println("socket = " + socket);
            } catch (IOException e) {
                System.out.println("Can't accept");
                System.exit(-1);
            } finally {
            }
        }

        public static void CloseSocket()
        {
            try{socket.close();} 
            catch(IOException e){}
        
        }
        public static Object readStream() throws IOException, ClassNotFoundException{
            InputStream sin = socket.getInputStream();
            BufferedInputStream in = new BufferedInputStream(sin);
            ObjectInputStream inObject = new ObjectInputStream(sin);
            
            return inObject.readObject();
        }     
        public static void writeStream(Object object) throws IOException{
            ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.writeObject(object);
            System.out.println("Object writed in socket");
            
        }
    }
    static class RecvRunnable implements Runnable{
        SocketConnection socketConnection;
        
        private RecvRunnable(SocketConnection socketConnection) {
            this.socketConnection = socketConnection;
        }
        
        @Override
        public void run() {

            System.out.println("In thread");
            while (true) {
                try {
                    world = (World) SocketConnection.readStream();
                    System.out.println("I've read world");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        
       
        
    }
    
    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException {
        clientTeam = Team.Player1;
        Cell cell1 = new Cell(new Vector2f(100, 100), 20, Team.Player1);
        Cell cell2 = new Cell(new Vector2f(200, 100), 20, Team.Player1);
        Cell cell3 = new Cell(new Vector2f(100, 200), 20, Team.Player2);
        Cell cell4 = new Cell(new Vector2f(300, 200), 20, Team.Player2);
        Cell cell5 = new Cell(new Vector2f(150, 150), 10, Team.Neutral);
        Cell cell6 = new Cell(new Vector2f(400, 400), 20, Team.Player1);

        world = new world.World(cell1, cell2, cell3, cell4, cell5, cell6);
        SocketConnection socketConnection = new SocketConnection(null);
 
        Thread drawThread = new Thread(new DrawRunnable());
        Thread recvThread = new Thread(new RecvRunnable(socketConnection));
        Thread updateThread = new Thread(new UpdateRunnable(world));

        recvThread.start();
        drawThread.start();
       
        updateThread.start();
        drawThread.join();
        recvThread.join();
        updateThread.interrupt();
    }
}
