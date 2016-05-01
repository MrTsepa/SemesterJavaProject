package world;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;
import world.game.objects.Cell;
import world.game.objects.Tentacle;

import java.util.HashMap;

/**
 * Обновляет мир несколькими нитями
 * запускается как на сервере так и на клиенте
 */
public class UpdateRunnable implements Runnable {
    private final World world;

    public UpdateRunnable(World world) {
        this.world = world;
    }

    private class CellEnergyIncrease implements Runnable {

        @Override
        public void run() {
            while(true) {
                synchronized (world) {
                    for (Cell cell : world.cellArray) {
                        cell.setEnergy(cell.getEnergy() + 1);
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    class TentaclesMovement implements Runnable {

        HashMap<Tentacle.State, Float> velocityMap = new HashMap<>();

        TentaclesMovement() {
            velocityMap.put(Tentacle.State.MovingForward, 5f);
            velocityMap.put(Tentacle.State.MovingBackward, -1*10f);
        }

        @Override
        public void run() {
            while (true) {
                for (Cell cell : world.cellArray) {
                    for (Tentacle tentacle : cell.tentacleSet) {
                        if (tentacle.state != Tentacle.State.Still) {
                            float velocity = velocityMap.get(tentacle.state);
                            float newDistancePart = tentacle.getDistancePart() +
                                    velocity / tentacle.getDistance();
                            float collisionPoint = 1;
                            if (tentacle.targetCell.tentacleExists(cell)) {
                                collisionPoint = 0.5f;
                            }

                            if (newDistancePart > collisionPoint) {
                                tentacle.setDistancePart(collisionPoint);
                                if (tentacle.targetCell.tentacleExists(cell)) {
                                    tentacle.targetCell.getTentacle(cell).setDistancePart(0.5f);
                                }
                                tentacle.state = Tentacle.State.Still;
                            } else {
                                tentacle.setDistancePart(newDistancePart);
                                if (tentacle.targetCell.tentacleExists(cell)) {
                                    tentacle.targetCell.getTentacle(cell).setDistancePart(1-newDistancePart);
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    class YellowTrianglesMovement implements Runnable {

        @Override
        public void run() {

        }
    }

    @Override
    public void run() {
        Thread energyThread = new Thread(new CellEnergyIncrease());
        Thread tentacleThread = new Thread(new TentaclesMovement());
        energyThread.start();
        tentacleThread.start();
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                energyThread.interrupt();
                tentacleThread.interrupt();
                break;
            }
        }
    }
}
