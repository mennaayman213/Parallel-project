import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ParallelProcessingGUI extends JFrame {

    private JTextField requestField;
    private JButton startButton;
    private JTextArea outputArea;

    public ParallelProcessingGUI() {
        setTitle("Parallel Customer Request Processing");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Number of Requests:"));
        requestField = new JTextField(5);
        topPanel.add(requestField);
        startButton = new JButton("Start Processing");
        topPanel.add(startButton);
        add(topPanel, BorderLayout.NORTH);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Button Action
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText(""); // clear previous output
                try {
                    int numRequests = Integer.parseInt(requestField.getText());
                    ExecutorService executor = Executors.newFixedThreadPool(5);

                    for (int i = 1; i <= numRequests; i++) {
                        executor.execute(new CustomerRequest(i, outputArea));
                    }

                    executor.shutdown();
                    outputArea.append("Submitted " + numRequests + " requests for parallel execution...\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number!");
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ParallelProcessingGUI());
    }
}

class CustomerRequest implements Runnable {
    private final int requestId;
    private static final Random rand = new Random();
    private final JTextArea outputArea;

    public CustomerRequest(int requestId, JTextArea outputArea) {
        this.requestId = requestId;
        this.outputArea = outputArea;
    }

    @Override
    public void run() {
        int duration = rand.nextInt(5) + 1;
        String threadName = Thread.currentThread().getName();
        LocalTime startTime = LocalTime.now();

        appendText("Request #" + requestId + " started on " + threadName +
                " at " + startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                " and will take " + duration + " seconds.");

        try {
            Thread.sleep(duration * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LocalTime endTime = LocalTime.now();
        appendText("Request #" + requestId + " finished on " + threadName +
                " at " + endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                " and actually took: " + duration + " seconds.");
    }

    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> outputArea.append(text + "\n"));
    }
}
