import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelProcessingGUI extends JFrame {

    private JTextField requestField;
    private JButton startButton;
    private JButton stopButton;
    private JTextArea outputArea;
    private JProgressBar progressBar;
    private ExecutorService executor;
    private AtomicInteger completedTasks = new AtomicInteger(0);
    private int totalTasks = 0;

    public ParallelProcessingGUI() {
        setTitle("Parallel Customer Request Processing");
        setSize(600, 500); // زيادة حجم النافذة
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // إضافة هوامش

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        topPanel.add(new JLabel("Number of Requests:"));
        requestField = new JTextField(10);
        requestField.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(requestField);
        
        startButton = new JButton("Start Processing");
        startButton.setFont(new Font("Arial", Font.BOLD, 12));
        startButton.setBackground(new Color(76, 175, 80));
        startButton.setForeground(Color.WHITE);
        topPanel.add(startButton);
        
        stopButton = new JButton("Stop");
        stopButton.setFont(new Font("Arial", Font.BOLD, 12));
        stopButton.setBackground(new Color(244, 67, 54));
        stopButton.setForeground(Color.WHITE);
        stopButton.setEnabled(false);
        topPanel.add(stopButton);
        
        add(topPanel, BorderLayout.NORTH);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Progress Bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 12));
        progressBar.setForeground(new Color(33, 150, 243));
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
        add(progressPanel, BorderLayout.SOUTH);

        // Button Actions
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
                completedTasks.set(0);
                
                try {
                    totalTasks = Integer.parseInt(requestField.getText());
                    progressBar.setMaximum(totalTasks);
                    progressBar.setValue(0);
                    
                    executor = Executors.newFixedThreadPool(5);
                    outputArea.append("Starting " + totalTasks + " parallel requests...\n");
                    outputArea.append("Using 5 worker threads\n\n");
                    
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    requestField.setEnabled(false);

                    for (int i = 1; i <= totalTasks; i++) {
                        executor.execute(new CustomerRequest(i, outputArea, 
                            () -> updateProgress()));
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (executor != null) {
                    executor.shutdownNow();
                    outputArea.append("\nProcessing stopped by user!\n");
                    stopButton.setEnabled(false);
                    startButton.setEnabled(true);
                    requestField.setEnabled(true);
                }
            }
        });

        setVisible(true);
    }
    
    private void updateProgress() {
        int completed = completedTasks.incrementAndGet();
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(completed);
            if (completed == totalTasks) {
                outputArea.append("\nAll tasks completed!\n");
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                requestField.setEnabled(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ParallelProcessingGUI());
    }
}

class CustomerRequest implements Runnable {
    private final int requestId;
    private static final Random rand = new Random();
    private final JTextArea outputArea;
    private final Runnable onComplete;

    public CustomerRequest(int requestId, JTextArea outputArea, Runnable onComplete) {
        this.requestId = requestId;
        this.outputArea = outputArea;
        this.onComplete = onComplete;
    }

    @Override
    public void run() {
        int duration = rand.nextInt(5) + 1;
        String threadName = Thread.currentThread().getName();
        LocalTime startTime = LocalTime.now();

        appendText("Request #" + requestId + " started on " + threadName +
                " at " + startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                " (Duration: " + duration + "s)");

        try {
            Thread.sleep(duration * 1000L);
        } catch (InterruptedException e) {
            appendText("Request #" + requestId + " was interrupted!");
            return;
        }

        LocalTime endTime = LocalTime.now();
        appendText("Request #" + requestId + " completed on " + threadName +
                " at " + endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        
        onComplete.run();
    }

    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }
}
