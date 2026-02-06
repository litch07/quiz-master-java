import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    static int correct = 0;
    private ArrayList<Questions> questions;
    private int currentIndex;
    private String playerName;
    private String studentId;
    private JLabel questionLabel, progressLabel, scoreLabel;
    private JRadioButton[] radioButtons;
    private JButton nextButton, finishButton;
    private ButtonGroup buttonGroup;
    private JProgressBar progressBar;

    public MainWindow(ArrayList<Questions> questions, int index, String playerName, String studentId) {
        this.questions = questions;
        this.currentIndex = index;
        this.playerName = playerName;
        this.studentId = studentId;

        setTitle("Quiz - Question " + (index + 1) + " of " + questions.size());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        progressLabel = new JLabel("Question " + (currentIndex + 1) + " of " + questions.size());
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        progressLabel.setForeground(new Color(30, 41, 59));
        headerPanel.add(progressLabel, BorderLayout.WEST);

        scoreLabel = new JLabel("Correct: " + correct);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(37, 99, 235));
        headerPanel.add(scoreLabel, BorderLayout.EAST);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(headerPanel);
        topPanel.add(Box.createVerticalStrut(6));

        progressBar = new JProgressBar(0, questions.size());
        progressBar.setValue(currentIndex + 1);
        progressBar.setStringPainted(true);
        progressBar.setString((currentIndex + 1) + "/" + questions.size());
        progressBar.setForeground(new Color(37, 99, 235));
        progressBar.setPreferredSize(new Dimension(0, 18));
        topPanel.add(progressBar);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setOpaque(false);
        questionsPanel.setBorder(new EmptyBorder(12, 6, 12, 6));

        questionLabel = new JLabel("<html>" + questions.get(currentIndex).ques + "</html>");
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        questionLabel.setForeground(new Color(33, 33, 33));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionsPanel.add(questionLabel);
        questionsPanel.add(Box.createVerticalStrut(14));

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
            optionPanel.setBackground(Color.WHITE);
            optionPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                    new EmptyBorder(10, 12, 10, 12)));
            optionPanel.add(radioButtons[i], BorderLayout.CENTER);
            optionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            optionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, optionPanel.getPreferredSize().height));

            buttonGroup.add(radioButtons[i]);
            questionsPanel.add(optionPanel);
            questionsPanel.add(Box.createVerticalStrut(8));
        }

        mainPanel.add(questionsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttonPanel.setOpaque(false);

        nextButton = new JButton("Next Question");
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nextButton.setBackground(new Color(37, 99, 235));
        nextButton.setForeground(Color.WHITE);
        nextButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        nextButton.setFocusPainted(false);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.setPreferredSize(new Dimension(150, 38));
        nextButton.addActionListener(e -> handleNextQuestion());
        buttonPanel.add(nextButton);

        finishButton = new JButton("Finish Quiz");
        finishButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        finishButton.setBackground(new Color(14, 116, 144));
        finishButton.setForeground(Color.WHITE);
        finishButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        finishButton.setFocusPainted(false);
        finishButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        finishButton.setPreferredSize(new Dimension(150, 38));
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
        quitButton.setBackground(new Color(100, 116, 139));
        quitButton.setForeground(Color.WHITE);
        quitButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        quitButton.setFocusPainted(false);
        quitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quitButton.setPreferredSize(new Dimension(100, 38));
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
            new MainWindow(questions, currentIndex + 1, playerName, studentId);
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

        new ResultsWindow(questions, correct, playerName, studentId);
        correct = 0;
        dispose();
    }
}
