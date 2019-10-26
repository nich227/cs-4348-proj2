import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * Creates and joins all customer threads. When last customer has exited, prints
 * the summary report and ends the simulation.
 *
 * @author Kevin Chen
 * @version 1.0
 */

public class Main {

	public static Semaphore access_balance = new Semaphore(1, true);
	public static Semaphore access_loan_amt = new Semaphore(1, true);
	public static Semaphore cust_ready_teller = new Semaphore(0, true);
	public static Semaphore cust_ready_loan = new Semaphore(0, true);
	public static Semaphore teller_ready = new Semaphore(4, true);
	public static Semaphore loan_ready = new Semaphore(1, true);
	public static Semaphore[] teller_finished = new Semaphore[] { new Semaphore(0, true), new Semaphore(0, true),
			new Semaphore(0, true), new Semaphore(0, true), new Semaphore(0, true) };
	public static Semaphore[] loan_finished = new Semaphore[] { new Semaphore(0, true), new Semaphore(0, true),
			new Semaphore(0, true), new Semaphore(0, true), new Semaphore(0, true) };
	public static Semaphore queue_mutex = new Semaphore(1);

	public static Queue<Customer> teller_line = new LinkedList<>();
	public static Queue<Customer> loan_line = new LinkedList<>();

	public static void main(String[] args) {
		Customer[] customers = new Customer[] { new Customer(0), new Customer(1), new Customer(2), new Customer(3),
				new Customer(4) };
		LoanOfficer loan0 = new LoanOfficer(0);

		BankTeller tell0 = new BankTeller(0);
		BankTeller tell1 = new BankTeller(1);

		Thread tell_th0 = new Thread(tell0);
		Thread tell_th1 = new Thread(tell1);

		Thread loan_th0 = new Thread(loan0);

		Thread cust_th0 = new Thread(customers[0]);
		Thread cust_th1 = new Thread(customers[1]);
		Thread cust_th2 = new Thread(customers[2]);
		Thread cust_th3 = new Thread(customers[3]);
		Thread cust_th4 = new Thread(customers[4]);

		// Starting loaner thread
		loan_th0.start();

		// Starting teller threads
		tell_th0.start();
		tell_th1.start();

		// Starting customer threads
		cust_th0.start();
		cust_th1.start();
		cust_th2.start();
		cust_th3.start();
		cust_th4.start();

		// Joining customer threads
		try {
			cust_th0.join();
		} catch (InterruptedException e) {
		}
		System.out.println("Customer 0 is joined by main");
		try {
			cust_th1.join();
		} catch (InterruptedException e) {
		}
		System.out.println("Customer 1 is joined by main");
		try {
			cust_th2.join();
		} catch (InterruptedException e) {
		}
		System.out.println("Customer 2 is joined by main");
		try {
			cust_th3.join();
		} catch (InterruptedException e) {
		}
		System.out.println("Customer 3 is joined by main");
		try {
			cust_th4.join();
		} catch (InterruptedException e) {
		}
		System.out.println("Customer 4 is joined by main");

		int total_bal = 0, total_loan = 0;
		for (Customer customer : customers) {
			total_bal += customer.getBalance();
			total_loan += customer.getLoanBal();
		}

		// Printing out neatly formatted table
		System.out.println("\n\n\nBank Simulation Summary");
		System.out.format("+------------+--------------------+---------------+%n");
		System.out.format("| Customer # |   Ending balance   |  Loan Amount  |%n");
		System.out.format("-------------+--------------------+---------------+%n");
		for (Customer customer : customers) {
			System.out.format("| %-10d | %-18s | %-13s |%n", customer.getThreadNum(), "$" + customer.getBalance(),
					"$" + customer.getLoanBal());
		}
		System.out.format("+------------+--------------------+---------------+%n");
		System.out.format("| Totals     | %-18s | %-13s |%n", "$" + total_bal, "$" + total_loan);
		System.out.format("+------------+--------------------+---------------+%n");

		System.exit(0);

	}

}
