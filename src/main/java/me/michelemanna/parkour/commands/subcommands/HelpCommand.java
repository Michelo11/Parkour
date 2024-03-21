package me.michelemanna.parkour.commands.subcommands;

import me.michelemanna.parkour.commands.SubCommand;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage("§7§m-----------------------------------");
        player.sendMessage("§b§lParkour Help");
        player.sendMessage("§7/parkour create <parkour> §8- §fCreate a new parkour");
        player.sendMessage("§7/parkour checkpoint <parkour> §8- §fCreate a new checkpoint");
        player.sendMessage("§7/parkour finish <parkour> §8- §fCreate a new finish");
        player.sendMessage("§7/parkour restart §8- §fTeleport to the last checkpoint");
        player.sendMessage("§7/parkour §8- §fList all parkours");
        player.sendMessage("§7§m-----------------------------------");
    }
}
