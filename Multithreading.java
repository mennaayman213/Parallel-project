import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelProcessingGUI extends JFrame {
    private JTextField requestField;
    private JSlider threadSlider;
    private JLabel threadLabel;
    private JButton startButton;
    private JButton stopButton;
    private JCheckBox parallelCheckBox;
    private JTextArea outputArea;
    private JProgressBar progressBar;
    private ExecutorService executor;
    private AtomicInteger completedTasks = new AtomicInteger(0);
    private int totalTasks = 0;
    private long startTime;

    public ParallelProcessingGUI() {
        setTitle("Parallel vs Sequential Request Processor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Request Processing Simulator", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(new Color(33, 150, 243));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Top Controls
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        inputPanel.setBackground(new Color(245, 245, 245));

        requestField = new JTextField(5);
        requestField.setFont(new Font("Arial", Font.PLAIN, 14));

        parallelCheckBox = new JCheckBox("Enable Parallel Processing", true);

        threadLabel = new JLabel("Threads: 1");

        threadSlider = new JSlider(1, 50, 1);
        threadSlider.setMajorTickSpacing(10);
        threadSlider.setMinorTickSpacing(1);
        threadSlider.setPaintTicks(true);
        threadSlider.addChangeListener(e -> threadLabel.setText("Threads: " + threadSlider.getValue()));

        inputPanel.add(new JLabel("Number of Requests:"));
        inputPanel.add(requestField);
        inputPanel.add(parallelCheckBox);
        inputPanel.add(threadLabel);
        inputPanel.add(threadSlider);

        topPanel.add(inputPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        startButton = new JButton("Start Processing");
        styleButton(startButton, new Color(76, 175, 80));

        stopButton = new JButton("Stop");
        styleButton(stopButton, new Color(244, 67, 54));
        stopButton.setEnabled(false);

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        topPanel.add(buttonPanel);

        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(250, 250, 250));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        add(scrollPane, BorderLayout.CENTER);

        // Progress Bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 12));
        progressBar.setForeground(new Color(33, 150, 243));
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
        add(progressPanel, BorderLayout.SOUTH);

        // Events
        startButton.addActionListener(this::startProcessing);
        stopButton.addActionListener(e -> stopProcessing());
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void startProcessing(ActionEvent e) {
        outputArea.setText("");
        completedTasks.set(0);

        try {
            totalTasks = Integer.parseInt(requestField.getText());
            if (totalTasks <= 0) throw new NumberFormatException();

            int numThreads = threadSlider.getValue();
            if (numThreads > totalTasks) {
                JOptionPane.showMessageDialog(this,
                        "Number of threads can't be greater than number of requests!",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            progressBar.setMaximum(totalTasks);
            progressBar.setValue(0);
            startTime = System.currentTimeMillis();

            outputArea.append("Starting " + totalTasks + " requests...\n");
            outputArea.append("Mode: " + (parallelCheckBox.isSelected() ? "PARALLEL" : "SEQUENTIAL") + "\n\n");

            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            requestField.setEnabled(false);
            threadSlider.setEnabled(false);
            parallelCheckBox.setEnabled(false);

            if (parallelCheckBox.isSelected()) {
                executor = Executors.newFixedThreadPool(numThreads);
                outputArea.append("Using " + numThreads + " worker threads\n");

                for (int i = 1; i <= totalTasks; i++) {
                    final int taskId = i;
                    executor.execute(new CustomerRequest(taskId, outputArea, this::updateProgress));
                }

            } else {
                new Thread(() -> {
                    for (int i = 1; i <= totalTasks; i++) {
                        if (Thread.currentThread().isInterrupted()) break;

                        final int taskId = i;
                        SwingUtilities.invokeLater(() ->
                                new CustomerRequest(taskId, outputArea, this::updateProgress).run()
                        );

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                }).start();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of requests!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            enableControls();
        }
    }

    private void stopProcessing() {
        if (executor != null) executor.shutdownNow();
        outputArea.append("\nProcessing stopped by user!\n");
        enableControls();
    }

    private void updateProgress() {
        int completed = completedTasks.incrementAndGet();
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(completed);
            if (completed == totalTasks) {
                long totalTime = (System.currentTimeMillis() - startTime) / 1000;
                outputArea.append("\nAll tasks completed in " + totalTime + " seconds.\n");
                enableControls();
            }
        });
    }

    private void enableControls() {
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        requestField.setEnabled(true);
        threadSlider.setEnabled(true);
        parallelCheckBox.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ParallelProcessingGUI gui = new ParallelProcessingGUI();
            gui.setVisible(true);
        });
    }
}

// CustomerRequest class remains the same
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
