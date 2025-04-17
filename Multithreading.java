import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelProcessingGUI extends JFrame {
    private JTextField requestField;
    private JButton startButton;
    private JButton stopButton;
    private JCheckBox parallelCheckBox;
    private JTextArea outputArea;
    private JProgressBar progressBar;
    private ExecutorService executor;
    private AtomicInteger completedTasks = new AtomicInteger(0);
    private int totalTasks = 0;

    public ParallelProcessingGUI() {
        setTitle("Parallel/Sequential Processing");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        inputPanel.add(new JLabel("Number of Requests:"));
        requestField = new JTextField(10);
        requestField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(requestField);

        parallelCheckBox = new JCheckBox("Enable Parallel Processing", true);
        inputPanel.add(parallelCheckBox);
        topPanel.add(inputPanel);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        startButton = new JButton("Start Processing");
        styleButton(startButton, new Color(76, 175, 80));
        buttonPanel.add(startButton);

        stopButton = new JButton("Stop");
        styleButton(stopButton, new Color(244, 67, 54));
        stopButton.setEnabled(false);
        buttonPanel.add(stopButton);

        topPanel.add(buttonPanel);
        add(topPanel, BorderLayout.NORTH);

        
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 12));
        progressBar.setForeground(new Color(33, 150, 243));
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
        add(progressPanel, BorderLayout.SOUTH);

        
        startButton.addActionListener(this::startProcessing);
        stopButton.addActionListener(e -> stopProcessing());
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
    }

    private void startProcessing(ActionEvent e) {
        outputArea.setText("");
        completedTasks.set(0);
        
        try {
            totalTasks = Integer.parseInt(requestField.getText());
            progressBar.setMaximum(totalTasks);
            progressBar.setValue(0);
            
            outputArea.append("Starting " + totalTasks + " requests...\n");
            outputArea.append("Mode: " + (parallelCheckBox.isSelected() ? "PARALLEL" : "SEQUENTIAL") + "\n\n");
            
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            requestField.setEnabled(false);
            parallelCheckBox.setEnabled(false);

            if (parallelCheckBox.isSelected()) {
                
                executor = Executors.newFixedThreadPool(5);
                outputArea.append("Using 5 worker threads\n");
                
                for (int i = 1; i <= totalTasks; i++) {
                    final int taskId = i;
                    executor.execute(new CustomerRequest(taskId, outputArea, this::updateProgress));
                }
            } else {
                
                new Thread(() -> {
                    for (int i = 1; i <= totalTasks; i++) {
                        if (Thread.currentThread().isInterrupted()) break;
                        
                        final int taskId = i;
                        SwingUtilities.invokeLater(() -> {
                            new CustomerRequest(taskId, outputArea, this::updateProgress).run();
                        });
                        
                        try {
                            Thread.sleep(100); 
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                }).start();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            enableControls();
        }
    }

    private void stopProcessing() {
        if (executor != null) {
            executor.shutdownNow();
        }
        outputArea.append("\nProcessing stopped by user!\n");
        enableControls();
    }

    private void updateProgress() {
        int completed = completedTasks.incrementAndGet();
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(completed);
            if (completed == totalTasks) {
                outputArea.append("\nAll tasks completed!\n");
                enableControls();
            }
        });
    }

    private void enableControls() {
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        requestField.setEnabled(true);
        parallelCheckBox.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ParallelProcessingGUI gui = new ParallelProcessingGUI();
            gui.setVisible(true);
        });
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

        appendText("Request #" + requestId + " started" + 
                 (threadName.contains("main") ? "" : " on " + threadName) +
                 " (Duration: " + duration + "s)");

        try {
            Thread.sleep(duration * 1000L);
        } catch (InterruptedException e) {
            appendText("Request #" + requestId + " was interrupted!");
            return;
        }

        appendText("Request #" + requestId + " completed");
        onComplete.run();
    }

    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }
}
