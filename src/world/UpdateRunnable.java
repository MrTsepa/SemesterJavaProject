package world;

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

    /**
     * Отвечает за прибавление клеткам энергии
     */
    private class CellEnergyIncrease implements Runnable {

        @Override
        public void run() {
            while(true) {
                synchronized (world) {
                    for (Cell cell : world.cellArray) {
                        if (cell.getEnergy() < 100)
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

    /**
     * Отвечает за движение щупалец
     */
    class TentaclesMovement implements Runnable {

        HashMap<Tentacle.State, Float> velocityMap = new HashMap<>();

        TentaclesMovement() {
            velocityMap.put(Tentacle.State.MovingForward, 5f);
            velocityMap.put(Tentacle.State.IsDestroyed, 10f);
        }

        @Override
        public void run() {
            while (true) {
                for (Cell cell : world.cellArray) {
                    Tentacle removedTentacle = null;
                    for (Tentacle tentacle : cell.tentacleSet) {
                        if (tentacle.getState() == Tentacle.State.MovingForward) {
                            float velocity = velocityMap.get(Tentacle.State.MovingForward);
                            float newDistancePart = tentacle.getDistancePart() +
                                    velocity / tentacle.getDistance();
                            if (!tentacle.isConfronted()) {
                                if (newDistancePart > 1) {
                                    tentacle.setDistancePart(1);
                                    tentacle.setState(Tentacle.State.Still);
                                } else {
                                    tentacle.setDistancePart(newDistancePart);
                                }
                            }
                            else {
                                if (newDistancePart > (1 - tentacle.getConfronting().getDistancePart())) {
                                    tentacle.setDistancePart(1 - tentacle.getConfronting().getDistancePart());
                                    if (tentacle.getConfronting().getState() != Tentacle.State.IsDestroyed) {
                                        tentacle.setState(Tentacle.State.IsCollided);
                                        tentacle.getConfronting().setState(Tentacle.State.IsCollided);
                                    }
                                }
                                else {
                                    tentacle.setDistancePart(newDistancePart);
                                }
                            }
                            continue;
                        }
                        if (tentacle.getState() == Tentacle.State.IsCollided) {
                            float velocity = velocityMap.get(Tentacle.State.MovingForward);
                            if (tentacle.getDistancePart() > 0.5) {
                                float newDistancePart = tentacle.getDistancePart() -
                                        velocity / tentacle.getDistance();
                                if (newDistancePart < 0.5) {
                                    tentacle.setDistancePart(0.5f);
                                    tentacle.setState(Tentacle.State.Still);
                                    tentacle.getConfronting().setDistancePart(0.5f);
                                    tentacle.getConfronting().setState(Tentacle.State.Still);
                                }
                                else {
                                    tentacle.setDistancePart(newDistancePart);
                                }
                            }
                            if (tentacle.getDistancePart() < 0.5) {
                                float newDistancePart = tentacle.getDistancePart() +
                                        velocity / tentacle.getDistance();
                                if (newDistancePart > 0.5) {
                                    tentacle.setDistancePart(0.5f);
                                    tentacle.setState(Tentacle.State.Still);
                                    tentacle.getConfronting().setDistancePart(0.5f);
                                    tentacle.getConfronting().setState(Tentacle.State.Still);
                                }
                                else {
                                    tentacle.setDistancePart(newDistancePart);
                                }
                            }
                            continue;
                        }
                        if (tentacle.getState() == Tentacle.State.IsDestroyed) {
                            float velocity = velocityMap.get(Tentacle.State.IsDestroyed);
                            float newDistancePart = tentacle.getDistancePart() -
                                    velocity / tentacle.getDistance();
                            if (newDistancePart < 0) {
                                removedTentacle = tentacle;
                                //cell.removeTentacle(tentacle);
                            }
                            else {
                                tentacle.setDistancePart(newDistancePart);
                            }
                            continue;
                        }
                        if (tentacle.getState() == Tentacle.State.IsCutted) {
                            float velocity = velocityMap.get(Tentacle.State.IsDestroyed);
                            float newDistancePart = tentacle.getDistancePart() -
                                    velocity / tentacle.getDistance();
                            float newCuttedPart = tentacle.getCuttedDistancePart() +
                                    velocity / tentacle.getDistance();
                            if (newDistancePart < 0) {
                                tentacle.setDistancePart(0);
                            }
                            else {
                                tentacle.setDistancePart(newDistancePart);
                            }
                            if (newCuttedPart > 1) {
                                tentacle.setCuttedDistancePart(1);
                            }
                            else {
                                tentacle.setCuttedDistancePart(newCuttedPart);
                            }
                            if (tentacle.getDistancePart() <= 0.01f && tentacle.getCuttedDistancePart() >= 0.99f) {
                                removedTentacle = tentacle;
                                //cell.removeTentacle(tentacle);
                            }
                            continue;
                        }
                    }
                    if (removedTentacle != null) {
                        cell.removeTentacle(removedTentacle);
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

    /**
     * Отвечает за передачу энергии щупальцами
     */
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
