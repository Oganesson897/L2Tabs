package dev.xkmc.l2tabs.tabs.contents;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2tabs.init.data.AttrDispEntry;
import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.item.component.ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT;

public abstract class BaseAttributeScreen extends BaseTextScreen {

	private static int getSize() {
		return L2TabsConfig.CLIENT.attributeLinePerPage.get();
	}

	private final int page;

	protected BaseAttributeScreen(Component title, int page) {
		super(title, ResourceLocation.fromNamespaceAndPath("l2tabs", "textures/gui/empty.png"));
		this.page = page;
	}

	public abstract LivingEntity getEntity();

	@Override
	public void init() {
		super.init();
		int w = 10;
		int h = 11;
		int size = AttrDispEntry.get(getEntity()).size();
		int totalPage = (size - 1) / getSize() + 1;
		int x = (this.width + this.imageWidth) / 2 - 16,
				y = (this.height - this.imageHeight) / 2 + 4;
		if (page > 0) {
			addRenderableWidget(Button.builder(Component.literal("<"), e -> click(-1))
					.pos(x - w - 1, y).size(w, h).build());
		}
		if (page < totalPage - 1) {
			addRenderableWidget(Button.builder(Component.literal(">"), e -> click(1))
					.pos(x, y).size(w, h).build());
		}
	}

	protected abstract void click(int nextPage);

	@Override
	public void render(GuiGraphics g, int mx, int my, float ptick) {
		super.render(g, mx, my, ptick);
		render(g, mx, my, ptick, getEntity(), AttrDispEntry.get(getEntity()));
	}

	public void render(GuiGraphics g, int mx, int my, float ptick, LivingEntity player, List<Pair<Holder<Attribute>, AttrDispEntry>> list) {
		int x = leftPos + 8;
		int y = topPos + 6;
		Pair<Holder<Attribute>, AttrDispEntry> focus = null;
		int count = 0;
		for (var entry : list) {
			count++;
			if (count <= page * getSize() || count > (page + 1) * getSize()) continue;
			double val = player.getAttributeValue(entry.getFirst());
			Component comp = Component.translatable(
					"attribute.modifier.equals." + (entry.getSecond().usePercent() ? 1 : 0),
					ATTRIBUTE_MODIFIER_FORMAT.format(entry.getSecond().usePercent() ? val * 100 : val),
					Component.translatable(entry.getFirst().value().getDescriptionId()));
			g.drawString(font, comp, x, y, 0, false);
			if (mx > x && mx < x + font.width(comp) && my > y && my < y + 10) focus = entry;
			y += 10;
		}
		if (focus != null) {
			g.renderComponentTooltip(font, getAttributeDetail(player, focus), mx, my);
		}
	}

	public List<Component> getAttributeDetail(LivingEntity entity, Pair<Holder<Attribute>, AttrDispEntry> entry) {
		var ans = getAttributeDetail(entity, entry.getFirst());
		if (entry.getSecond().intrinsic() != 0) {
			ans.add(L2TabsLangData.INTRINSIC.get(number("%s", entry.getSecond().intrinsic()))
					.withStyle(ChatFormatting.BLUE));
		}
		return ans;
	}

	public List<Component> getAttributeDetail(LivingEntity entity, Holder<Attribute> attr) {
		AttributeInstance ins = entity.getAttribute(attr);
		if (ins == null) return List.of();
		var all = ins.getModifiers();
		var adds = new ArrayList<AttributeModifier>();
		var m0s = new ArrayList<AttributeModifier>();
		var m1s = new ArrayList<AttributeModifier>();
		for (var e : all) {
			var x = switch (e.operation()) {
				case ADD_VALUE -> adds;
				case ADD_MULTIPLIED_BASE -> m0s;
				case ADD_MULTIPLIED_TOTAL -> m1s;
			};
			x.add(e);
		}
		double base = ins.getBaseValue();
		double addv = 0;
		double m0v = 0;
		double m1v = 1;
		for (var e : adds) addv += e.amount();
		for (var e : m0s) m0v += e.amount();
		for (var e : m1s) m1v *= 1 + e.amount();
		double total = (base + addv) * (1 + m0v) * m1v;
		List<Component> ans = new ArrayList<>();
		ans.add(Component.translatable(attr.value().getDescriptionId()).withStyle(ChatFormatting.GOLD));
		boolean shift = Screen.hasShiftDown();
		ans.add(L2TabsLangData.BASE.get(number("%s", base)).withStyle(ChatFormatting.BLUE));
		ans.add(L2TabsLangData.ADD.get(numberSigned("%s", addv)).withStyle(ChatFormatting.BLUE));
		if (shift) {
			for (var e : adds) {
				ans.add(numberSigned("%s", e.amount()).append(name(e)));
			}
		}
		ans.add(L2TabsLangData.MULT_BASE.get(numberSigned("%s%%", m0v * 100)).withStyle(ChatFormatting.BLUE));
		if (shift) {
			for (var e : m0s) {
				ans.add(numberSigned("%s%%", e.amount() * 100).append(name(e)));
			}
		}
		ans.add(L2TabsLangData.MULT_TOTAL.get(number("x%s", m1v)).withStyle(ChatFormatting.BLUE));
		if (shift) {
			for (var e : m1s) {
				ans.add(number("x%s", 1 + e.amount()).append(name(e)));
			}
		}
		ans.add(L2TabsLangData.FORMAT.get(
				number("%s", base),
				numberSigned("%s", addv),
				numberSigned("%s", m0v),
				number("%s", m1v),
				number("%s", total)));
		if (!shift) ans.add(L2TabsLangData.DETAIL.get().withStyle(ChatFormatting.GRAY));
		return ans;
	}

	private static MutableComponent number(String base, double value) {
		return Component.literal(String.format(base, ATTRIBUTE_MODIFIER_FORMAT.format(value))).withStyle(ChatFormatting.GREEN);
	}

	private static MutableComponent numberSigned(String base, double value) {
		if (value >= 0)
			return Component.literal(String.format("+" + base, ATTRIBUTE_MODIFIER_FORMAT.format(value))).withStyle(ChatFormatting.GREEN);
		return Component.literal(String.format("-" + base, ATTRIBUTE_MODIFIER_FORMAT.format(-value))).withStyle(ChatFormatting.RED);
	}

	private static MutableComponent name(AttributeModifier e) {
		return Component.literal("  (" + e.id() + ")").withStyle(ChatFormatting.DARK_GRAY);
	}

}
