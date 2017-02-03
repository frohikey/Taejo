package co.orbu.taejo.version.service;

import co.orbu.taejo.version.model.youtube.Channel;

import javax.annotation.Nullable;

public interface YouTubeService {

    @Nullable
    Channel getChannel(String channel);

}
