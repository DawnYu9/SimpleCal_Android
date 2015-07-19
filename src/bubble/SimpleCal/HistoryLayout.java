package bubble.SimpleCal;

import android.content.Context;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bubble.SimpleCal.R;

/**
 * <p>Title: HistoryLayout</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0.0.150719   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-7-19 ÏÂÎç2:42:57
 */
public class HistoryLayout extends LinearLayout {
	private Context context;
	String history;
	TextView historyTV;
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param context
	 */
	public HistoryLayout(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.history_layout, this);
		historyTV = (TextView)findViewById(R.id.historyTV);
		initHistory();
	}
	/**
	 * <p>Title: initHistory</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-7-19 
	 */
	public void initHistory(){
		historyTV.setMovementMethod(ScrollingMovementMethod.getInstance());
		historyTV.setText("");
		historyTV.setBackgroundColor(Color.WHITE);
	}
	/**
	 * <p>Title: getHistory</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-7-19
	 */
	public String getHistory(){
		return history;
	}
	/**
	 * <p>Title: setHistory</p>
	 * <p>Description: </p>
	 * @param history
	 * @author bubble
	 * @date 2015-7-19
	 */
	public void setHistory(String history){
		this.history = history;
		historyTV.setText(history);
	}

}
