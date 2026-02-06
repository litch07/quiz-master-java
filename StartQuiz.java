import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class StartQuiz extends JFrame {
    public StartQuiz(ArrayList<Questions> questions) {
        setTitle("Start Your Quiz");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel with a neutral background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(60, 40, 60, 40));

        // Title
        JLabel titleLabel = new JLabel("Quiz Application");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(30));

        // Description
        JLabel descLabel = new JLabel("<html><center>Ready to test your knowledge?<br>You are about to answer " +
                questions.size() + " questions.<br>Good luck!</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(new Color(51, 65, 85));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(descLabel);

        mainPanel.add(Box.createVerticalStrut(50));

        // Quiz details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel detailLabel = new JLabel("Total Questions: " + questions.size());
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailLabel.setForeground(new Color(51, 65, 85));
        detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(detailLabel);

        detailsPanel.add(Box.createVerticalStrut(10));

        JLabel typeLabel = new JLabel("Multiple Choice (4 Options)");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeLabel.setForeground(new Color(51, 65, 85));
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(typeLabel);

        mainPanel.add(detailsPanel);

        mainPanel.add(Box.createVerticalStrut(50));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startButton.setBackground(new Color(37, 99, 235));
        startButton.setForeground(Color.WHITE);
        startButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.addActionListener(e -> {
            String playerName = promptForName();
            if (playerName == null) {
                return;
            }
            new MainWindow(questions, 0, playerName);
            dispose();
        });
        buttonPanel.add(startButton);

        buttonPanel.add(Box.createHorizontalStrut(20));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cancelButton.setBackground(new Color(100, 116, 139));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(150, 50));
        cancelButton.addActionListener(e -> {
            new Dashboard(questions);
            dispose();
        });
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }

    private String promptForName() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter your name:", "Player Name",
                    JOptionPane.PLAIN_MESSAGE);
            if (input == null) {
                return null;
            }
            String name = input.trim();
            if (!name.isEmpty()) {
                return name;
            }
            JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Invalid Name",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
