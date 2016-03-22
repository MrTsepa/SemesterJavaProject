package client;

import world.PlayerInfo;
import world.World;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

class SocketConnection implements ExchangePlayerInfo {

    private static Socket socket;
    static InetAddress addr;
    private static World world;

    private static final int playerCount = 2;

    private static void connect() {
        try {
            addr = InetAddress.getByName(null);
            System.out.println("addr = " + addr);
            socket = new Socket(addr, 8080);
            System.out.println("socket = " + socket);
        } catch (IOException e) {
            System.out.println("Can't accept");
            System.exit(-1);
        } finally {
            System.out.println("closing...");
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Socket closing error");
            }
        }
    }

    private void recv() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

            world = (World) objectInputStream.readObject();
        } catch (IOException e) {
            System.out.println("Recv error");
        } catch (ClassNotFoundException e) {

        }
    }

    private void send(PlayerInfo playerInfo) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

            objectOutputStream.writeObject(playerInfo);

            objectOutputStream.flush();
        } catch (IOException e) {

        }
    }

    @Override
    public World getWorld() {
        recv();
        return world;
    }

    @Override
    public void setSelfPlayerInfo(PlayerInfo playerInfo) {
        send(playerInfo);
    }

    @Override
    public int getPlayerCount() {
        return playerCount;
    }
}