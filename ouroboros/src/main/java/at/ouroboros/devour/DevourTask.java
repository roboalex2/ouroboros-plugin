package at.ouroboros.devour;

import at.ouroboros.main.Ouroboros;
import at.ouroboros.utils.FakeBlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class DevourTask {

    private final Player target;
    private final int maxBlocksPerTick;

    private final Queue<Block> queue = new LinkedList<>();
    private final Set<Block> visited = new HashSet<>();

    public DevourTask(Player target, Block startBlock, int maxBlocksPerTick) {
        this.target = target;
        this.maxBlocksPerTick = maxBlocksPerTick;
        enqueueBlock(startBlock);
    }

    public boolean processTick() {
        int processed = 0;

        while (!queue.isEmpty() && processed < maxBlocksPerTick) {
            Block current = queue.poll();
            if (current == null) continue;

            // Remove the block
            devour(current);

            // Enqueue neighboring blocks for further processing.
            for (Block neighbor : getAdjacentBlocks(current)) {
                enqueueBlock(neighbor);
            }

            processed++;
        }

        // Task is finished when there are no blocks left to process.
        return queue.isEmpty();
    }

    /**
     * Re‑adds a previously erased block to the devour queue so that it
     * will be removed again. For instance, if you modify the criteria
     * or need to “reset” a part of the erasure.
     *
     * @param block the block to re‑add.
     */
    public void readdBlock(Block block) {
        visited.remove(block);
        enqueueBlock(block);
    }

    /**
     * Returns the target player for this task.
     *
     * @return the target player.
     */
    public Player getTarget() {
        return target;
    }

    // ─── PRIVATE HELPER METHODS ─────────────────────────────

    private void devour(Block block) {
        ArmorStand armorStand = FakeBlockUtils.spawnFakeBlock(block.getLocation());
        (new BukkitRunnable() {
            @Override
            public void run() {
                armorStand.remove();
            }
        }).runTaskLater(Ouroboros.getPlugin(), 20L * 5);
        block.breakNaturally();
    }

    /**
     * Enqueues a block for processing if it hasn’t been visited and qualifies for devouring.
     *
     * @param block the block to potentially enqueue.
     */
    private void enqueueBlock(Block block) {
        if (block == null) return;
        if (visited.contains(block)) return;

        if (isValidForDevour(block)) {
            visited.add(block);
            queue.add(block);
        }
    }

    /**
     * Determines whether a block qualifies for removal.
     * Customize this logic as needed (e.g. ignore air, bedrock, etc.).
     *
     * @param block the block to test.
     * @return true if the block should be devoured, false otherwise.
     */
    private boolean isValidForDevour(Block block) {
        Material mat = block.getType();
        return mat != Material.AIR && mat != Material.BEDROCK;
    }

    /**
     * Gets the neighboring blocks in 6 directions (up, down, north, south, east, west).
     *
     * @param block the current block.
     * @return an iterable over adjacent blocks.
     */
    private Iterable<Block> getAdjacentBlocks(Block block) {
        Location loc = block.getLocation();
        World world = block.getWorld();
        Queue<Block> neighbors = new LinkedList<>();

        neighbors.add(world.getBlockAt(loc.clone().add(1, 0, 0)));
        neighbors.add(world.getBlockAt(loc.clone().add(-1, 0, 0)));
        neighbors.add(world.getBlockAt(loc.clone().add(0, 1, 0)));
        neighbors.add(world.getBlockAt(loc.clone().add(0, -1, 0)));
        neighbors.add(world.getBlockAt(loc.clone().add(0, 0, 1)));
        neighbors.add(world.getBlockAt(loc.clone().add(0, 0, -1)));

        return neighbors;
    }
}