# Quiz Master

[![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/GUI-Swing-007396?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

A professional quiz application built with **Java Swing** for the Object-Oriented Programming Lab at United International University. Create questions, run quizzes, and track results with a clean, consistent UI.

## Features

- Interactive quiz flow with real-time score and progress
- Student identity capture (name + ID)
- Configurable max attempts per student
- Results window with statistics, recent results, and leaderboard tabs
- Question manager with validation (password protected)
- Reset Data option to clear attempts and results

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 8+ |
| GUI | Swing |
| Storage | Text files |
| Dependencies | None |

## Prerequisites

- Java Development Kit (JDK) 8+
- Git (optional)

## Getting Started

```bash
# Clone the repository
git clone https://github.com/litch07/quiz-master-java.git
cd quiz-master-java

# Compile all Java files
javac *.java

# Run the application
java Main
```

## Portable Build (Release Asset)

If you download the release ZIP, it includes `QuizMaster.jar`, `questions.txt`, and launcher scripts.
Run `run.bat` on Windows or `run.sh` on macOS/Linux. Java 8+ is required.

## Usage Notes

- Default teacher password: `admin`
- Teacher can set max attempts in **Settings** (0 = no trials)
- **Results** opens a window with tabs for Statistics, Recent Results, and Leaderboard
- **Reset Data** clears attempts, results, and statistics (and resets max attempts to 1)

## Question File Format

Each question uses 6 lines in `questions.txt`:

```
Question text
Option 1
Option 2
Option 3
Option 4
Correct option text
```

Blank lines may separate questions.

## Project Structure

```
quiz-master-java/
+-- Main.java              # Entry point
+-- Dashboard.java         # Main navigation
+-- StartQuiz.java         # Quiz intro screen
+-- MainWindow.java        # Quiz interface
+-- ResultsWindow.java     # Results & grading
+-- QuestionManager.java   # Question CRUD
+-- Questions.java         # Data model
+-- questions.txt          # Question database
+-- .gitignore
+-- LICENSE
+-- README.md
```

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

## Author

Sadid Ahmed
