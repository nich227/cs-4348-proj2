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
	public static Semaphore teller_ready = new Semaphore(2, true);
	public static Semaphore loan_ready = new Semaphore(1, true);
	public static Semaphore teller_finished = new Semaphore(0, true);
	public static Semaphore loan_finished = new Semaphore(0, true);

	public static Queue<Customer> teller_line = new LinkedList<>();
	public static Queue<Customer> loan_line = new LinkedList<>();

	public static void main(String[] args) {
		Customer cust0 = new Customer(0);
		Customer cust1 = new Customer(1);
		Customer cust2 = new Customer(2);
		Customer cust3 = new Customer(3);
		Customer cust4 = new Customer(4);

		LoanOfficer loan0 = new LoanOfficer(0);

		BankTeller tell0 = new BankTeller(0);
		BankTeller tell1 = new BankTeller(1);

		Thread tell_th0 = new Thread(tell0);
		Thread tell_th1 = new Thread(tell1);

		Thread loan_th0 = new Thread(loan0);

		Thread cust_th0 = new Thread(cust0);
		Thread cust_th1 = new Thread(cust1);
		Thread cust_th2 = new Thread(cust2);
		Thread cust_th3 = new Thread(cust3);
		Thread cust_th4 = new Thread(cust4);

		loan_th0.start();

		tell_th0.start();
		tell_th1.start();

		cust_th0.start();
		/*
		cust_th1.start();
		cust_th2.start();
		cust_th3.start();
		cust_th4.start();
		*/

		try {
			cust_th0.join();
			System.out.println("Customer 0 is joined by main");
			cust_th1.join();
			System.out.println("Customer 1 is joined by main");
			cust_th2.join();
			System.out.println("Customer 2 is joined by main");
			cust_th3.join();
			System.out.println("Customer 3 is joined by main");
			cust_th4.join();
			System.out.println("Customer 4 is joined by main");
		} catch (InterruptedException e) {
		}
		
		System.exit(0);

	}

}
