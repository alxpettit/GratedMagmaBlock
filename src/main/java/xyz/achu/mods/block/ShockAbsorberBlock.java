package xyz.achu.mods.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static xyz.achu.mods.VanillaScentedAdditions.SOUND_EVENT_LOWER_SHOCK_ABSORBER;
import static xyz.achu.mods.VanillaScentedAdditions.SOUND_EVENT_RAISE_SHOCK_ABSORBER;

// TODO: fix culling when this block is adjacent to itself
// TODO: fix culling when this block is adjacent to itself

@SuppressWarnings("deprecation") // mojang sthap
public class ShockAbsorberBlock extends TransparentBlock {
    // Shape when normal
    protected static final VoxelShape NORMAL_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D,
            16.0D, 16.0D, 16.0D);
    // Shape when we are collapsed (something landed on us)
    protected static final VoxelShape COLLAPSED_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D,
            16.0D, 8.0D, 16.0D);

    // This is the area we check in to see if something is on top of us
    protected static final Box ENTITY_SCANNING_BOX =
            new Box(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    // Current shape
    protected static VoxelShape SHAPE;
    // Property for tracking whether we are collapsed or not
    public static final BooleanProperty COLLAPSED_STATE = BooleanProperty.of("collapsed");

    public ShockAbsorberBlock(Settings settings) {
        super(settings);
        // Set our default state
        setDefaultState(getStateManager().getDefaultState().with(COLLAPSED_STATE, false));
        SHAPE = NORMAL_SHAPE; // By default our shape is normal
    }

    ////////////// OVERRIDE STUFFS /////////////////

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(COLLAPSED_STATE);
    }

    @Override // No need to do random ticks if we're in a normal state :)
    public boolean hasRandomTicks(BlockState state) {
        return state.get(COLLAPSED_STATE);
    }

    @Override
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance) {
        BlockState state = world.getBlockState(pos);
        // No entity damage
        entity.handleFallDamage(distance, 0.0F);
        if (distance > 4) {
            // set state to collapsed
            setCollapsedState(true, state, world, pos);
            // later on, we'll want to tick this block so we can update once player steps off
            world.getBlockTickScheduler().schedule(pos, this, getTickRate());
        }
    }

    // parent abstract class will cull faces automatically based on cached shape -- this prevents that.
    @Override // For some reason tho it still culls when these Shock Absorber blocks are adjacent... WHYYY!???
    public boolean hasDynamicBounds() { return true; }

    // Let game know our shape.
    // Stolen from snow code :)
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) { return SHAPE; }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) { return SHAPE; }
    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) { return SHAPE; }
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) { return SHAPE; }

    @Override
    public boolean hasSidedTransparency(BlockState state) { return true; }
    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) { return true; }

    // Enable checking for block redstone status :)
    @Override
    public boolean emitsRedstonePower(BlockState state) { return true; }

    // Weak redstone power is redstone power that doesn't weakly power all adjacent blocks
    // We set level to 15 when state is collapsed.
    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(COLLAPSED_STATE) ? 15 : 0;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (noEntitiesAbove(world, pos)) {
            // pop up block
            setCollapsedState(false, state, world, pos);
        } else {
            // Check again during next tick
            if (!world.getBlockTickScheduler().isScheduled(pos, this)) {
                world.getBlockTickScheduler().schedule(pos, this, getTickRate());
            }
            setCollapsedState(true, state, world, pos); // we should be collapsed
        }
    }

    /////////////////// METHODS NOT PRESENT IN PARENT CLASS ///////////////////

    // Check if living entities are above us
    protected boolean noEntitiesAbove(World world, BlockPos pos) {
        Box box = ENTITY_SCANNING_BOX.offset(pos);
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box, null);
        return entities.isEmpty();
    }

    protected void setCollapsedState(boolean is_collapsed, BlockState state, World world, BlockPos pos) {
        if (state.get(COLLAPSED_STATE) == is_collapsed) {
            return;
        }
        SHAPE = is_collapsed ? COLLAPSED_SHAPE : NORMAL_SHAPE;
        BlockState newState = state.with(COLLAPSED_STATE, is_collapsed);
        world.scheduleBlockRerenderIfNeeded(pos, state, newState);
        world.setBlockState(pos, newState);
        world.updateNeighborsAlways(pos, this);
        if (is_collapsed) {
            world.playSound(null, pos, SOUND_EVENT_LOWER_SHOCK_ABSORBER, SoundCategory.BLOCKS, 1F, 1F);
        } else {
            world.playSound(null, pos, SOUND_EVENT_RAISE_SHOCK_ABSORBER, SoundCategory.BLOCKS, 1F, 1F);
        }
    }

    protected int getTickRate() {
        return 20;
    }
}
