import java.util.Random;

/**
 * Each customer will make 3 visits to the bank. Each customer starts with a
 * balance of $1000. On each visit a customer is randomly assigned one of the
 * following tasks: 1. Make a deposit of a random amount from $100 to $500 in
 * increments of $100 2. Make a withdrawal of a random amount from $100 to $500
 * in increments of $100 a. Withdrawals may exceed the balance 3. Request a loan
 * of a random amount from $100 to $500 in increments of $100
 *
 * @author Kevin Chen
 * @version 1.0
 */

public class Customer implements Runnable {

	private int balance;
	private int loan_bal;
	private int numTimesVisited;
	private int threadNum;
	private int choice;

	private int amt_change;

	// Accessors
	public int getBalance() {
		return balance;
	}

	public int getNumTimesVisited() {
		return numTimesVisited;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public int getLoanBal() {
		return loan_bal;
	}

	public int getChoice() {
		return choice;
	}

	public int getAmtChange() {
		return amt_change;
	}

	// Mutators
	public void setBalance(int bal) {
		balance = bal;
	}

	public void setLoanBal(int lamt) {
		loan_bal = lamt;
	}

	public Customer(int tn) {
		balance = 1000;
		loan_bal = 0;
		numTimesVisited = 0;
		threadNum = tn;
		choice = 0;
		amt_change = 0;
	}

	public void run() {
		System.out.println("Customer " + threadNum + " created");

		int[] amt_change_choices = { 100, 200, 300, 400, 500 };
		
		Random rand_gen = new Random(System.currentTimeMillis());

		while (numTimesVisited < 3) {

			// Randomly choose amount to change balance
			int min = 0;
			int max = 4;
			this.amt_change = amt_change_choices[rand_gen.nextInt(max) + min];

			// Randomly choose to deposit or withdraw or loan
			min = 1;
			max = 3;

			this.choice = rand_gen.nextInt(max) + min;

			// Requires the use of a teller
			if (choice == 1 || choice == 2) {
				System.out.println("\n--Teller--");
				// Wait for a teller to be ready
				try {
					Main.teller_ready.acquire();
				} catch (InterruptedException e) {
				}
				// Request deposit or request withdrawal time
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}

				// Add customer to teller line
				Main.teller_line.add(this);
				Main.cust_ready_teller.release();
				try {
					Main.teller_finished.acquire();
				} catch (InterruptedException e) {
				}
			}

			// Requires the use of a loaner
			if (choice == 3) {
				System.out.println("\n--Loaner--");
				// Wait for the loaner to be ready
				try {
					Main.loan_ready.acquire();
				} catch (InterruptedException e) {

				}
				// Request loan time
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}

				// Add customer to loan line
				Main.loan_line.add(this);
				Main.cust_ready_loan.release();
				try {
					Main.loan_finished.acquire();
				} catch (InterruptedException e) {
				}
			}

			numTimesVisited++;
		}

		System.out.println("Customer " + threadNum + " departs the bank");
	}
}
