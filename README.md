# Quiz Master

[![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/GUI-Swing-007396?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

A professional interactive quiz application built with **Java Swing** for the Object-Oriented Programming Lab at United International University. Features a modern Material Design interface for creating, managing, and taking quizzes with real-time feedback and comprehensive performance tracking.

## Features

### Quiz Taking Experience
- **Interactive Quiz Interface** - Live progress tracking and real-time scoring
- **Progress Bar** - Visual feedback on quiz completion
- **Instant Feedback** - Score updates as you answer each question
- **Professional Results** - Detailed performance breakdown with letter grades

### Question Management
- **GUI-Based Management** - Add, delete, and manage questions without file editing
- **Input Validation** - Comprehensive validation ensures data integrity
- **Instant Persistence** - Questions auto-saved to `questions.txt`
- **Unique Questions** - Prevents duplicate question entries

### Grading System
- **Automatic Scoring** - Real-time calculation of quiz performance
- **Letter Grades** - A+, A, B, C, F based on percentage
- **Performance Feedback** - Clear feedback on quiz results
- **Retake Option** - Ability to retake quizzes for improvement

### Modern UI
- **Material Design 2019** - Professional color palette and design
- **Gradient Backgrounds** - Visually appealing interface
- **Color-Coded Actions** - Intuitive button colors (green=success, red=cancel, blue=navigation)
- **Responsive Layout** - Clean, organized interface elements

## Tech Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Java 8+ |
| **GUI Framework** | Swing |
| **Storage** | Text file (questions.txt) |
| **Architecture** | Component-based (MVC-like) |
| **Dependencies** | None (Pure Java) |

## Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher
  - Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
  - Verify installation: `java -version`
- **Git** (optional, for cloning)

## Getting Started

### Installation
```bash
# Clone the repository
git clone https://github.com/litch07/quiz-master-java.git
cd quiz-master-java

# Compile all Java files
javac *.java

# Run the application
java Main
```

The application will launch with a Dashboard containing four main options:
1. **Start Quiz** - Begin taking a quiz
2. **Manage Questions** - Add/delete questions
3. **Statistics** - View quiz statistics
4. **Exit** - Close the application

### Portable Build (Release Asset)
If you download the release ZIP, it includes QuizMaster.jar, questions.txt, and launcher scripts.
Run un.bat on Windows or un.sh on macOS/Linux. Java 8+ is required.

## User Guide

### Adding Questions

1. From the Dashboard, click **"Manage Questions"**
2. Click **"+ Add New Question"** button
3. Enter your question text in the text area
4. Fill in all four multiple-choice options
5. Select the correct answer from the dropdown menu
6. Click **"Save Question"** - changes are instantly saved

**Requirements:**
- Questions must have unique text
- All four options must be filled
- The correct answer must match one of the options exactly

### Taking a Quiz

1. From the Dashboard, click **"Start Quiz"**
2. Review the quiz information and click **"Start Quiz"** to begin
3. Read each question and select your answer by clicking one of the four option buttons
4. Click **"Next Question"** to proceed
5. On the final question, the button changes to **"Finish Quiz"**
6. Submit your answers to see detailed results

**Scoring:**
- Each correct answer = 1 point
- Progress bar shows your position in the quiz
- Score updates in real-time as you answer

### Grading Scale

| Letter Grade | Percentage | Description |
|--------------|------------|-------------|
| **A+** | 90-100% | Excellent |
| **A** | 80-89% | Very Good |
| **B** | 70-79% | Good |
| **C** | 60-69% | Satisfactory |
| **F** | Below 60% | Needs Improvement |

## Question File Format

Questions are stored in `questions.txt` in a simple, human-readable format:
```
What is the capital of France?
London
Berlin
Paris
Madrid
Paris
```

**Format Specification:**
- Line 1: Question text
- Lines 2-5: Four answer options
- Line 6: Correct answer (must match one of the options exactly)
- Each question is 6 consecutive lines
- Blank lines separate questions

**Example with Multiple Questions:**
```
What is 2 + 2?
3
4
5
6
4

Who wrote Romeo and Juliet?
Christopher Marlowe
William Shakespeare
Ben Jonson
John Webster
William Shakespeare
```

## Architecture

### Class Structure
```
Main.java
+-- Loads questions from questions.txt
+-- Initializes Dashboard

Dashboard.java
+-- Navigation hub with 4 main buttons
+-- Connects to:
    +-- StartQuiz.java -> Quiz flow
    +-- QuestionManager.java -> CRUD operations
    +-- Exit

StartQuiz.java
+-- Quiz confirmation screen
    +-- MainWindow.java -> Main quiz interface
        +-- ResultsWindow.java -> Results & grading

Questions.java
+-- Data model (question, options, answer)

QuestionManager.java
+-- GUI for question management
    +-- Saves/loads from questions.txt

ResultsWindow.java
+-- Displays results with grading system
```

### Data Flow
```
questions.txt
    |
Main.java (loads & parses)
    |
Dashboard (navigation hub)
    +-> Start Quiz Flow
    |   +-> StartQuiz (intro)
    |   +-> MainWindow (quiz interface)
    |   +-> ResultsWindow (results & grading)
    |
    +-> Manage Questions
        +-> View all questions
        +-> Add new question
        +-> Delete question
```

## Design System

**Material Design 2019 Color Palette:**

| Element | Color Code | Purpose |
|---------|-----------|---------|
| Primary Gradient | RGB(25,118,210) -> RGB(13,71,161) | Main UI background |
| Success Action | RGB(76,175,80) | Start, Save, Retake buttons |
| Secondary Action | RGB(33,150,243) | Navigation buttons |
| Destructive Action | RGB(244,67,54) | Quit, Delete, Cancel buttons |
| Accent | RGB(156,39,176) | Dashboard highlights |
| Highlight | RGB(255,193,7) | Percentage display |

**Typography:** Segoe UI font family for clean, modern appearance

## Project Structure
```
quiz-master-java/
+-- Main.java              # Entry point (question loading)
+-- Dashboard.java         # Main navigation hub
+-- StartQuiz.java         # Quiz introduction screen
+-- MainWindow.java        # Interactive quiz interface
+-- ResultsWindow.java     # Results & grading display
+-- QuestionManager.java   # Question CRUD operations
+-- Questions.java         # Question data model
+-- questions.txt          # Question database
+-- .gitignore            # Git configuration
+-- LICENSE               # MIT License
+-- README.md             # This file
```

## Educational Context

**Institution:** United International University (UIU)  
**Course:** Object-Oriented Programming Lab  

**OOP Concepts Demonstrated:**
- Object-oriented design principles
- GUI development with Swing
- Event-driven programming
- File I/O and data persistence
- Input validation and error handling
- Component-based architecture
- Real-time data processing

## Future Enhancements

- [ ] User authentication and profiles
- [ ] Quiz categories and difficulty levels
- [ ] Question shuffling and randomization
- [ ] Timer-based quizzes with countdown
- [ ] Quiz history and performance analytics
- [ ] Database integration (MySQL/SQLite)
- [ ] PDF report generation
- [ ] Dark mode theme
- [ ] Sound effects and animations
- [ ] Multiplayer/competitive mode

## Testing

**Test Checklist:**

1. **Question Management**
```
   - Add 5-10 test questions
   - Verify unique question validation
   - Test deletion functionality
   - Confirm persistence after restart
```

2. **Quiz Taking**
```
   - Take full quiz
   - Verify progress bar updates
   - Check real-time scoring
   - Validate final results
```

3. **Grading System**
```
   - Test each grade tier (A+, A, B, C, F)
   - Verify percentage calculations
   - Confirm retake functionality
```

## Troubleshooting

**Problem:** "Exception in thread 'main'"
- **Solution:** Ensure all Java files are compiled: `javac *.java`

**Problem:** Questions not saving
- **Solution:** Verify `questions.txt` is in the same directory as `.class` files

**Problem:** Compilation errors
- **Solution:** Check Java version: `javac -version` (must be 8+)

**Problem:** GUI doesn't display (Linux)
- **Solution:** Set display variable: `export DISPLAY=:0`

## Contributing

Contributions are welcome! Here's how:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Make your changes with clear commit messages
4. Test thoroughly
5. Submit a pull request with detailed description

**Ideas for Contributions:**
- Add user authentication system
- Implement quiz categories
- Create timer functionality
- Add database support
- Improve UI/UX design
- Write unit tests

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## Author

Sadid Ahmed

---

<div align="center">

**If you find this project useful, please consider giving it a star!**

Made with care for academic excellence

</div>

