package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		BigInteger x= new BigInteger();
		integer=integer.trim();
		if (integer.length()==0) {
			throw new IllegalArgumentException ("Incorrect Format");
		}
		Character ch=integer.charAt(0);
		//check first character
		int i =0;
		if (integer.length()==1&&!Character.isDigit(ch)) {
			throw new IllegalArgumentException ("Incorrect Format");
		}
		if(ch!='-' && ch!='+'&& !Character.isDigit(ch)) {
			// if ("+-0123456789".contains(ch))
			throw new IllegalArgumentException ("Incorrect Format");
		} else {
			if(ch=='-') {
				x.negative=true;
			
				integer = integer.substring(1);
			} else if(ch =='+') {
				integer = integer.substring(1);
			
			} 
		}
		//check rest of the string
		for( ; i<integer.length(); i++ ) {
			char cho=integer.charAt(i);
			if (!Character.isDigit(cho)){
				throw new IllegalArgumentException ("Incorrect Format");
			}else {

			int y = Integer.parseInt(""+cho);
			
			if (x.front==null&&y==0&&integer.length()>1) {
				integer = integer.substring(1);
				i--;
				System.out.println(integer.length());
				continue;
			}else if (x.front==null&&y==0&&integer.length()==1) {
				
				x.negative=false;
				System.out.println(x.numDigits);
				return x;
			}else {
				x.numDigits++;
				x.front= new DigitNode(y,x.front);
			}
				
				
			
		}
	}
	
		
		
		
		/* IMPLEMENT THIS METHOD */
		
		// following line is a placeholder for compilation
		System.out.println(x.numDigits);
		return x;
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger one, BigInteger two) {
		
		/* IMPLEMENT THIS METHOD */
		
		// following line is a placeholder for compilation
		//DigitNode ptr1 = first.front;
		//DigitNode ptr2=second.front;
		
		//BigInteger one= new BigInteger();
		//BigInteger two = new BigInteger();
		
		String integer1= one.toString();
		BigInteger first= parse(integer1);
		if (first.front==null) {
			first.front=new DigitNode(0,null);
			first.numDigits++;		
		}
		String integer2= two.toString();
		BigInteger second= parse(integer2);
		if (second.front==null) {
			second.front=new DigitNode(0,null);
			second.numDigits++;		
		}
		
		
		BigInteger added = new BigInteger();
		DigitNode ptr3 = added.front;
		if((first.negative&&second.negative)||(!first.negative&&!second.negative)) {
			if (first.negative) {
				added.negative= true;
			}
		//add normally because if they are both negative then it is normal addition	and add negative sign
			boolean carry=false;
			DigitNode ptr1 = first.front, ptr2=second.front;
			for (; ptr1!=null&&ptr2!=null;ptr1=ptr1.next, ptr2=ptr2.next) {
				int f = ptr1.digit;
				int s =ptr2.digit;
				int x = f+s;
				if (carry==true) {
					x++;
					carry=false;
				}
				if (x>= 10) {
					carry=true;
					x=x%10;
					
				}
				
				if (added.front==null) {
					ptr3=new DigitNode(x, null);
					added.front= ptr3;
					continue;
				}
				ptr3.next= new DigitNode(x , null);
				ptr3=ptr3.next;
		
				 
			}
			
			//fist longer than second then we would make the
			if (first.numDigits>second.numDigits) {
				if (carry==true) {
					for ( DigitNode ninefinder=ptr1; ninefinder!=null&&carry==true; ninefinder=ninefinder.next) {
						ninefinder.digit+=1;
						if(ninefinder.digit>=10) {
							ninefinder.digit=ninefinder.digit%10;
						}else {
							carry=false;
						}
					}
				}
				for (; ptr1!=null; ptr1=ptr1.next) {
					ptr3.next= new DigitNode(ptr1.digit,null);
					ptr3=ptr3.next;
				}
				//second longer than first
			}else if (second.numDigits>first.numDigits) {
				if (carry==true) {
					for ( DigitNode ninefinder=ptr2; ninefinder!=null&&carry==true; ninefinder=ninefinder.next) {
						ninefinder.digit+=1;
						if(ninefinder.digit>=10) {
							ninefinder.digit=ninefinder.digit%10;
						}else {
							carry=false;
						}
					}
				}
				for (; ptr2!=null; ptr2=ptr2.next) {
					ptr3.next= new DigitNode(ptr2.digit,null);
					ptr3=ptr3.next;
				}
				
			} if (carry==true) {
				ptr3.next= new DigitNode (1,null);
				ptr3=ptr3.next;
			}
			
			
			
			
			
			
			
			
			
			
			//subtraction method
		}else {
			//here one is negative so it is subtraction
			BigInteger neg;
			BigInteger pos;
			if ( first.negative) {
				
				 neg= first;
				 pos=second;
				
			}else {
				 neg= second;
				 pos=first;
			}
			DigitNode negptr=neg.front;
			DigitNode posptr=pos.front;
			boolean greaterneg=false;
			if (pos.numDigits==neg.numDigits) {
				for (; posptr!=null&&negptr!=null;posptr=posptr.next, negptr=negptr.next) {
				 if (posptr.digit>negptr.digit) {
					 greaterneg=false;
				 }else if (posptr.digit<negptr.digit) {
					 greaterneg=true;
				 }
				}	
			
			if (greaterneg==true) {
				BigInteger temp= neg;
				neg=pos;
				pos=temp;
				added.negative=true;
			}
			negptr=neg.front;
			posptr=pos.front;
			for (; posptr!=null&&negptr!=null;posptr=posptr.next, negptr=negptr.next) {
				int d;
				int p= posptr.digit;
				int n= negptr.digit;
				if (n>p) {
					if(posptr.next==null) {
						added.negative=true;
						d=n-p;
					}else {
						if (posptr.next.digit==0) {
							DigitNode x= posptr.next.next;
							for (; x!=null; x=x.next) {
								if(x.digit!=0) {
									x.digit--;
									break;
								}
							}
							DigitNode y =posptr.next;
							for(; y!=x; y=y.next) {
								y.digit+=9;
							}
						} else {
							posptr.next.digit--;
						}
						p+=10;
						d=p-n;
					}
					
				}else {
					d= p-n;
				}
				if (added.front==null) {
					ptr3=new DigitNode(d, null);
					added.front= ptr3;
					continue;
				}
				ptr3.next= new DigitNode(d , null);
				ptr3=ptr3.next;
			}
			if (ptr3.digit==0) {
				String x = added.toString();
				added=parse(x);
				ptr3=added.front;
				if (added.numDigits>1) {
				while(ptr3.next!=null) {
				ptr3=ptr3.next;
			}
				}
			}
			}else if (pos.numDigits > neg.numDigits) {
				negptr=neg.front;
				posptr=pos.front;
				for (; negptr!=null; negptr=negptr.next,posptr=posptr.next) {
					int d;
					int p= posptr.digit;
					int n= negptr.digit;
					if (n>p) {
						if(posptr.next==null) {
							added.negative=true;
							d=n-p;
						}else {
							if (posptr.next.digit==0) {
								DigitNode x= posptr.next.next;
								for (; x!=null; x=x.next) {
									if(x.digit!=0) {
										x.digit--;
										break;
									}
								}
								DigitNode y =posptr.next;
								for(; y!=x; y=y.next) {
									y.digit+=9;
								}
							} else {
								posptr.next.digit--;
							}
							p+=10;
							d=p-n;
						}
						
					}else {
						d= p-n;
					}
					if (added.front==null) {
						ptr3=new DigitNode(d, null);
						added.front= ptr3;
						continue;
					}
					ptr3.next= new DigitNode(d , null);
					ptr3=ptr3.next;
					
				}
				
				for (; posptr!=null; posptr=posptr.next) {
					ptr3.next=new DigitNode(posptr.digit,null);
					ptr3=ptr3.next;
				}
				if (ptr3.digit==0) {
					String x = added.toString();
					added=parse(x);
					ptr3=added.front;
					if (added.numDigits>1) {
					while(ptr3.next!=null) {
					ptr3=ptr3.next;
				}
					}
				}
				
				
				
			}else if (neg.numDigits>pos.numDigits) {
				added.negative=true;
				BigInteger temp= neg;
				neg=pos;
				pos=temp;
				negptr=neg.front;
				posptr=pos.front;
				for (; negptr!=null; negptr=negptr.next,posptr=posptr.next) {
					int d;
					int p= posptr.digit;
					int n= negptr.digit;
					if (n>p) {
						if(posptr.next==null) {
							added.negative=true;
							d=n-p;
						}else {
							if (posptr.next.digit==0) {
								DigitNode x= posptr.next.next;
								for (; x!=null; x=x.next) {
									if(x.digit!=0) {
										x.digit--;
										break;
									}
								}
								DigitNode y =posptr.next;
								for(; y!=x; y=y.next) {
									y.digit+=9;
								}
							} else {
								posptr.next.digit--;
							}
							p+=10;
							d=p-n;
						}
						
					}else {
						d= p-n;
					}
					if (added.front==null) {
						ptr3=new DigitNode(d, null);
						added.front= ptr3;
						continue;
					}
					ptr3.next= new DigitNode(d , null);
					ptr3=ptr3.next;
					
				}
				
				for (; posptr!=null; posptr=posptr.next) {
					ptr3.next=new DigitNode(posptr.digit,null);
					ptr3=ptr3.next;
				}
				if (ptr3.digit==0) {
					String x = added.toString();
					added=parse(x);
					ptr3=added.front;
					if (added.numDigits>1) {
					while(ptr3.next!=null) {
					ptr3=ptr3.next;
				}
					}
				}
				
				
				
			}
			
		}
		added=parse(added.toString());
		System.out.println(added.front==null);
		
		return added;
		
		
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		/* IMPLEMENT THIS METHOD */
		
		// following line is a placeholder for compilation
		
		BigInteger one= new BigInteger();
		BigInteger two= new BigInteger();
		BigInteger product= new BigInteger();
		
		
		if( first.numDigits> second.numDigits) {
		one=parse(second.toString());
		two=parse(first.toString());
		}else {
			one=parse(first.toString());
			two=parse(second.toString());
		}
		
		
		//System.out.println("one="+one);
		//System.out.println("two="+two);
		
		
		DigitNode ptr1= one.front;
		int carryint=0;
		
		for(int i=1 ;i<=one.numDigits&&ptr1!=null; ptr1=ptr1.next,i++) {
			int j = i;
			BigInteger x = new BigInteger();
			DigitNode ptr3= x.front;
			for (DigitNode ptr2= two.front; ptr2!=null;ptr2=ptr2.next) {
				//System.out.println("ptr2="+ptr2.digit);
				//System.out.println("ptr1="+ ptr1.digit);
				int y= ptr1.digit*ptr2.digit+carryint;
				carryint=0;
				if (y>=10) {
					carryint=y/10;
					y=y%10;
					//System.out.println("y= "+y);
					//System.out.println("carryint= "+ carryint);
				}
				if(x.front==null) {
					ptr3= new DigitNode(y,null);
					x.front=ptr3;
				}else {
					
					ptr3.next=new DigitNode(y,null);
					ptr3=ptr3.next;
					//System.out.println("ptr3="+ptr3.digit);
					
				} 
			}
			if (carryint>0) {
				ptr3.next=new DigitNode(carryint,null);
				carryint=0;
			}
			while (j>1) {
				
			    x.front=new DigitNode(0,x.front);
					
				j--;
			}
			x=parse(x.toString());
			
			
			//System.out.println("x     "+x);
			
			product= add(product,x);
			//System.out.println("product     "+product);
			
		}
		
		if (one.negative!=two.negative) {
			product.negative=true;
		}

		product=parse(product.toString());
		System.out.println("frist:"+first);
		System.out.println("second:"+second);
		System.out.println(product.front==null);
		return product;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
