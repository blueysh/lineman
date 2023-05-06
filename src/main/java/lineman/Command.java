package lineman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public interface Command {
    HashMap<String, Command> subcommands = new HashMap<>();

    void run(Lineman lineman, String[] commands);

    default Command addSubcommand(String name, Command command) {
        subcommands.put(name, command);
        return this;
    }

    default void runSubcommand(Lineman lineman, String[] args) {
        if (subcommands.containsKey(args[0])) {
            ArrayList<String> cmds = new ArrayList<>();
            Collections.addAll(cmds, args);
            cmds.remove(0);
            subcommands.get(args[0]).run(lineman, cmds.toArray(String[]::new));
        } else {
            if (subcommands.isEmpty()) {
                lineman.getLogger().severe("unknown subcommand '" + args[0] + "'");
                return;
            }
            List<String> predictions = new ArrayList<>();
            subcommands.forEach((name, command) -> {if (name.startsWith(args[0]) || name.contains(args[0])) predictions.add(name);});
            lineman.getLogger().severe("unknown command '" + args[0] + "'. did you mean one of, " + predictions.toString().replace("[", "").replace("]", "") + "?");
        }
    }

}
