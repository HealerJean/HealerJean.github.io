package cn.bjsxt.array2;

/**
 * ��װ�˾����õ��㷨
 * @author dell
 *
 */
public class Matrix {
	
	/**
	 * ��ӡָ���ľ���
	 * @param c
	 */
	public static void print(int[][] c){
		//��ӡ����
		for(int i=0;i<c.length;i++){
			for(int j=0;j<c.length;j++){
				System.out.print(c[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	/**
	 * ����ӷ�
	 * @param a
	 * @param b
	 * @return
	 */
	public static int[][]  add(int[][] a,int[][] b){
		int[][] c = new int[a.length][a.length];
		for(int i=0;i<c.length;i++){
			for(int j=0;j<c.length;j++){
				c[i][j] = a[i][j]+b[i][j];
			}
		}
		return c;
	}
	
	public static void main(String[] args) {
		int[][]  a = {
						{1,3,3},
						{2,4,7},
						{6,4,9}
					 };
		int[][]  b = {
				{3,3,3},
				{2,4,7},
				{1,4,9}
		};
		
		int[][] c = add(a, b);
		
		print(c);
		
	}
}
