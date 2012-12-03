package stkl.spectropolarisclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class GameView extends View {
	private int w;
	private int h;

	public GameView(Context context) {
		super(context);

	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle((int)(0.5 * w), (int)(0.5 * h), 15, new Paint());
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.w = w;
		this.h = h;		
	}
}
