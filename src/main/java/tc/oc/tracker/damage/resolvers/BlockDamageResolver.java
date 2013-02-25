package tc.oc.tracker.damage.resolvers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import tc.oc.tracker.DamageInfo;
import tc.oc.tracker.DamageResolver;
import tc.oc.tracker.Lifetime;
import tc.oc.tracker.damage.SimpleBlockDamageInfo;

public class BlockDamageResolver implements DamageResolver {
    public @Nullable DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime, @Nonnull EntityDamageEvent damageEvent) {
        if(damageEvent instanceof EntityDamageByBlockEvent) {
            EntityDamageByBlockEvent event = (EntityDamageByBlockEvent) damageEvent;
            if(damageEvent.getCause() == DamageCause.CONTACT) {
                return new SimpleBlockDamageInfo(event.getDamager().getState());
            }
        }
        return null;
    }
}
