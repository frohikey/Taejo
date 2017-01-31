package co.orbu.taejo.version.service.impl;

import co.orbu.taejo.version.model.github.Release;
import co.orbu.taejo.version.service.GitHubService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Service
public class GitHubServiceImpl implements GitHubService {

    private static final String REQUEST_URL = "https://api.github.com/repos/%s/%s/releases/latest";

    @Nullable
    @Override
    public Release getVersion(@Nonnull String user, @Nonnull String project) {
        String url = String.format(REQUEST_URL, user, project);
        System.out.println("Calling: " + url);

        Client client = ClientBuilder.newClient();
        Response response = client.target(url).request().get();
        if (response == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response.readEntity(String.class), new TypeReference<Release>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
