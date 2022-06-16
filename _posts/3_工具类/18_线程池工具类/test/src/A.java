/**
 * @author zhangyujin
 * @date 2021/7/31  2:12 下午.
 * @description
 */
public class A {

    private volatile static A a = null;

    private A() {
    }

    public static A getA() {
        if (a != null) {
            return a;
        }
        return method();
    }

    public static synchronized A method() {
        if (a == null) {
            a = new A();
            return a;
        }
        return a;
    }

}




// 2、三次握手 客户端发起，服务端没有收到