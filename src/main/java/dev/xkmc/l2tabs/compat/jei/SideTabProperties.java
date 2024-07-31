package dev.xkmc.l2tabs.compat.jei;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.l2tabs.tabs.core.ITabScreen;
import dev.xkmc.l2tabs.tabs.core.TabGroup;
import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.api.registration.IGuiHandlerRegistration;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public record SideTabProperties(TabGroup<?> group) {

	@SafeVarargs
	public final void register(IGuiHandlerRegistration event, Class<? extends ITabScreen>... cls) {
		for (var c : cls) event.addGuiScreenHandler(Wrappers.cast(c), e -> this.create(Wrappers.cast(e)));
	}

	@Nullable
	public IGuiProperties create(ITabScreen screen) {
		if (screen.screenWidth() <= 0 || screen.screenHeight() <= 0) {
			return null;
		}
		int w = screen.getXSize();
		int h = screen.getYSize();
		if (w <= 0 || h <= 0) {
			return null;
		}
		int x0 = screen.getGuiLeft();
		int y0 = screen.getGuiTop();
		int x1 = x0 + w;
		int y1 = y0 + h;
		int tx = group.type.getTabX(w, 0);
		int ty = group.type.getTabY(h, 0);
		x0 += Math.min(tx, 0);
		y0 += Math.min(ty, 0);
		x1 = Math.max(x1, x0 + tx + group.type.width);
		y1 = Math.max(y1, x0 + ty + group.type.height);

		return new GuiProperties(screen.asScreen().getClass(),
				x0, y0, x1 - x0, y1 - y0,
				screen.screenWidth(), screen.screenHeight()
		);
	}

}
