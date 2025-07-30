package Labs;
import java.time.LocalDate;
import java.util.*;
public class BookingDisplay {
	private Labselect1 labselect;
	private SeminarHall seminarHall;
	public BookingDisplay(Labselect1 labselect, SeminarHall seminarHall) {
		this.labselect = labselect;
		this.seminarHall = seminarHall;
	}
	public void displayBookedLabs() {
		System.out.println("\nLab bookings:");
		System.out.println("───────────────────────────────────────────────────────────────────────────────");
		System.out.printf("%-15s %-20s %-10s %-10s %-10s%n", "Date", "Time", "Floor", "Room", "Lab");
		System.out.println("───────────────────────────────────────────────────────────────────────────────");
		Map<LocalDate, Map<String, boolean[]>> labBookings = labselect.getBookings();
		for (LocalDate date : labBookings.keySet()) {
			Map<String, boolean[]> dailyBookings = labBookings.get(date);
			for (String lab : dailyBookings.keySet()) {
				boolean[] slots = dailyBookings.get(lab);
				for (int i = 0; i < slots.length; i++) {
					if (slots[i]) {
						System.out.printf("%-15s %-20s %-10s %-10s %-10s%n",date, labselect.getTimeSlots()[i], labselect.getFloorNumber(lab), labselect.getRoomNumber(lab), lab);
					}
				}
			}
		}
		System.out.println("-------------------------------------------------------------------------------");
	}
	public void displayBookedSeminarHalls() {
		System.out.println("\nSeminarHall bookings:");
		System.out.println("────────────────────────────────────────────────────────────────────────────────");
		System.out.printf("%-15s %-15s %-15s %-20s %-10s%n", "Date", "Time", "Floor", "Seminar Hall", "Duration");
		System.out.println("────────────────────────────────────────────────────────────────────────────────");
		Map<LocalDate, List<BookingInfo>> seminarBookings = seminarHall.getBookings();
		for (LocalDate date : seminarBookings.keySet()) {
			List<BookingInfo> bookingsForDate =	seminarBookings.get(date);
			for (BookingInfo booking : bookingsForDate) {
				System.out.printf("%-15s %-15s %-15s %-15s %-15s%n", booking.date, booking.startTime, booking.floor, "Seminar Hall", booking.duration + "hour(s)");
			}
		}
		System.out.println("---------------------------------------------------------------------------------");
	}
}