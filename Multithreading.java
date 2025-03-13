/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package multithreading;

/**
 *
 * @author menna
 */
//public class Multithreading {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//    }
//    
//}
//package multithreading;

public class Multithreading {
    public static void main(String[] args) {
        // إنشاء 3 ثريدات جديدة
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
        System.out.println("🔹 Task " + taskId + " is running on thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000); // محاكاة عملية طويلة
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("✅ Task " + taskId + " is finished.");
    }
}
