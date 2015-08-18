package bubble.SimpleCal;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bubble.SimpleCal.R;

/**
 * <p>Title: EquationAdapter</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-7-20 
 */
public class EquationAdapter extends ArrayAdapter<String>{
	private int resourdId;
	public EquationAdapter(Context context,int viewResourceId,List<String> objects){
		super(context,viewResourceId,objects);
		resourdId = viewResourceId;
	}
	
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		String equation = getItem(position);
		View view;
		ViewHolder viewHolder;
		if ( convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourdId, null);
			viewHolder = new ViewHolder();
			viewHolder.equationView = (TextView)view.findViewById(R.id.equation_item_view);
			view.setTag(viewHolder);	
		}else {
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();	
		}
		viewHolder.equationView.setText(equation);
		return view;
	}
	
	class ViewHolder {
		TextView equationView;
	}
}