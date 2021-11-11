package love.marblegate.homing_ender_eye.misc;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderEyeDestroyData extends WorldSavedData {
    private Gson gson;
    private Type type;
    private boolean shared;
    private int count;
    private Map<UUID,Integer> countMap;

    public EnderEyeDestroyData(String string) {
        super(string);
        shared = !Configuration.INDIVIDUAL_MODE;
        count = 0;
        gson = new Gson();
        countMap = new HashMap<>();
        type = new TypeToken<HashMap<UUID,Integer>>() {}.getType();
    }

    public static EnderEyeDestroyData get(World world){
        if (!(world instanceof WorldServer)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }

        WorldServer worldServer = world.getMinecraftServer().getWorld(0);
        MapStorage storage = worldServer.getMapStorage();
        EnderEyeDestroyData enderEyeDestroyData = (EnderEyeDestroyData) storage.getOrLoadData(EnderEyeDestroyData.class, "endereyedestroy");
        if(enderEyeDestroyData == null){
            enderEyeDestroyData = new EnderEyeDestroyData("endereyedestroy");
            storage.setData("endereyedestroy",enderEyeDestroyData);
        }
        return enderEyeDestroyData;
    }

    public int getCount(@Nullable UUID uuid) {
        if(shared){
            return count;
        } else {
            return uuid == null?0:countMap.getOrDefault(uuid,0);
        }
    }

    public void setCount(@Nullable UUID uuid, int count) {
        count = Math.max(count, 0);
        if(shared) {
            this.count = count;
            markDirty();
        }
        else{
            if(uuid!=null){
                countMap.put(uuid,count);
                markDirty();
            }
        }
    }

    public void increaseCount(@Nullable UUID uuid){
        if(shared) {
            count += 1;
            markDirty();
        }
        else{
            if(uuid!=null){
                if(countMap.containsKey(uuid)){
                    countMap.put(uuid,countMap.get(uuid)+1);
                } else {
                    countMap.put(uuid,1);
                }
                markDirty();
            }
        }
    }

    public void decreaseCount(@Nullable UUID uuid){
        if(shared) {
            count = Math.max(0,count - 1);
            markDirty();
        }
        else{
            if(uuid!=null){
                countMap.put(uuid,Math.max(0,countMap.get(uuid)-1));
                markDirty();
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("shared_destroy_count"))
            count = nbt.getInteger("shared_destroy_count");
        if(nbt.hasKey("individual_destroy_count"))
            countMap = gson.fromJson(nbt.getString("individual_destroy_count"),type);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("shared_destroy_count",count);
        compound.setString("individual_destroy_count",gson.toJson(countMap,type));
        return compound;
    }
}
