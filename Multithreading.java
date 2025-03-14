/**
 *
 * @author menna
 */

package multithreading;
public class Multithreading {
    public static void main(String[] args) {

        for (int i = 1; i <= 3; i++) {
            Thread thread = new Thread(new Task(i));
            thread.start();
        }
    }
}

class Task implements Runnable {
    private final int taskId;

    public Task(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println("Task " + taskId + " is running on thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Task " + taskId + " is finished.");
    }
}
