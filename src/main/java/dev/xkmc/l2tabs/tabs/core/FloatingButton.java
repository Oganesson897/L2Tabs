package dev.xkmc.l2tabs.tabs.core;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.IntSupplier;

public class FloatingButton extends Button {

	private IntSupplier x0, y0;
	private int x1, y1;

	protected FloatingButton(int w, int h, Component title, OnPress press) {
		this(() -> 0, () -> 0, 0, 0, w, h, title, press);
	}

	public FloatingButton(IntSupplier x0, IntSupplier y0, int x, int y, int w, int h, Component title, OnPress press) {
		super(x0.getAsInt() + x, y0.getAsInt() + y, w, h, title, press, DEFAULT_NARRATION);
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x;
		this.y1 = y;
	}

	public void setXRef(IntSupplier x0, int x1) {
		this.x0 = x0;
		this.x1 = x1;
	}

	public void setYRef(IntSupplier y0, int y1) {
		this.y0 = y0;
		this.y1 = y1;
	}

	@Override
	public int getX() {
		return x0.getAsInt() + x1;
	}

	@Override
	public int getY() {
		return y0.getAsInt() + y1;
	}

}
