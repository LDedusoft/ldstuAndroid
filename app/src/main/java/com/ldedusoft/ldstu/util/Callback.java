package com.ldedusoft.ldstu.util;

public interface Callback {
	void onBefore();

	boolean onRun();

	void onAfter(boolean b);
}
