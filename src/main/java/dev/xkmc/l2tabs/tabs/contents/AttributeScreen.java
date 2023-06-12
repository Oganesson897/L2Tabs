package dev.xkmc.l2tabs.tabs.contents;

import dev.xkmc.l2tabs.init.L2TabsClient;
import dev.xkmc.l2tabs.init.data.AttributeDisplayConfig;
import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class AttributeScreen extends BaseTextScreen {

	protected AttributeScreen(Component title) {
		super(title, new ResourceLocation("l2tabs:textures/gui/empty.png"));
	}

	@Override
	public void init() {
		super.init();
		new TabManager(this).init(this::addRenderableWidget, L2TabsClient.TAB_ATTRIBUTE);
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float ptick) {
		super.render(g, mx, my, ptick);
		Player player = Minecraft.getInstance().player;
		assert player != null;

		int x = leftPos + 8;
		int y = topPos + 6;
		Attribute focus = null;
		for (AttributeEntry entry : AttributeDisplayConfig.get()) {
			double val = player.getAttributeValue(entry.attr());
			Component comp = Component.translatable(
					"attribute.modifier.equals." + (entry.usePercent() ? 1 : 0),
					ATTRIBUTE_MODIFIER_FORMAT.format(entry.usePercent() ? val * 100 : val),
					Component.translatable(entry.attr().getDescriptionId()));
			g.drawString(font, comp, x, y, 0);
			if (mx > x && my > y && my < y + 10) focus = entry.attr();
			y += 10;
		}
		if (focus != null) {
			g.renderComponentTooltip(font, getAttributeDetail(focus), mx, my);
		}
	}

	public List<Component> getAttributeDetail(Attribute attr) {
		Player player = Minecraft.getInstance().player;
		assert player != null;
		AttributeInstance ins = player.getAttribute(attr);
		if (ins == null) return List.of();
		var adds = ins.getModifiers(AttributeModifier.Operation.ADDITION);
		var m0s = ins.getModifiers(AttributeModifier.Operation.MULTIPLY_BASE);
		var m1s = ins.getModifiers(AttributeModifier.Operation.MULTIPLY_TOTAL);
		double base = ins.getBaseValue();
		double addv = 0;
		double m0v = 0;
		double m1v = 1;
		for (var e : adds) addv += e.getAmount();
		for (var e : m0s) m0v += e.getAmount();
		for (var e : m1s) m1v *= 1 + e.getAmount();
		double total = (base + addv) * (1 + m0v) * m1v;
		List<Component> ans = new ArrayList<>();
		ans.add(Component.translatable(attr.getDescriptionId()).withStyle(ChatFormatting.GOLD));
		boolean shift = Screen.hasShiftDown();
		ans.add(L2TabsLangData.BASE.get(number("%s", base)).withStyle(ChatFormatting.BLUE));
		ans.add(L2TabsLangData.ADD.get(numberSigned("%s", addv)).withStyle(ChatFormatting.BLUE));
		if (shift) {
			for (var e : adds) {
				ans.add(numberSigned("%s", e.getAmount()).append(name(e)));
			}
		}
		ans.add(L2TabsLangData.MULT_BASE.get(numberSigned("%s%%", m0v * 100)).withStyle(ChatFormatting.BLUE));
		if (shift) {
			for (var e : m0s) {
				ans.add(numberSigned("%s%%", e.getAmount() * 100).append(name(e)));
			}
		}
		ans.add(L2TabsLangData.MULT_TOTAL.get(number("x%s", m1v)).withStyle(ChatFormatting.BLUE));
		if (shift) {
			for (var e : m1s) {
				ans.add(number("x%s", 1 + e.getAmount()).append(name(e)));
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
		return Component.literal("  (" + e.getName() + ")").withStyle(ChatFormatting.DARK_GRAY);
	}

}
