package at.ouroboros.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public class FakeBlockUtils {

    /**
     * Spawns an invisible ArmorStand with a black concrete block as its head.
     * The ArmorStand has gravity disabled and is marked so that no additional
     * model parts are visible.
     *
     * @param loc the location at which to spawn the fake block.
     * @return a reference to the ArmorStand representing the fake block.
     */
    public static ArmorStand spawnFakeBlock(Location loc) {
        World world = loc.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location must have a valid world!");
        }

        // Spawn the ArmorStand. Using a lambda to configure it immediately.
        return world.spawn(loc, ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setBasePlate(false);
            armorStand.setArms(false);

            armorStand.setMarker(true);

            armorStand.getEquipment().setHelmet(new ItemStack(Material.BLACK_CONCRETE));

            armorStand.setInvulnerable(true);
        });
    }
}