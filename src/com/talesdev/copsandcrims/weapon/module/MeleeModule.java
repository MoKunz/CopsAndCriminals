package com.talesdev.copsandcrims.weapon.module;

import com.talesdev.core.entity.BoundingBox;
import com.talesdev.core.player.ActionBar;
import com.talesdev.core.system.NMSClass;
import com.talesdev.core.system.ReflectionUtils;
import com.talesdev.core.system.ReflectionUtils.RefClass;
import com.talesdev.core.system.ReflectionUtils.RefMethod;
import com.talesdev.core.world.NearbyEntity;
import com.talesdev.core.world.RayTrace;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

/**
 * Melee Module
 *
 * @author MoKunz
 */
public class MeleeModule extends WeaponModule {
    private double damage = 4;
    private double headDamage = 8;
    private double headBias = 0.36;
    private double backStabRange = 20;
    private double backStabDamage = 8;
    private double backStabHeadDamage = 12;
    private double meleeRange = 1.8D;

    public MeleeModule(double damage, double headDamage, double backStabDamage, double backStabHeadDamage) {
        super("Melee");
        this.damage = damage;
        this.headDamage = headDamage;
        this.backStabDamage = backStabDamage;
        this.backStabHeadDamage = backStabHeadDamage;
    }

    public MeleeModule(double damage, double headDamage) {
        this(damage, headDamage, headDamage, headDamage + damage);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            // type casting
            LivingEntity entity = (LivingEntity) event.getEntity();
            Player player = (Player) event.getDamager();
            if (getWeapon().isWeapon(player.getItemInHand())) {
                player.getItemInHand().setDurability((short) 0);
                // check for distance first
                if (entity.getLocation().distance(player.getLocation()) > getMeleeRange()) {
                    event.setCancelled(true);
                    return;
                }
                // op
                double damage;
                boolean backStab = false;
                if (isBackStab(Math.abs(getNMSYaw(entity)), Math.abs(getNMSYaw(player)))) {
                    backStab = true;
                }
                String message = ChatColor.RED + "";
                if (rayTrace(player)) {
                    message += "Headshot";
                    if (backStab) {
                        damage = getBackStabHeadDamage();
                    } else {
                        damage = getHeadDamage();
                    }
                } else {
                    if (backStab) {
                        damage = getBackStabDamage();
                    } else {
                        damage = getDamage();
                    }
                }
                if (backStab) message += " & Backstab!";
                else message += "!";
                ActionBar actionBar = new ActionBar(message);
                actionBar.send(player);
                // apply
                event.setDamage(damage);
            }
        }
    }

    private boolean isBackStab(double victimAngle, double backStabAngle) {
        return isAngleBetween(backStabAngle, normalizeAngle(victimAngle - getBackStabRange()), normalizeAngle(victimAngle + getBackStabRange()));
    }

    private boolean isAngleBetween(double target, double angle1, double angle2) {
        // make the angle from angle1 to angle2 to be <= 180 degrees
        double rAngle = ((angle2 - angle1) % 360 + 360) % 360;
        if (rAngle >= 180) {
            // swap
            double tmp = angle1;
            angle1 = angle2;
            angle2 = tmp;
        }
        // check if it passes through zero
        if (angle1 <= angle2)
            return target >= angle1 && target <= angle2;
        else
            return target >= angle1 || target <= angle2;
    }

    private double normalizeAngle(double angle) {
        if (angle >= 360) {
            return angle - 360;
        } else if (angle <= 0) {
            return angle + 360;
        } else {
            return angle;
        }
    }

    private boolean rayTrace(Player player) {
        NearbyEntity<LivingEntity> nearbyEntity = new NearbyEntity<>(player.getEyeLocation(), LivingEntity.class);
        Vector vector = player.getEyeLocation().toVector();
        RayTrace rayTrace = new RayTrace(player.getWorld(), player.getEyeLocation().toVector(), player.getEyeLocation().getDirection());
        for (int i = 0; i < 100; i++) {
            vector = rayTrace.iterate(0.05D);
            nearbyEntity.setLocation(vector.toLocation(player.getWorld()));
            LivingEntity entity = nearbyEntity.findNearestInRadius(3, true);
            if (entity != null) {
                if (entity.getName().equalsIgnoreCase(player.getName())) {
                    continue;
                }
                BoundingBox box = new BoundingBox(entity);
                // hit
                if (box.isInside(vector)) {
                    if ((Math.abs(entity.getEyeLocation().toVector().getY() - vector.getY()) <= 0.3) &&
                            (entity.getEyeLocation().toVector().distanceSquared(vector) <= getHeadBias())
                            ) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private double calculateYaw(Vector firstLocation, Vector secondLocation) {
        return Math.toDegrees(Math.atan2(
                Math.abs(firstLocation.getZ() - secondLocation.getZ()),
                Math.abs(firstLocation.getX() - secondLocation.getX())
        ));
    }

    private float getNMSYaw(LivingEntity entity) {
        RefClass craftLivingEntityClass = ReflectionUtils.getRefClass(NMSClass.getCBClass("entity.CraftLivingEntity"));
        RefClass nmsEntityLivingClass = ReflectionUtils.getRefClass(NMSClass.getNMSClass("EntityLiving"));
        RefMethod getHandleMethod = craftLivingEntityClass.getMethod("getHandle");
        RefMethod getHeadRotationMethod = nmsEntityLivingClass.getMethod("getHeadRotation");
        return (float) getHeadRotationMethod.of(getHandleMethod.of(entity).call()).call();
    }

    private double getOppositeAngle(double angle) {
        double newAngle = angle + 180;
        if (newAngle >= 360) {
            newAngle -= 360;
        }
        return newAngle;
    }

    public double getBackStabHeadDamage() {
        return backStabHeadDamage;
    }

    public void setBackStabHeadDamage(double backStabHeadDamage) {
        this.backStabHeadDamage = backStabHeadDamage;
    }

    public double getBackStabDamage() {
        return backStabDamage;
    }

    public void setBackStabDamage(double backStabDamage) {
        this.backStabDamage = backStabDamage;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getHeadDamage() {
        return headDamage;
    }

    public void setHeadDamage(double headDamage) {
        this.headDamage = headDamage;
    }

    public double getMeleeRange() {
        return meleeRange;
    }

    public void setMeleeRange(double meleeRange) {
        this.meleeRange = meleeRange;
    }

    public double getHeadBias() {
        return headBias;
    }

    public void setHeadBias(double headBias) {
        this.headBias = headBias;
    }

    public double getBackStabRange() {
        return backStabRange;
    }

    public void setBackStabRange(double backStabRange) {
        this.backStabRange = backStabRange;
    }
}