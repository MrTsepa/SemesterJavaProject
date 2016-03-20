import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.*;
import org.jsfml.window.VideoMode;

public class HelloJSFML {
    public static void main(String args[]) {
        RenderWindow window = new RenderWindow();
        window.create(new VideoMode(640, 480), "Hello JSFML!");
        window.setFramerateLimit(30);

        LocalSocketWrapper socketConnection = new LocalSocketWrapper();

        CircleShape[] circleShapes = new CircleShape[socketConnection.getPlayerCount()];
        circleShapes[0] = new CircleShape(50);
        circleShapes[1] = new CircleShape(50);
        Vector2f[] XYArray;
        Vector2f selfXY = socketConnection.getSelfXY();

        circleShapes[0].setFillColor(Color.RED);
        circleShapes[1].setFillColor(Color.GREEN);

        while (window.isOpen()) {
            for (Event event : window.pollEvents()) {
                if (event.type == Event.Type.CLOSED) {
                    window.close();
                }
                if (event.type == Event.Type.KEY_PRESSED) {
                    if (event.asKeyEvent().key == Keyboard.Key.LEFT) {
                        socketConnection.setSelfXY(new Vector2f(selfXY.x - 10, selfXY.y));
                    }
                    if (event.asKeyEvent().key == Keyboard.Key.RIGHT) {
                        socketConnection.setSelfXY(new Vector2f(selfXY.x + 10, selfXY.y));
                    }
                    if (event.asKeyEvent().key == Keyboard.Key.UP) {
                        socketConnection.setSelfXY(new Vector2f(selfXY.x, selfXY.y - 10));
                    }
                    if (event.asKeyEvent().key == Keyboard.Key.DOWN) {
                        socketConnection.setSelfXY(new Vector2f(selfXY.x, selfXY.y + 10));
                    }
                    selfXY = socketConnection.getSelfXY();
                }
            }

            XYArray = socketConnection.getRemoteXYArray();
            for (int i = 0; i < circleShapes.length; i++) {
                circleShapes[i].setPosition(XYArray[i]);
            }

            window.clear(Color.BLACK);
            for (CircleShape shape: circleShapes) {
                window.draw(shape);
            }

            window.display();
        }
    }
}

