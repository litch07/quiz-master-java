import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    static int correct = 0;
    private ArrayList<Questions> questions;
    private int currentIndex;
    private JLabel questionLabel, progressLabel, scoreLabel;
    private JRadioButton[] radioButtons;
    private JButton nextButton, finishButton;
    private ButtonGroup buttonGroup;
    private JProgressBar progressBar;

    public MainWindow(ArrayList<Questions> questions, int index) {
        this.questions = questions;
        this.currentIndex = index;

        setTitle("Quiz - Question " + (index + 1) + " of " + questions.size());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(245, 245, 245),
                        0, getHeight(), new Color(224, 224, 224));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header panel with progress and score
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        progressLabel = new JLabel("Question " + (currentIndex + 1) + " of " + questions.size());
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerPanel.add(progressLabel, BorderLayout.WEST);

        scoreLabel = new JLabel("Correct: " + correct);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(76, 175, 80));
        headerPanel.add(scoreLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Progress bar
        progressBar = new JProgressBar(0, questions.size());
        progressBar.setValue(currentIndex + 1);
        progressBar.setStringPainted(true);
        progressBar.setString((currentIndex + 1) + "/" + questions.size());
        progressBar.setForeground(new Color(33, 150, 243));
        progressBar.setPreferredSize(new Dimension(0, 20));
        mainPanel.add(progressBar, BorderLayout.NORTH);

        // Questions panel
        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setOpaque(false);
        questionsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Question label
        questionLabel = new JLabel("<html>" + questions.get(currentIndex).ques + "</html>");
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        questionLabel.setForeground(new Color(33, 33, 33));
        questionsPanel.add(questionLabel);
        questionsPanel.add(Box.createVerticalStrut(20));

        // Options
        radioButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        Questions currentQuestion = questions.get(currentIndex);

        for (int i = 0; i < 4; i++) {
            radioButtons[i] = new JRadioButton(currentQuestion.opt[i]);
            radioButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            radioButtons[i].setOpaque(false);
            radioButtons[i].setForeground(new Color(51, 51, 51));
            radioButtons[i].setActionCommand(currentQuestion.opt[i]);
            radioButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            JPanel optionPanel = new JPanel(new BorderLayout());
            optionPanel.setOpaque(false);
            optionPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                    new EmptyBorder(12, 12, 12, 12)));
            optionPanel.add(radioButtons[i], BorderLayout.CENTER);

            buttonGroup.add(radioButtons[i]);
            questionsPanel.add(optionPanel);
            questionsPanel.add(Box.createVerticalStrut(10));
        }

        mainPanel.add(questionsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        nextButton = new JButton("Next Question");
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nextButton.setBackground(new Color(33, 150, 243));
        nextButton.setForeground(Color.WHITE);
        nextButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        nextButton.setFocusPainted(false);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.setPreferredSize(new Dimension(150, 40));
        nextButton.addActionListener(e -> handleNextQuestion());
        buttonPanel.add(nextButton);

        finishButton = new JButton("Finish Quiz");
        finishButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        finishButton.setBackground(new Color(76, 175, 80));
        finishButton.setForeground(Color.WHITE);
        finishButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        finishButton.setFocusPainted(false);
        finishButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        finishButton.setPreferredSize(new Dimension(150, 40));
        finishButton.addActionListener(e -> showResults());

        if (currentIndex == questions.size() - 1) {
            buttonPanel.add(finishButton);
            nextButton.setVisible(false);
        } else {
            buttonPanel.add(nextButton);
            finishButton.setVisible(false);
        }

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        quitButton.setBackground(new Color(244, 67, 54));
        quitButton.setForeground(Color.WHITE);
        quitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        quitButton.setFocusPainted(false);
        quitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quitButton.setPreferredSize(new Dimension(100, 40));
        quitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to quit the quiz? Your progress will be lost.",
                    "Confirm Quit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                correct = 0;
                new Dashboard(questions);
                dispose();
            }
        });
        buttonPanel.add(quitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void handleNextQuestion() {
        if (buttonGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Please select an option before proceeding.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedAnswer = buttonGroup.getSelection().getActionCommand();
        if (selectedAnswer.equals(questions.get(currentIndex).ans)) {
            correct++;
        }

        if (currentIndex + 1 < questions.size()) {
            new MainWindow(questions, currentIndex + 1);
            dispose();
        }
    }

    private void showResults() {
        if (buttonGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Please select an option before finishing.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedAnswer = buttonGroup.getSelection().getActionCommand();
        if (selectedAnswer.equals(questions.get(currentIndex).ans)) {
            correct++;
        }

        new ResultsWindow(questions, correct);
        correct = 0;
        dispose();
    }
}