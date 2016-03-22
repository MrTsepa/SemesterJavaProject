package server_socket;

import org.jsfml.system.Vector2f;

public class World {
    public Vector2f[] array = new Vector2f[2];
    public  Vector2f myVector; 
    public  Vector2f competitor;
    
    public Vector2f getSelfXY(){        
      return myVector;
    }
    public void setSelfXY(Vector2f XY) {
       myVector = XY;
    }
    public Vector2f[] getRemoteXYArray() {
        array[0] = myVector;
        array[1] = competitor; 
        return array;
    }
}
