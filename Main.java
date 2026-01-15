import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Questions> questions = new ArrayList<>();
        File file = new File("questions.txt");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String q_file = sc.nextLine();
                String[] opt_file = new String[4];
                String ans_file;
                for (int i = 0; i < 4; i++) {
                    opt_file[i] = sc.nextLine();
                }
                ans_file = sc.nextLine();
                questions.add(new Questions(q_file, opt_file, ans_file));
            }
            sc.close();
            new Dashboard(questions);
        } catch (FileNotFoundException e) {
            System.out.println("Questions file not found! Creating new quiz dashboard...");
            new Dashboard(new ArrayList<>());
        }
    }
}