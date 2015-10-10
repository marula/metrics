import java.util.ArrayList;
import java.util.Scanner;

public class RunMetrics {
    public static void main(String[] args) {
        // Read Username and Password
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = "Rick-KLN"; // sc.nextLine();
        System.out.println("Enter password:");
        String password = "71307130b"; // sc.nextLine();
        sc.close();

        GitHubAPI API = new GitHubAPI(username, password, "marula", "yumee-web");
        CalculateMetrics metrics = new CalculateMetrics(API, "data/README.txt", "Rick Kleinhans");
        ArrayList<Revision> revisions = metrics.calculateRevisions("README.md");
        ArrayList<VisualObject> visualObjects = metrics.calculateVisualObjects(revisions);
        for (VisualObject visualObject : visualObjects) {
            System.out.println(visualObject);
        }
    }
}
