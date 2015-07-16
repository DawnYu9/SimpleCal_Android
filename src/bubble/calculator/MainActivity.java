package bubble.calculator;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import bubble.calculator.utils.ParseExpression;

import com.bubble.calculator.R;

/**
 * <p>Title: MainActivity</p>
 * <p>Description: Android计算器</p>
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-7-2
 */
public class MainActivity extends Activity implements OnTouchListener,OnClickListener{
	private ViewPager mPager;	//页卡内容
	private List<View> listViews;	//页面列表
	
	final static String ERROR = "格式错误";
	String parenthesis = "( )";
	String[] btTexts = new String[]{
			"C", "÷", "×", "D",
			"7", "8", "9", "-",
			"4", "5", "6", "+",
			"1", "2", "3", parenthesis,
			"0", ".", "%", "="
	};
	
	GridLayout gridLayout;
	EditText showEditText;
	
	String resultString;
	String exp;
	String expAndResult;
	boolean cursorEnd = true;	//光标是否在尾端，默认为真
	String frontExp = exp;	//光标前的表达式,默认光标在尾端
	String rearExp = "";	//光标后的表达式,默认光标在尾端
	String regOperator = "\\+|-|×|÷";
	char lastChar = ' ';

	StringBuilder history = new StringBuilder();	//保存历史记录
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity_layout);
        
        showEditText = (EditText)findViewById(R.id.showET);
         
        initVal();	//初始化字符串变量
        initUI();	//初始化界面
    }
    
    /**
     * <p>Title: initCal</p>
     * <p>Description: 初始化</p>
     * @author bubble
     * @date 2015-7-3
     */
    private void initVal(){
    	resultString = "";
    	exp = "";
    	expAndResult = "";
    	frontExp = exp;
    	rearExp = "";
    	cursorEnd = true;
    	showEditText.setText("");
    	showEditText.setSelection(exp.length());
    }
 
    private void initUI(){
    	Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        
        int cellWidth = (int)( (size.x - 3) / 4);
        int cellHeight = (int)( (size.y - 5) / 7 );

		showEditText.setTextIsSelectable(true);
		showEditText.setBackgroundColor(Color.WHITE);
		GridLayout.LayoutParams tvParams = (GridLayout.LayoutParams)showEditText.getLayoutParams();
		tvParams.height = (int)( size.y - cellHeight * 5 - 44 );
		showEditText.setLayoutParams(tvParams);
		
        gridLayout = (GridLayout)findViewById(R.id.main_activity);
        GridLayout.Spec rowSpec;
        GridLayout.Spec columnSpec;
        gridLayout.setBackgroundColor(Color.parseColor("#D1D1D1"));
        GridLayout.LayoutParams cellParams;
        
        //批量初始化按钮
        Button btn[] = new Button[btTexts.length];

        for(int i = 0; i < btTexts.length; i++){
        	btn[i] = new Button(this);
        	btn[i].setText(btTexts[i]);
        	btn[i].setTextColor(Color.parseColor("#666666"));
        	btn[i].setTextSize(30);
        	btn[i].setBackgroundColor(Color.WHITE);
        	
        	rowSpec = GridLayout.spec(i/4 + 2);  
        	columnSpec = GridLayout.spec(i % 4 );  
        	cellParams = new GridLayout.LayoutParams(rowSpec, columnSpec); 
            cellParams.width = cellWidth;  
            cellParams.height = cellHeight; 
            cellParams.setMargins(0, 1, 1, 0);
            gridLayout.addView(btn[i], cellParams);  

            btn[i].setOnTouchListener(this);
            
            btn[i].setOnClickListener(this);
        }
    }
	/* (non-Javadoc)
     * <p>Title: onTouch</p>
     * <p>Description: </p>
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     * @param arg0
     * @param event
     * @return
     * @author bubble
     * @date 2015-7-13 下午6:45:19
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
    	Button bt = (Button)v;
    	switch ( event.getAction() ){
    	case MotionEvent.ACTION_DOWN:
    		bt.setBackgroundColor(Color.parseColor("#F2F2F2"));
    		break;
    	case MotionEvent.ACTION_UP:
    		bt.setBackgroundColor(Color.WHITE);
    		bt.performClick();
    		break;
    	default:
    		break;
    	}
    	return true;
    }
    
    /* (non-Javadoc)
     * <p>Title: onClick</p>
     * <p>Description: </p>
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     * @param v
     * @author bubble
     * @date 2015-7-13 下午6:45:32
     */
    @Override
	public void onClick(View v) {
		Button bt = (Button)v;
		String btText = bt.getText().toString();
		
		exp = showEditText.getText().toString();
        expAndResult = "";
        frontExp = exp;
        rearExp = "";
		String inputString = bt.getText().toString();
		
		if ( resultString.matches(ERROR) ){
			initVal();
		}
		
		int cursorIndex = showEditText.getSelectionStart();
		if ( cursorIndex != showEditText.getText().length()) {
			cursorEnd = false;
		}
		if ( ! cursorEnd ){
			if ( resultString == ""){
				if ( (exp.length() > 0) ){
					if ( cursorIndex > 0 ){
						frontExp = exp.substring(0, cursorIndex);
						rearExp = exp.substring(cursorIndex, exp.length());
					}
					else if ( cursorIndex == 0 ){
						frontExp = "";
						rearExp = exp;
					}
				}
			}
			else{
				initVal();
			}
		}
		
		
		if(btText.matches("[0-9]|\\+|-|×|÷|\\.|(\\( \\))|=")){
			//已经运算过一次并有了运算结果
			if((resultString != "")){
				//判断光标位置
				if ( cursorEnd ){	//光标在尾端
					if(ParseExpression.isOperator(inputString)){	//如果输入的是运算符，表明是继续运算
						exp = resultString;
						resultString = "";
					}
					else if(inputString.matches("=") ){	//若输入"="，继续重复上一次的运算符运算
						exp = exp.replaceAll("\n=.*", "");
						String[] op = ParseExpression.splitInfixExp(exp);
						int opLen = op.length;
						exp = resultString + op[opLen - 2] + op[opLen - 1];
						resultString = "";
					}
					else if(inputString == parenthesis){	//若输入括号
						exp = "(" + resultString;
						inputString = "";
						resultString = "";
					}
					else {	//重新开始新的运算
						initVal();
					}
				}
				else{	//光标不在尾端，则开始新的运算
					initVal();
				}
			}
			
			if ( inputString.matches("\\.") ) {
				if ( cursorEnd ) {
					if ( ParseExpression.appendDotValid(exp) ) {
						if ( exp.matches(".*?(" + regOperator + "|\\()$|()") )
							inputString = "0.";
					}
					else
						return;
				}
				else {
					if ( ParseExpression.appendDotValid(frontExp) ){
						if ( frontExp.matches(".*?(" + regOperator + "|\\()$|()") ){
							inputString = "0.";
						}
						else {
							inputString = ".";
						}
					}
					else{
						return;
					}
				}
			}
			else if( cursorEnd ){
				if ( inputString == parenthesis ) {
					inputString = ParseExpression.inputParenthesis(exp);
					if ( inputString.matches("\\)") ) {
						if(exp.length() > 1)
							lastChar = exp.charAt(exp.length()-1);
						if (lastChar == '.')
							exp = exp.substring(0, exp.length()-1);
					}
				}
				else if ( ParseExpression.isOperator(inputString) ) {
					if( exp.endsWith("(")){
						if( ! inputString.matches("-") )
							return;
					}
					//如果lastCHar是运算符且倒数第二个字符是数字，则替换成input输入的运算符
					else if(exp.length() > 1){
						lastChar = exp.charAt(exp.length()-1);
						String penultCharString = String.valueOf(exp.charAt(exp.length()-2));
						if ( ParseExpression.isOperator(lastChar) ){
							if(penultCharString.matches("[0-9]|\\)|\\.|%"))
								exp = exp.substring(0, exp.length()-1);
							else{
								return;
							}
						}
						else if ( (lastChar == '.')){
							exp = exp.substring(0, exp.length()-1);
						}
					}
					else if ( exp.length() == 1 ){
						if( exp.matches("-") )
							return;
					}
					else if( ! inputString.matches("-") )
						return;
				}
				else if (inputString.matches("[0-9]")){
					if( (exp.length() > 0) ){
						lastChar = exp.charAt(exp.length()-1);
						if( (lastChar == ')') || (lastChar == '%') )
							return;
					}
					if(exp.endsWith("0")){
						if(exp.length() == 1)
							exp = "";
						else
							exp= exp.replaceAll("(.*?)(" + regOperator + "|\\()(0)$","$1$2");
					}
				}
				lastChar = ' ';
			}
			
			if (inputString.equals("=")) {
				//若表达式以运算符结尾，则去掉末端的运算符 再执行运算
				exp = exp.replaceAll("(.*?)(\\+|-|×|÷)(\\(?)$", "$1");
				if( !ParseExpression.isParenthesisMatch(exp))
					return;
				if( exp == "")
					return;
				
				resultString = ParseExpression.calInfix(exp);
				expAndResult = exp + "\n=" + resultString;
				history.append(expAndResult + "\n");
				showEditText.setText(expAndResult);
			}
			else {
				if ( cursorEnd ){
					exp = exp + inputString;
				}
				else{
					if( inputString == parenthesis)
						inputString = "()";
					exp = frontExp + inputString + rearExp;
				}
				showEditText.setText("");
				showEditText.setText(exp);
			}
		}
		
		else if(btText.matches("%")){
			if ( cursorEnd ){
				if((resultString != "")){
					exp = resultString;
					resultString = "";
				}
				else if( !exp.matches(".*?(\\)|[0-9])$") ){
	    			return;
				}
				showEditText.setText(exp + "%");
			}
			else{
				if((resultString != ""))
					return;
				showEditText.setText(frontExp + "%" + rearExp);
			}
    	}
		
    	else if(btText.matches("C")){
			initVal();
			return;
    	}
		
    	else if(btText.matches("D")){
	        
			if (frontExp.equals(""))
				return;
			
			if(resultString != ""){
				initVal();
				return;
			}
			
			frontExp = frontExp.substring(0, frontExp.length()-1);
			showEditText.setText(frontExp + rearExp);
			showEditText.setSelection(cursorIndex - 1);
			return;
    	}
		
		if ( cursorEnd || (inputString == "=") )
			showEditText.setSelection(showEditText.getText().length());
		else if ( !cursorEnd && inputString.matches("(\\(\\))|(0\\.)") )
			showEditText.setSelection(cursorIndex + 2);
		else if ( cursorIndex < showEditText.getText().length() )
			showEditText.setSelection(cursorIndex + 1);
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
