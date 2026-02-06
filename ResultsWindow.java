import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ResultsWindow extends JFrame {
    private static final String STATS_FILE = "stats.txt";
    private static final String RESULTS_FILE = "results.log";
    private static final String ATTEMPTS_FILE = "attempts.txt";

    public ResultsWindow(ArrayList<Questions> questions, int correctAnswers, String playerName, String studentId) {
        setTitle("Quiz Results");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel titleLabel = new JLabel("Quiz Completed!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(12));

        JLabel nameLabel = new JLabel("Student: " + playerName + " (" + studentId + ")");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameLabel.setForeground(new Color(51, 65, 85));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(nameLabel);

        mainPanel.add(Box.createVerticalStrut(30));

        int totalQuestions = questions.size();
        double percentage = (correctAnswers * 100.0) / totalQuestions;
        updateStats(totalQuestions, correctAnswers, percentage, playerName, studentId);
        appendResultLog(totalQuestions, correctAnswers, percentage, playerName, studentId);
        incrementAttempts(studentId);

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

    private void appendResultLog(int totalQuestions, int correctAnswers, double percentage,
                                 String playerName, String studentId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timestamp = LocalDateTime.now().format(formatter);
        String line = String.format(Locale.US, "%s | %s (%s) | %d/%d | %.1f%%",
                timestamp, playerName, studentId, correctAnswers, totalQuestions, percentage);

        try (FileWriter writer = new FileWriter(RESULTS_FILE, true)) {
            writer.write(line + System.lineSeparator());
        } catch (Exception e) {
            System.out.println("Warning: Could not write results log.");
        }
    }

    private void incrementAttempts(String studentId) {
        File file = new File(ATTEMPTS_FILE);
        ArrayList<String> lines = new ArrayList<>();
        boolean updated = false;

        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split("\\|", 2);
                    if (parts.length == 2 && parts[0].trim().equals(studentId)) {
                        int count = 0;
                        try {
                            count = Integer.parseInt(parts[1].trim());
                        } catch (NumberFormatException e) {
                            count = 0;
                        }
                        count += 1;
                        lines.add(studentId + "|" + count);
                        updated = true;
                    } else {
                        lines.add(line);
                    }
                }
            } catch (Exception e) {
                lines.clear();
            }
        }

        if (!updated) {
            lines.add(studentId + "|1");
        }

        try (PrintWriter out = new PrintWriter(file)) {
            for (String line : lines) {
                out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not update attempts.");
        }
    }

    private void updateStats(int totalQuestions, int correctAnswers, double percentage,
                             String playerName, String studentId) {
        int totalQuizzes = 0;
        int totalCorrect = 0;
        int totalQuestionsAll = 0;
        double bestPercent = 0.0;
        String bestName = "";
        String bestId = "";

        Stats existing = readStats();
        if (existing != null) {
            totalQuizzes = existing.totalQuizzes;
            totalCorrect = existing.totalCorrect;
            totalQuestionsAll = existing.totalQuestions;
            bestPercent = existing.bestPercent;
            bestName = existing.bestName;
            bestId = existing.bestId;
        }

        totalQuizzes += 1;
        totalCorrect += correctAnswers;
        totalQuestionsAll += totalQuestions;
        if (percentage >= bestPercent) {
            bestPercent = percentage;
            bestName = playerName;
            bestId = studentId;
        }

        try (PrintWriter out = new PrintWriter(STATS_FILE)) {
            out.println(totalQuizzes);
            out.println(totalCorrect);
            out.println(totalQuestionsAll);
            out.println(String.format(Locale.US, "%.4f", bestPercent));
            out.println(bestName);
            out.println(bestId);
            out.println(String.format(Locale.US, "%.4f", percentage));
            out.println(playerName);
            out.println(studentId);
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

            if (lines.size() >= 11) {
                Stats stats = new Stats();
                stats.totalQuizzes = Integer.parseInt(lines.get(0).trim());
                stats.totalCorrect = Integer.parseInt(lines.get(1).trim());
                stats.totalQuestions = Integer.parseInt(lines.get(2).trim());
                stats.bestPercent = Double.parseDouble(lines.get(3).trim());
                stats.bestName = lines.get(4).trim();
                stats.bestId = lines.get(5).trim();
                stats.lastPercent = Double.parseDouble(lines.get(6).trim());
                stats.lastName = lines.get(7).trim();
                stats.lastId = lines.get(8).trim();
                stats.lastCorrect = Integer.parseInt(lines.get(9).trim());
                stats.lastTotal = Integer.parseInt(lines.get(10).trim());
                return stats;
            }
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
        String bestId;
        double lastPercent;
        String lastName;
        String lastId;
        int lastCorrect;
        int lastTotal;
    }
}
