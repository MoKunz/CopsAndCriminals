package com.talesdev.copsandcrims.weapon.module;

import com.talesdev.copsandcrims.CopsAndCrims;
import com.talesdev.copsandcrims.player.CvCPlayer;
import com.talesdev.copsandcrims.weapon.WeaponCooldownTag;
import com.talesdev.copsandcrims.weapon.bullet.Accuracy;
import com.talesdev.copsandcrims.weapon.bullet.BulletAccuracy;
import com.talesdev.copsandcrims.weapon.bullet.BulletTask;
import com.talesdev.copsandcrims.weapon.bullet.DelayedBullet;
import com.talesdev.core.math.Range;
import com.talesdev.core.player.ClickingAction;
import com.talesdev.core.world.sound.Sound;
import com.talesdev.core.world.sound.SoundEffect;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Dummy module
 */
public class DummyModule extends WeaponModule {
    public DummyModule() {
        super("Dummy");
    }

    //@EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (getWeapon().isWeapon(event.getItem()) &&
                ClickingAction.isRightClick(event.getAction())) {
            WeaponCooldownTag tag = new WeaponCooldownTag(3, event.getPlayer());
            if (!tag.isCooldown()) {
                CvCPlayer player = CopsAndCrims.getPlugin().getServerCvCPlayer().getPlayer(event.getPlayer());
                double recoil = 0.0;
                if (player != null) recoil = player.getPlayerRecoil().getRecoil(getWeapon());
                Accuracy accuracy = new Accuracy(new Range(-1, 1), new Range(-1, 1), new Range(-1, 1));
                DelayedBullet bullet = new DelayedBullet(
                        event.getPlayer(), null, 4,
                        new BulletAccuracy(
                                accuracy,
                                new Accuracy(new Range(0, 0), new Range(0, 0), new Range(0, 0)),
                                accuracy,
                                accuracy,
                                accuracy
                        ),
                        5.0D, getWeapon()
                );
                bullet.setRecoil(recoil);
                bullet.setRayParameter(2000, 0.05, 4);
                bullet.setSpeed(500);
                (new BulletTask(bullet, 1, getWeapon())).runTaskTimer(CopsAndCrims.getPlugin(), 0, 1);
                float volume = 1.0F;
                if (event.getPlayer().getEyeLocation().getBlock().isLiquid()) volume = 0.8F;
                (new Sound(SoundEffect.MOB_SKELETON_DEATH, volume, 1)).playSound(event.getPlayer(), event.getPlayer().getLocation());
                if (player != null) player.getPlayerRecoil().addRecoil(getWeapon(), 5.0D);
                tag.attach();
            }
            event.setUseItemInHand(Event.Result.DENY);
        }
    }
}