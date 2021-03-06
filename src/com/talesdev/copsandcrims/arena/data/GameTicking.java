package com.talesdev.copsandcrims.arena.data;

import com.talesdev.copsandcrims.arena.game.TDMArenaController;
import com.talesdev.copsandcrims.player.CvCPlayer;
import com.talesdev.core.math.DigitalTime;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Task for game logic loop
 *
 * @author MoKunz
 */
public class GameTicking extends BukkitRunnable {
    private TDMArenaController controller;
    private DigitalTime timer;

    public GameTicking(TDMArenaController controller) {
        this.controller = controller;
        this.timer = new DigitalTime(300);
    }

    @Override
    public void run() {
        timer.oneSecond();
        if (controller.getArenaStatus().equals(ArenaStatus.END)) {
            this.cancel();
            return;
        }
        for (CvCPlayer player : controller.getAllPlayers()) {
            controller.getGameScoreboard().updateGame(player, timer);
        }
        if (timer.timesUp()) {
            controller.endArena();
            this.cancel();
            return;
        }
    }
}
