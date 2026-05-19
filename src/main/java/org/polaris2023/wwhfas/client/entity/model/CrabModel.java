package org.polaris2023.wwhfas.client.entity.model;

import org.polaris2023.wwhfas.WildWindMod;
import org.polaris2023.wwhfas.entity.animal.Crab;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

/**
 * 螃蟹实体的几何模型定义喵~
 */
public class CrabModel extends DefaultedEntityGeoModel<Crab> {
	/**
	 * 创建螃蟹模型实例喵~
	 */
	public CrabModel() {
		super(WildWindMod.id("crab"));
	}

	/**
	 * 获取当前螃蟹变种对应的材质资源喵~
	 *
	 * @param animatable 螃蟹实体喵~
	 * @return 材质资源位置喵~
	 */
	@Override
	public ResourceLocation getTextureResource(Crab animatable) {
		return animatable.getVariant().value().textureLocation();
	}
}
