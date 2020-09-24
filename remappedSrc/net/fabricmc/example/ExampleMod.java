package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class ExampleMod implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Hello Fabric world!");
		public static final Block GRATED_MAGMA_BLOCK;
		//noinspection deprecation
		GRATED_MAGMA_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).hardness(4.0f));
	}
}
