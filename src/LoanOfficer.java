/**
 * Processes customer request and updates customer balance.
 *
 * @author Kevin Chen
 * @version 1.0
 */

public class LoanOfficer implements Runnable {
	private int threadNum;

	// Accessors
	public int getThreadNum() {
		return threadNum;
	}

	public LoanOfficer(int tn) {
		threadNum = tn;
	}

	public void run() {
		System.out.println("Loan Officer " + threadNum + " created");

		while (true) {
			// Wait for customer waiting in loan line to be ready
			try {
				Main.cust_ready_loan.acquire();
			} catch (InterruptedException e) {
			}

			try {
				Main.queue_mutex.acquire();
			} catch (InterruptedException e) {
			}
			Customer cur_cust = Main.loan_line.remove();
			System.out.println("Customer " + cur_cust.getThreadNum() + " gets loan from loan officer");
			Main.queue_mutex.release();
			
			// Process loan time
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
			}

			// Enforce mutual exclusive access to loan amount
			try {
				Main.access_loan_amt.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			cur_cust.setLoanBal(cur_cust.getLoanBal() + cur_cust.getAmtChange());

			Main.access_loan_amt.release();

			// Get receipt time
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}

			System.out.println("Loan Officer approves loan for customer " + cur_cust.getThreadNum());

			// Loaner is now ready
			Main.loan_finished[cur_cust.getThreadNum()].release();
			Main.loan_ready.release();
		}
	}
}
