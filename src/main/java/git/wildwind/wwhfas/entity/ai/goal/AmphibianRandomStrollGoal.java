package git.wildwind.wwhfas.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;

public class AmphibianRandomStrollGoal extends RandomStrollGoal {
    public AmphibianRandomStrollGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }

    public AmphibianRandomStrollGoal(PathfinderMob mob, double speedModifier, int interval) {
        super(mob, speedModifier, interval);
    }

    public AmphibianRandomStrollGoal(PathfinderMob mob, double speedModifier, int interval, boolean checkNoActionTime) {
        super(mob, speedModifier, interval, checkNoActionTime);
    }

    @Override
    protected Vec3 getPosition() {
        return this.mob.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value()) ?
                BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7) :
                super.getPosition();
    }
}
