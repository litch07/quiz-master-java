# Quiz Master

A professional interactive quiz application built with **Java Swing**, designed for the Object-Oriented Programming Lab at United International University. Features a modern GUI for creating, managing, and taking quizzes with real-time feedback and comprehensive performance tracking.

## Features

- **Dashboard Navigation** - Centralized hub for all operations
- **Interactive Quiz Interface** - Take quizzes with live progress tracking and real-time scoring
- **GUI Question Management** - Add, delete, and manage questions without file editing
- **Grading System** - Automatic scoring with letter grades (A+, A, B, C, F)
- **Material Design UI** - Professional, modern interface with intuitive color scheme
- **Data Persistence** - Questions auto-saved to `questions.txt`
- **Zero Dependencies** - Pure Java Swing (no external libraries required)
- **Input Validation** - Comprehensive validation on all user inputs

## Tech Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Java 8+ |
| **GUI Framework** | Swing |
| **Storage** | Text file (questions.txt) |
| **Architecture** | Component-based (MVC-like) |
| **Dependencies** | None |

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Git (for cloning the repository)

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

## How to Use

### Adding Questions

1. From the Dashboard, click **"Manage Questions"**
2. Click **"+ Add New Question"** button
3. Enter your question text in the text area
4. Fill in all four multiple-choice options
5. Select the correct answer from the dropdown menu
6. Click **"Save Question"** - changes are instantly saved to `questions.txt`

**Tips:**
- Questions must have unique text
- All four options must be filled
- The correct answer must match one of the options exactly

### Taking a Quiz

1. From the Dashboard, click **"Start Quiz"**
2. Review the quiz information and click **"Start Quiz"** to begin
3. Read each question and select your answer by clicking one of the four option buttons
4. Click **"Next Question"** to proceed to the next question
5. On the final question, the button changes to **"Finish Quiz"**
6. Submit your answers to see detailed results

**Scoring:**
- Each correct answer = 1 point
- Progress bar shows your position in the quiz
- Score updates in real-time as you answer

### Viewing Results

After completing a quiz, you'll see:
- **Score** - Number of correct answers (e.g., 8 out of 10)
- **Percentage** - Score as a percentage (highlighted in yellow)
- **Grade** - Letter grade based on performance:
  - A+ (90-100%) - Excellent
  - A (80-89%) - Very Good
  - B (70-79%) - Good
  - C (60-69%) - Satisfactory
  - F (Below 60%) - Needs Improvement

From the results screen, you can:
- **Retake Quiz** - Take the same quiz again
- **Go to Dashboard** - Return to main menu

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

**Example Structure:**
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
├── Loads questions from questions.txt
└── Initializes Dashboard

Dashboard.java
├── Navigation hub with 4 main buttons
└── Connects to:
    ├── StartQuiz.java → Quiz flow
    ├── QuestionManager.java → CRUD operations
    └── Exit

StartQuiz.java
└── Quiz confirmation screen
    └── MainWindow.java → Main quiz interface
        └── ResultsWindow.java → Results & grading

Questions.java
└── Simple data model (question, options, answer)

QuestionManager.java
└── GUI for question management
    └── Saves/loads from questions.txt

ResultsWindow.java
└── Displays results and statistics
```

### Data Flow

```
questions.txt
    ↓
Main.java (loads)
    ↓
Dashboard (navigation hub)
    ├→ Start Quiz Flow
    │   ├→ StartQuiz (intro)
    │   ├→ MainWindow (quiz)
    │   └→ ResultsWindow (results)
    │
    └→ Manage Questions
        ├→ View questions
        ├→ Add question
        └→ Delete question
