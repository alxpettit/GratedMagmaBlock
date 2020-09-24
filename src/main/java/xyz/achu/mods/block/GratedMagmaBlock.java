package xyz.achu.mods.block;

import net.minecraft.block.MagmaBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GratedMagmaBlock extends MagmaBlock {
    public GratedMagmaBlock(Settings settings) {
        super(settings);
    }

    public void onSteppedOn(World world, BlockPos pos, Entity entity) {
        // do nothing
    }
}