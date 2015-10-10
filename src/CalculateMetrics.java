import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryContents;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class CalculateMetrics {
    private GitHubAPI API;
    private String filePath;
    private String author;

    public CalculateMetrics(GitHubAPI API, String filePath, String author) {
        this.API = API;
        this.filePath = filePath;
        this.author = author;
    }

    public ArrayList<Revision> calculateRevisions(String filePath) {
        ArrayList<Revision> revisions = new ArrayList<>();
        try {
            List<RepositoryCommit> repoCommits = API.getCommitService().getCommits(API.getRepo(), null, filePath);
            for (RepositoryCommit repoCommit : repoCommits) {
                String sha = repoCommit.getSha();
                RepositoryCommit newRepoCommit = API.getCommitService().getCommit(API.getRepo(), sha);
                String commitAuthor = repoCommit.getCommit().getAuthor().getName();

                List<CommitFile> commitFiles = newRepoCommit.getFiles();

                if (commitFiles != null) {
                    for(CommitFile commitFile : commitFiles) {
                        if (filePath.equals(commitFile.getFilename())) {
                            String commitFilePath = commitFile.getFilename();
                            revisions.add(new Revision(commitAuthor, decodeContents(commitFilePath)));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("GitHub API was unreachable. Test your internet connection and login details and try again.");
        }

        return revisions;
    }

    public String decodeContents(String path) throws IOException {
        List<RepositoryContents> repositoryContentsList = API.getContentsService().getContents(API.getRepo(), path);
        if (repositoryContentsList.size() > 1) {
            System.err.println("Path error, please enter file path not directory path.");
            return null;
        } else {
            String content = repositoryContentsList.get(0).getContent().replaceAll("\n", "");
            return new String(Base64.getDecoder().decode(content));
        }

    }

    public ArrayList<VisualObject> calculateVisualObjects(ArrayList<Revision> revisions) {
        ArrayList<VisualObject> visualObjects = new ArrayList<>();
        for (Revision revision : revisions) {
            Scanner sc = new Scanner(revision.getContents());
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int lineNumber = lookupLine(filePath, line);
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
}
