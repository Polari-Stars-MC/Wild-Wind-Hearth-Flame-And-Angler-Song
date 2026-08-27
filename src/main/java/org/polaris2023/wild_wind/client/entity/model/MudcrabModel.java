package org.polaris2023.wild_wind.client.entity.model;

import net.minecraft.resources.ResourceLocation;
import org.polaris2023.wild_wind.WildWindMod;
import org.polaris2023.wild_wind.entity.animal.Mudcrab;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

/**
 * 泥沼蟹实体的几何模型定义喵~
 */
public class MudcrabModel extends DefaultedEntityGeoModel<Mudcrab> {
	/**
	 * 创建泥沼蟹模型实例喵~
	 */
	public MudcrabModel() {
		super(WildWindMod.id("mudcrab"));
	}

	/**
	 * 获取当前泥沼蟹变种对应的材质资源喵~
	 *
	 * @param animatable 泥沼蟹实体喵~
	 * @return 材质资源位置喵~
	 */
	@Override
	public ResourceLocation getTextureResource(Mudcrab animatable) {
		return animatable.getVariant().value().textureLocation();
	}
}
