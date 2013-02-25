package tc.oc.tracker;

import javax.annotation.Nonnull;

import org.bukkit.block.BlockState;

/**
 * Provides more detailed information about a damage instance.
 *
 * Subclasses should be completely immutable.
 */
public interface BlockDamageInfo extends DamageInfo {
    /**
     * Gets the block responsible for this damage.
     *
     * @return Block doing the damage
     */
    @Nonnull BlockState getBlockDamager();
}
