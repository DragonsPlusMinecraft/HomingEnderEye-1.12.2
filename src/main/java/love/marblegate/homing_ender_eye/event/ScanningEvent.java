package love.marblegate.homing_ender_eye.event;

import love.marblegate.homing_ender_eye.HomingEnderEye;
import love.marblegate.homing_ender_eye.misc.Configuration;
import love.marblegate.homing_ender_eye.misc.EnderEyeDestroyData;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = HomingEnderEye.MOD_ID)
public class ScanningEvent {
    @SubscribeEvent
    public static void scanningForFrame(TickEvent.PlayerTickEvent event){
        if(!event.player.world.isRemote && event.phase.equals(TickEvent.Phase.START) && event.player.world.provider.getDimension() == 0){
            if(event.player.world.getWorldTime() % Configuration.SCANNING_RATE == 0){
                EnderEyeDestroyData data = EnderEyeDestroyData.get(event.player.world);
                if(data.getCount(event.player.getUniqueID()) > 0){
                    EntityPlayer player = event.player;
                    World world = player.world;
                    BlockPos center = player.getPosition();
                    int offSet = Configuration.SCANNING_RADIUS;
                    for(BlockPos blockpos : BlockPos.getAllInBox(center.add(offSet,offSet,offSet),center.add(-offSet,-offSet,-offSet))){
                        IBlockState blockState = world.getBlockState(blockpos);

                        // Scanning for Frame and Filling
                        if (blockState.getBlock() == Blocks.END_PORTAL_FRAME && !blockState.getValue(BlockEndPortalFrame.EYE)){
                            data.decreaseCount(player.getUniqueID());
                            IBlockState newBlockState = blockState.withProperty(BlockEndPortalFrame.EYE, true);
                            world.setBlockState(blockpos, newBlockState,2);
                            world.updateComparatorOutputLevel(blockpos, Blocks.END_PORTAL_FRAME);
                            // Copy from Item Usage, Seems like it's for playing sound
                            // We do not need this
                            // world.levelEvent(1503, blockpos, 0);

                            // Check if portal is qualified or not
                            BlockPattern.PatternHelper patternhelper = BlockEndPortalFrame.getOrCreatePortalShape().match(world, blockpos);
                            if (patternhelper != null) {
                                BlockPos blockpos1 = patternhelper.getFrontTopLeft().add(-3, 0, -3);
                                for(int i = 0; i < 3; ++i) {
                                    for(int j = 0; j < 3; ++j) {
                                        world.setBlockState(blockpos1.add(i, 0, j), Blocks.END_PORTAL.getDefaultState(), 2);
                                    }
                                }
                                break;
                            }

                        }

                        // Check for remaining ender eye
                        if (data.getCount(player.getUniqueID()) == 0)
                            break;
                    }
                }
            }
        }
    }
}
