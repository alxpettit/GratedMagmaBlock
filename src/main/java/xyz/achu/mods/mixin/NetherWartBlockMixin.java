package xyz.achu.mods.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static xyz.achu.mods.VanillaScentedAdditions.GRATED_SOUL_SAND_BLOCK;

@Mixin(NetherWartBlock.class)
public class NetherWartBlockMixin {
    @Inject(method = "canPlantOnTop", at = @At("RETURN"), cancellable = true)
    private void canPlantOnTopMixin(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        // Can be planted on whatever it was before *AND* our block.
        cir.setReturnValue(cir.getReturnValue() || floor.isOf(GRATED_SOUL_SAND_BLOCK));
    }
}
