package tc.oc.tracker.damage.resolvers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import tc.oc.tracker.DamageInfo;
import tc.oc.tracker.DamageResolver;
import tc.oc.tracker.Lifetime;
import tc.oc.tracker.damage.MeleeDamageInfo;

public class MeleeDamageResolver implements DamageResolver {
    public @Nullable DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime, @Nonnull EntityDamageEvent damageEvent) {
        if(damageEvent instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) damageEvent;
            if(damageEvent.getCause() == DamageCause.ENTITY_ATTACK) {
                return new MeleeDamageInfo((LivingEntity) event.getEntity());
            }
        }
        return null;
    }
}
