import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FunctionCache {

	volatile private String data;
	private volatile long time;
	private static final long refTime = 5000;
	private static final long invalidTime = 10000;
	private static final ExecutorService SERIAL_EXECUTER = Executors.newSingleThreadExecutor();

	private volatile Object lock = new Object();

	private static final FunctionCache INSTANCE = new FunctionCache();

	public static FunctionCache getInstance() {
		return INSTANCE;
	}

	private void refersh(final String name) {
	
		SERIAL_EXECUTER.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("before time :: "+ time);
				if (System.currentTimeMillis() - time < invalidTime && System.currentTimeMillis() - time > refTime) {
					System.out.println(" get data called need to actually refersh " + name);
					String d;
					try {
						d = FetchDataUtility.sendGET();
						data = d;
						time = System.currentTimeMillis();
						System.out.println("after time :: "+ time);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

	public String getData() {
		if (data == null || System.currentTimeMillis() - time > invalidTime) {
			System.out.println(" get data called need to block for fresh data " + Thread.currentThread().getName());
			synchronized (lock) {
				if (data == null || System.currentTimeMillis() - time > invalidTime) {
					System.out.println(" get data called got lock here " + Thread.currentThread().getName());

					try {
						data = FetchDataUtility.sendGET();
						time = System.currentTimeMillis();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} else if (System.currentTimeMillis() - time < invalidTime && System.currentTimeMillis() - time > refTime) {
			System.out.println(" get data called need to refersh " + Thread.currentThread().getName());
			refersh(Thread.currentThread().getName());
		}

		return data;
	}

}
