import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Dashboard extends JFrame {
    private static final String STATS_FILE = "stats.txt";
    private static final String RESULTS_FILE = "results.log";
    private static final String SETTINGS_FILE = "settings.txt";
    private static final String ATTEMPTS_FILE = "attempts.txt";

    public Dashboard(ArrayList<Questions> questions) {
        final ArrayList<Questions> quizQuestions = questions;

        setTitle("Quiz Application - Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Quiz Application");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createVerticalStrut(6));

        JLabel countLabel = new JLabel("Questions in Bank: " + quizQuestions.size());
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        countLabel.setForeground(new Color(51, 65, 85));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(countLabel);

        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(26));

        JPanel actionsPanel = new JPanel();
        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = createStyledButton("Start Quiz", new Color(37, 99, 235));
        startButton.addActionListener(e -> {
            if (quizQuestions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No questions available! Please add questions first.",
                        "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                new StartQuiz(quizQuestions);
                dispose();
            }
        });
        actionsPanel.add(startButton);
        actionsPanel.add(Box.createVerticalStrut(10));

        JButton manageButton = createStyledButton("Manage Questions", new Color(67, 56, 202));
        manageButton.addActionListener(e -> {
            if (!verifyPassword()) {
                return;
            }
            new QuestionManager(this, quizQuestions);
            dispose();
        });
        actionsPanel.add(manageButton);
        actionsPanel.add(Box.createVerticalStrut(10));

        JButton resultsButton = createStyledButton("Results", new Color(15, 118, 110));
        resultsButton.addActionListener(e -> showResultsWindow());
        actionsPanel.add(resultsButton);
        actionsPanel.add(Box.createVerticalStrut(10));

        JButton settingsButton = createStyledButton("Settings", new Color(71, 85, 105));
        settingsButton.addActionListener(e -> openSettings());
        actionsPanel.add(settingsButton);
        actionsPanel.add(Box.createVerticalStrut(10));

        JButton resetDataButton = createStyledButton("Reset Data", new Color(148, 163, 184));
        resetDataButton.addActionListener(e -> resetData());
        actionsPanel.add(resetDataButton);
        actionsPanel.add(Box.createVerticalStrut(10));

        JButton exitButton = createStyledButton("Exit", new Color(100, 116, 139));
        exitButton.addActionListener(e -> System.exit(0));
        actionsPanel.add(exitButton);

        mainPanel.add(actionsPanel);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(260, 44));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

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

    private void showResultsWindow() {
        JDialog dialog = new JDialog(this, "Results", true);
        dialog.setSize(700, 520);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(12, 12));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.setBackground(new Color(248, 250, 252));
        tabs.addTab("Statistics", buildStatsPanel());
        tabs.addTab("Recent Results", buildRecentResultsPanel());
        tabs.addTab("Leaderboard", buildLeaderboardPanel());
        panel.add(tabs, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeButton.setBackground(new Color(100, 116, 139));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.add(closeButton);
        panel.add(footer, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(248, 250, 252));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Stats stats = loadStats();
        if (stats == null || stats.totalQuizzes == 0) {
            JLabel label = new JLabel("No quiz statistics yet. Complete a quiz to see results.");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            label.setForeground(new Color(100, 116, 139));
            panel.add(label);
            return panel;
        }

        double average = stats.totalQuestions == 0 ? 0.0
                : (stats.totalCorrect * 100.0) / stats.totalQuestions;

        String lastName = (stats.lastName == null || stats.lastName.isEmpty()) ? "Unknown" : stats.lastName;
        String lastId = (stats.lastId == null || stats.lastId.isEmpty()) ? "N/A" : stats.lastId;
        String bestName = (stats.bestName == null || stats.bestName.isEmpty()) ? "Unknown" : stats.bestName;
        String bestId = (stats.bestId == null || stats.bestId.isEmpty()) ? "N/A" : stats.bestId;

        panel.add(makeStatLine("Total Quizzes", String.valueOf(stats.totalQuizzes)));
        panel.add(makeStatLine("Average Score", String.format("%.1f%%", average)));
        panel.add(makeStatLine("Last Result", lastName + " (" + lastId + ") - " +
                stats.lastCorrect + "/" + stats.lastTotal +
                String.format(" (%.1f%%)", stats.lastPercent)));
        panel.add(makeStatLine("Best Result", bestName + " (" + bestId + ") - " +
                String.format("%.1f%%", stats.bestPercent)));

        return panel;
    }

    private JPanel buildRecentResultsPanel() {
        List<String> results = readRecentResults(20);
        return buildListPanel(results, "No results yet. Complete a quiz to see results.");
    }

    private JPanel buildLeaderboardPanel() {
        List<String> leaderboard = buildLeaderboard(20);
        return buildListPanel(leaderboard, "No scores yet. Complete a quiz to populate the leaderboard.");
    }

    private JPanel buildListPanel(List<String> lines, String emptyMessage) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        if (lines.isEmpty()) {
            JLabel label = new JLabel(emptyMessage);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            label.setForeground(new Color(100, 116, 139));
            panel.add(label, BorderLayout.CENTER);
            return panel;
        }

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String line : lines) {
            model.addElement(line);
        }
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setBackground(new Color(248, 250, 252));
        list.setBorder(new EmptyBorder(8, 8, 8, 8));
        list.setSelectionBackground(new Color(226, 232, 240));
        list.setSelectionForeground(new Color(30, 41, 59));

        JScrollPane scrollPane = new JScrollPane(list);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel makeStatLine(String label, String value) {
        JPanel line = new JPanel(new BorderLayout(10, 0));
        line.setOpaque(false);
        line.setBorder(new EmptyBorder(6, 2, 6, 2));

        JLabel left = new JLabel(label);
        left.setFont(new Font("Segoe UI", Font.BOLD, 13));
        left.setForeground(new Color(30, 41, 59));

        JLabel right = new JLabel(value);
        right.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        right.setForeground(new Color(51, 65, 85));

        line.add(left, BorderLayout.WEST);
        line.add(right, BorderLayout.EAST);
        return line;
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

    private void resetData() {
        if (!verifyPassword()) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Reset all results, statistics, and attempts? This cannot be undone.",
                "Confirm Reset", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        deleteFileIfExists(new File(ATTEMPTS_FILE));
        deleteFileIfExists(new File(RESULTS_FILE));
        deleteFileIfExists(new File(STATS_FILE));

        Settings settings = loadSettings();
        settings.maxTrials = 1;
        saveSettings(settings);

        JOptionPane.showMessageDialog(this, "Data reset complete. Max trials set to 1.",
                "Reset Data", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteFileIfExists(File file) {
        if (file.exists()) {
            file.delete();
        }
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

    private List<String> readRecentResults(int limit) {
        File file = new File(RESULTS_FILE);
        ArrayList<String> lines = new ArrayList<>();
        if (!file.exists()) {
            return lines;
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
        } catch (Exception e) {
            return lines;
        }

        int start = Math.max(0, lines.size() - limit);
        return lines.subList(start, lines.size());
    }

    private List<String> buildLeaderboard(int limit) {
        File file = new File(RESULTS_FILE);
        Map<String, LeaderEntry> bestByStudent = new HashMap<>();

        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split("\\|");
                    if (parts.length < 4) {
                        continue;
                    }
                    String nameId = parts[1].trim();
                    String percentPart = parts[3].trim();
                    double percent;
                    try {
                        percent = Double.parseDouble(percentPart.replace("%", ""));
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    String name = nameId;
                    String id = "";
                    int open = nameId.lastIndexOf('(');
                    int close = nameId.lastIndexOf(')');
                    if (open >= 0 && close > open) {
                        name = nameId.substring(0, open).trim();
                        id = nameId.substring(open + 1, close).trim();
                    }

                    String key = id.isEmpty() ? name : id;
                    LeaderEntry existing = bestByStudent.get(key);
                    if (existing == null || percent > existing.percent) {
                        bestByStudent.put(key, new LeaderEntry(name, id, percent));
                    }
                }
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }

        ArrayList<LeaderEntry> entries = new ArrayList<>(bestByStudent.values());
        entries.sort(Comparator.comparingDouble((LeaderEntry e) -> e.percent).reversed());

        List<String> lines = new ArrayList<>();
        int count = Math.min(limit, entries.size());
        for (int i = 0; i < count; i++) {
            LeaderEntry entry = entries.get(i);
            String label = String.format("%d. %s (%s) - %.1f%%", i + 1,
                    entry.name,
                    entry.id.isEmpty() ? "N/A" : entry.id,
                    entry.percent);
            lines.add(label);
        }

        return lines;
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

    private static class LeaderEntry {
        String name;
        String id;
        double percent;

        LeaderEntry(String name, String id, double percent) {
            this.name = name;
            this.id = id;
            this.percent = percent;
        }
    }
}
