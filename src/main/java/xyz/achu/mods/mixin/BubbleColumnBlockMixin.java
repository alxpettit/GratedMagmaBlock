package xyz.achu.mods.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static xyz.achu.mods.VanillaScentedAdditions.GRATED_MAGMA_BLOCK;
import static xyz.achu.mods.VanillaScentedAdditions.GRATED_SOUL_SAND_BLOCK;

@Mixin(BubbleColumnBlock.class)
public class BubbleColumnBlockMixin {
	// big thanks to user '!!' on the Fabric Discord for keying me in on this solution! :D
	@Inject(method = "canPlaceAt", at = @At("RETURN"), cancellable = true)
	private void canPlaceAtMixin(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(cir.getReturnValue() ||
				world.getBlockState(pos.down()).isOf(GRATED_MAGMA_BLOCK) ||
				world.getBlockState(pos.down()).isOf(GRATED_SOUL_SAND_BLOCK)
				);
	}
}

