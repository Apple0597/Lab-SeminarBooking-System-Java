package Labs;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
public class DateSelect {
	private final Scanner scanner = new Scanner(System.in);
	public LocalDate selectBookingDate() {
		LocalDate currentDate = LocalDate.now();
		LocalDate selectedDate = null;
		while (true) {
			System.out.print("\nEnter booking date (YYYY-MM-DD): ");
			String dateInput = scanner.next();
			try {
				selectedDate = LocalDate.parse(dateInput,
				DateTimeFormatter.ISO_LOCAL_DATE);
				long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(currentDate,selectedDate);
				if (daysBetween < 0) {
					System.out.println("\nThe selected date cannot be in the past. Please choose today or a future date.");
				} else if (daysBetween > 30) {
					System.out.println("\nBooking can only be made for today or within the next 30 days.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("Invalid date format. Please use YYYY-MM-DD.");
			}
		}
		return selectedDate;
	}
}
