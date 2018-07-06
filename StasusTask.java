package concurent;

public class StatusTask implements Runnable, IStatus{
	Runnable task;
	Hvm vm;
	String status;
	public  StatusTask(Runnable task,Hvm vm){
		this.task = task;
		this.vm = vm;
		this.status = "pending";
	}

	@Override
	public void run() {		
		task.run();		
		setStaus("finished");
		vm.info(this,status);
	}

	@Override
	public void setStaus(String msg) {
		status =msg;		
	}

	@Override
	public String getStatus() {		
		return status;
	}

}
