package love.marblegate.homing_ender_eye.mixin;

import love.marblegate.homing_ender_eye.HomingEnderEye;
import love.marblegate.homing_ender_eye.misc.Configuration;
import love.marblegate.homing_ender_eye.misc.EnderEyeDestroyData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEnderEye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.UUID;

@Mixin(ItemEnderEye.class)
public class MixinEnderEyeItem {

    @Inject(method ="onItemRightClick",
            at= @At(value ="INVOKE", target = "Lnet/minecraft/entity/item/EntityEnderEye;moveTowards(Lnet/minecraft/util/math/BlockPos;)V", shift = At.Shift.AFTER))
    public void captureThrowSource(World worldIn, EntityPlayer playerIn, EnumHand handIn, CallbackInfoReturnable<ActionResult<ItemStack>> cir){
        // Only Signal Cache to Remember Who just throw an ender eye
        // Only works if the mode is running on Individual Mode
        if(worldIn instanceof WorldServer && worldIn.provider.getDimension()==0){
            if(Configuration.INDIVIDUAL_MODE){
                HomingEnderEye.EYE_THROW_CACHE.putThrowRecord(playerIn.getUniqueID());
            }
        }

    }

    @ModifyArg(method="onItemRightClick",
            at=@At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), index = 0)
    public Entity captureEnderEyeBreak(Entity entity) throws NoSuchFieldException, IllegalAccessException {
        // Pre Handling Ender Eye Break
        if(entity.world instanceof WorldServer && entity.world.provider.getDimension()==0){
            boolean shatterOrDrop = ObfuscationReflectionHelper.getPrivateValue(EntityEnderEye.class,(EntityEnderEye) entity,"field_70221_f");
            if(!shatterOrDrop){
                EnderEyeDestroyData data = EnderEyeDestroyData.get(entity.world);

                if(Configuration.INDIVIDUAL_MODE){
                    UUID throwerUUID = HomingEnderEye.EYE_THROW_CACHE.peek();
                    if(throwerUUID!=null){
                        data.increaseCount(throwerUUID);
                    }
                } else {
                    data.increaseCount(null);
                }
            }
            // Once a eye is thrown, remove a record
            if(Configuration.INDIVIDUAL_MODE){
                HomingEnderEye.EYE_THROW_CACHE.retrieveThrowerRecord();
            }
        }
        return entity;
    }

}
