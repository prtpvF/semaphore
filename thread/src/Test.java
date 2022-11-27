import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// сипользуется когда есть один ресурс и много потоков используют его
// класс semaphore ограничивает доступ к ресурсу
public class Test {
    public static void main(String[] args) throws InterruptedException {
    ExecutorService service = Executors.newFixedThreadPool(200);
    Connection connection = Connection.getConnection();
        for (int i = 0; i < 200; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection.Work();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.DAYS);


    }
}
// Singleton
class Connection{
    private static  Connection connection = new Connection();
    private int connectionsCount;
    private Semaphore semaphore = new Semaphore(10);


    private Connection(){

    }
    public static Connection getConnection(){
        return connection;
    }
    public void Work() throws InterruptedException {
        semaphore.acquire();
        try {
            doWork();
        } finally {
            semaphore.release();
        }

    }

    private void doWork() throws InterruptedException {
        synchronized (this){

            connectionsCount++;
            System.out.println(connectionsCount);
        }
        Thread.sleep(5000);
         synchronized (this){
             connectionsCount--;
         }
    }


}
