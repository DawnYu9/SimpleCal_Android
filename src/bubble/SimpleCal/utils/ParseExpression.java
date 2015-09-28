package bubble.SimpleCal.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


/**
 * <p>Title: ParseInfixExp</p>
 * <p>Description: 计算中缀表达式的值</p>
 * <p>Company: </p> 
 * @version 2.0.0.150717   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-7-2
 */
public class ParseExpression {
	private final static String ERROR = "格式错误";
	private final static String[] ERROR_ARRAY = ERROR.split("");
	private static String regOperator = "\\+|-|×|÷";
	
	private static final Map<String, Integer> OPERATORS = new HashMap<String, Integer>();
	private static String s;
	private static String str;
	private static String arrString;
	private static String[] outputs;
	private static String suffixExp;
	private static String[] inputs;
	private static Stack<String> suffixStack;
	private static String[] expressions;
	private static String resultString;
	private static String v1;
	private static String v2;
	private static String val;
	private static String regNum;
	private static Stack<String> calStack;
	private static String suffixExp2;
	private static String inputParenthesis;
	private static String lastCharString;
	private static boolean isParenthesisMatch;
	private static Stack<Character> parenthesisStack;
	private static char[] expArrays;
 
	static {	//运算符优先级
		OPERATORS.put( "+", 0);
		OPERATORS.put( "-", 0);
		OPERATORS.put( "×", 1);
		OPERATORS.put( "÷", 1);
	 }

	/**
	 * <p>Title: isOperator</p>
	 * <p>Description: 判断字符串是否是运算符</p>
	 * @param str 
	 * @return boolean
	 * @date 2015-7-2 
	 */
	public static boolean isOperator(String str) {
		return OPERATORS.containsKey(str);
	}
	
	/**
	 * <p>Title: isOperator</p>
	 * <p>Description: 判断字符是否是运算符</p>
	 * @param ch
	 * @return boolean
	 * @author bubble
	 * @date 2015-7-13 下午6:02:58
	 */
	public static boolean isOperator(char ch) {
		s = String.valueOf(ch);
		return OPERATORS.containsKey(s);
	}
	
 
	/**
	 * <p>Title: comparePrior</p>
	 * <p>Description: 比较运算符优先级</p>
	 * @param op1 运算符
	 * @param op2 运算符
	 * @return op1<op2,返回负数，op1=op2返回0，op1>op2返回正数
	 * @date 2015-7-2
	 */
	public static final int comparePrior(String op1, String op2) {
		if (! isOperator(op1) || !isOperator(op2)) {
			throw new IllegalArgumentException( "Invalid operators:" + op1 + " " + op2);
		}
       
        return OPERATORS.get(op1) - OPERATORS.get(op2);
 }
 
	/**
	 * <p>Title: splitInfixExp</p>
	 * <p>Description: 分离中缀表达式，将操作数、运算符分离并存进数组里</p>
	 * @param infixExp 中缀表达式
	 * @return 返回存放操作数、运算符的String数组
	 * @date 2015-7-2
	 */
	public static String[] splitInfixExp(String infixExp) {
		if ( infixExp.matches(".*?\\(|" + regOperator + "$")){
	    	   return ERROR_ARRAY;
	       }
		else if ( ! isParenthesisMatch(infixExp) ){	//括号不匹配
			return ERROR_ARRAY;
		}
		else if ( infixExp.matches(".*?(" + regOperator + ")(" +regOperator + ")+.*?" ) ){	//出现2个相邻运算符
			return ERROR_ARRAY;
		}
		else if ( infixExp.matches(".*?(\\.|%)[0-9](\\.|%).*?") ){
			return ERROR_ARRAY;
		}
		
		ArrayList<String> strArr = new ArrayList<String>();
       
		StringBuilder lastOperand = new StringBuilder();
		//遍历中缀字符数组
		for ( char ch: infixExp.toCharArray()) {
			str = Character.toString(ch);
			arrString = strArr.toString().replaceAll("(.*?)(\\])$", "$1");
			
			if (str.matches("\\(")){
				strArr.add(str);
				lastOperand.setLength(0);
			}
			else if ( str.matches("\\)") ){
				if(lastOperand.length() != 0){
					strArr.add(lastOperand.toString());
					lastOperand.setLength(0);
					strArr.add(str);
				}
				else if(arrString.endsWith(")")){
					strArr.add(str);
				}
			}
			else if ( str.matches("%") ){
				if( arrString.endsWith(")") ){
					strArr.add(str);
				}
				else if( lastOperand.length() > 0){
					strArr.add(lastOperand.toString());
					lastOperand.setLength(0);
					strArr.add(str);
				}
			}
			else if ( isOperator(str) ) {
				if(lastOperand.length() != 0){
					strArr.add(lastOperand.toString());
				}
				else if( (arrString.endsWith("[")) || (arrString.endsWith("(")) ){
					if (str.equals("-")){	//负号
						lastOperand.append(ch);
						continue;
					}
				}
				
				strArr.add(str);
				lastOperand.setLength(0);
			}
			else if ( str.matches("[0-9]|\\.") ){
				lastOperand.append(str);
			}
			else {
				return ERROR_ARRAY;
			}
		}
   
		if(lastOperand.length() != 0){
			strArr.add(lastOperand.toString());
		}
       
		outputs = new String[strArr.size()];
        return strArr.toArray(outputs);
	}   
	
