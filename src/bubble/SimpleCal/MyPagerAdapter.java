package bubble.SimpleCal;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>Title: MyAdapter</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0.0.150719 
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-7-18 上午10:38:58
 */
public class MyPagerAdapter extends PagerAdapter {  
	private ArrayList<View> viewList;
	private Context context;
	private String history;
	
	public MyPagerAdapter(Context context,ArrayList<View> list){
		this.viewList = list;
		this.context = context;
	}
	
    @Override  
    public int getCount() {  
    	if (viewList != null){
            return viewList.size();
        }
        return 0;
    }  

    @Override  
    public boolean isViewFromObject(View arg0, Object arg1) {  
        return arg0 == arg1;  
    }  

    @Override  
    public int getItemPosition(Object object) {  
        // TODO Auto-generated method stub 
    	int position =  super.getItemPosition(object);  
    	return position;
    }  

    @Override  
    public void destroyItem(ViewGroup container, int position, Object object){     
        container.removeView(viewList.get(position));//删除页卡  
    }  

    @Override  
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
        container.addView(viewList.get(position));//添加页卡  
        return viewList.get(position);  
    }  

    @Override  
    public void restoreState(Parcelable arg0, ClassLoader arg1) {  
        // TODO Auto-generated method stub  

    }  

    @Override  
    public Parcelable saveState() {  
        // TODO Auto-generated method stub  
        return null;  
    }  

    @Override  
    public void startUpdate(View arg0) {  
        // TODO Auto-generated method stub  

    }  

    @Override  
    public void finishUpdate(View arg0) {  
        // TODO Auto-generated method stub  

    }  
}  