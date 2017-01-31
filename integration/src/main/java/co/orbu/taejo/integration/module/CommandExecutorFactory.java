package co.orbu.taejo.integration.module;

public interface CommandExecutorFactory {

    CommandExecutor getCommandExecutor(String commandExecutor);

}
