package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
    	expr=expr.replaceAll("\\s+", ""); //remove all whitespace
		String varm = "[a-zA-Z]+\\[?"; //regex for all variables
		
		//String arraym= "[a-zA-Z]+\\[.+]";//regex for all array
		
		//Pattern a = Pattern.compile(arraym);
		Pattern v = Pattern.compile(varm);
		//Matcher m = a.matcher(expr);
		Matcher vm = v.matcher(expr);
		
//		int indexOfBracket=0;
//		while (m.find(indexOfBracket)) { //must use an index because regex doesnt sperate nested arrays
//			indexOfBracket= expr.indexOf('[', m.start()+1); //if i make this .lastIndexOf(']') this will ignore all nested arrays wich may be use ful for recursion
//			int startOfArrayName = m.start();
//			int endOfArrayName= indexOfBracket;
//			String arrayName= expr.substring(startOfArrayName, endOfArrayName);
//			arrayName=arrayName.trim();
//			boolean newItem = true;
//			for (int i =0; i<arrays.size(); i++) {
//				Array x= arrays.get(i);
//				if (x.name.contentEquals(arrayName)) {
//					newItem=false;
//					break;
//				}
//			}
//			if(newItem) {
//				Array arr = new Array(arrayName);
//				arrays.add(arr);
//			}
//		}
		while(vm.find()) {
			String varName= vm.group();
			if (varName.contains("[")) { //if it is an array the regex will match with all letters and one open bracket, if it has a braket next to it i will ignore it bc it is an array not var
				String arrayName = varName.substring(0,varName.length()-1);
				boolean newItem = true;
				for (int i =0; i<arrays.size(); i++) {
					Array x= arrays.get(i);
					if (x.name.contentEquals(arrayName)) {
						newItem=false;
						break;
					}
				}
				if(newItem) {
					Array arr = new Array(arrayName);
					arrays.add(arr);
				}
				
			}else {
				boolean newItem=true;
				for ( int i = 0; i<vars.size(); i++) {
					Variable x = vars.get(i);
					if (x.name.contentEquals(varName)) {
						newItem=false;
					}
				}
				if(newItem) {
					Variable var = new Variable (varName);
					vars.add(var);
				}
			}
		}
		System.out.println(arrays);
    	System.out.println(vars);
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	expr=expr.replaceAll("\\s+", ""); //remove all whitespace
		
		String arraym= "[a-zA-Z]+\\[.+]";//regex for all array
