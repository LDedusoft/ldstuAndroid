package com.ldedusoft.ldstu.component.widge.sideslip;

public interface OnDeleteListioner {
	public abstract boolean isCandelete(int position);
	public abstract void onDelete(int ID);
	public abstract void onBack();
}
