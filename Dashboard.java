import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Dashboard extends JFrame {
    private static final String STATS_FILE = "stats.txt";
    private static final String RESULTS_FILE = "results.log";
    private static final String SETTINGS_FILE = "settings.txt";
    private static final String ATTEMPTS_FILE = "attempts.txt";

    private ArrayList<Questions> questions;
    private JPanel mainPanel;

    public Dashboard(ArrayList<Questions> questions) {
        this.questions = questions;

        setTitle("Quiz Application - Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Quiz Application");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createVerticalStrut(6));

        JLabel countLabel = new JLabel("Questions in Bank: " + questions.size());
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        countLabel.setForeground(new Color(51, 65, 85));
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(countLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel actionsPanel = new JPanel();
        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));

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
        actionsPanel.add(startButton);
        actionsPanel.add(Box.createVerticalStrut(12));

        JButton manageButton = createStyledButton("Manage Questions", new Color(79, 70, 229));
        manageButton.addActionListener(e -> {
            if (!verifyPassword()) {
                return;
            }
            new QuestionManager(this, questions);
            dispose();
        });
        actionsPanel.add(manageButton);
        actionsPanel.add(Box.createVerticalStrut(12));

        JButton statsButton = createStyledButton("View Statistics", new Color(14, 116, 144));
        statsButton.addActionListener(e -> showStatsDialog());
        actionsPanel.add(statsButton);
        actionsPanel.add(Box.createVerticalStrut(12));

        JButton settingsButton = createStyledButton("Settings", new Color(71, 85, 105));
        settingsButton.addActionListener(e -> openSettings());
        actionsPanel.add(settingsButton);
        actionsPanel.add(Box.createVerticalStrut(12));

        JButton resetAttemptsButton = createStyledButton("Reset Attempts", new Color(148, 163, 184));
        resetAttemptsButton.addActionListener(e -> resetAttempts());
        actionsPanel.add(resetAttemptsButton);
        actionsPanel.add(Box.createVerticalStrut(12));

        JButton exitButton = createStyledButton("Exit", new Color(100, 116, 139));
        exitButton.addActionListener(e -> System.exit(0));
        actionsPanel.add(exitButton);

        mainPanel.add(actionsPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(260, 44));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

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

    private void showStatsDialog() {
        Stats stats = loadStats();
        if (stats == null || stats.totalQuizzes == 0) {
            JOptionPane.showMessageDialog(this,
                    "No quiz statistics yet. Complete a quiz to see results.",
                    "Statistics", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double average = stats.totalQuestions == 0 ? 0.0
                : (stats.totalCorrect * 100.0) / stats.totalQuestions;

        String lastName = (stats.lastName == null || stats.lastName.isEmpty()) ? "Unknown" : stats.lastName;
        String lastId = (stats.lastId == null || stats.lastId.isEmpty()) ? "N/A" : stats.lastId;
        String bestName = (stats.bestName == null || stats.bestName.isEmpty()) ? "Unknown" : stats.bestName;
        String bestId = (stats.bestId == null || stats.bestId.isEmpty()) ? "N/A" : stats.bestId;

        String message = "Total Quizzes: " + stats.totalQuizzes + "\n" +
                "Last: " + lastName + " (" + lastId + ") - " + stats.lastCorrect + "/" + stats.lastTotal +
                String.format(" (%.1f%%)\n", stats.lastPercent) +
                String.format("Best: %s (%s) - %.1f%%\n", bestName, bestId, stats.bestPercent) +
                String.format("Average Score: %.1f%%", average);

        JOptionPane.showMessageDialog(this, message, "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean verifyPassword() {
        Settings settings = loadSettings();
        String password = settings.password;

        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(this, passwordField,
                "Enter teacher password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) {
            return false;
        }

        String entered = new String(passwordField.getPassword());
        if (!entered.equals(password)) {
            JOptionPane.showMessageDialog(this, "Incorrect password.", "Access Denied",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void openSettings() {
        if (!verifyPassword()) {
            return;
        }

        Settings settings = loadSettings();

        JTextField maxTrialsField = new JTextField(String.valueOf(settings.maxTrials));
        JPasswordField currentPassword = new JPasswordField();
        JPasswordField newPassword = new JPasswordField();
        JPasswordField confirmPassword = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("Max trials per student (0 = no trials):"));
        panel.add(maxTrialsField);
        panel.add(new JLabel("Current password (required to change password):"));
        panel.add(currentPassword);
        panel.add(new JLabel("New password (leave empty to keep current):"));
        panel.add(newPassword);
        panel.add(new JLabel("Confirm new password:"));
        panel.add(confirmPassword);

        int result = JOptionPane.showConfirmDialog(this, panel, "Settings",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        int maxTrials;
        try {
            maxTrials = Integer.parseInt(maxTrialsField.getText().trim());
            if (maxTrials < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Max trials must be 0 or a positive number.",
                    "Invalid Value", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newPasswordValue = new String(newPassword.getPassword()).trim();
        String confirmValue = new String(confirmPassword.getPassword()).trim();
        String currentValue = new String(currentPassword.getPassword());

        if (!newPasswordValue.isEmpty()) {
            if (!currentValue.equals(settings.password)) {
                JOptionPane.showMessageDialog(this, "Current password is incorrect.",
                        "Invalid Password", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPasswordValue.equals(confirmValue)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.",
                        "Invalid Password", JOptionPane.WARNING_MESSAGE);
                return;
            }
            settings.password = newPasswordValue;
        }

        settings.maxTrials = maxTrials;
        saveSettings(settings);
        JOptionPane.showMessageDialog(this, "Settings updated successfully.",
                "Settings", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetAttempts() {
        if (!verifyPassword()) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Reset all student attempts? This cannot be undone.",
                "Confirm Reset", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        File file = new File(ATTEMPTS_FILE);
        if (file.exists()) {
            file.delete();
        }
        Settings settings = loadSettings();
        settings.maxTrials = 1;
        saveSettings(settings);
        JOptionPane.showMessageDialog(this, "Attempts reset. Max trials set to 1.",
                "Reset Attempts", JOptionPane.INFORMATION_MESSAGE);
    }

    private Settings loadSettings() {
        File file = new File(SETTINGS_FILE);
        Settings settings = new Settings();
        settings.password = "admin";
        settings.maxTrials = 1;

        if (!file.exists()) {
            return settings;
        }

        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) {
                settings.password = sc.nextLine().trim();
            }
            if (sc.hasNextLine()) {
                settings.maxTrials = Integer.parseInt(sc.nextLine().trim());
            }
        } catch (Exception e) {
            return settings;
        }

        return settings;
    }

    private void saveSettings(Settings settings) {
        try (java.io.PrintWriter out = new java.io.PrintWriter(SETTINGS_FILE)) {
            out.println(settings.password);
            out.println(settings.maxTrials);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not save settings.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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

            if (lines.size() >= 9) {
                Stats stats = new Stats();
                stats.totalQuizzes = Integer.parseInt(lines.get(0).trim());
                stats.totalCorrect = Integer.parseInt(lines.get(1).trim());
                stats.totalQuestions = Integer.parseInt(lines.get(2).trim());
                stats.bestPercent = Double.parseDouble(lines.get(3).trim());
                stats.bestName = lines.get(4).trim();
                stats.bestId = "";
                stats.lastPercent = Double.parseDouble(lines.get(5).trim());
                stats.lastName = lines.get(6).trim();
                stats.lastId = "";
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
                stats.bestId = "";
                stats.lastName = "";
                stats.lastId = "";
                tokenScanner.close();
                return stats;
            }
            tokenScanner.close();
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    private static class Settings {
        String password;
        int maxTrials;
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
