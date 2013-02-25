package tc.oc.tracker.damage;

import javax.annotation.Nullable;

import org.bukkit.entity.LivingEntity;

import tc.oc.tracker.DamageInfo;

public class FallDamageInfo implements DamageInfo {

    @Nullable public LivingEntity getResolvedDamager() {
        return null;
    }
}
