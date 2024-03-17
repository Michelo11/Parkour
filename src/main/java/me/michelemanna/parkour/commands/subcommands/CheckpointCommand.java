package me.michelemanna.parkour.commands.subcommands;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.commands.SubCommand;
import org.bukkit.entity.Player;

public class CheckpointCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage("§cUsage: /parkour checkpoint <parkour>");
            return;
        }

        ParkourPlugin
                .getInstance()
                .getParkourManager()
                .getParkours().get(args[1])
                .getCheckpoints()
                .add(player.getLocation().getBlock().getLocation());

        player.sendMessage("§aCheckpoint created!");
    }
}