package git.wildwind.wwhfas.interaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import git.wildwind.wwhfas.WildWindMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class BlockPropertyBookFactory {
    private static final int MAX_LINES_PER_PAGE = 13;
    private static final String KEY_PREFIX = "book." + WildWindMod.MOD_ID + ".block_property.";

    private BlockPropertyBookFactory() {
    }

    /**
     * Build a lectern-readable written book that summarizes the clicked block's core properties and current state.
     */
    public static ItemStack create(ServerPlayer player, Level level, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        ResourceLocation itemId = getItemId(block.asItem());
        MapColor mapColor = state.getMapColor(level, pos);
        SoundType soundType = state.getSoundType(level, pos, player);
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;

        List<Component> lines = new ArrayList<>();
        lines.add(block.getName());
        lines.add(entry("block_id", blockId));
        lines.add(entry("position", pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));
        lines.add(Component.empty());
        lines.add(section("section.basic"));
        lines.add(entry("item_id", itemId == null ? fallback("none") : Component.literal(itemId.toString())));
        lines.add(entry("hardness", formatFloat(state.getDestroySpeed(level, pos))));
        lines.add(entry("blast_resistance", formatFloat(block.getExplosionResistance())));
        lines.add(entry("tool", getMiningTool(state)));
        lines.add(entry("needs_correct_tool", yesNo(state.requiresCorrectToolForDrops())));
        lines.add(entry("flammable", yesNo(fireBlock.getIgniteOdds(state) > 0 || fireBlock.getBurnOdds(state) > 0)));
        lines.add(entry("fire_odds", fireBlock.getIgniteOdds(state) + " / " + fireBlock.getBurnOdds(state)));
        lines.add(entry("ignited_by_lava", yesNo(state.ignitedByLava())));
        lines.add(entry("light_level", Integer.toString(state.getLightEmission(level, pos))));
        lines.add(entry("map_color", "id=" + mapColor.id + ", hex=" + formatHexColor(mapColor.col)));
        lines.add(entry("sound", BuiltInRegistries.SOUND_EVENT.getKey(soundType.getBreakSound())));
        lines.add(entry("random_ticks", yesNo(state.isRandomlyTicking())));
        lines.add(entry("has_block_entity", yesNo(state.hasBlockEntity())));
        lines.add(entry("piston_reaction", formatPushReaction(state.getPistonPushReaction())));
        lines.add(Component.empty());
        lines.add(section("section.behavior"));
        lines.add(entry("occludes", yesNo(state.canOcclude())));
        lines.add(entry("full_collision", yesNo(state.isCollisionShapeFullBlock(level, pos))));
        lines.add(entry("redstone_conductor", yesNo(state.isRedstoneConductor(level, pos))));
        lines.add(entry("signal_source", yesNo(state.isSignalSource())));
        lines.add(entry("suffocating", yesNo(state.isSuffocating(level, pos))));
        lines.add(entry("blocks_view", yesNo(state.isViewBlocking(level, pos))));
        lines.add(Component.empty());
        lines.add(section("section.state"));
        appendStateLines(lines, state);
        lines.add(Component.empty());
        lines.add(section("section.extra"));
        lines.add(entry("description_key", block.getDescriptionId()));
        lines.add(entry("namespace", blockId.getNamespace()));
        lines.add(entry("path", blockId.getPath()));
        lines.add(entry("generated_by", WildWindMod.MOD_ID));

        List<Filterable<Component>> pages = new ArrayList<>();
        List<Component> currentPage = new ArrayList<>();
        for (Component line : lines) {
            if (currentPage.size() >= MAX_LINES_PER_PAGE) {
                pages.add(toPage(currentPage));
                currentPage = new ArrayList<>();
            }
            currentPage.add(line);
        }
        if (!currentPage.isEmpty()) {
            pages.add(toPage(currentPage));
        }

        ItemStack writtenBook = new ItemStack(Items.WRITTEN_BOOK);
        writtenBook.set(
            DataComponents.WRITTEN_BOOK_CONTENT,
            new WrittenBookContent(
                Filterable.passThrough(trimTitle(blockId.getPath())),
                player.getGameProfile().getName(),
                0,
                pages,
                true
            )
        );
        return writtenBook;
    }

    private static void appendStateLines(List<Component> lines, BlockState state) {
        if (state.getValues().isEmpty()) {
            lines.add(fallback("no_state_properties"));
            return;
        }

        state.getProperties()
            .stream()
            .sorted(Comparator.comparing(Property::getName))
            .forEach(property -> lines.add(Component.literal(property.getName() + ": " + formatPropertyValue(state, property))));
    }

    private static <T extends Comparable<T>> String formatPropertyValue(BlockState state, Property<T> property) {
        return property.getName(state.getValue(property));
    }

    private static Component getMiningTool(BlockState state) {
        List<Component> tools = new ArrayList<>();
        if (state.is(BlockTags.MINEABLE_WITH_AXE)) {
            tools.add(fallback("tool.axe"));
        }
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            tools.add(fallback("tool.pickaxe"));
        }
        if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
            tools.add(fallback("tool.shovel"));
        }
        if (state.is(BlockTags.MINEABLE_WITH_HOE)) {
            tools.add(fallback("tool.hoe"));
        }
        if (tools.isEmpty()) {
            return fallback("tool.none");
        }

        MutableComponent joined = Component.empty();
        for (int i = 0; i < tools.size(); i++) {
            if (i > 0) {
                joined.append(" / ");
            }
            joined.append(tools.get(i));
        }
        return joined;
    }

    private static String formatPushReaction(PushReaction reaction) {
        return reaction.name().toLowerCase(Locale.ROOT);
    }

    private static ResourceLocation getItemId(Item item) {
        return item == Items.AIR ? null : BuiltInRegistries.ITEM.getKey(item);
    }

    private static Component yesNo(boolean value) {
        return fallback(value ? "yes" : "no");
    }

    private static String formatFloat(float value) {
        if (value == Math.rint(value)) {
            return Integer.toString((int) value);
        }
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private static String formatHexColor(int color) {
        return String.format(Locale.ROOT, "#%06X", color & 0xFFFFFF);
    }

    private static String trimTitle(String title) {
        return title.length() <= WrittenBookContent.TITLE_MAX_LENGTH
            ? title
            : title.substring(0, WrittenBookContent.TITLE_MAX_LENGTH);
    }

    private static Component section(String suffix) {
        return Component.translatable(KEY_PREFIX + suffix);
    }

    private static Component entry(String suffix, Object value) {
        return Component.translatable(KEY_PREFIX + "entry." + suffix, value);
    }

    private static Component fallback(String suffix) {
        return Component.translatable(KEY_PREFIX + suffix);
    }

    private static Filterable<Component> toPage(List<Component> lines) {
        MutableComponent page = Component.empty();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) {
                page.append("\n");
            }
            page.append(lines.get(i));
        }
        return Filterable.passThrough(page);
    }
}
