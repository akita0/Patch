package me.jade;/*

@Author https://github.com/akita0
2023

*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.jade.config.Config;
import me.jade.event.GlobalListener;
import me.jade.patch.PatchManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Patcher extends JavaPlugin implements CommandExecutor {
    public static final String VERSION = "1.0.0";
    public static Config config;
    public static JavaPlugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        start(false);
    }
    private void setupConfig() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if(!getDataFolder().exists())
            getDataFolder().mkdirs();
        File file = new File(getDataFolder().getAbsolutePath() , "config.bok");
        if(!file.exists()) {
            file.createNewFile();
            config = new Config();
            Files.write(file.toPath(), gson.toJson(config).getBytes());
            return;
        }
        config = gson.fromJson(new String(Files.readAllBytes(file.toPath())) , Config.class);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(s.startsWith("jade")) {
            if (commandSender.isOp()) {
                if(strings.length > 0) {
                    switch (strings[0]) {
                        case "reload":
                            start(true);
                            return true;
                        case "notify":
                            commandSender.sendMessage("ekledim kanka flag gorme listeye");
                            if(PatchManager.notify.contains(commandSender.getName())) {
                                PatchManager.notify.remove(commandSender.getName());
                                return true;
                            }else {
                                commandSender.sendMessage("ekledim kanka flag gorme listeye");
                                PatchManager.notify.add(commandSender.getName());
                                return true;
                            }
                    }
                }else {
                    commandSender.sendMessage("arg lazim knk biliyonmu olmuyo oyle");
                }
            }else {
                commandSender.sendMessage("knk op lazim onun icin biliyonmu ya");
            }
        }
        return false;
    }
    public void start(boolean reload) {
        if(reload) {
            getLogger().info("Patcher");
            getLogger().info("Version: " + VERSION);
            getLogger().info("Author: akita");
            getLogger().info("Reloading config file....");
            try {
                setupConfig();
            }catch (Exception e) {
                config = new Config();
                e.printStackTrace();
            }
            getLogger().info("Done!");
            getLogger().info("Patcher Enabled");
            getLogger().info("-------------------");
            return;
        }
        getCommand("patcher").setExecutor(this);
        getLogger().info("-------------------");
        try {
            setupConfig();
        }catch (Exception e) {
            config = new Config();
            e.printStackTrace();
        }
        getLogger().info("Patcher");
        getLogger().info("Version: " + VERSION);
        getLogger().info("Author: akita");
        getLogger().info("Setting up Patch Manager");
        PatchManager.registerPatchs();
        getLogger().info("Patch Manager is done!");
        getLogger().info("Setting up listener");
        getServer().getPluginManager().registerEvents(new GlobalListener() , this);
        getLogger().info("Patcher Enabled");
        getLogger().info("-------------------");
    }

}