```

## Design & UI

The application uses a **Material Design 2019** color palette:

| Element | Color | Purpose |
|---------|-------|---------|
| Primary Gradient | Blue (25,118,210 → 13,71,161) | Main UI background |
| Success Action | Green (76,175,80) | Start, Save, Retake buttons |
| Secondary Action | Blue (33,150,243) | Navigation buttons |
| Destructive Action | Red (244,67,54) | Quit, Delete, Cancel buttons |
| Accent | Purple (156,39,176) | Dashboard button |
| Highlight | Yellow (255,193,7) | Percentage display |

**Typography:** Segoe UI font for clean, modern appearance

**Interface Elements:**
- Gradient backgrounds for visual appeal
- Color-coded buttons for intuitive UX
- Progress bars showing quiz completion
- Input validation with error messages
- Confirmation dialogs for critical actions

## Compilation & Execution

### Compilation
```bash
javac *.java
```

This compiles all 7 Java source files into corresponding `.class` files.

### Running
```bash
java Main
```

The JVM will:
1. Load `questions.txt`
2. Parse questions into Question objects
3. Display the Dashboard window
4. Enable user interactions

### Files Generated
- `.class` files - Compiled bytecode (can be deleted and regenerated)
- `questions.txt` - Auto-updated when questions are modified

## Project Structure

```
quiz-master-java/
├── Main.java              # Entry point
├── Dashboard.java         # Main navigation (5.02 KB)
├── StartQuiz.java         # Quiz intro (4.64 KB)
├── MainWindow.java        # Quiz interface (8.21 KB)
├── ResultsWindow.java     # Results display (5.29 KB)
├── QuestionManager.java   # Question management (11.41 KB)
├── Questions.java         # Data model (0.33 KB)
├── questions.txt          # Question database
├── .gitignore             # Git configuration
└── README.md              # This file
```

## Course Information

**Institution:** United International University (UIU)  
**Course:** Object-Oriented Programming Lab  
**Language:** Java  

**Concepts Demonstrated:**
- Object-oriented design principles
- GUI development with Swing
- Event-driven programming
- File I/O and data persistence
- Input validation and error handling
- Component-based architecture
- Real-time data processing

## Future Enhancements

Potential features for future versions:

- **User Authentication** - Login system with user profiles
- **Quiz Categories** - Organize quizzes by subject/difficulty
- **Difficulty Levels** - Easy, Medium, Hard question ratings
- **Question Shuffling** - Randomize question and option order
- **Timer-Based Quizzes** - Timed quizzes with countdown
- **Quiz History** - Track past quiz attempts and scores
- **Statistics Dashboard** - Detailed performance analytics
- **Database Integration** - MySQL/SQLite for scalable storage
- **PDF Export** - Generate quiz reports as PDF
- **Dark Mode Theme** - Alternative UI theme
- **Sound Effects** - Audio feedback for interactions
- **Multiplayer Mode** - Competitive quiz taking

## Development Notes

### Code Quality
- Clean, readable code with meaningful variable names
- Proper exception handling throughout
- Input validation on all user interactions
- Well-organized class structure

### Performance
- Efficient file I/O operations
- Responsive UI with minimal lag
- Fast question parsing and loading
- Memory-efficient data structures

### Compatibility
- Works on Windows, macOS, and Linux
- Requires Java 8 or higher
- No platform-specific code

## Testing

To test the application:

1. **Add Questions**
   ```
   Launch app → Manage Questions → Add 5-10 test questions
   ```

2. **Take Quiz**
   ```
   Click Start Quiz → Answer all questions → Check results
   ```

3. **Verify Scoring**
   ```
   Compare your selected answers with results displayed
   ```

4. **Test Persistence**
   ```
   Close app → Reopen → Verify questions are saved
   ```

## Troubleshooting

**Problem:** "Exception in thread 'main'"
- **Solution:** Ensure all 7 Java files are compiled: `javac *.java`

**Problem:** Questions not saving
- **Solution:** Verify `questions.txt` is in the same directory as `.class` files

**Problem:** Compilation errors
- **Solution:** Check Java version: `javac -version` (should be 8 or higher)

**Problem:** GUI doesn't display
- **Solution:** On Linux, may need to set display: `export DISPLAY=:0`

## Contributing

This is an academic project. For improvements or bug fixes:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request with detailed explanation

## License

This project is provided for educational purposes as part of the Object-Oriented Programming Lab at United International University.

## Author

Sadid Ahmed

---

**Made with focus on clean code and professional design principles.** 🚀
