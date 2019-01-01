package hust.phone.web.network.common;

public class CommonUtils {
	//阈值
	public  final static int subValue = 100;
	
	//比较两个浮点型的是否相等,不相等返回ture,相等返回false
	public static boolean compareFloat(int a,int b)
	{
		return Math.abs((a-b))>subValue?true:false;
	}
	
	public static void main(String[] args) {
		System.out.println(CommonUtils.compareFloat(228152075, 228152084));
	}
}
