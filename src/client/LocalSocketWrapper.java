package client;

import world.PlayerInfo;
import world.PlayerInfoXY;
import world.World;

/**
 * Этот класс нужен для проверки работы интерфейса
 * Он рисует второй кружок сдвинутым на (200, 200) относительно первого
 * Created by stas on 20.03.16.
 */
class LocalSocketWrapper implements ExchangePlayerInfo {

    World world = new World();

    public LocalSocketWrapper() {
        world.playerInfos.add(new PlayerInfoXY(100, 100));
        world.playerInfos.add(new PlayerInfoXY(300, 300));
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public void setSelfPlayerInfo(PlayerInfo playerInfo) {
        world.playerInfos.set(0, playerInfo);
    }

    @Override
    public int getPlayerCount() {
        return 2;
    }
}
