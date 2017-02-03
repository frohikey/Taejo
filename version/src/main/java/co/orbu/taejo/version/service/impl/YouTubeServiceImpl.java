package co.orbu.taejo.version.service.impl;

import co.orbu.taejo.version.model.youtube.Channel;
import co.orbu.taejo.version.service.YouTubeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Service
public class YouTubeServiceImpl implements YouTubeService {

    private static final String REQUEST_URL = "https://www.youtube.com/feeds/videos.xml?channel_id=%s";

    @Nullable
    @Override
    public Channel getChannel(String channel) {
        String url = String.format(REQUEST_URL, channel);
        System.out.println("Calling: " + url);

        Client client = ClientBuilder.newClient();
        Response response = client.target(url).request().get();
        if (response == null) {
            return null;
        }

        XmlMapper mapper = new XmlMapper();
        try {
            return mapper.readValue(response.readEntity(String.class), new TypeReference<Channel>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
