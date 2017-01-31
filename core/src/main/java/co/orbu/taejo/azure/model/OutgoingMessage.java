package co.orbu.taejo.azure.model;

/**
 * Message that will be send to service bus.
 */
public class OutgoingMessage {

    private String channel = "general";
    private String emoji = ":rabbit:";
    private String bot = "Taejo";
    private String message;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getBot() {
        return bot;
    }

    public void setBot(String bot) {
        this.bot = bot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OutgoingMessage{" +
                "channel='" + channel + '\'' +
                ", emoji='" + emoji + '\'' +
                ", bot='" + bot + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
