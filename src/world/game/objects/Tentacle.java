package world.game.objects;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.Serializable;
import java.util.HashSet;

import static utils.Geometry.length;
import static utils.Geometry.normalize;

public class Tentacle implements Drawable, Serializable {

    public Cell parentCell;
    public Cell targetCell;

    /**
     * число в инетрвале [0, 1] отражающее какую часть
     * отрезка между клетками прошла тентакля
     */
    private float distancePart = 0;
    public float getDistancePart() {
        return distancePart;
    }
    public void setDistancePart(float distancePart) {
        float dEnergy = (distancePart - this.distancePart) * getDistance() / 2;
        if (Math.abs(dEnergy) < 10)
            parentCell.setEnergy(parentCell.getEnergy() - (int) dEnergy);
        if (parentCell.getEnergy() < 0)
            this.setState(State.IsDestroyed);
        this.distancePart = distancePart;
    }

    private float cuttedDistancePart = 1;
    public float getCuttedDistancePart() {
        return cuttedDistancePart;
    }
    public void setCuttedDistancePart(float cuttedDistancePart) {
        float dEnergy = (this.getCuttedDistancePart() - cuttedDistancePart) * getDistance() / 2;
        if (Math.abs(dEnergy) < 10)
            targetCell.setEnergy(targetCell.getEnergy() +
                    ((int) dEnergy) * ((parentCell.getTeam() == targetCell.getTeam()) ? -1 : 1));
        if (targetCell.getEnergy() < 0) {
            targetCell.setTeam(parentCell.getTeam());
            targetCell.setEnergy(10);
        }
        if (parentCell.getEnergy() < 0)
            this.setState(State.IsDestroyed);
        this.cuttedDistancePart = cuttedDistancePart;
    }


    static final float triangleHeight = 10;
    private Integer triangleCount = 0;
    public Integer getTriangleCount() {
        return triangleCount;
    }
    public HashSet<Integer> yellowTriangles = new HashSet<>();

    public enum State { MovingForward, IsDestroyed, Still, IsCutted, IsCollided  }
    private State state  = State.MovingForward;
    public synchronized State getState() {
        return state;
    }
    public synchronized void setState(State state) {
        this.state = state;
    }


    public Tentacle(Cell parentCell, Cell targetCell) {
        this.parentCell = parentCell;
        this.targetCell = targetCell;
    }

    public boolean isConfronted() {
        return targetCell.tentacleExists(parentCell);
    }

    public Tentacle getConfronting() {
        return targetCell.getTentacle(parentCell);
    }

    public float getDistance() {
        return length(getDistanceVector());
    }

    public Vector2f getDistanceVector() {
        Vector2f distanceVector = Vector2f.sub(targetCell.getPosition(), parentCell.getPosition());
        return distanceVector;
    }

    public Vector2f getNormalizedDistanceVector() {
        Vector2f normalizedDistanceVector = normalize(getDistanceVector());
        return normalizedDistanceVector;
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        Vector2f distanceVector = getDistanceVector();
        Vector2f normalizedDistanceVector = normalize(distanceVector);
        Vector2f normalizedOrthogonalVector;
        if (distanceVector.y != 0) {
            normalizedOrthogonalVector = normalize(new Vector2f(1, -1 * distanceVector.x / distanceVector.y));
        } else {
            normalizedOrthogonalVector = normalize(new Vector2f(-1 * distanceVector.y / distanceVector.x, 1));
        }
        Vector2f parentBorderPosition = Vector2f.add(parentCell.getPosition(),
                Vector2f.mul(normalizedDistanceVector, parentCell.getRadius()));
        Vector2f targetBorderPosition = Vector2f.sub(targetCell.getPosition(),
                Vector2f.mul(normalizedDistanceVector, targetCell.getRadius()));
        distanceVector = Vector2f.sub(targetBorderPosition, parentBorderPosition);
        Vector2f headPositionVector = Vector2f.add(parentBorderPosition, Vector2f.mul(distanceVector, distancePart));
        triangleCount = 0;
        while (true) {
            ConvexShape polygon = new ConvexShape(headPositionVector,
                    Vector2f.add(Vector2f.sub(headPositionVector, Vector2f.mul(normalizedDistanceVector, triangleHeight)),
                            Vector2f.mul(normalizedOrthogonalVector, triangleHeight/2)),
                    Vector2f.sub(Vector2f.sub(headPositionVector, Vector2f.mul(normalizedDistanceVector, triangleHeight)),
                            Vector2f.mul(normalizedOrthogonalVector, triangleHeight/2))
            );
            if (yellowTriangles.contains(triangleCount))
                polygon.setFillColor(Color.YELLOW);
            else
                polygon.setFillColor(Cell.teamColorMap.get(parentCell.getTeam()));
            renderTarget.draw(polygon);
            if (Float.compare(length(Vector2f.sub(headPositionVector, parentBorderPosition)), triangleHeight) < 0) {
                break;
            }
            headPositionVector = Vector2f.sub(headPositionVector,
                    Vector2f.mul(normalizedDistanceVector, triangleHeight));
            triangleCount++;
        }
        if (this.state == State.IsCutted) {
            Vector2f tailPositionVector = Vector2f.add(parentBorderPosition,
                    Vector2f.mul(distanceVector, cuttedDistancePart));
            tailPositionVector = Vector2f.add(tailPositionVector,
                    Vector2f.mul(normalizedDistanceVector, triangleHeight));
            while (true) {
                ConvexShape polygon = new ConvexShape(tailPositionVector,
                        Vector2f.add(Vector2f.sub(tailPositionVector,
                                Vector2f.mul(normalizedDistanceVector, triangleHeight)),
                                Vector2f.mul(normalizedOrthogonalVector, triangleHeight/2)),
                        Vector2f.sub(Vector2f.sub(tailPositionVector,
                                Vector2f.mul(normalizedDistanceVector, triangleHeight)),
                                Vector2f.mul(normalizedOrthogonalVector, triangleHeight/2))
                );
                polygon.setFillColor(Cell.teamColorMap.get(parentCell.getTeam()));
                renderTarget.draw(polygon);
                if (length(tailPositionVector, targetCell.getPosition()) < targetCell.getRadius()) {
                    break;
                }
                tailPositionVector = Vector2f.add(tailPositionVector,
                        Vector2f.mul(normalizedDistanceVector, triangleHeight));
            }
        }
    }
}
