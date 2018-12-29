package hust.phone.web.network.common;

public class CommonUtils {
	//阈值
	public  final static int subValue = 10;
	
	//比较两个浮点型的是否相等
	public static boolean compareFloat(int a,int b)
	{
		return Math.abs((a-b))>subValue?false:true;
	}
	
	public static void main(String[] args) {
		System.out.println(CommonUtils.compareFloat(228152079, 228152084));
	}
}
