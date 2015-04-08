package com.talesdev.copsandcrims.guns;

import com.talesdev.copsandcrims.weapon.Weapon;
import com.talesdev.copsandcrims.weapon.WeaponType;
import com.talesdev.copsandcrims.weapon.bullet.Accuracy;
import com.talesdev.copsandcrims.weapon.bullet.BulletAccuracy;
import com.talesdev.copsandcrims.weapon.module.ItemControlModule;
import com.talesdev.copsandcrims.weapon.module.ShootingModule;
import com.talesdev.core.math.Range;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Desert Eagle
 *
 * @author MoKunz
 */
public class DesertEagle extends Weapon {

    public DesertEagle() {
        super("DesertEagle", "Desert Eagle", blankAliases(), WeaponType.PISTOL);
        ShootingModule shootingModule = new ShootingModule();
        shootingModule.setRecoil(5.0D);
        shootingModule.setMaxBullet(30);
        shootingModule.setBulletDelay(1);
        shootingModule.setCooldownTime(3);
        shootingModule.setAccuracy(new BulletAccuracy(
                new Accuracy(new Range(-1, 1), new Range(-1, 1), new Range(-1, 1)), // default
                new Accuracy(new Range(0, 0), new Range(0, 0), new Range(0, 0)), // sneaking
                new Accuracy(new Range(-1, 1), new Range(-1, 1), new Range(-1, 1)), // walking
                new Accuracy(new Range(-1, 1), new Range(-1, 1), new Range(-1, 1)), // sprinting
                new Accuracy(new Range(-1, 1), new Range(-1, 1), new Range(-1, 1)) // jumping
        ));
        addModule(shootingModule);
        ItemControlModule controlModule = new ItemControlModule();
        addModule(controlModule);
    }

    @Override
    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.GOLD_HOE, 30);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + getDisplayName());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public boolean isWeapon(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getType().equals(Material.GOLD_HOE)) {
            return true;
        }
        return super.isWeapon(itemStack);
    }
}