	/**
	 * <p>Title: infix2Suffix</p>
	 * <p>Description: 将中缀表达式转换成后缀表达式</p>
	 * @param infixExp 中缀表达式
	 * @return 后缀表达式
	 * @date 2015-7-2
	 */
	public static String infix2Suffix(String infixExp) {
		suffixExp = "";
	   
		inputs = splitInfixExp(infixExp);
	   
		if (inputs == null)
			return null;
	   
		suffixStack = new Stack<String>();
	   
		for (String input: inputs) {
			if ( input.matches("\\(") ){
				suffixStack.push(input);
			}
			else if (input.matches("%")){
				suffixStack.push(input);
			}
			else if( input.matches("\\)") ){
				while ( !suffixStack.empty() ){
					while( !suffixStack.peek().matches("\\(") )
						suffixExp =suffixExp + " " + suffixStack.pop();
					if(suffixStack.peek().matches("\\("))
						break;
				}
				if( suffixStack.peek().matches("\\(") ){
					suffixStack.pop();
				}
				else
					return ERROR;
			}
			else if ( isOperator(input) ) {
				while ( !suffixStack.empty() ) {
					if( isOperator(suffixStack.peek()) ){
						if ( comparePrior(input, suffixStack.peek()) <= 0) {
							suffixExp =suffixExp + " " + suffixStack.pop();
							continue;
						}
					}
					else if( suffixStack.peek().matches("%") ){
						suffixExp =suffixExp + " " + suffixStack.pop();
						continue;
					}
					break;
				}
				suffixStack.push(input);
			}
			else {
				suffixExp = suffixExp + " " + input;
			}
		}
	   
		while (!suffixStack.empty()) {
			suffixExp = suffixExp + " " + suffixStack.pop();
		}
	       
		return suffixExp.trim();
	} 
	
