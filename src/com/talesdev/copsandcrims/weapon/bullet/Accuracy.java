package com.talesdev.copsandcrims.weapon.bullet;

import com.talesdev.core.math.MathRandom;
import com.talesdev.core.math.Range;
import org.bukkit.util.Vector;

/**
 * A class stored information about bullet accuracy in each scenario
 *
 * @author MoKunz
 */
public class Accuracy {
    private Range xSpread, ySpread, zSpread;

    public Accuracy(Range xSpread, Range ySpread, Range zSpread) {
        this.xSpread = xSpread;
        this.ySpread = ySpread;
        this.zSpread = zSpread;
    }

    public Range getXSpread() {
        return xSpread;
    }

    public void setXSpread(Range xSpread) {
        this.xSpread = xSpread;
    }

    public Range getYSpread() {
        return ySpread;
    }

    public void setYSpread(Range ySpread) {
        this.ySpread = ySpread;
    }

    public Range getZSpread() {
        return zSpread;
    }

    public void setZSpread(Range zSpread) {
        this.zSpread = zSpread;
    }

    public Vector toVector() {
        return new Vector(
                MathRandom.randomRange(
                        getXSpread().getStart(),
                        getXSpread().getEnd()) / 100D,
                MathRandom.randomRange(
                        getYSpread().getStart(),
                        getYSpread().getEnd()) / 100D,
                MathRandom.randomRange(
                        getZSpread().getStart(),
                        getZSpread().getEnd()) / 100D
        );
    }
}