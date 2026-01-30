package com.github.MoonAndStuff.ability.skill.extra;

import com.github.MoonAndStuff.hag;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.tensura.ability.skill.Skill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;


/**
 * This Common Skill is a simple one to show toggleable skills.
 * When toggled, it will activate an absorption effect for the player, giving them
 * a little more health.
 *
 *
 */
public class EfficientBait extends Skill {

    /**
     * This is where you define the path to the actual icon image.
     * @return
     */
    public ResourceLocation getSkillIcon() {
        return new ResourceLocation(hag.MODID, "textures/skill/common/example_common.png");
    }

    // Here are some easy to change parameters to configure for the skill
    private final double skillCastCost = 1000.0;     // How many magicules it costs to cast
    private final double epUnlockCost = 10000.0;   // EP Level required for unlocking the skill
    private final double learnCost = 500.0;           // When learning the skill, how hard is it. (Higher = harder). Default is 2.0

    public EfficientBait() {
        super(SkillType.COMMON);
    }

    /**
     * This function determines whether or not the player has the skill unlocked.
     * In this case, I will make the requirements be that you have to kill 10 vanilla slimes, and have at least 10,000 ep
     *
     * @param entity
     * @param curEP
     * @return
     */
    public boolean meetEPRequirement(Player entity, double curEP) {
        if (entity instanceof ServerPlayer player) {
            int timesFished = player.getStats().getValue(Stats.CUSTOM.get(Stats.FISH_CAUGHT));

            return timesFished >= 20 && curEP >= epUnlockCost;
        } else {
            return false;
        }
    }

    /**
     * A function that determines the cost of using the skill. In this case, it will cost 100.0 a tick
     * @param entity
     * @param instance
     * @return
     */
    public double magiculeCost(LivingEntity entity, ManasSkillInstance instance) {
        return skillCastCost;
    }

    public double learningCost() {
        return learnCost;
    }

    public boolean canBeToggled(ManasSkillInstance instance, LivingEntity living) {
        return false;
    }

    public boolean canBeSlotted(ManasSkillInstance instance) {
        return true;
    }

    /**
     * Determines whether the tick method will fire. In this case, only when it is toggled
     * @param instance
     * @param entity
     * @return
     */
    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return false;
    }

    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        // Here we just make a little sound for the speed effect
        entity.getLevel().playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (entity.isHolding(Items.FISHING_ROD)) {
            entity.getItemInHand(InteractionHand.MAIN_HAND).enchant(Enchantments.FISHING_SPEED, instance.isMastered(entity) ? 7 : 5);
            this.addMasteryPoint(instance, entity);
        }
    }

    /**
     * When toggled on, apply the Absorption effect to the player. If the skill is mastered, increase the level of it.
     * This only applies the first tick, then the onTick() method takes over
     * @param instance
     * @param entity
     */
    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {
    }
    /**
     * Simply remove the absorbtion effect when toggled off.
     * @param instance
     * @param entity
     */
    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
    }


    public void onTick(ManasSkillInstance instance, LivingEntity entity) {
    }


}
