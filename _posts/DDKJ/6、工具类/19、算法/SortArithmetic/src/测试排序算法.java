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

        for(int i = 1 ; i<a.length;i++){
            for(int j = i; j>0 ; j--){
                if(a[j-1]>a[j]){
                    int temp  = a[j-1];
                    a[j-1] = a[j];
                    a[j] = temp;
                }
            }
        }
        print(a);
    }


    @Test
    public void 选择排序(){
        int a[] = {2,123,12,45,23,234,4,5};

        for(int i  = 0; i<a.length-1;i++){
            int min = i;
            for(int j = i+1;j<a.length;j++){
                if(a[min]>a[j]){
                    min = j;
                }
            }
            if(min!=i){
                int temp  = a[min];
                 a[min] = a[i];
                 a[i] = temp;

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
        int high = a.length-1;

        qspx(a,low,high);
        print(a);


    }
    public void qspx(int a[],int low,int hign){

        int i = low;
        int j = hign;
        if(i<j){
            int flag= a[low];
            while (i<j){
                while (i<j&&flag<a[j]){
                    j--;
                }
                if(i<j){
                    int temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                    i++;
                }

                while (i<j&&flag>a[i]){
                    i++;
                }
                if(i<j){
                    int temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                    j--;
                }

            }

            qspx(a,low,j-1);
            qspx(a,j+1,hign);
        }

    }


    @Test
    public void 希尔排序(){

        int a[] = {2,123,12,45,23,234,4,5};

        int incr = a.length/2;
        while (incr>=1) {
            for (int i = 0; i < a.length; i++) {
                for (int j = i; j < a.length - incr; j = j + incr) {
                    if (a[j] > a[j + incr]) {
                        int temp = a[j];
                        a[j] = a[j + incr];
                        a[j + incr] = temp;
                    }
                }

            }
            incr = incr/2;
        }
        print(a);

    }


    @Test
    public void 选择排序加折半查找(){
        int a[] = {2,123,12,45,23,234,4,5};
        for(int i = 0;i<a.length-1;i++){
            int min = i;
            for(int j = i+1;j<a.length;j++){
                if(a[min]>a[j]){
                    min = j;
                }
            }
            if(min!=i){
                int temp = a[i];
                a[i] = a[min];
                a[min] = temp;
            }
        }
        print(a);


        //开始折半查找
        int x=45 ;
        int low = 0;
        int high = a.length-1;

        while (low<=high){
           int mid=(low+high)/2;
            if(a[mid]==x){
                System.out.println("x所在位置是"+mid);
                break;
            }
            if(a[mid]>x){
                high = mid -1;
            }
            if(a[mid]<x){
                low = mid+1;
            }
            if(low>high){
                System.out.println("查找失败");
                break;

            }

        }



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
