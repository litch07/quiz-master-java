import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Dashboard extends JFrame {
    private static final String STATS_FILE = "stats.txt";
    private ArrayList<Questions> questions;
    private JPanel mainPanel;

    public Dashboard(ArrayList<Questions> questions) {
        this.questions = questions;

        setTitle("Quiz Application - Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with neutral background
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Title
        JLabel titleLabel = new JLabel("Quiz Application");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(30));

        // Questions count
        JLabel countLabel = new JLabel("Questions in Bank: " + questions.size());
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        countLabel.setForeground(new Color(51, 65, 85));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(countLabel);

        mainPanel.add(Box.createVerticalStrut(50));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start Quiz Button
        JButton startButton = createStyledButton("Start Quiz", new Color(37, 99, 235));
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
        JButton manageButton = createStyledButton("Manage Questions", new Color(79, 70, 229));
        manageButton.addActionListener(e -> {
            new QuestionManager(this, questions);
            dispose();
        });
        buttonPanel.add(manageButton);

        buttonPanel.add(Box.createVerticalStrut(15));

        // View Statistics Button
        JButton statsButton = createStyledButton("View Statistics", new Color(14, 116, 144));
        statsButton.addActionListener(e -> {
            Stats stats = loadStats();
            if (stats == null || stats.totalQuizzes == 0) {
                JOptionPane.showMessageDialog(this,
                        "No quiz statistics yet. Complete a quiz to see results.",
                        "Statistics", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            double average = stats.totalQuestions == 0 ? 0.0
                    : (stats.totalCorrect * 100.0) / stats.totalQuestions;

            String lastName = stats.lastName == null || stats.lastName.isEmpty() ? "Unknown" : stats.lastName;
            String bestName = stats.bestName == null || stats.bestName.isEmpty() ? "Unknown" : stats.bestName;

            String message = "Total Quizzes: " + stats.totalQuizzes + "\n" +
                    "Last: " + lastName + " - " + stats.lastCorrect + "/" + stats.lastTotal +
                    String.format(" (%.1f%%)", stats.lastPercent) + "\n" +
                    String.format("Best: %s (%.1f%%)\n", bestName, stats.bestPercent) +
                    String.format("Average Score: %.1f%%", average);

            JOptionPane.showMessageDialog(this, message, "Statistics", JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(statsButton);

        buttonPanel.add(Box.createVerticalStrut(15));

        // Exit Button
        JButton exitButton = createStyledButton("Exit", new Color(100, 116, 139));
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
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

    private Stats loadStats() {
        File file = new File(STATS_FILE);
        if (!file.exists()) {
            return null;
        }

        try (Scanner sc = new Scanner(file)) {
            ArrayList<String> lines = new ArrayList<>();
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }

            if (lines.size() >= 9) {
                Stats stats = new Stats();
                stats.totalQuizzes = Integer.parseInt(lines.get(0).trim());
                stats.totalCorrect = Integer.parseInt(lines.get(1).trim());
                stats.totalQuestions = Integer.parseInt(lines.get(2).trim());
                stats.bestPercent = Double.parseDouble(lines.get(3).trim());
                stats.bestName = lines.get(4).trim();
                stats.lastPercent = Double.parseDouble(lines.get(5).trim());
                stats.lastName = lines.get(6).trim();
                stats.lastCorrect = Integer.parseInt(lines.get(7).trim());
                stats.lastTotal = Integer.parseInt(lines.get(8).trim());
                return stats;
            }

            Scanner tokenScanner = new Scanner(file);
            if (tokenScanner.hasNextInt()) {
                Stats stats = new Stats();
                stats.totalQuizzes = tokenScanner.nextInt();
                stats.totalCorrect = tokenScanner.nextInt();
                stats.totalQuestions = tokenScanner.nextInt();
                stats.bestPercent = tokenScanner.nextDouble();
                stats.lastPercent = tokenScanner.nextDouble();
                stats.lastCorrect = tokenScanner.nextInt();
                stats.lastTotal = tokenScanner.nextInt();
                stats.bestName = "";
                stats.lastName = "";
                tokenScanner.close();
                return stats;
            }
            tokenScanner.close();
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    private static class Stats {
        int totalQuizzes;
        int totalCorrect;
        int totalQuestions;
        double bestPercent;
        String bestName;
        double lastPercent;
        String lastName;
        int lastCorrect;
        int lastTotal;
    }
}
