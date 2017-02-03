package co.orbu.taejo.version.impl;

import co.orbu.taejo.version.VersionCheckExecutor;
import co.orbu.taejo.version.model.VersionInfo;
import co.orbu.taejo.version.model.youtube.Channel;
import co.orbu.taejo.version.model.youtube.Video;
import co.orbu.taejo.version.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

// TODO: this is a really stupid way how to do it, but for now this is a quick & dirty hack how to accomplish this
@Component("hots")
public class HotsVersionCheckExecutorImpl implements VersionCheckExecutor {

    private static final String CHANNEL_NAME = "UCXAeKsZQL7koKko46kfIXXQ";

    private final YouTubeService youTubeService;

    @Autowired
    public HotsVersionCheckExecutorImpl(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    @Nullable
    @Override
    public VersionInfo getLatestVersion() {
        Channel channel = youTubeService.getChannel(CHANNEL_NAME);
        if (channel == null) {
            return null;
        }

        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setName(channel.getName());

        // TODO: there needs to be a way how to customize this (not suited for all cases, e.g. YouTube video does not have a version)
        if (channel.getVideos().size() > 0) {
            Video video = channel.getVideos().get(0);
            versionInfo.setVersion(video.getTitle());
            versionInfo.setDownloadUrl(video.getLink().getUrl());

            // TODO: yikes
            versionInfo.setReleaseNotesUrl("http://www.youtube.com/channel/" + CHANNEL_NAME);
        }

        return versionInfo;
    }

}
