import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class ResultsWindow extends JFrame {
    public ResultsWindow(ArrayList<Questions> questions, int correctAnswers) {
        setTitle("Quiz Results");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient
        JPanel mainPanel = new JPanel() {
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
        JLabel titleLabel = new JLabel("Quiz Completed!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(40));

        // Score display
        int totalQuestions = questions.size();
        double percentage = (correctAnswers * 100.0) / totalQuestions;

        JPanel scorePanel = new JPanel();
        scorePanel.setOpaque(false);
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Your Score");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(scoreLabel);

        scorePanel.add(Box.createVerticalStrut(15));

        JLabel scoreValue = new JLabel(correctAnswers + " out of " + totalQuestions);
        scoreValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        scoreValue.setForeground(new Color(76, 175, 80));
        scoreValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(scoreValue);

        scorePanel.add(Box.createVerticalStrut(20));

        JLabel percentageLabel = new JLabel(String.format("%.1f%%", percentage));
        percentageLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        percentageLabel.setForeground(new Color(255, 193, 7));
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(percentageLabel);

        mainPanel.add(scorePanel);

        mainPanel.add(Box.createVerticalStrut(40));

        // Grade
        String grade;
        if (percentage >= 90) {
            grade = "A+ (Excellent!)";
        } else if (percentage >= 80) {
            grade = "A (Very Good!)";
        } else if (percentage >= 70) {
            grade = "B (Good!)";
        } else if (percentage >= 60) {
            grade = "C (Satisfactory)";
        } else {
            grade = "F (Try Again)";
        }

        JLabel gradeLabel = new JLabel("Grade: " + grade);
        gradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gradeLabel.setForeground(Color.WHITE);
        gradeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(gradeLabel);

        mainPanel.add(Box.createVerticalStrut(40));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton retakeButton = new JButton("Retake Quiz");
        retakeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        retakeButton.setBackground(new Color(76, 175, 80));
        retakeButton.setForeground(Color.WHITE);
        retakeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        retakeButton.setFocusPainted(false);
        retakeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        retakeButton.addActionListener(e -> {
            new StartQuiz(questions);
            dispose();
        });
        buttonPanel.add(retakeButton);

        buttonPanel.add(Box.createHorizontalStrut(20));

        JButton dashboardButton = new JButton("Go to Dashboard");
        dashboardButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dashboardButton.setBackground(new Color(156, 39, 176));
        dashboardButton.setForeground(Color.WHITE);
        dashboardButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        dashboardButton.setFocusPainted(false);
        dashboardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dashboardButton.addActionListener(e -> {
            new Dashboard(questions);
            dispose();
        });
        buttonPanel.add(dashboardButton);

        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }
}
