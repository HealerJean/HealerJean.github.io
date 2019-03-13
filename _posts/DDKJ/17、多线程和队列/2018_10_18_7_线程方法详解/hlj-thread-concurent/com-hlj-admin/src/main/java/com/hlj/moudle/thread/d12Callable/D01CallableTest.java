package com.hlj.moudle.thread.d12Callable;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/** 
 * @author : HealerJean
 * @date   �� 2017��12��20�� ����4:58:19
 *
 * 1���������Ĳ�ͬ���ǣ�ʵ��Callable�ӿڵ������߳��ܷ���ִ�н������ʵ��Runnable�ӿڵ������̲߳��ܷ��ؽ����
 * 2��Callable�ӿڵ�call()���������׳��쳣����Runnable�ӿڵ�run()�������쳣ֻ�����ڲ����������ܼ������ף�
 * 3����Callable��������õ�һ��Future����Future ��ʾ�첽����Ľ�������ṩ�˼������Ƿ���ɵķ������Եȴ��������ɣ�����ȡ����Ľ����
 *
 * ������ɺ�ֻ��ʹ�� get ��������ȡ���������߳�û��ִ���꣬Future.get()�������ܻ�������ǰ�̵߳�ִ��
 *
 */

public class D01CallableTest {

  /**
   * 
   	1�������̵߳ĵ�һ�ַ���
   */ 
	@Test
	public void testStart() throws InterruptedException, ExecutionException{
		
	        Callable<String> callable = new CallableTaskWithResult(45);

			//��������߳������൱����һ������ķ���
	        FutureTask<String> task = new FutureTask<String>(callable);
	        
	        new Thread(task).start();
	        
	        String result = task.get(); 
	        
	        System.out.println(result); 
	}
	

	
	

class CallableTaskWithResult implements Callable<String> {  
    private int id;  
    public CallableTaskWithResult(int id) {  
        this.id=id;  
    }
    
	@Override
	public String call() throws Exception {
		System.out.println(Thread.currentThread().getName()); 
		return "idΪ��"+id;
	}  
}

}