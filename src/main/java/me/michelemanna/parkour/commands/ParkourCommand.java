package me.michelemanna.parkour.commands;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.commands.subcommands.CheckpointCommand;
import me.michelemanna.parkour.commands.subcommands.CreateCommand;
import me.michelemanna.parkour.commands.subcommands.FinishCommand;
import me.michelemanna.parkour.commands.subcommands.RestartCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ParkourCommand implements CommandExecutor {
    private final ParkourPlugin plugin;
    private Map<String, SubCommand> subCommands = new HashMap<>();

    public ParkourCommand(ParkourPlugin plugin) {
        this.plugin = plugin;
        this.subCommands.put("create", new CreateCommand());
        this.subCommands.put("checkpoint", new CheckpointCommand());
        this.subCommands.put("finish", new FinishCommand());
        this.subCommands.put("restart", new RestartCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§c You must specify a subcommand!");
            return true;
        }

        if (subCommands.containsKey(args[0].toLowerCase())) {
            subCommands.get(args[0].toLowerCase()).execute(player, args);
            return true;
        }

        return true;
    }
}