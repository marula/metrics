import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.*;

public class GitHubAPI {
    private RepositoryId repo;
    private CommitService commitService;
    private ContentsService contentsService;
    private GitHubClient client;

    public GitHubAPI(String username, String password, String repoOwner, String repoName) {
        // Setup Authenticated Client
        this.client = new GitHubClient();
        this.client.setCredentials(username, password);

        // Setup Repository Details
        this.repo = new RepositoryId(repoOwner, repoName);

        // Setup services
        this.commitService = new CommitService(client);
        this.contentsService = new ContentsService(client);
    }

    public GitHubClient getClient() {
        return client;
    }

    public RepositoryId getRepo() {
        return repo;
    }

    public CommitService getCommitService() {
        return commitService;
    }

    public ContentsService getContentsService() {
        return contentsService;
    }

    public ArrayList<Revision> calculateRevisions(String filePath) throws IOException {
        ArrayList<Revision> revisions = new ArrayList<>();
        List<RepositoryCommit> repoCommits = commitService.getCommits(repo, null, filePath);
        for (RepositoryCommit repoCommit : repoCommits) {
            String sha = repoCommit.getSha();
            RepositoryCommit newRepoCommit = commitService.getCommit(repo, sha);
            String author = repoCommit.getCommit().getAuthor().getName();

            List<CommitFile> commitFiles = newRepoCommit.getFiles();

            if (commitFiles != null) {
                for(CommitFile commitFile : commitFiles) {
                    if (filePath.equals(commitFile.getFilename())) {
                        String commitFilePath = commitFile.getFilename();
                        revisions.add(new Revision(author, decodeContents(commitFilePath)));
                    }
                }
            }
        }

        return revisions;
    }

    public String decodeContents(String path) throws IOException {
        List<RepositoryContents> repositoryContentsList = contentsService.getContents(repo, path);
        if (repositoryContentsList.size() > 1) {
            System.err.println("Path error, please enter file path not directory path.");
            return null;
        } else {
            String content = repositoryContentsList.get(0).getContent().replaceAll("\n", "");
            return new String(Base64.getDecoder().decode(content));
        }

    }

    public ArrayList<VisualObject> calculateVisualObjects(ArrayList<Revision> revisions, String author) {
        ArrayList<VisualObject> visualObjects = new ArrayList<>();
        for (Revision revision : revisions) {
            Scanner sc = new Scanner(revision.getContents());
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int lineNumber = lookupLine("data/README.txt", line);
                if (lineNumber != -1) {
                    if (author.equals(revision.getAuthor())) {
                        visualObjects.add(new VisualObject(lineNumber, 0, 0, 0, true, 0, -1));
                    } else {
                        visualObjects.add(new VisualObject(lineNumber, 255, 255, 255, true, 0, -1));
                    }
                }
            }
            sc.close();
        }
        return visualObjects;
    }

    public int lookupLine(String filePath, String revisionLine) {
        File file = new File(filePath);

        try {
            Scanner sc = new Scanner(file);
            int lineNumber = 0;
            while (sc.hasNextLine()) {
                lineNumber ++;
                if (sc.nextLine().contains(revisionLine)) {
                    sc.close();
                    return lineNumber;
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void main(String[] args) throws IOException {
        // Read Username and Password
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = "Rick-KLN"; // sc.nextLine();
        System.out.println("Enter password:");
        String password = "71307130b"; // sc.nextLine();
        sc.close();


        File file = new File("data/README.txt");

        try {
            sc = new Scanner(file);
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }


        GitHubAPI API = new GitHubAPI(username, password, "marula", "yumee-web");
        String author = "Rick Kleinhans";
        ArrayList<Revision> revisions = API.calculateRevisions("README.md");
        ArrayList<VisualObject> visualObjects = API.calculateVisualObjects(revisions, author);
        for (VisualObject visualObject : visualObjects) {
            System.out.println(visualObject);
        }
    }
}