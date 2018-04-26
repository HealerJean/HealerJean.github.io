import org.junit.Test;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/24  上午10:54.
 */
public class 测试排序算法 {


    /**
     * 直接插入排序思想是 依次走下去 ，然后将它放到前面的位置
     */
    @Test
    public void 直接插入排序(){
        int a[] = {2,123,12,45,23,234,4,5};
        for(int i = 1 ; i<a.length; i++){
            for(int  j = i; j>0;j--){
                if(a[j-1]>a[j]){
                    int temp = a[j-1];
                    a[j-1] = a[j];
                    a[j] = temp;
                }
            }
        }
        print(a);
    }


    @Test
    public void 选择排序(){ //每次选择一个小的放到左边的右面
        int a[] = {2,123,12,45,23,234,4,5};
        for(int i = 0; i <a.length-1 ;i++){
            int min = i;
            for(int j = i+1;j<a.length;j++ ){
                if(a[min] > a[j]){
                    min = j;
                }
            }
            if(min!=i){
                int temp = a[i];
                a[i] =a[min];
                a[min] = temp;
            }
        }

        print(a);

    }


    /**
     * 快速排序为从两边逼近
     */
    @Test
    public void 快速排序(){
        int a[] = {2,123,12,45,23,234,4,5};
        int low  = 0;
        int high  =a.length -1;
        qspx(a,low,high);
        print(a);


    }
    public void qspx(int a[],int low,int hign){
        int i = low;
        int j = hign;
        if(i<j){
            int po = a[low];//给定一个中介值
            while (i<j) { //每次当i比j小的时候小的时候开始比较，当它大于的时候，就会重新开始一次排序
                while (i < j && po < a[j]) {
                    j--;
                }
                if (i < j) {
                    int temp = a[j];
                    a[j] = a[i];
                    a[i] = temp;
                    i++;
                }
                while (i < j && po > a[i]) {
                    i++;
                }
                if (i < j) {
                    int temp = a[j];
                    a[j] = a[i];
                    a[i] = temp;
                    j--;
                }
            }
            //这个时候i就比j大了，所以需要进行下一趟排序。知道最终的结果low =j-1
            qspx(a,low,j-1);
            qspx(a,j+1,hign);
        }



    }


    @Test
    public void 希尔排序(){

        int a[] = {2,123,12,45,23,234,4,5};
        int incr = a.length-1;

        while (incr>=1) {
            for (int i = 0; i < a.length - 1; i++) {
                for (int j = 0; j < a.length - incr; j=j+incr){
                    if(a[j]>a[j+incr]){
                        int temp = a[j];
                        a[j] = a[incr+j];
                        a[incr+j] = temp;
                    }
                }
            }
            incr = incr/2;

        }
        print(a);

    }


    @Test
    public void 希尔排序加折半查找(){

        int a[] = {2,123,12,45,23,234,4,5};
        int incr = a.length-1;

        while (incr>=1) {
            for (int i = 0; i < a.length - 1; i++) {
                for (int j = 0; j < a.length - incr; j=j+incr){
                    if(a[j]>a[j+incr]){
                        int temp = a[j];
                        a[j] = a[incr+j];
                        a[incr+j] = temp;
                    }
                }
            }
            incr = incr/2;

        }

        int low  = 0;
        int high = a.length-1;

        int flag = a.length/2;
        int x = 45;
        while (low<=high){ //最后一定的是low和hig重合和x得坐标相等

            if(a[flag]==45){
                System.out.println(flag);
                break;
            }
            if(x > a[flag]){
                low = flag+1;
            }
            if(x < a[flag]){
                high = flag-1;
            }
            if(low >high){
                System.out.println("不存在");
                break;
            }

             flag = (low+high)/2; //取中值

        }





        print(a);





    }



    /**
     *
     打印的结果
     */
    private static void print(int []a) {
        for (int i : a){
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
