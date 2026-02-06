import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class StartQuiz extends JFrame {
    private static final String SETTINGS_FILE = "settings.txt";
    private static final String ATTEMPTS_FILE = "attempts.txt";

    public StartQuiz(ArrayList<Questions> questions) {
        setTitle("Start Your Quiz");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel titleLabel = new JLabel("Quiz Application");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(18));

        JLabel descLabel = new JLabel("<html><center>Ready to test your knowledge?<br>You are about to answer " +
                questions.size() + " questions.<br>Good luck!</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(51, 65, 85));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(descLabel);

        mainPanel.add(Box.createVerticalStrut(26));

        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel detailLabel = new JLabel("Total Questions: " + questions.size());
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailLabel.setForeground(new Color(51, 65, 85));
        detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(detailLabel);

        detailsPanel.add(Box.createVerticalStrut(6));

        JLabel typeLabel = new JLabel("Multiple Choice (4 Options)");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeLabel.setForeground(new Color(51, 65, 85));
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(typeLabel);

        mainPanel.add(detailsPanel);

        mainPanel.add(Box.createVerticalStrut(26));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        startButton.setBackground(new Color(37, 99, 235));
        startButton.setForeground(Color.WHITE);
        startButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setPreferredSize(new Dimension(150, 44));
        startButton.addActionListener(e -> {
            StudentIdentity identity = promptForIdentity();
            if (identity == null) {
                return;
            }

            Settings settings = loadSettings();
            int maxTrials = settings.maxTrials;
            if (maxTrials == 0) {
                JOptionPane.showMessageDialog(this,
                        "No trials are allowed for this quiz.\nPlease contact your teacher.",
                        "Trials Disabled", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int attempts = getAttempts(identity.studentId);
            if (attempts >= maxTrials) {
                JOptionPane.showMessageDialog(this,
                        "No attempts remaining for this student ID.\nPlease contact your teacher.",
                        "Attempts Limit Reached", JOptionPane.WARNING_MESSAGE);
                return;
            }

            new MainWindow(questions, 0, identity.name, identity.studentId);
            dispose();
        });
        buttonPanel.add(startButton);

        buttonPanel.add(Box.createHorizontalStrut(20));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(100, 116, 139));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(150, 44));
        cancelButton.addActionListener(e -> {
            new Dashboard(questions);
            dispose();
        });
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }

    private StudentIdentity promptForIdentity() {
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("Student Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);

        while (true) {
            int option = JOptionPane.showConfirmDialog(this, panel, "Student Information",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option != JOptionPane.OK_OPTION) {
                return null;
            }

            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and ID are required.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                continue;
            }

            return new StudentIdentity(name, id);
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

    private int getAttempts(String studentId) {
        File file = new File(ATTEMPTS_FILE);
        if (!file.exists()) {
            return 0;
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2 && parts[0].trim().equals(studentId)) {
                    return Integer.parseInt(parts[1].trim());
                }
            }
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    private static class Settings {
        String password;
        int maxTrials;
    }

    private static class StudentIdentity {
        String name;
        String studentId;

        StudentIdentity(String name, String studentId) {
            this.name = name;
            this.studentId = studentId;
        }
    }
}
