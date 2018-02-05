package net.thedragonteam.pec.obj;

import net.minecraft.util.math.BlockPos;

import static java.lang.Double.parseDouble;

public class BeamPos {
    private double x, y, z;

    public BeamPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BeamPos(String[] xyz) {
        this(parseDouble(xyz[0]), parseDouble(xyz[1]), parseDouble(xyz[2]));
    }

    public BeamPos(double x, String[] yz) {
        this(x, parseDouble(yz[1]), parseDouble(yz[2]));
    }

    public BeamPos(double x, double y, String[] z) {
        this(x, y, parseDouble(z[2]));
    }

    public BeamPos(String[] x, double y, double z) {
        this(parseDouble(x[0]), y, z);
    }

    public BeamPos(String[] xy, double z) {
        this(parseDouble(xy[0]), parseDouble(xy[1]), z);
    }

    public BeamPos(double x, String[] y, double z) {
        this(x, parseDouble(y[1]), z);
    }

    public BeamPos(String[] x, double y, String[] z) {
        this(parseDouble(x[0]), y, parseDouble(z[2]));
    }

    public BlockPos getBlockPos() {
        return new BlockPos(x, y, z);
    }
}