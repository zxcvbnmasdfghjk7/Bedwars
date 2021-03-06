package io.github.eddiediamondfire.bedwars.storage.yaml;

import com.google.common.io.Files;
import io.github.eddiediamondfire.bedwars.Bedwars;
import io.github.eddiediamondfire.bedwars.arenadata.GameInstance;
import io.github.eddiediamondfire.bedwars.arenadata.Team;
import io.github.eddiediamondfire.bedwars.game.arena.ArenaManager;
import io.github.eddiediamondfire.bedwars.storage.FileManager;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arena_Spawn implements AbstractYamlFile{
    private FileConfiguration config = null;
    private final Bedwars plugin;

    public Arena_Spawn(FileManager fileManager){
        plugin = fileManager.getPlugin();
    }
    @Override
    public String getFileName() {
        return "arena_spawns.yml";
    }

    @Override
    public void load() {
        File file = new File(plugin.getDataFolder().getAbsolutePath() + "/arenas/", getFileName());
        if(!file.exists()){
            try{
                file.getParentFile().mkdirs();
                plugin.saveResource(getFileName(), false);
                Files.move(new File(plugin.getDataFolder(), getFileName()), new File(plugin.getDataFolder() + "/arenas/" + getFileName()));
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }

        config = new YamlConfiguration();

        try{
            config.load(file);
        }catch (IOException |InvalidConfigurationException ex){
            ex.printStackTrace();
        }

        ArenaManager arenaManager = plugin.getArenaManager();

        ConfigurationSection section = config.getConfigurationSection("arenas");

        if(section != null){
            Map<String, Location> locationsMap = new HashMap<>();
            for(String arenaName: section.getKeys(false)){
                locationsMap.put("lobbySpawn", config.getLocation("arenas." + arenaName + ".lobby_spawn_location"));
                locationsMap.put("spectatorSpawn", config.getLocation("arenas." + arenaName + ".spectator_spawn_location"));
                locationsMap.put("endSpawn", config.getLocation("arenas." + arenaName + ".end_spawn_location"));

                GameInstance gameInstance = plugin.getGameManager().getGameInstances().get(arenaManager.returnID(arenaName));
                List<Team> teams = gameInstance.getTeams();

                for(Team team: teams){
                    locationsMap.put(team.getTeamName() + "SpawnLocation", config.getLocation("arenas." + arenaName + ".game_spawn_locations." + team.getTeamName()));
                    plugin.getLogger().info("Found " + team.getTeamName() + " spawn location");
                }
                plugin.getLogger().info("Loaded arena " + arenaName + " spawns");
                arenaManager.loadArenaSpawnLocationData(arenaName, locationsMap);
            }
        }else{
            plugin.getLogger().info("Error: There is nothing in arena_spawn.yml, probably its the first time loading Bedwars plugin.");
        }


    }

    @Override
    public void save() {

    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }
}
