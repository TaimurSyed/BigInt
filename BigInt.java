import java.util.ArrayList;

public class BigInt {

	private boolean sign;
	ArrayList<Integer> numbers = new ArrayList<Integer>();


	public BigInt() {
		this.sign = true;
		this.numbers = new ArrayList<Integer>();
		numbers.add(0);
	}

	public BigInt(String s) {


		String wholeNumber = setSignAndRemoveItIfItIsThere(s);
			
      	if(wholeNumber.length()==1 && wholeNumber.charAt(0)=='0'){
          numbers.add(0);
          sign = true;
        }
        else {
            for (int i = wholeNumber.length()-1; i>=0; i--) {
                if(Character.isDigit(wholeNumber.charAt(i))) {
                    numbers.add(Character.digit(wholeNumber.charAt(i),10));
                }
                else {
                    throw new BigIntFormatException(s + " is not a valid BigInt");
                }
            }
		}
	}

	public BigInt(BigInt b) {
		sign = b.sign;
		numbers = new ArrayList<Integer>(b.numbers);
	}



	public BigInt(int x ) {
		sign = true;
		if(x==0) {
			numbers.add(x);
		}
		else {
			if (x <0) {
				sign = false;
				x=-x;
			}
			while(x >0) {
				int y = x % 10;
				numbers.add(y);
				x = x/10;
			}
		}
	}

	private String setSignAndRemoveItIfItIsThere(String s) {
		// TODO Auto-generated method stub

		String result = s;
		sign = true;

		if(s==null || s.length()==0) {
			throw new BigIntFormatException(s + " is an invalid BigInt");

		}

		if (s.charAt(0)== '+' || s.charAt(0)== '-') {
			result = s.substring(1);
			sign = s.charAt(0) == '+';
			if (s.length()==1) {
				throw new BigIntFormatException(s + " is an invalid bigInt");
			}
		}

		return result;
	}


	public String toString() {
		String answer = "";

		for(int i = numbers.size()-1; i>=0; i--) {
			answer = answer + numbers.get(i);
		}
		if(this.sign == false) {
			answer = '-' + answer;
		}


		return answer;
	}

	public BigInt add(BigInt b) {

		BigInt answer = new BigInt();

		if (this.sign == true && b.sign == false) {
			//call add method
			answer = doSubtracting(this.numbers,b.numbers);
		}
		else if (this.sign == false && b.sign == false) {
			//call add method

			answer.numbers = doAdding(this.numbers,b.numbers);
			answer.sign = false;
		}
		else if (this.sign== true && b.sign == true) {
			//call add
			answer.numbers = doAdding(this.numbers,b.numbers);
		}
		else {
			//add 
			answer = doSubtracting(b.numbers,this.numbers);

		}
		return answer;

	}

	public BigInt subtract(BigInt b) {

		BigInt answer = new BigInt();

		if(this.sign == true && b.sign == false) {
			answer.numbers = doAdding(this.numbers,b.numbers);

		}
		else if (this.sign == false && b.sign == false) {
			answer = doSubtracting(b.numbers,this.numbers);
		}
		else if (this.sign && b.sign == true){
			answer = doSubtracting(this.numbers,b.numbers);
		}
		else {
			answer.numbers = doAdding(this.numbers,b.numbers);
			answer.sign = false;
		}
		return answer;
	}

	public BigInt multiply(BigInt b) {

		BigInt answer = new BigInt();

		if(compareTo(b.numbers, answer.numbers) ==0 || compareTo(this.numbers,answer.numbers)==0) {
			return answer;
		}

		BigInt increment = new BigInt(1);
		BigInt counter = new BigInt();

		while(compareTo(counter.numbers,this.numbers)!=0) {

			answer.numbers = doAdding(answer.numbers,b.numbers);
			counter.numbers = doAdding(counter.numbers, increment.numbers);
		}

		if(this.sign != b.sign) {
			answer.sign = false;
		}
		return answer;
	}

