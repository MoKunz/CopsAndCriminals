package com.talesdev.copsandcrims;

import com.talesdev.copsandcrims.arena.data.ArenaCollection;
import com.talesdev.copsandcrims.arena.CvCArena;
import com.talesdev.copsandcrims.arena.system.CvCArenaController;
import com.talesdev.core.config.ConfigFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ServerCvCArena
 *
 * @author MoKunz
 */
public class ServerCvCArena {
    private ConfigFile configFile;
    private List<CvCArena> arenaList;
    private List<CvCArenaController> controllerList;
    private CopsAndCrims plugin;

    public ServerCvCArena(CopsAndCrims plugin) {
        this.plugin = plugin;
        File dir = new File("plugins/CopsAndCrims/arena");
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) System.out.println("Error while creating directory plugins/CopsAndCrims/arena !");
        }
        this.configFile = new ConfigFile("plugins/CopsAndCrims/arena.yml");
        this.arenaList = new ArrayList<>();
        this.controllerList = new ArrayList<>();
    }

    public List<CvCArenaController> getControllerList() {
        return controllerList;
    }

    public void addController(CvCArenaController controller) {
        if (!containsController(controller)) getControllerList().add(controller);
    }

    public void loadArenaInfo() {
        // begin arena loading
        ArenaCollection collection = new ArenaCollection(this);
        collection.getAll().forEach(this::addArena);
    }

    public CvCArenaController getController(String type) {
        for (CvCArenaController arenaController : getControllerList()) {
            if (arenaController.getArenaType().equalsIgnoreCase(type)) {
                return arenaController;
            }
        }
        return null;
    }

    public void removeController(CvCArenaController controller) {
        if (containsController(controller)) getControllerList().remove(controller);
    }

    public boolean containsController(CvCArenaController controller) {
        for (CvCArenaController arenaController : getControllerList()) {
            if (arenaController.getArenaType().equalsIgnoreCase(controller.getArenaType())) {
                return true;
            }
        }
        return false;
    }

    public void addArena(CvCArena arena) {
        if (arena != null) {
            if (!containsArena(arena.getArenaName())) {
                getArenaList().add(arena);
            }
        }
    }

    public CvCArena getArena(String arenaName) {
        if (getArenaList().size() > 0) {
            for (CvCArena cArena : getArenaList()) {
                if (cArena.getArenaName().equals(arenaName)) {
                    return cArena;
                }
            }
        }
        return null;
    }

    public void removeArena(CvCArena arena) {
        getArenaList().remove(arena);
    }

    public int totalArena() {
        return getArenaList().size();
    }

    public boolean containsArena(String arena) {
        if (getArenaList().size() > 0) {
            for (CvCArena cArena : getArenaList()) {
                if (cArena.getArenaName().equals(arena)) {
                    return true;
                }
            }
        }
        return false;
    }

    public FileConfiguration getConfig() {
        return configFile.getConfig();
    }

    public List<CvCArena> getArenaList() {
        return arenaList;
    }

    public void shutDown() {
        save();
        getArenaList().forEach(CvCArena::shutDown);
    }

    public void save() {
        // save individuals arena
        getArenaList().forEach(CvCArena::save);
        // write global arena info to config object
        ArenaCollection collection = new ArenaCollection(this);
        collection.saveAll(getArenaList());
        // save global arena info
        configFile.save();
    }

    public void load() {
        // load config
        configFile.load();
        getArenaList().forEach(CvCArena::load);
    }
}
