package io.github.eddiediamondfire.bedwars.command.subcommands;

import io.github.eddiediamondfire.bedwars.Bedwars;
import io.github.eddiediamondfire.bedwars.arenadata.GameInstance;
import io.github.eddiediamondfire.bedwars.command.CommandManager;
import io.github.eddiediamondfire.bedwars.command.SubCommand;
import io.github.eddiediamondfire.bedwars.game.GameManager;
import io.github.eddiediamondfire.bedwars.utils.Utils;
import io.github.eddiediamondfire.bedwars.utils.based.BedwarsMathUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Map;

public class Create implements SubCommand {

    private final Bedwars plugin;
    private final CommandManager commandManager;

    public Create(CommandManager commandManager){
        this.commandManager = commandManager;
        this.plugin = commandManager.getPlugin();
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getSyntax() {
        return "/bedwars create <Arena Name>";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if(args.length > 1){
            String arenaName = args[1];

            if(plugin.getArenaManager().arenaAlreadyExist(arenaName)){
                player.sendMessage(ChatColor.RED + "Arena Already exist with name " + arenaName);
                return true;
            }

            int randomNumber = BedwarsMathUtils.getRandomNumber(1, 1000, plugin.getGameManager().getGameInstances());
            GameInstance gameInstance = new GameInstance(arenaName, randomNumber);

            Map<Integer, GameInstance> gameInstances = plugin.getGameManager().getGameInstances();
            gameInstances.put(randomNumber, gameInstance);
            return false;
        }else{
            player.sendMessage(ChatColor.RED + "Not Enough Arguments");
            return true;
        }
    }
}