	public BigInt divide(BigInt b) {
		BigInt answer = new BigInt();

		if(compareTo(b.numbers,answer.numbers)==0) {
			throw new ArithmeticException("Error , division by 0");
		}
		else if(compareTo(this.numbers,answer.numbers)==0) {
			return answer;
		}
		BigInt count = new BigInt();
		BigInt increment = new BigInt(1);

		BigInt copy = new BigInt(this);

		while(compareTo(copy.numbers,b.numbers)>=0) {
			copy = doSubtracting(b.numbers,copy.numbers);
			count.numbers = doAdding(count.numbers,increment.numbers);
		}
		if (this.sign !=b.sign) {
			count.sign = false;
		}
		return count;
	}
	
  	public BigInt modulus(BigInt b) {
		
		BigInt copy = new BigInt(this);
      
      	if(b.numbers.size() == 1 && b.numbers.get(0) == 0) {
          throw new ArithmeticException("Error");
        }
       	if(copy.numbers.size()==1&& copy.numbers.get(0)==0) {
          	return new BigInt(b);
        }
      
		while(compareTo(b.numbers,copy.numbers)<=0) {		
			copy = doSubtracting(copy.numbers,b.numbers);
		}
	
        if(this.sign == true && b.sign == true) {
            return copy;
        }
		else if(this.sign == false && b.sign == true) {
			return doSubtracting(b.numbers,copy.numbers);
        }
		else if(this.sign == false && b.sign == false) {
          copy.sign = false;
          return copy;
        }
        else {
			return doSubtracting(copy.numbers, b.numbers);
        }
      
	}


	private BigInt doSubtracting(ArrayList<Integer> array, ArrayList<Integer> array2) {
		// TODO Auto-generated method stub

		BigInt answer = new BigInt();

		answer.numbers = new ArrayList<>();

		if(compareTo(array,array2)<0) {
			ArrayList<Integer> temporaryArray = array;
			array= array2;
			array2 = temporaryArray;
			answer.sign = false;
		}
		else {
			answer.sign = true;
		}
		int carry =0;
		

		for (int i =0; i < array2.size(); i++) {
			int sum = array.get(i) - array2.get(i) + carry;

			if (sum <0) {
				sum+=10;
				carry =-1;
			}
			else {
				carry =0;
			}
			answer.numbers.add(sum);
		}
		for (int i = array2.size(); i<array.size(); i++) {
			int sum = array.get(i) + carry;

			if (sum < 0) {
				sum+=10;
				carry = -1;
			}
			else {
				carry =0;
			}
			answer.numbers.add(sum);
		}
		for(int i = answer.numbers.size()-1; i>0; i--) {
			if(answer.numbers.get(i)==0) {
				answer.numbers.remove(i);

			}

			else {
				break;
			}
		}
		return answer;
	}

	public int compareTo(ArrayList<Integer> array, ArrayList<Integer> array2) {

		if(array.size()>array2.size()) {
			return 1;
		}
		else if(array.size()<array2.size()) {
			return -1;
		}
		else {
			for (int i = array.size()-1; i >=0; i--) {
				if(array.get(i) > array2.get(i)) {
					return 1;
				}
				else if (array.get(i)< array2.get(i)) {
					return -1;
				}
			}
			return 0;
		}
	}

	private ArrayList<Integer> doAdding(ArrayList<Integer> array, ArrayList<Integer> array2) {
		// TODO Auto-generated method stub

		ArrayList<Integer> numbers = new ArrayList<Integer>();

		int shortest = 0;
		if (array.size()<array2.size()) {
			shortest = array.size();
		}
		else if (array2.size() < array.size()) {
			shortest = array2.size();
		}
		else {
			shortest = array.size();
		}

		int carry =0;
		for(int i =0; i < shortest; i++) {
			int sum = array.get(i) + array2.get(i) + carry;

			if (sum > 9) {
				carry =1;
				int x = sum%10;
				numbers.add(x);
			}
			else {
				carry =0;
				numbers.add(sum);
			}
		}

		for (int i = shortest; i < array.size(); i++) {

			int sum = array.get(i) + carry;

			if (sum > 9) {
				carry =1;
				int x = sum%10;
				numbers.add(x);
			}
			else {
				carry =0;
				numbers.add(sum);
			}
		}

		for (int i = shortest; i < array2.size(); i++) {
			int sum = array2.get(i) + carry;

			if (sum > 9) {
				carry =1;
				int x = sum%10;
				numbers.add(x);
			}
			else {
				carry =0;
				numbers.add(sum);
			}
		}

		if (carry==1) {
			numbers.add(1);
		}
		return numbers;
	}

}
