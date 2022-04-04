package cp.week15;

public class NotifyWait
{
	private static final Object monitor = new Object();
	private static boolean t2Done = false;
	private static boolean t1Done = false;
	
	public static void main(String[] args)
	{
		Thread t1 = new Thread( () -> {
			synchronized( monitor ) {
				if ( !t2Done ) {
					try {
						monitor.wait();
					} catch( InterruptedException e ) {
						e.printStackTrace();
					}
				}
			}
			System.out.println( "Hello from t1" );
			synchronized( monitor ) {
				monitor.notify();
				t1Done = true;
			}
		} );
		
		Thread t2 = new Thread( () -> {
			System.out.println( "Hello from t2" );
			synchronized( monitor ) {
				monitor.notify();
				t2Done = true;
			}
		} );

		Thread t3 = new Thread( () -> {
			synchronized( monitor ) {
				if ( !t1Done ) {
					try {
						monitor.wait();
					} catch( InterruptedException e ) {
						e.printStackTrace();
					}
				}
			}
			System.out.println( "Hello from t3" );
		});
		
		t1.start();
		t2.start();
		t3.start();
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}
	}
}
