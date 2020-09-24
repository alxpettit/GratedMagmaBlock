package xyz.achu.mods;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.achu.mods.block.GratedMagmaBlock;

public class VanillaScentedAdditions implements ModInitializer {
	public static final Block GRATED_MAGMA_BLOCK = new GratedMagmaBlock(FabricBlockSettings.of(Material.METAL).hardness(4.0f));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialize
		// Proceed with mild caution.

		System.out.println("Hello Fabric world!");
		Registry.register(Registry.BLOCK, new Identifier("vanillascented", "grated_magma_block"), GRATED_MAGMA_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("vanillascented", "grated_magma_block"), new BlockItem(GRATED_MAGMA_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
	}

}
