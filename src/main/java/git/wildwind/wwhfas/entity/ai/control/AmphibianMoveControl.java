package git.wildwind.wwhfas.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.neoforged.neoforge.common.NeoForgeMod;

public class AmphibianMoveControl extends MoveControl {
    public AmphibianMoveControl(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (!mob.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value())) {
            super.tick();
            return;
        }

        if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            float modifiedSpeed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.mob.setSpeed(Mth.lerp(0.125F, this.mob.getSpeed(), modifiedSpeed));
            double relativeX = this.wantedX - this.mob.getX();
            double relativeY = this.wantedY - this.mob.getY();
            double relativeZ = this.wantedZ - this.mob.getZ();
            if (relativeY != 0.0) {
                double distance = Math.sqrt(relativeX * relativeX + relativeY * relativeY + relativeZ * relativeZ);
                this.mob.setDeltaMovement(this.mob.getDeltaMovement()
                        .add(0.0, (double) this.mob.getSpeed() * (relativeY / distance) * 0.1, 0.0)
                );
            }

            if (relativeX != 0.0 || relativeZ != 0.0) {
                float yRot = (float) (Mth.atan2(relativeZ, relativeX) * 180.0F / (float) Math.PI) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRot, 90.0F));
                this.mob.yBodyRot = this.mob.getYRot();
            }
        }
    }
}
