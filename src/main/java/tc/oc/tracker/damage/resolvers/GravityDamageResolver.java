package tc.oc.tracker.damage.resolvers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import tc.oc.tracker.DamageInfo;
import tc.oc.tracker.DamageResolver;
import tc.oc.tracker.Lifetime;
import tc.oc.tracker.damage.GravityDamageInfo;
import tc.oc.tracker.trackers.base.gravity.Attack;
import tc.oc.tracker.trackers.base.gravity.SimpleGravityKillTracker;

public class GravityDamageResolver implements DamageResolver {
    private final SimpleGravityKillTracker tracker;

    public GravityDamageResolver(SimpleGravityKillTracker tracker) {
        this.tracker = tracker;
    }

    public @Nullable DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime, @Nonnull EntityDamageEvent damageEvent) {
        if(!(entity instanceof Player)) return null;

        Player player = (Player) entity;
        long time = this.tracker.timer.getTicks();

        if(damageEvent instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) damageEvent;
            this.tracker.playerAttacked(player, event.getDamager(), time);
        }

        if(this.tracker.attacks.containsKey(player)) {
            Attack attack = this.tracker.attacks.get(player);
            EntityDamageEvent.DamageCause damageCause = damageEvent.getCause();

            if(this.tracker.wasAttackFatal(attack, damageCause, time)) {
                return new GravityDamageInfo(attack.attacker, attack.cause, attack.from, attack.isClimbing, attack.isSwimming, attack.isInLava, player.isOnGround());
            }
        }

        return null;
    }
}
