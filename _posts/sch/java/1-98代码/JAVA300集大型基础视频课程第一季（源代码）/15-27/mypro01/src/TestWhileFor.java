
/**
 * while��forѭ�������ϰ��Ŀ
 * @author dell
 *
 */
public class TestWhileFor {
	public static void main(String[] args) {
		int oddSum = 0;  //�������������ĺ�
		int evenSum = 0;  //�������ż���ĺ�
		for(int i=0;i<=100;i++){
			if(i%2!=0){
				oddSum += i;
			}else{
				evenSum += i;
			}
			
		}
		System.out.println("�����ĺͣ�"+oddSum); 
		System.out.println("ż���ĺͣ�"+evenSum); 
		
		System.out.println("#########################"); 
		
		for(int j = 1;j<=1000;j++){
			if(j%5==0){
				System.out.print(j+"\t");
			}
			if(j%(5*3)==0){
				System.out.println();
			}
		}
		
	}
}
