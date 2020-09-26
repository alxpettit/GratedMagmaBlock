package xyz.achu.mods.mixin;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Our homemade datafixer because I fucked up the name in the previous iteration
@Mixin(Identifier.class)
class IdentifierMixin {
    @Shadow
    protected String namespace;
    @Shadow
    protected String path;

    @Inject(method = "<init>([Ljava/lang/String;)V", at = @At("RETURN"))
    private void onConstructor(String[] id, CallbackInfo ci) {
        if(namespace == "vanillascented" && path == "grated_magma_block")
            path = "grated_magma";
    }
}
