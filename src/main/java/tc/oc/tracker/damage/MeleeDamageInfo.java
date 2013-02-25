package tc.oc.tracker.damage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.LivingEntity;

import tc.oc.tracker.base.AbstractDamageInfo;

import com.google.common.base.Preconditions;

public class MeleeDamageInfo extends AbstractDamageInfo {
    public MeleeDamageInfo(@Nullable LivingEntity resolvedDamager) {
        super(resolvedDamager);

        Preconditions.checkNotNull(resolvedDamager, "resolvedDamager");
    }

    @Override
    public @Nonnull LivingEntity getResolvedDamager() {
        return this.resolvedDamager;
    }

    @Override
    public @Nonnull String toString() {
        return "MeleeDamageInfo{damager=" + this.resolvedDamager + "}";
    }
}
