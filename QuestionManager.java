import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionManager extends JFrame {
    private ArrayList<Questions> questions;    private JPanel questionsPanel;
    private JScrollPane scrollPane;

    public QuestionManager(Dashboard dashboard, ArrayList<Questions> questions) {
        this.questions = questions;
        setTitle("Question Manager");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Manage Question Bank");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JButton addButton = new JButton("+ Add New Question");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> openAddQuestionDialog());
        headerPanel.add(addButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Questions display panel
        questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setBackground(new Color(33, 150, 243));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            saveQuestionsToFile();
            new Dashboard(questions);
            dispose();
        });
        footerPanel.add(backButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        refreshQuestionsList();
        setVisible(true);
    }

    private void openAddQuestionDialog() {
        JDialog dialog = new JDialog(this, "Add New Question", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Question label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Question:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextArea questionArea = new JTextArea(3, 40);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        JScrollPane qScroll = new JScrollPane(questionArea);
        panel.add(qScroll, gbc);

        // Options
        JTextField[] optionFields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.weightx = 0;
            panel.add(new JLabel("Option " + (i + 1) + ":"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            optionFields[i] = new JTextField(30);
            panel.add(optionFields[i], gbc);
        }

        // Correct answer
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        panel.add(new JLabel("Correct Answer:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        JComboBox<String> answerBox = new JComboBox<>(new String[] { "Option 1", "Option 2", "Option 3", "Option 4" });
        panel.add(answerBox, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton saveBtn = new JButton("Save Question");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveBtn.setBackground(new Color(76, 175, 80));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            String question = questionArea.getText().trim();
            String[] options = new String[4];
            for (int i = 0; i < 4; i++) {
                options[i] = optionFields[i].getText().trim();
            }
            String correctAnswer = optionFields[answerBox.getSelectedIndex()].getText().trim();

            if (question.isEmpty() || correctAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean hasEmpty = false;
            for (String opt : options) {
                if (opt.isEmpty()) {
                    hasEmpty = true;
                    break;
                }
            }

            if (hasEmpty) {
                JOptionPane.showMessageDialog(dialog, "Please fill all options!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            questions.add(new Questions(question, options, correctAnswer));
            JOptionPane.showMessageDialog(dialog, "Question added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            refreshQuestionsList();
        });
        buttonPanel.add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelBtn.setBackground(new Color(244, 67, 54));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);

        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void refreshQuestionsList() {
        questionsPanel.removeAll();

        if (questions.isEmpty()) {
            JLabel emptyLabel = new JLabel("No questions yet. Click 'Add New Question' to get started!");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(new Color(149, 165, 166));
            emptyLabel.setBorder(new EmptyBorder(30, 30, 30, 30));
            questionsPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < questions.size(); i++) {
                addQuestionCard(i);
            }
        }

        questionsPanel.revalidate();
        questionsPanel.repaint();
    }

    private void addQuestionCard(int index) {
        Questions q = questions.get(index);

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        cardPanel.setBackground(new Color(245, 245, 245));
        cardPanel.setMaximumSize(new Dimension(700, 180));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(12, 12, 12, 12)));

        // Question number and text
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setOpaque(false);

        JLabel qNumLabel = new JLabel("Q" + (index + 1) + ".");
        qNumLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        questionPanel.add(qNumLabel);

        JLabel qLabel = new JLabel("<html>" + q.ques + "</html>");
        qLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        questionPanel.add(qLabel);

        cardPanel.add(questionPanel, BorderLayout.WEST);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actionPanel.setOpaque(false);

        JButton editBtn = new JButton("Edit");
        editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        editBtn.setBackground(new Color(33, 150, 243));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        editBtn.addActionListener(e -> editQuestion(index));
        actionPanel.add(editBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        deleteBtn.setBackground(new Color(244, 67, 54));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this question?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                questions.remove(index);
                saveQuestionsToFile();
                refreshQuestionsList();
            }
        });
        actionPanel.add(deleteBtn);

        cardPanel.add(actionPanel, BorderLayout.EAST);

        questionsPanel.add(cardPanel);
        questionsPanel.add(Box.createVerticalStrut(10));
    }

    private void editQuestion(int index) {
        JOptionPane.showMessageDialog(this,
                "Edit functionality:\nTo edit a question, delete it and add a new one with the correct information.",
                "Edit Question", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveQuestionsToFile() {
        try (FileWriter fw = new FileWriter("questions.txt")) {
            for (Questions q : questions) {
                fw.write(q.ques + "\n");
                for (String opt : q.opt) {
                    fw.write(opt + "\n");
                }
                fw.write(q.ans + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving questions!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

