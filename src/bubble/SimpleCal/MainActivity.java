package bubble.SimpleCal;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bubble.SimpleCal.R;

/**
 * <p>Title: MainActivity</p>
 * <p>Description: Android计算器</p>
 * @version 3.0.0.150723 
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-7-2
 */
public class MainActivity extends Activity implements OnPageChangeListener{
	private final String FILENAME = "history";
	
	private LayoutInflater inflater;
	private ViewGroup viewGroup;
	private ViewPager viewPager;  
	private MyPagerAdapter mAdapter;
	private ArrayList<View> viewList; 
	
	private CalLayout calView;
	private HistoryLayout hisView;
	private String calHistory;
	private String hisHistory;
	private boolean isFirstOpenHistory = true;
	private boolean isCalHisChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        inflater = getLayoutInflater();
        calView = new CalLayout(this);
        hisView = new HistoryLayout(this);
        viewList = new ArrayList<View>();  
        viewList.add(calView);
        viewList.add(hisView);
        
        viewGroup = (ViewGroup)inflater.inflate(R.layout.activity_main, null);  
        viewPager = (ViewPager)viewGroup.findViewById(R.id.viewPager);  
        mAdapter = new MyPagerAdapter(this,viewList);
        viewPager.setAdapter(mAdapter); 
        
        viewPager.addOnPageChangeListener(this);
        setContentView(viewGroup); 
    }
    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
        
    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * <p>Title: onPageSelected</p>
     * <p>Description: </p>
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     * @param position
     * @author bubble
     * @date 2015-7-19 下午7:03:09
     */
    @Override
    public void onPageSelected(int position) {
    	switch(position){
    	case 0:
    		break;
    	case 1:
    		calHistory = calView.getHistory();
    		if ( isFirstOpenHistory ) {	//第一次打开，加载本地历史记录
    			isFirstOpenHistory = false;
    			hisHistory = hisView.load();
    			if ( calHistory != "") {
        			hisView.updateHistory(hisHistory + calHistory);
        			calView.clearCalHistory();
        		}
        		else if ( !TextUtils.isEmpty(hisHistory) ){
        			hisView.updateHistory(hisHistory);
        		}
    		}
    		else if ( calHistory != "") {
    			hisView.updateHistory(calHistory);
    			calView.clearCalHistory();
    		}
    		break;
    	default:
    		break;
    	}
    }
    /* (non-Javadoc)
     * <p>Title: onDestroy</p>
     * <p>Description: </p>
     * @see android.app.Activity#onDestroy()
     * @author bubble
     * @date 2015-7-22 上午09:34:19
     */
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	hisHistory = hisView.getHistory();
		save(hisHistory + calView.getHistory());
	}
    
    /**
     * <p>Title: save</p>
     * <p>Description: </p>
     * @param s
     * @author bubble
     * @date 2015-7-22 上午09:34:01
     */
    public void save(String s){
    	FileOutputStream out = null;
    	BufferedWriter writer = null;
    	try{
    		out = openFileOutput(FILENAME, Context.MODE_PRIVATE);
    		writer = new BufferedWriter(new OutputStreamWriter(out));
    		writer.write(s);
    	} catch (IOException e){
    		e.printStackTrace();
    	} finally {
    		try {
    			if ( writer != null) {
    				writer.flush();
    				writer.close();
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
      
}