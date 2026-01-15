import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class Dashboard extends JFrame {
    private ArrayList<Questions> questions;
    private JPanel mainPanel;

    public Dashboard(ArrayList<Questions> questions) {
        this.questions = questions;

        setTitle("Quiz Application - Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient-like background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(25, 118, 210),
                        0, getHeight(), new Color(13, 71, 161));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Title
        JLabel titleLabel = new JLabel("Quiz Application");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(30));

        // Questions count
        JLabel countLabel = new JLabel("Questions in Bank: " + questions.size());
        countLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        countLabel.setForeground(Color.WHITE);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(countLabel);

        mainPanel.add(Box.createVerticalStrut(50));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start Quiz Button
        JButton startButton = createStyledButton("Start Quiz", new Color(76, 175, 80));
        startButton.addActionListener(e -> {
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No questions available! Please add questions first.",
                        "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                new StartQuiz(questions);
                dispose();
            }
        });
        buttonPanel.add(startButton);

        buttonPanel.add(Box.createVerticalStrut(15));

        // Manage Questions Button
        JButton manageButton = createStyledButton("Manage Questions", new Color(156, 39, 176));
        manageButton.addActionListener(e -> {
            new QuestionManager(this, questions);
            dispose();
        });
        buttonPanel.add(manageButton);

        buttonPanel.add(Box.createVerticalStrut(15));

        // View Statistics Button
        JButton statsButton = createStyledButton("View Statistics", new Color(33, 150, 243));
        statsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Total Questions: " + questions.size() + "\n\n" +
                            "You can view detailed statistics after taking a quiz!",
                    "Statistics", JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(statsButton);

        buttonPanel.add(Box.createVerticalStrut(15));

        // Exit Button
        JButton exitButton = createStyledButton("Exit", new Color(244, 67, 54));
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(300, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
}
