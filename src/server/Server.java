import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    Map services; // связывает порты с объектами Listener
    Set connections; // текущие подключения
    int maxConnections; // максимальное количестов подклчений
    ThreadGroup threadGroup; //все потоки исполнения
    
    // конструктор класса, задаёт значения полей класса 
    public Server(int maxConnections){
        threadGroup = new ThreadGroup(Server.class.getName());
        this.maxConnections = maxConnections;
        services = new HashMap();
        connections = new HashSet(maxConnections);
    }  
    
    //запускает объект сервер на заданно порте
    public void addService(Service service, int port) throws IOException
    {
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
        Integer key = new Integer(port);
        //ищем в хэш таблице объект Listener, соответствующий заданному порту
        final Listener listener = (Listener) services.get(key);
        if(listener == null)
            return;
        //остановка Listener
        listener.pleaseStop();
        //удаляем из хэш таблицы
        services.remove(key);
    }
    
    //вложенный класс, слушает заданный порт 
    public class Listener extends Thread{
        ServerSocket listen_socket; // объект ServerSocket ожидает подключений
        int port; // порт, который слушаем
        Service service; // служба по этому порту
        volatile boolean stop = false; //флаг для команды остановки
    
        //конструктор, создает ServerSocket по заданному порту и настраивает его
        public Listener(ThreadGroup group, int port, Service service) throws IOException{
            super(group, "Listener:" + port);
            listen_socket = new ServerSocket(port);
            //задаем паузу для возможномти прерывания
            listen_socket.setSoTimeout(600000);
            this.port = port;
            this.service = service;
        }
        
        //прекращение подключений
        public void pleaseStop(){
            this.stop = true; 
            this.interrupt();
            try{listen_socket.close();} 
            catch(IOException e){}
        }
        // вызов addConnection
        public void run(){
            while(!stop){
            try{
                Socket client = listen_socket.accept();
                addConnection(client, service);
            }     
            catch(InterruptedIOException e){}
            catch(IOException e){}
            }
        }
       
    }
    protected void addConnection(Socket s, Service service)
    {
        if(connections.size() >= maxConnections){
            try{
                PrintWriter out = new PrintWriter(s.getOutputStream());
                out.print("Сервер перегружен");
                out.flush();
                s.close();
            }catch(IOExcepion e){}  
            }else{
                Connection c = new Connection(s, service);
                connections.add(c);
                c.start();
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
            out.println("подключение к" + c.client.getInetAddress().getHostAddress() + ":" +
            c.client.getPort() + "по порту" + c.client.getLocalPort() + 
            "для службы" + c.service.getClass().getName());
        }
        
    }
    //класс для обработки подключений, каждое подключени обладает собсвенным потоком
    public class Connection extends Thread {
        Socket client;
        Service service;
        
        //конструктор для создания потока и задания полей
         public Connection(Socket client, Service service){
            //создание потока исполнения
            super("Server.Connection:" + client.getInetAddress().getHostAddress() + ":" + client.getPort());
            this.client = client;
            this.service = service;
        
        }
        //передает полученные потоки в службу
        public void run(){
            try{
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();
                service.serve(in, out);
            }
            catch(IOException e) {}
            finally{endConnection(this);}
        }
    }


}

