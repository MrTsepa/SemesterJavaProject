package client;

import org.jsfml.system.Vector2f;
import world.PlayerInfo;
import world.World;

interface ExchangePlayerInfo {
    World getWorld();
//    PlayerInfo getSelfPlayerInfo();
    void setSelfPlayerInfo(PlayerInfo playerInfo);
    int getPlayerCount();
}
