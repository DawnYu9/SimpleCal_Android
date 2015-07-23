package bubble.SimpleCal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bubble.SimpleCal.R;

/**
 * <p>Title: HistoryLayout</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 2.0.0.150723   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-7-19 下午2:42:57
 */
public class HistoryLayout extends LinearLayout {
	Context context;
	final String FILENAME = "history";
	private List<String> equationList = new ArrayList<String>();
	String hisHistory = "";
	StringBuilder hisHistorySB = new StringBuilder();
	ListView historyLV;
	String[] hisItemArray;
	String exp;
	String result;
	String[] hisItem;
	EquationAdapter eAdapter;
	boolean appendHistory;	//是否需要追加history字符串
	public HistoryLayout(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.history_layout, this);
		eAdapter = new EquationAdapter(context, R.layout.history_item, equationList);
		historyLV = (ListView)findViewById(R.id.history_list_view);
		historyLV.setAdapter(eAdapter);
	}
	/**
	 * <p>Title: updateHistory</p>
	 * <p>Description: </p>
	 * @param calHistory
	 * @author bubble
	 * @date 2015-7-21 上午12:16:45
	 */
	public void updateHistory(String calHistory){
		hisHistorySB.append(calHistory);
		hisItemArray = calHistory.split(";");
		if ( hisItemArray.length > 0 ){
			for (String item:hisItemArray){
				if ( ! TextUtils.isEmpty(item) ) {
					equationList.add(item.replace(",", "\n"));
				}
			}
			eAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * <p>Title: getHistory</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-7-19
	 */
	public String getHistory(){
		return hisHistorySB.toString();
	}
	
	/**
	 * <p>Title: load</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-7-22 上午9:42:06
	 */
	public String load(){
		FileInputStream in = null;
		BufferedReader reader = null;
		StringBuilder contentSB = new StringBuilder();
		String fileDir = context.getFilesDir() + File.separator + FILENAME;
		File file=new File(fileDir);
		try {
			if ( ! file.exists() )
				return "";
			in = context.getApplicationContext().openFileInput(FILENAME);
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ( (line = reader.readLine()) != null) {
				contentSB.append(line);
			}
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
//		this.hisHistorySB = contentSB;
//		return hisHistorySB.toString();
		return contentSB.toString();
	}
}
