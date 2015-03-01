import java.math.BigInteger;
import java.util.Random;


public class Diffie {
	
	public static int[] Generate()
	{	
		int[] array = new int[2];
		array[0] =  65537;
		array[1] = 605;
		return array;
	}
	
	public static int Secret(int a)
	{
		Random rand = new Random();
		int r = rand.nextInt(a);
		return r;
	}
	
	public static BigInteger Power(int base, int exp)
	{	
		BigInteger b = BigInteger.valueOf(base);
		b = b.pow(exp);
		
		return b;
	}
	
	public static int GenMessage(int array[], int a)
	{
		BigInteger temp =  Power(array[1], a);
		BigInteger G = BigInteger.valueOf(array[0]);
		BigInteger result = temp.mod(G);
		int message = result.intValue();
		return message;
	}
	
	public static int GenKey(int array[], int M, int s)
	{	
		BigInteger temp =  Power(M, s);
		//System.out.println("Temp = " + temp);
		BigInteger G = BigInteger.valueOf(array[0]);
		BigInteger result = temp.mod(G);
		int key = result.intValue();
		return key;
	}
	
	public static boolean Test(int A, int B){
		System.out.print("A's Secret Key = " + A + "  B's Secret Key = " + B + "\n");
		if(A==B)
			return true;
		else
			return false;
	}
	
	

}
