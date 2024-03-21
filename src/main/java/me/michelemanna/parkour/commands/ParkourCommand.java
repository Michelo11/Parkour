package me.michelemanna.parkour.commands;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.commands.subcommands.*;
import me.michelemanna.parkour.gui.ParkourMenus;
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
        this.subCommands.put("help", new HelpCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ParkourPlugin.getInstance().getMessage("commands.player-only"));
            return true;
        }


        if (args.length == 0) {
            if (player.hasPermission("parkour.admin")) {
                ParkourMenus.openParkours(player);
                return true;
            }

            player.sendMessage(ParkourPlugin.getInstance().getMessage("commands.no-permission"));
            return true;
        }

        if (subCommands.containsKey(args[0].toLowerCase())) {
            subCommands.get(args[0].toLowerCase()).execute(player, args);
            return true;
        }

        return true;
    }
}