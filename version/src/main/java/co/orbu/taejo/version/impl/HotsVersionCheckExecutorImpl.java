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

        // TODO: there needs to be a way how to customize this (not suited for all cases, e.g. YouTube video does not have a version)
        Video video = channel.getVideos().size() > 0 ? channel.getVideos().get(0) : null;
        if (video == null || video.getTitle() == null || video.getTitle().equals(version.getVersion())) {
            // this version is already in the database
            return;
        }

        System.out.println("Found new video: " + video.getTitle());

        version.setName(channel.getName());
        version.setVersion(video.getTitle());
        version.setDownloadUrl(video.getLink().getUrl());
        version.setLastUpdate(new Date());

        // TODO: yikes
        version.setReleaseNotesUrl("http://www.youtube.com/channel/" + CHANNEL_NAME);

        // do not report if there is no entry in the database at all (this prevents spamming on init)
        version.setWasReported(version.getId() == null);

        versionRepository.saveAndFlush(version);
    }

}
