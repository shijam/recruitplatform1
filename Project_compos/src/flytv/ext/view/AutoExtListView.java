package flytv.ext.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义gridview，解决ListView中嵌套gridview显示不正常的问题（1行半）
 * @author nike
 * @version 1.0.0 2013-9-14
 */
public class AutoExtListView extends ListView{
	  public AutoExtListView(Context context, AttributeSet attrs) { 
	        super(context, attrs); 
	    } 
	 
	    public AutoExtListView(Context context) { 
	        super(context); 
	    } 
	 
	    public AutoExtListView(Context context, AttributeSet attrs, int defStyle) { 
	        super(context, attrs, defStyle); 
	    } 
	 
	    @Override 
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
	 
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
	                MeasureSpec.AT_MOST); 
	        super.onMeasure(widthMeasureSpec, expandSpec); 
	    } 
}