package stkl.spectropolarisclient;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
	private int w;
	private int h;
	private Model model;

	public GameView(Context context, Model model) {
		super(context);
		this.model = model;
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		model.draw(canvas);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.w = w;
		this.h = h;		
	}
}
