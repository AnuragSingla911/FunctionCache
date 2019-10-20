
public class Launcher {

	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new Thread(new Runnable() {

				@Override
				public void run() {
					String cache = FunctionCache.getInstance().getData();
					// System.out.println("in main \n" + cache);

				}
			}).start();

			new Thread(new Runnable() {

				@Override
				public void run() {
					String cache = FunctionCache.getInstance().getData();
					// System.out.println("in main \n" + cache);

				}
			}).start();
		}
	}

}
