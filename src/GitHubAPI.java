import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.*;

public class GitHubAPI {
    private RepositoryId repo;
    private CommitService commitService;
    private ContentsService contentsService;

    public GitHubAPI(String username, String password, String repoOwner, String repoName) {
        // Setup Authenticated Client
        GitHubClient client = new GitHubClient();
        client.setCredentials(username, password);

        // Setup Repository Details
        this.repo = new RepositoryId(repoOwner, repoName);

        // Setup services
        this.commitService = new CommitService(client);
        this.contentsService = new ContentsService(client);
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
}