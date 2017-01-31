package co.orbu.taejo.azure.model;

/**
 * Received message from service bus.
 */
public class IncomingMessage {

    private String channel;
    private String command;
    private String user;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "IncomingMessage{" +
                "channel='" + channel + '\'' +
                ", command='" + command + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
