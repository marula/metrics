import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.egit.github.core.Authorization;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.OAuthService;

/**
 * Create a Gist using an OAuth2 token
 */
public class GistViaOAuthToken {

    /**
     * Request an OAuth2 token with 'gist' scope and then create a {@link Gist}
     * using the granted token
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        OAuthService oauthService = new OAuthService();

        // Replace with actual login and password
        oauthService.getClient().setCredentials("Rick-KLN", "a");

        // Create authorization with 'gist' scope only
        Authorization auth = new Authorization();
        auth.setScopes(Arrays.asList("gist"));
        auth = oauthService.createAuthorization(auth);

        // Create Gist service configured with OAuth2 token
        GistService gistService = new GistService();
        gistService.getClient().setOAuth2Token(auth.getToken());

        // Create Gist
        Gist gist = new Gist();
        gist.setPublic(false);
        gist.setDescription("Created using OAuth2 token via Java API");
        GistFile file = new GistFile();
        file.setContent("Gist!");
        file.setFilename("gist.txt");
        gist.setFiles(Collections.singletonMap(file.getFilename(), file));
        gist = gistService.createGist(gist);
        System.out.println("Created Gist at " + gist.getHtmlUrl());
    }
}