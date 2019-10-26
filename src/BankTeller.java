/**
 * Serves next customer in the teller line. Processes customer request and
 * updates customer balance.
 *
 * @author Kevin Chen
 * @version 1.0
 */

public class BankTeller implements Runnable {
	private int threadNum;

	// Accessors
	public int getThreadNum() {
		return threadNum;
	}

	public BankTeller(int tn) {
		threadNum = tn;
	}

	public void run() {
		System.out.println("Teller " + threadNum + " created");

		// Wait for customer waiting in teller line to be ready
		try {
			Main.cust_ready_teller.acquire();
		} catch (InterruptedException e) {
		}

		Customer cur_cust = Main.teller_line.remove();
		System.out.println("Teller " + threadNum + " begins serving customer " + cur_cust.getThreadNum());

		switch (cur_cust.getChoice()) {
		// Deposit
		case 1:
			// Process deposit time
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
			}

			// Enforcing mutual exclusive access to balance
			try {
				Main.access_balance.acquire();
			} catch (InterruptedException e1) {
			}
			cur_cust.setBalance(cur_cust.getBalance() + cur_cust.getAmtChange());
			System.out.println("Customer " + cur_cust.getThreadNum() + " requests of teller " + threadNum
					+ " to make a deposit of $" + cur_cust.getAmtChange());
			Main.access_balance.release();

			break;
		// Withdraw
		case 2:
			// Process withdrawal time
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
			}

			// Enforcing mutual exclusive access to balance
			try {
				Main.access_balance.acquire();
			} catch (InterruptedException e1) {
			}
			cur_cust.setBalance(cur_cust.getBalance() - cur_cust.getAmtChange());
			System.out.println("Customer " + cur_cust.getThreadNum() + " requests of teller " + threadNum
					+ " to make a withdrawal of $" + cur_cust.getAmtChange());
			Main.access_balance.release();

			break;
		}

		// Get receipt time
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		if (cur_cust.getChoice() == 1)
			System.out.println("Customer " + cur_cust.getThreadNum() + " gets receipt from teller " + threadNum);
		else
			System.out.println("Customer " + cur_cust.getThreadNum() + " gets cash and receipt from teller " + threadNum);
		
		// Teller is now ready
		Main.teller_finished.release();
		Main.teller_ready.release();
	}
}
