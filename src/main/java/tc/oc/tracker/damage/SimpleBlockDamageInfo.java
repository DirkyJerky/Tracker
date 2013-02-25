package tc.oc.tracker.damage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;

import tc.oc.tracker.BlockDamageInfo;

public class SimpleBlockDamageInfo implements BlockDamageInfo {
    private final BlockState block;

    public SimpleBlockDamageInfo(@Nonnull BlockState block) {
        this.block = block;
    }

    @Nonnull public BlockState getBlockDamager() {
        return this.block;
    }

    @Nullable public LivingEntity getResolvedDamager() {
        return null;
    }
}
