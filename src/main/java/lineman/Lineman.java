package lineman;

import java.util.*;

public class Lineman {
    private final List<Thread> startupHooks;
    private final List<Thread> shutdownHooks;
    private final HashMap<String, Command> commandsRegistry;
    private final Logger logger;

    private Lineman() {
        this.startupHooks = new ArrayList<>();
        this.shutdownHooks = new ArrayList<>();
        this.commandsRegistry = new HashMap<>();
        this.logger = new Logger();
    }

    public static Lineman create() {
        return new Lineman();
    }

    public Logger getLogger() { return logger; }

    public void addStartupHook(Thread hook) {
        startupHooks.add(hook);
    }

    public void clearStartupHooks() {
        startupHooks.clear();
    }

    public void addShutdownHook(Thread hook) {
        shutdownHooks.add(hook);
    }

    public void clearShutdownHooks() {
        shutdownHooks.clear();
    }

    public Command addCommand(String name, Command command) {
        commandsRegistry.put(name, command);
        return command;
    }


    public void run(String[] args) {
        // run startup hooks
        for (Thread hook:startupHooks) {
            hook.start();
        }
        Runtime r = Runtime.getRuntime();
        for (Thread hook:shutdownHooks) {
            r.addShutdownHook(hook);
        }

        if (commandsRegistry.containsKey(args[0])) {
            ArrayList<String> commands = new ArrayList<>();
            Collections.addAll(commands, args);
            commands.remove(0);
            commandsRegistry.get(args[0]).run(this, commands.toArray(String[]::new));
        } else {
            if (commandsRegistry.isEmpty()) {
                logger.severe("unknown command '" + args[0] + "'");
                return;
            }
            List<String> predictions = new ArrayList<>();
            commandsRegistry.forEach((name, command) -> {if (name.startsWith(args[0]) || name.contains(args[0])) predictions.add(name);});
            logger.severe("unknown command '" + args[0] + "'. did you mean one of, " + predictions.toString().replace("[", "").replace("]", "") + "?");
        }
    }
}