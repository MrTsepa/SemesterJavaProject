package server;


import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Server {
    public static void main(String[] args) throws IOException{
        Server s = new Server(10);  
        
        int port = 8080;
        s.addService(new Service(), port);
    }
    Map<Integer, Listener> services; // связывает порты с объектами Listener
    Set<Connection> connections; // текущие подключения
    int maxConnections; // максимальное количестов подклчений
    ThreadGroup threadGroup; //все потоки исполнения
    
    // конструктор класса, задаёт значения полей класса 
    public Server(int maxConnections){
        threadGroup = new ThreadGroup(Server.class.getName());
        this.maxConnections = maxConnections;
        services = new HashMap<>();
        connections = new HashSet<>(maxConnections);
    }  
    
    //запускает объект Service на заданно порте
    public void addService(Service service, int port) throws IOException {
        Integer key = new Integer(port);//ключ хеш-таблицы
        //проверяем не занят ли этот порт службой
        if(services.get(key) != null)
            throw new IllegalArgumentException("Порт" + port + "уже используется");
        
        //создаем объект Listener для этого порта
        Listener listener = new Listener(threadGroup, port, service);
        //сохраняем в хеш-таблице
        services.put(key, listener);
        //запускаем Listener
        listener.start();
       
    }   
            
    
    
    // прекращение принятия сервером новых подключений
    public void removeService(int port){
        Integer key = port;
        //ищем в хэш таблице объект Listener, соответствующий заданному порту
        final Listener listener = services.get(key);
        if(listener == null)
            return;
        //остановка Listener
        listener.pleaseStop();
        //удаляем из хэш таблицы
        services.remove(key);
    }
    
    //вложенный класс, слушает заданный порт 
    public class Listener extends Thread{
        ServerSocket listenSocket; // объект ServerSocket ожидает подключений
        int port; // порт, который слушаем
        Service service; // служба по этому порту
        volatile boolean stop = false; //флаг для команды остановки
    
        //конструктор, создает ServerSocket по заданному порту и настраивает его
        public Listener(ThreadGroup group, int port, Service service) throws IOException{
            super(group, "Listener:" + port);
            System.out.println("I am Listener!");
            System.out.println(port);
            listenSocket = new ServerSocket(port);
            //задаем паузу для возможномти прерывания
            listenSocket.setSoTimeout(600000);
            this.port = port;
            this.service = service;
        }
        
        //прекращение подключений
        public void pleaseStop(){
            this.stop = true; 
            this.interrupt();
            try{listenSocket.close();} 
            catch(IOException e){}
        }
        // вызов addConnection
        @Override
        public void run(){
            System.out.println("in run listener");
            while(!stop){
            try{
                Socket client = listenSocket.accept();
                System.out.println("I did accept");
                addConnection(client, service);
                System.out.println("I did addConnection");
            }     
            catch(InterruptedIOException e){}
            catch(IOException e){}
            }
        }
       
    }
    protected void addConnection(Socket socket, Service service)
    {
        if(connections.size() >= maxConnections){
            try{
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.print("Сервер перегружен");
                out.flush();
                socket.close();
            }catch(IOException e){}  
            }else{
                Connection c = new Connection(socket, service);
                connections.add(c);
                //c.start();
                c.run();
            }
    }  
    
    protected void endConnection(Connection c){
        connections.remove(c);
    }
    
    public void setMaxConnections(int max){
        maxConnections = max;
    }
    
    //выводит состояние сервера
    public void displayStatus(PrintWriter out){
        //отображает список всех служб
        Iterator keys = services.keySet().iterator();
        while(keys.hasNext()){
            Integer port = (Integer) keys.next();
            Listener listener = (Listener) services.get(port);
            out.println("служба" + listener.service.getClass().getName() + "по порту" + port);
        }
        //ограничение на число подключений
        out.println("максимальное число подключений" + maxConnections);
        
        //отображает список всех подключений
        Iterator conns = connections.iterator();
        while(conns.hasNext()){
            Connection c = (Connection) conns.next();
            out.println("подключение к" + c.clientSocket.getInetAddress().getHostAddress() + ":" +
            c.clientSocket.getPort() + "по порту" + c.clientSocket.getLocalPort() + 
            "для службы" + c.service.getClass().getName());
        }
        
    }
    //класс для обработки подключений, каждое подключени обладает собсвенным потоком
    public class Connection extends Thread {
        Socket clientSocket;
        Service service;
        
        //конструктор для создания потока и задания полей
         public Connection(Socket clientSocket, Service service){
            //создание потока исполнения
            super("Server.Connection:" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
            this.clientSocket = clientSocket;
            this.service = service;
        
        }
        //передает полученные потоки в службу
        @Override
        public void run(){
            try{
                InputStream in = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream();
               
                System.out.println("Serve started");
               
                service.serve(in, out);
            }
            catch(IOException e) {}
            finally{endConnection(this);}
        }
    }
   
        

