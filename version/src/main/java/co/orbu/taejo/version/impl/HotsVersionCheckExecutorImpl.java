package co.orbu.taejo.version.impl;

import co.orbu.taejo.version.AbstractVersionCheckExecutor;
import co.orbu.taejo.version.model.repository.Version;
import co.orbu.taejo.version.model.youtube.Channel;
import co.orbu.taejo.version.model.youtube.Video;
import co.orbu.taejo.version.repository.VersionRepository;
import co.orbu.taejo.version.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

// TODO: this is a really stupid way how to do it, but for now this is a quick & dirty hack how to accomplish this
@Component(HotsVersionCheckExecutorImpl.COMMAND)
public class HotsVersionCheckExecutorImpl extends AbstractVersionCheckExecutor {

    static final String COMMAND = "hots";

    private static final String CHANNEL_NAME = "UCXAeKsZQL7koKko46kfIXXQ";

    private final YouTubeService youTubeService;

    @Autowired
    public HotsVersionCheckExecutorImpl(VersionRepository versionRepository, YouTubeService youTubeService) {
        super(versionRepository);

        Assert.notNull(youTubeService);
        this.youTubeService = youTubeService;
    }

    @Override
    protected String getCommandName() {
        return COMMAND;
    }

    @Scheduled(fixedDelay = 3_600_000)
    private void fetchLatestVersion() {
        System.out.println("Fetching " + COMMAND);

        Channel channel = youTubeService.getChannel(CHANNEL_NAME);
        if (channel == null) {
            return;
        }

        Version version = versionRepository.findByCommand(COMMAND);
        if (version == null) {
            version = new Version();
            version.setCommand(COMMAND);
        }

        if (channel.getName() != null && channel.getName().equals(version.getName())) {
            // this video is already in the database
            return;
        }

        version.setName(channel.getName());

        // TODO: there needs to be a way how to customize this (not suited for all cases, e.g. YouTube video does not have a version)
        if (channel.getVideos().size() > 0) {
            Video video = channel.getVideos().get(0);
            System.out.println("Found new video: " + video.getTitle());

            version.setVersion(video.getTitle());
            version.setDownloadUrl(video.getLink().getUrl());

            // TODO: yikes
            version.setReleaseNotesUrl("http://www.youtube.com/channel/" + CHANNEL_NAME);
        }

        version.setLastUpdate(new Date());

        // do not report if there is no entry in the database at all (this prevents spamming the channel on init)
        version.setWasReported(version.getId() == null);

        versionRepository.saveAndFlush(version);
    }

}
