package net.twolay.legacyefficiency.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.FluidTags;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Final @Shadow PlayerInventory inventory;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author 2lay, uperehostile
     * @reason overrides block breaking speeds
     */


    @Overwrite
    public float getBlockBreakingSpeed(BlockState block) {
        float speed = this.inventory.getBlockBreakingSpeed(block);
        int efficiencyLevel = EnchantmentHelper.getEfficiency(this);
        ItemStack heldItem = this.getMainHandStack();

        if (efficiencyLevel > 0 && !heldItem.isEmpty()) {
            speed += (float) (efficiencyLevel * efficiencyLevel + 1);
        }
        if (this.hasStatusEffect(StatusEffects.HASTE)) {
            speed *= 1.0f + (float) (Objects.requireNonNull(this.getStatusEffect(StatusEffects.HASTE)).getAmplifier() + 1) * 0.2f;
        }
        if (this.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            float fatigueMultiplier = switch (Objects.requireNonNull(this.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier()) {
                case 0 -> 0.3f;
                case 1 -> 0.09f;
                case 2 -> 0.0027f;
                default -> 8.1E-4f;
            };
            speed *= fatigueMultiplier;
        }
        if (this.isSubmergedIn(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
            speed /= 5.0f;
        }
        if (!this.isOnGround()) {
            speed /= 5.0f;
        }
        return speed;
    }
}
