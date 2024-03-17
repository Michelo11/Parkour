package me.michelemanna.parkour.commands.subcommands;

import me.michelemanna.parkour.ParkourPlugin;
import me.michelemanna.parkour.commands.SubCommand;
import me.michelemanna.parkour.data.Parkour;
import org.bukkit.entity.Player;

public class FinishCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage("§cUsage: /parkour finish <parkour>");
            return;
        }

        Parkour parkour = ParkourPlugin
                .getInstance()
                .getParkourManager()
                .getParkours().get(args[1]);
        parkour.getCheckpoints()
                .add(player.getLocation().getBlock().getLocation());

        ParkourPlugin
                .getInstance()
                .getDatabase()
                .createParkour(parkour);

        parkour.setFinished(true);

        player.sendMessage("§aFinish created!");
    }
}
