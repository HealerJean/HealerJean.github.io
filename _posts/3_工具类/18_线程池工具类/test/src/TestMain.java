/**
 * @author zhangyujin
 * @date 2021/7/31  2:48 下午.
 * @description
 */
public class TestMain {

    public static void main(String[] args) {
        int[][] m = {{1, 2, 3},
                    {5, 6, 7},
                    {9, 10, 11}};

        int[][] m2 = {{9, 5, 1},
                    {10, 6, 2},
                    {11, 7, 3}};

        int n = m.length;
        //先右上角和左下角 对角线对调
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int tmp = m[j][i];
                m[j][i] = m[i][j];
                m[i][j] = tmp;
            }
        }

        // 左右 对调
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int tmp = m[i][j];
                m[i][j] = m[i][n - j - 1];
                m[i][n - j - 1] = tmp;
            }
        }

        //断点观察
        System.out.println();
    }

}

