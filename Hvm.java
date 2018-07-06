package concurent;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Hvm  implements Runnable{
	private BlockingQueue<StatusTask> pending;
	private ConcurrentMap<StatusTask,String> running;
	private ConcurrentMap<StatusTask,StatusTask> finished;
	private int numRunning;
	private Button btn;
	private int threadNum;
	final ExecutorService exService;
	
	
	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public Hvm(){
		pending = new ArrayBlockingQueue<StatusTask>(100);
		running = new ConcurrentHashMap<StatusTask,String>(10);
		finished = new  ConcurrentHashMap<StatusTask,StatusTask>(100);
		numRunning = 0;		
		threadNum = 10;
		exService = Executors.newFixedThreadPool(getThreadNum());	
	}
	
	@Override
	public void run() {
		btn.setOn(true);
		try {
			mainloop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void setButton(Button btn){
		this.btn = btn;
	}	

	public void stop(){
		this.btn.setOn(false);
		exService.shutdown();
	}
	public void submit(Runnable task){
		// create status, bound togerther
		StatusTask stask = new StatusTask(task,this);
		pending.add(stask);
	}
	
	public void info(StatusTask t, String msg){
		// find task in running map
		// update status,  list
		t.setStaus(msg);
	}
	public boolean getOn(){
		return this.btn.isOn();
	}
	
	private void doTask(){
		if(getOn()){
			System.out.println("pending task: " + pending.size());
			System.out.println("running task: " + running.size());
			System.out.println("finished task: " + finished.size());
			// check pending, update pending
			if(running.size()< 10 && pending.size()>0){
				StatusTask t = pending.poll();
				running.put(t,"running");				
				//exService.submit(t);
				//t.run();
				Thread th = new Thread(t);
				th.start();
			}
			// check running, update running
			
			 Iterator it = running.entrySet().iterator();
		     while (it.hasNext()) {
		    	ConcurrentMap.Entry pair = (ConcurrentMap.Entry)it.next();
		    	StatusTask task = (StatusTask) pair.getKey();
		    	if("finished".equals(task.getStatus())){			    		
		    		finished.put(task, task);
		    		it.remove();
		    	}		       
		        
		     }
			
		}
	}
	public void mainloop() throws Exception{
		Timer timer = new Timer();		
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
			     doTask();
			  }
			}, 0, 1*100);
	}
	


	public BlockingQueue<StatusTask> getPending() {
		return pending;
	}

	public void setPending(BlockingQueue<StatusTask> pending) {
		this.pending = pending;
	}

	public ConcurrentMap<StatusTask,String> getRunning() {
		return running;
	}

	public void setRunning(ConcurrentMap<StatusTask,String> running) {
		this.running = running;
	}

	public  ConcurrentMap<StatusTask,StatusTask> getFinished() {
		return finished;
	}

	public void setFinished( ConcurrentMap<StatusTask,StatusTask> finished) {
		this.finished = finished;
	}

	public synchronized void incNumRunning() {
		 numRunning++;
	}

	public synchronized void decNumRunning() {
		numRunning--;
	}
	
	public synchronized int getNumRunning(){
		return numRunning;
	}
	
	public class Button{
		private volatile boolean on;

		public boolean isOn() {
			return on;
		}

		public void setOn(boolean on) {
			this.on = on;
		}
		
	}
	
	

}


