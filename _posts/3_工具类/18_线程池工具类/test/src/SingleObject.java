/**
 * @author zhangyujin
 * @date 2021/7/31  3:17 下午.
 * @description
 */
public class SingleObject {

    private volatile static SingleObject singleObject = null;

    private SingleObject() {
    }

    public static SingleObject getSingleObject() {
        if (singleObject == null) {
            synSingle();
        }
        return singleObject;
    }

    private static synchronized void synSingle() {
        if (singleObject == null) {
            singleObject = new SingleObject();
        }
    }
}