	/**
	 * <p>Title: calSuffix</p>
	 * <p>Description: 计算后缀表达式的值</p>
	 * @param suffixExp 后缀表达式
	 * @return 表达式的值
	 * @date 2015-7-2
	 */
	public static String calSuffix(String suffixExp) {
		expressions = suffixExp.split(" ");
		resultString = "";
		v1 = "";
		v2 = "";
		val = "";
		//字符串是否为合法操作数
		regNum = "-?[0-9]+(\\.[0-9]+)?";
		
		calStack = new Stack<String>();
		for (String op: expressions) {
			if( isOperator(op)) {
				if (calStack.size() < 2)
					return ERROR;
		 
				v2 = calStack.pop();
				v1 = calStack.pop();
	 
				if ( v1.matches(regNum) && v2.matches(regNum) ){
					switch(op.charAt(0)){
						case '+':
							val = Arith.add(v1, v2);
							break;
						case '-':
							val = Arith.sub(v1, v2);
							break;
						case '×':
							val = Arith.mul(v1, v2);
							break;
						case '÷':
							if( v2.matches("0") )
								return ERROR;
							val = Arith.div(v1, v2);
							break;
					}
					calStack.push(val);
				}
				else 
					return ERROR;
			}
			else if( op.matches("%") ){
				if( calStack.size() < 1)
					return ERROR;
				
				v1 = calStack.pop();
				v2 = "100";
				if ( ! v1.matches(regNum) )
					return ERROR;
				
				val = Arith.div(v1, v2);
				calStack.push(val);
			}
			else if ( op.matches(regNum) ){
				calStack.push(op);
			}
			else{
				return ERROR;
			}
		}
		
		resultString = calStack.pop();

		if ( ! calStack.empty() ){
	    	return ERROR;
	    }
		
		//格式化数值
		if(resultString.indexOf(".") > 0){  
			resultString = resultString.replaceAll("(0+?)$", "");//去掉多余的0  
			resultString = resultString.replaceAll("(\\.)$", "");//如最后一位是.则去掉  
        }  
		else if(resultString.indexOf(".") == 0){
			resultString = "0" + resultString;
		}
		
    	return resultString;
	}
	 
 	// Evaluate an infix expression.
	//计算中缀表达式
	/**
	 * <p>Title: calInfix</p>
	 * <p>Description: 计算中缀表达式的值</p>
	 * @param exp 中缀表达式
	 * @return 中缀表达式的值
	 * @date 2015-7-2
	 */
	public static String calInfix(String exp) {
		suffixExp2 = infix2Suffix(exp);
		if (suffixExp2 == null)
			return "";
		
		return calSuffix(suffixExp2);
	}

    /**
     * <p>Title: inputParenthesis</p>
     * <p>Description: 输入括号，"("和")"</p>
     * @return "("或")"
     * @author bubble
     * @date 2015-7-6 上午11:15:17
     */
    public static String inputParenthesis(String expression){
    	inputParenthesis = "";
    	if(expression.length() > 0){
    		lastCharString = String.valueOf(expression.charAt(expression.length()-1));
    		if(isOperator(lastCharString) || lastCharString.matches("\\(") )
    			inputParenthesis = "(";
    		else if(lastCharString.matches( "[0-9]|\\.|\\)|%") ){
    			isParenthesisMatch = isParenthesisMatch(expression);
    			if(isParenthesisMatch)
    				return "";
    			else
    				inputParenthesis = ")";
    		}
    		else 
    			return "";
    	}
    	else{ 
    		inputParenthesis = "(";
    	}
    	return inputParenthesis;
    }
    
    
    /**
     * <p>Title: isParenthesisMatch</p>
     * <p>Description: 判断表达式的小括号"()"是否匹配</p>
     * @param expression 表达式
     * @return 匹配返回true；不匹配返回false
     * @author bubble
     * @date 2015-7-10 下午3:38:04
     */
    public static boolean isParenthesisMatch(String expression){
    	parenthesisStack = new Stack<Character>();
		expArrays = expression.toCharArray();
		for (char c:expArrays){
			if(c == '(')
				parenthesisStack.push(c);
			else if( (c == ')') && (! parenthesisStack.isEmpty()) )
				parenthesisStack.pop();
		}
		if( parenthesisStack.isEmpty() )
			return true;
		else 
			return false;
    }
    
    /**
	 * <p>Title: insertDot2EndValid</p>
	 * <p>Description: 判断在表达式尾端追加的点'.'是否有效</p>
	 * @param expression 运算表达式
	 * @return 输入的点'.'有效则返回true
	 * @date 2015-7-3
	 */
    public static boolean appendDotValid(String expression) {
		//运算表达式为空，则可以输入点，有效
		if (expression.equals("")) 
			return true;
		
		int expLen = expression.length();
		for (int i = expLen - 1; i >= 0; --i) {
			char ch = expression.charAt(i);
			if ( isOperator(ch) || (ch == '(') )
				return true;
			else if ( ch == '.' || ch == '%' || ch == ')' )
				return false;
		}
		
		return true;
	}
}