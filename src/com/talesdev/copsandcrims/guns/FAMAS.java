package com.talesdev.copsandcrims.guns;

import com.talesdev.copsandcrims.weapon.Weapon;
import com.talesdev.copsandcrims.weapon.WeaponType;
import com.talesdev.copsandcrims.weapon.bullet.Accuracy;
import com.talesdev.copsandcrims.weapon.bullet.BulletAccuracy;
import com.talesdev.copsandcrims.weapon.module.AlternativeFireModule;
import com.talesdev.copsandcrims.weapon.module.DeathMessageModule;
import com.talesdev.copsandcrims.weapon.module.ItemControlModule;
import com.talesdev.copsandcrims.weapon.module.ShootingModule;
import com.talesdev.core.math.Range;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * FAMAS
 *
 * @author MoKunz & sonSunnoi
 */
public class FAMAS extends Weapon {

    public FAMAS() {
        super("FAMAS", ChatColor.GREEN + "FAMAS", blankAliases(), WeaponType.ASSAULT_RIFLE);
        ShootingModule shootingModule = new ShootingModule();
        ItemControlModule controlModule = new ItemControlModule();
        DeathMessageModule deathMessageModule = new DeathMessageModule();
        AlternativeFireModule alternativeFireModule = new AlternativeFireModule();
        shootingModule.setDamage(6);
        shootingModule.setHeadShotDamage(22);
        shootingModule.setUpperLegDamage(5.2);
        shootingModule.setLowerLegDamage(3.9);
        shootingModule.setRecoil(4.75D);
        shootingModule.setMaxBullet(25);
        shootingModule.setBulletDelay(2);
        shootingModule.setBulletCount(2);
        shootingModule.setCooldownTime(2);
        shootingModule.setReloadTime(66);
        shootingModule.setAccuracy(new BulletAccuracy(
                new Accuracy(new Range(-12, 12), new Range(-12, 12), new Range(-12, 12)), // default
                new Accuracy(new Range(5, 5), new Range(5, 5), new Range(5, 5)), // sneaking
                new Accuracy(new Range(-20, 20), new Range(-20, 20), new Range(-20, 20)), // walking
                new Accuracy(new Range(-80, 80), new Range(-80, 80), new Range(-80, 80)), // sprinting
                new Accuracy(new Range(-100, 100), new Range(-100, 100), new Range(-100, 100)) // jumping
        ));
        alternativeFireModule.setAlternativeFireBullet(3);
        alternativeFireModule.setAlternativeFireDelay(1);
        alternativeFireModule.setAlternativeFireCooldown(20);
        alternativeFireModule.setEnabled(true);
        addModule(shootingModule);
        addModule(alternativeFireModule);
        addModule(controlModule);
        addModule(deathMessageModule);
    }

    @Override
    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.WOOD_PICKAXE, 25);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + getDisplayName());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public boolean isWeapon(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getType().equals(Material.WOOD_PICKAXE)) {
            return true;
        }
        return super.isWeapon(itemStack);
    }
}