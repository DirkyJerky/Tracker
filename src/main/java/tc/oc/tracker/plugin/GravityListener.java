package tc.oc.tracker.plugin;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerOnGroundEvent;

import tc.oc.tracker.trackers.base.gravity.Attack;
import tc.oc.tracker.trackers.base.gravity.BrokenBlock;
import tc.oc.tracker.trackers.base.gravity.SimpleGravityKillTracker;
import tc.oc.tracker.util.PlayerBlockChecker;

public class GravityListener implements Listener {
    private final SimpleGravityKillTracker tracker;

    public GravityListener(SimpleGravityKillTracker tracker) {
        this.tracker = tracker;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerOnGroundChanged(final PlayerOnGroundEvent event) {
        final Player player = event.getPlayer();
        long time = this.tracker.timer.getTicks();

        if(this.tracker.attacks.containsKey(player)) {
            Attack attack = this.tracker.attacks.get(player);

            if(event.getOnGround()) {
                attack.onGroundTime = time;
                this.tracker.victimOnGround(attack, time);
            } else {
                this.tracker.victimOffGround(attack, time);
            }

        } else if(!event.getOnGround()) {
            this.tracker.nonVictimOffGround(player, time);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(this.tracker.attacks.containsKey(player)) {
            Attack attack = this.tracker.attacks.get(player);
            long time = this.tracker.timer.getTicks();

            boolean isClimbing = PlayerBlockChecker.isClimbing(player);
            boolean isSwimming = PlayerBlockChecker.isSwimming(player, Material.WATER);
            boolean isInLava = PlayerBlockChecker.isSwimming(player, Material.LAVA);

            if(isClimbing != attack.isClimbing) {
                if((attack.isClimbing = isClimbing)) {
                    attack.climbingTime = time;
                    this.tracker.victimOnLadder(attack, time);
                } else {
                    this.tracker.victimOffLadder(attack, time);
                }
            }

            if(isSwimming != attack.isSwimming) {
                if((attack.isSwimming = isSwimming)) {
                    attack.swimmingTime = time;
                    this.tracker.victimInWater(attack, time);
                } else {
                    this.tracker.victimOutOfWater(attack, time);
                }
            }

            if(isInLava != attack.isInLava) {
                if((attack.isInLava = isInLava)) {
                    attack.inLavaTime = time;
                    this.tracker.victimInLava(attack, time);
                } else {
                    this.tracker.victimOutOfLava(attack, time);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (this.tracker.attacks.containsKey(player)) {
            this.tracker.attacks.remove(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerGameModeChange(final PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode() == GameMode.CREATIVE) {
            this.tracker.attacks.remove(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        long time = this.tracker.timer.getTicks();

        final BrokenBlock brokenBlock = new BrokenBlock(
            event.getBlock(),
            player,
            time
        );

        final Location bl = brokenBlock.block.getLocation();
        this.tracker.brokenBlocks.put(bl, brokenBlock);

        this.tracker.plugin.scheduleSyncDelayedTask(new Runnable() {
            public void run() {
                if (GravityListener.this.tracker.brokenBlocks.containsKey(bl) && GravityListener.this.tracker.brokenBlocks.get(bl) == brokenBlock) {
                    GravityListener.this.tracker.brokenBlocks.remove(bl);
                }
            }
        }, SimpleGravityKillTracker.MAX_SPLEEF_TIME + 1);
    }

}
