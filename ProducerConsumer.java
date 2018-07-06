package concurent;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import filetask.SearchFolder;

public class ProduceConsumer {
	
	
	public static void main(String[] args) throws InterruptedException{
		BlockingQueue<Runnable> que = new ArrayBlockingQueue<Runnable>(50);		
		int num =10;
		int task =0;
        final ExecutorService exService = Executors.newFixedThreadPool(num);		
		//que.add(new Producer(Integer.toString(task)));
	    while(que.size()>0){
	    	 exService.submit(que.poll(), "done");
	    }     
	   
		exService.awaitTermination(30, TimeUnit.SECONDS);
		exService.shutdown();
	}

}

class Producer implements Runnable{	
	String taskId;
	public Producer(String taskId){
		this.taskId = taskId;
	}
	@Override
	public void run() {	
		System.out.println( taskId + "producer" );		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

class Consumer  implements Runnable{	
	String taskId;
	public Consumer(String taskId){
		this.taskId = taskId;
	}	
	@Override
	public void run() {
		System.out.println(taskId +"consumer" );
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
