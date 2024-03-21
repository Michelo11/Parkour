package me.michelemanna.parkour.commands.subcommands;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.commands.SubCommand;
import me.michelemanna.parkour.data.Parkour;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(ParkourPlugin.getInstance().getMessage("commands.create-usage"));
            return;
        }

        ParkourPlugin
                .getInstance()
                .getParkourManager()
                .getParkours().put(args[1], new Parkour(-1, args[1], List.of(player.getLocation().getBlock().getLocation())));

        player.sendMessage(ParkourPlugin.getInstance().getMessage("commands.create-success"));
    }
}
