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
import tc.oc.tracker.timer.TickTimer;
import tc.oc.tracker.trackers.base.gravity.Attack;
import tc.oc.tracker.trackers.base.gravity.SimpleGravityKillTracker;

import com.google.common.base.Preconditions;

public class GravityDamageResolver implements DamageResolver {
    public GravityDamageResolver(@Nonnull SimpleGravityKillTracker tracker, @Nonnull TickTimer timer) {
        Preconditions.checkNotNull(tracker, "tracker");
        Preconditions.checkNotNull(timer, "timer");

        this.tracker = tracker;
        this.timer = timer;
    }

    public @Nullable DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime, @Nonnull EntityDamageEvent damageEvent) {
        if(!(entity instanceof Player)) return null;

        Player player = (Player) entity;
        long time = this.timer.getTicks();

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

    private final @Nonnull SimpleGravityKillTracker tracker;
    private final @Nonnull TickTimer timer;
}
