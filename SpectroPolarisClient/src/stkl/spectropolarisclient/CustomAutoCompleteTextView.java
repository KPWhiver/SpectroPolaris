package stkl.spectropolarisclient;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class CustomAutoCompleteTextView extends AutoCompleteTextView {
	private int d_threshold;

	public CustomAutoCompleteTextView(Context context) {
		super(context);
	}
	
	public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	@Override
    public void setThreshold(int threshold) {
        if (threshold < 0) {
            threshold = 0;
        }
        d_threshold = threshold;
    }

    @Override
    public boolean enoughToFilter() {
        return getText().length() >= d_threshold;
    }
    
    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            performFiltering(getText(), 0);
            showDropDown();
        }
    }

    @Override
    public int getThreshold() {
        return d_threshold;
    }

}