//		String multi ="-?\\d*\\.?\\d+[*\\/]-?\\d*\\.?\\d+";//regex for multiplication/division
//		String add = "-?\\d*\\.?\\d+[+-]-?\\d*\\.?\\d+"; //regex for addition/subtraction
//		String digit = "-?\\d*\\.?\\d+"; //regex for digits
		String paren= "\\(.+\\)";//regex for parenthesis
		String multi ="-?(?:0|[1-9]\\d*)(?:\\.\\d*)?(?:[E][-]?\\d+)?[*\\/]-?(?:0|[1-9]\\d*)(?:\\.\\d*)?(?:[E][-]?\\d+)?";//regex for multiplication/division with scinoti
		String add = "-?(?:0|[1-9]\\d*)(?:\\.\\d*)?(?:[E][-]?\\d+)?[+-]-?(?:0|[1-9]\\d*)(?:\\.\\d*)?(?:[E][-]?\\d+)?"; //regex for addition/subtraction w scinoti
		String digit = "-?(?:0|[1-9]\\d*)(?:\\.\\d*)?(?:[E][-]?\\d+)?"; //regex for scintoi digits
	
		Pattern arr = Pattern.compile(arraym);
		
		//replaces all variables with their actual value
		for ( int i=0; i<vars.size(); i++) { //replace all variable with numbers
			Integer x;
			Variable v = vars.get(i);
	
			x=v.value;
			
			String y ="(?<!\\w)";
			y=y.concat(v.name);
			y= y.concat("(?!\\w)");
			expr=expr.replaceAll(y, x.toString());
		}
		System.out.println("expr="+expr);

		Matcher arraymatcher = arr.matcher(expr);

		//replaces all arrays with the coreesponding indexed value
		
		while (arraymatcher.find()) {
			System.out.println("group="+arraymatcher.group());
			int indexOfBracketInGroup= arraymatcher.group().indexOf('[');
			int endOfArrayInGroup = findIndexOfEndingBracket(indexOfBracketInGroup, arraymatcher.group(), '[');
			String array;
			if (endOfArrayInGroup+1==arraymatcher.group().length()) {
				 array = arraymatcher.group().substring(0);
			}else {
				 array = arraymatcher.group().substring(0,endOfArrayInGroup+1);
			}
			System.out.println("array=" +array);
			String subExpression = array.substring(array.indexOf('[')+1,array.length()-1);
			System.out.println("sub= "+subExpression);
			String arrayName = arraymatcher.group().substring(0,indexOfBracketInGroup);
			if (subExpression.matches(digit)) { //if the sub expression is already "evaluated" -> A[3], replaces A[3] w int
				float arrin = Float.parseFloat(subExpression);
				int arrayIndex= (int)arrin;
				Integer replace =getArrVal(arrayName, arrayIndex, arrays);
				expr = expr.replace(array, replace.toString());
			}else {
				float eval = evaluate(subExpression, vars, arrays);// evals sub expression  from A[2+3] to A[5] and replaces A[5] w int
				int arrayIndex = (int) eval;
				Integer replace =getArrVal(arrayName, arrayIndex, arrays);
				expr = expr.replace(array, replace.toString());
				
			}
			expr=expr.replaceAll("\\s+", "");
			System.out.println("expr="+expr);
			
			arraymatcher = arr.matcher(expr);
		}
		Pattern p = Pattern.compile(paren);
		Matcher parens = p.matcher(expr);
		while(parens.find()) { //at this point we should only have numbers in the parentheses
			int endOfParens= findIndexOfEndingBracket(0,parens.group(), '(');
			String parenexp;
			if(endOfParens+1==parens.group().length()) {
				parenexp=parens.group();
			}else {
				parenexp=parens.group().substring(0,endOfParens+1);
			}
			System.out.println(parenexp);
			
			String subExp = parenexp.substring(1,parenexp.length()-1);
			System.out.println(subExp);
			if(subExp.matches(digit)) {
				Float value = Float.parseFloat(subExp);
				expr=expr.replace(parenexp, value.toString());
			}else {
				Float value = evaluate(subExp, vars, arrays);
				expr=expr.replace(parenexp, value.toString());
			}
			expr=expr.replaceAll("\\s+", "");
			System.out.println("expr="+expr);
			
			
			parens = p.matcher(expr);
		}
		Pattern MD = Pattern.compile(multi);
		Matcher multidiv = MD.matcher(expr);
		while(multidiv.find()) {// now there should be no parenthesis only multiplciation division addition and subtraction
			char operator;
			if (multidiv.group().contains("*")) {
				operator = '*';
			}else operator='/';
			String digit1 = multidiv.group().substring(0, multidiv.group().indexOf(operator));
			System.out.println(digit1);
			String digit2 = multidiv.group().substring(multidiv.group().indexOf(operator)+1);
			System.out.println(digit2);
			Float dgt1 = Float.parseFloat(digit1);
			Float dgt2 = Float.parseFloat(digit2);
			Float product = operator=='*' ? dgt1*dgt2 : dgt1/dgt2;
			expr=expr.replace(multidiv.group(), product.toString());
			expr=expr.replaceAll("\\s+", "");
			System.out.println("expr="+expr);
			multidiv = MD.matcher(expr);
			
		}
		
		Pattern AS = Pattern.compile(add);
		Matcher addsub = AS.matcher(expr);
		while(addsub.find()) {
			char operator;
			if (addsub.group().contains("+")){
				operator='+';
			}else operator ='-';
			int indexOfOperator=0;
			 String addition = addsub.group();
			  if (operator=='-'){
			  		//get the index of the end of the first digit
			  		Pattern dgt = Pattern.compile(digit);
			  		Matcher dig1 = dgt.matcher(addition);
			  		if (dig1.find()){
			  			indexOfOperator = dig1.end();
			  }
			  }else indexOfOperator = addition.indexOf('+');
			 
//			if (operator=='-') {
//				if(addsub.group().contains("--")){
//					indexOfOperator=addsub.group().indexOf("--");
//				}else indexOfOperator=addsub.group().lastIndexOf('-');
//			}else indexOfOperator=addsub.group().indexOf('+');
////			

			
			String digit1 = addsub.group().substring(0,indexOfOperator);
			String digit2 = addsub.group().substring(indexOfOperator+1);
			Float dgt1 = Float.parseFloat(digit1);
			Float dgt2 = Float.parseFloat(digit2);
			Float result = operator=='+' ? dgt1+dgt2 : dgt1-dgt2;
			expr=expr.replace(addsub.group(),result.toString());
			expr=expr.replaceAll("\\s+", "");
			System.out.println("expr="+expr);
			addsub = AS.matcher(expr);
		}
    	return Float.parseFloat(expr);
    }
    private static int findIndexOfEndingBracket (int index, String array, char cha) {//helper method to find end of paren exp and array exp
    	int endOfArray=0;
    	int indexOfBracket= index;
    	Stack<Character> stk = new Stack<>();
    	stk.push(cha);
    	for (int c=indexOfBracket+1; c<array.length(); c++) {
			
			char ch= array.charAt(c);
			if (ch== '('||ch=='[') {
				stk.push(ch); //autoboxed and sent to push
			    continue;
			}
			if (ch==')'||ch==']') {
				stk.pop();
			}
			if(stk.isEmpty()) {
				endOfArray=c;
				break;
			}
		}
    	return endOfArray;
    }
    
    private static int getArrVal (String arrayName, int arrayIndex, ArrayList<Array> arrays ) {//helper method to get the value from the array at the given index
    	int x=0;
    	for ( int i =0; i<arrays.size(); i++) {
    		if(arrays.get(i).name.contentEquals(arrayName)) {
    			x=i;
    			break;
    		}
    	}
    	return arrays.get(x).values[arrayIndex];
    }
}
