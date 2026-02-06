import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ResultsWindow extends JFrame {
    private static final String STATS_FILE = "stats.txt";

    public ResultsWindow(ArrayList<Questions> questions, int correctAnswers, String playerName) {
        setTitle("Quiz Results");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with neutral background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Title
        JLabel titleLabel = new JLabel("Quiz Completed!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(12));

        JLabel nameLabel = new JLabel("Player: " + playerName);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameLabel.setForeground(new Color(51, 65, 85));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(nameLabel);

        mainPanel.add(Box.createVerticalStrut(30));

        // Score display
        int totalQuestions = questions.size();
        double percentage = (correctAnswers * 100.0) / totalQuestions;
        updateStats(totalQuestions, correctAnswers, percentage, playerName);

        JPanel scorePanel = new JPanel();
        scorePanel.setOpaque(false);
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Your Score");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        scoreLabel.setForeground(new Color(30, 41, 59));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(scoreLabel);

        scorePanel.add(Box.createVerticalStrut(12));

        JLabel scoreValue = new JLabel(correctAnswers + " out of " + totalQuestions);
        scoreValue.setFont(new Font("Segoe UI", Font.BOLD, 30));
        scoreValue.setForeground(new Color(37, 99, 235));
        scoreValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(scoreValue);

        scorePanel.add(Box.createVerticalStrut(18));

        JLabel percentageLabel = new JLabel(String.format("%.1f%%", percentage));
        percentageLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        percentageLabel.setForeground(new Color(14, 116, 144));
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(percentageLabel);

        mainPanel.add(scorePanel);

        mainPanel.add(Box.createVerticalStrut(30));

        // Grade
        String grade;
        if (percentage >= 90) {
            grade = "A+ (Excellent)";
        } else if (percentage >= 80) {
            grade = "A (Very Good)";
        } else if (percentage >= 70) {
            grade = "B (Good)";
        } else if (percentage >= 60) {
            grade = "C (Satisfactory)";
        } else {
            grade = "F (Try Again)";
        }

        JLabel gradeLabel = new JLabel("Grade: " + grade);
        gradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gradeLabel.setForeground(new Color(51, 65, 85));
        gradeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(gradeLabel);

        mainPanel.add(Box.createVerticalStrut(30));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton retakeButton = new JButton("Retake Quiz");
        retakeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        retakeButton.setBackground(new Color(37, 99, 235));
        retakeButton.setForeground(Color.WHITE);
        retakeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        retakeButton.setFocusPainted(false);
        retakeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        retakeButton.addActionListener(e -> {
            new StartQuiz(questions);
            dispose();
        });
        buttonPanel.add(retakeButton);

        buttonPanel.add(Box.createHorizontalStrut(16));

        JButton dashboardButton = new JButton("Go to Dashboard");
        dashboardButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dashboardButton.setBackground(new Color(100, 116, 139));
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

    private void updateStats(int totalQuestions, int correctAnswers, double percentage, String playerName) {
        int totalQuizzes = 0;
        int totalCorrect = 0;
        int totalQuestionsAll = 0;
        double bestPercent = 0.0;
        String bestName = "";

        File file = new File(STATS_FILE);
        if (file.exists()) {
            Stats existing = readStats();
            if (existing != null) {
                totalQuizzes = existing.totalQuizzes;
                totalCorrect = existing.totalCorrect;
                totalQuestionsAll = existing.totalQuestions;
                bestPercent = existing.bestPercent;
                bestName = existing.bestName;
            }
        }

        totalQuizzes += 1;
        totalCorrect += correctAnswers;
        totalQuestionsAll += totalQuestions;
        if (percentage >= bestPercent) {
            bestPercent = percentage;
            bestName = playerName;
        }

        try (PrintWriter out = new PrintWriter(STATS_FILE)) {
            out.println(totalQuizzes);
            out.println(totalCorrect);
            out.println(totalQuestionsAll);
            out.println(String.format(Locale.US, "%.4f", bestPercent));
            out.println(bestName);
            out.println(String.format(Locale.US, "%.4f", percentage));
            out.println(playerName);
            out.println(correctAnswers);
            out.println(totalQuestions);
        } catch (Exception e) {
            System.out.println("Warning: Could not save statistics.");
        }
    }

    private Stats readStats() {
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
