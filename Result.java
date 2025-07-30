package Labs;

import Labs.*;
import java.io.IOException;
import java.util.Scanner;
public class Result {
	public static void main(String[] args) throws InterruptedException, IOException {
		int opt;
		Scanner scanner = new Scanner(System.in);
		Labselect1 labselect = new Labselect1();
		SeminarHall seminarHall = new SeminarHall();
		BookingDisplay display = new BookingDisplay(labselect, seminarHall);
		do {
			System.out.println("\n--------------------------------------------------------------------");
			System.out.println("\t\t\tWELCOME");
			System.out.println("--------------------------------------------------------------------");
			System.out.println("\t1.\t Book a Lab");
			System.out.println("\t2.\t Book Seminar Hall");
			System.out.println("\t3.\t Modify Lab Booking");
			System.out.println("\t4.\t Modify Seminar Hall Booking");
			System.out.println("\t5.\t Display All Booked Labs and Seminar Halls");
			System.out.println("\t6.\t Exit");
			System.out.println("┌─────────────────────────────────────────────────────────────────┐");
			System.out.println("│ Before start booking once check booked labs and seminar halls   │");
			System.out.println("└─────────────────────────────────────────────────────────────────┘");
			System.out.print("\nEnter choice: ");
			opt = scanner.nextInt();
			switch (opt) {
				case 1:
					labselect.bookLab();
					break;
				case 2:
					seminarHall.bookSeminar();
					break;
				case 3:
					labselect.modifyBooking();
					break;
				case 4:
					seminarHall.modifyBooking();
					break;
				case 5:
					display.displayBookedLabs();
					display.displayBookedSeminarHalls();
					break;
				case 6:
					System.out.println("Exiting...");
					break;
				default:
					System.out.println("Invalid choice. Try again.");
			}
		} while (opt != 6);
	}
}