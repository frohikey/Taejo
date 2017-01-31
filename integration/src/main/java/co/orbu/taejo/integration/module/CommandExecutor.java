package co.orbu.taejo.integration.module;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CommandExecutor {

    /**
     * Executes given command from user.
     *
     * @param data Parameters provided by user. At first position is a command followed by the its parameters.
     * @return Formatted message for Slack.
     */
    @Nullable
    String doCommand(@Nonnull String[] data);

}
