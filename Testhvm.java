package concurent;

public class TestVM {
	
	public static void main(String[] args) throws InterruptedException{
		
		Hvm vm = new Hvm();
		Hvm.Button btn =  vm.new Button();
		vm.setButton(btn);
		btn.setOn(true);
		Thread t1 = new Thread(vm);
		t1.start();
		
		for(int i =0 ;i< 10;i++){

			assigntask( vm,i);	
			Thread.sleep(200);
		}
		//Thread.sleep(20);
		t1.join();
		//vm.stop();
	}
	
	
	private static void assigntask(Hvm vm,final int taskNo){
		int size = vm.getRunning().size();
	
		if(size<5){
			Producer st = new Producer(Integer.toString(taskNo));
			vm.submit(st);
		}else if(size>=5){
			Consumer cst = new Consumer(Integer.toString(taskNo));
			vm.submit(cst);
		}
	}
	

}
