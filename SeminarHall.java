package Labs;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
public class SeminarHall {
	private final Scanner scanner = new Scanner(System.in);
	private final String[] timeSlots = {"8:30 AM", "9:30 AM", "10:30 AM", "11:30 AM", "12:30 PM", "1:30 PM", "2:30 PM", "3:30 PM"};
	private String selectedTime;
	private LocalDate bookedDate;
	private int hours;
	private String selectedFloor;
	private boolean isModification;
	private Map<String, Map<LocalDate, Integer>> dailyBookings = new HashMap<>();
	private Map<String, Map<LocalDate, Set<String>>> bookedTimes = new HashMap<>();
	public void bookSeminar() throws InterruptedException, IOException {
		System.out.println("--------------------------------------------------------------");
		System.out.println("\t\tSEMINAR HALL BOOKINGS");
		System.out.println("--------------------------------------------------------------\n");
		DateSelect dateSelect = new DateSelect();
		bookedDate = dateSelect.selectBookingDate();
		selectedFloor = reserve();
		dailyBookings.putIfAbsent(selectedFloor, new HashMap<>());
		bookedTimes.putIfAbsent(selectedFloor, new HashMap<>());
		int bookedHours = dailyBookings.get(selectedFloor).getOrDefault(bookedDate, 0);
		Set<String> bookedTimeSlots = bookedTimes.get(selectedFloor).getOrDefault(bookedDate, new HashSet<>());
		if (bookedHours >= 8) {
			System.out.println("All 8 hours for this floor on this date are fully booked. Please choose another date or floor.");
			return;
		}
		getSeminarDuration();
		getStartTime();
		if (!isTimeAvailable(bookedTimeSlots)) {
			System.out.println("Selected time is unavailable. Please choose a different start time or date.");
			return;
		}
		dailyBookings.get(selectedFloor).put(bookedDate, bookedHours + hours);
		bookedTimes.get(selectedFloor).computeIfAbsent(bookedDate, k ->	new HashSet<>()).add(selectedTime);
		displayBookingConfirmation(bookedDate, selectedTime, hours);
		writeBookingToFile(bookedDate, selectedTime, hours, isModification);
	}
	public String reserve() {
		System.out.println("Select your floor:");
		String[] floors = {"Floor 1", "Floor 2", "Floor 3", "Floor 4"};
		for (int i = 0; i < floors.length; i++) {
			System.out.println("\t" + (i + 1) + "\t" + floors[i]);
		}
		System.out.print("Enter your choice: ");
		int choice = scanner.nextInt();
		if (choice < 1 || choice > floors.length) {
			System.out.println("Invalid floor choice. Please try again.");
			return reserve();
		}
		return floors[choice - 1];
	}
	private void getSeminarDuration() {
		System.out.print("\nHow many hours do you want to book the Seminar Hall? (1-8): ");
		hours = scanner.nextInt();
		while (hours < 1 || hours > 8) {
			System.out.println("Invalid number of hours! Please select between 1 and 8.");
			hours = scanner.nextInt();
		}
	}
	private String getStartTime() {
		boolean validTime = false;
		while (!validTime) {
			System.out.println("Select starting time:");
			for (int i = 0; i < timeSlots.length; i++) {
				System.out.println("\t" + (i + 1) + "\t" + timeSlots[i]);
			}
			int timeChoice = scanner.nextInt();
			while (timeChoice < 1 || timeChoice > timeSlots.length) {
				System.out.println("Invalid time selection! Please choose again.");
				timeChoice = scanner.nextInt();
			}
			selectedTime = timeSlots[timeChoice - 1];
			int selectedStartTimeInMinutes =
			convertTimeToMinutes(selectedTime);
			int endTimeInMinutes = selectedStartTimeInMinutes + (hours * 60);
			int collegeClosingTimeInMinutes = 16 * 60 + 30;
			if (endTimeInMinutes > collegeClosingTimeInMinutes) {
				System.out.println("The selected time and duration exceed the college's closing time of 4:30 PM.");
				System.out.println("Please select an earlier start time.");
			} else {
				validTime = true;
			}
		}
		return selectedTime;
	}
	private boolean isTimeAvailable(Set<String> bookedTimeSlots) {
		int selectedStartTimeInMinutes = convertTimeToMinutes(selectedTime);
		int endTimeInMinutes = selectedStartTimeInMinutes + (hours * 60);
		int collegeClosingTimeInMinutes = 16 * 60 + 30;
		if (endTimeInMinutes > collegeClosingTimeInMinutes) {
			System.out.println("The selected time and duration exceed the college's closing time of 4:30 PM.");
			return false;
		}
		for (String bookedTime : bookedTimeSlots) {
			int bookedStartTimeInMinutes = convertTimeToMinutes(bookedTime);
			int bookedEndTimeInMinutes = bookedStartTimeInMinutes + (getHoursFromTime(bookedTime) * 60);
			if ((selectedStartTimeInMinutes < bookedEndTimeInMinutes) && (bookedStartTimeInMinutes < endTimeInMinutes)) {
				System.out.println("The selected time " + selectedTime + "conflicts with an existing booking for " + bookedDate + "on floor " + selectedFloor + ".");
				return false;
			}
		}
		return true;
	}
	private int convertTimeToMinutes(String time) {
		String[] parts = time.split(" ");
		String[] timeParts = parts[0].split(":");
		int hours = Integer.parseInt(timeParts[0]);
		int minutes = Integer.parseInt(timeParts[1]);
		if (parts.length > 1 && parts[1].equalsIgnoreCase("PM") && hours != 12) {
			hours += 12;
		} else if (parts.length > 1 && parts[1].equalsIgnoreCase("AM") && hours == 12) {
			hours = 0;
		}
		return hours * 60 + minutes;
	}
	private int getHoursFromTime(String time) {
		String[] parts = time.split(" ");
		String[] timeParts = parts[0].split(":");
		return Integer.parseInt(timeParts[0]) == 12 ? 0 : Integer.parseInt(timeParts[0]) + (parts[1].equalsIgnoreCase("PM") ? 12 : 0);
	}
	private void displayBookingConfirmation(LocalDate date, String selectedTime, int hours) throws InterruptedException {
		System.out.println("\nYour Allotment is getting	ready.............");
		Thread.sleep(1000);
		System.out.println("\n\t\t____________________________________________");
		System.out.println("\t\t\t\tSEMINAR HALL BOOKING");
		System.out.println("\t\t____________________________________________");
		System.out.printf("\n\t\tDate\t\t: %s%n", date);
		System.out.printf("\t\tFloor\t\t: %s%n", selectedFloor);
		System.out.printf("\t\tStarting time\t: %s%n", selectedTime);
		System.out.printf("\t\tDuration\t: %d hours%n", hours);
		System.out.println("\n\t\tThanks for booking");
		System.out.println("\t\tHappy learning.....");
		System.out.println("\t\t____________________________________________");
	}
	private void writeBookingToFile(LocalDate date, String time, int hours, boolean isModification) throws IOException {
		try (FileWriter writer = new FileWriter("SeminarInfo.txt",	true)) {
			writer.write("________________________________________________\n");
			writer.write("\t\tSEMINAR HALL BOOKING\n");
			writer.write("________________________________________________\n");
			writer.write("Date\t\t: " + date + "\n");
			writer.write("Floor\t\t: " + selectedFloor + "\n");
			writer.write("Starting time\t: " + time + "\n");
			writer.write("Duration\t: " + hours + " hours\n");
			if (isModification) {
				writer.write("\nNote: This entry is a modification of the previous booking.\n");
			}
			writer.write("\n_______________________________________________\n\n");
		}
	}
	public Map<LocalDate, List<BookingInfo>> getBookings() {
		Map<LocalDate, List<BookingInfo>> seminarBookings = new HashMap<>();
		for (String floor : dailyBookings.keySet()) {
			for (LocalDate date : dailyBookings.get(floor).keySet()) {
				Set<String> bookedTimeSlots = bookedTimes.get(floor).get(date);
				int duration = dailyBookings.get(floor).get(date);
				if (bookedTimeSlots != null) {
					for (String time : bookedTimeSlots) {
						BookingInfo booking = new BookingInfo(date, time, floor, duration);
						seminarBookings.computeIfAbsent(date, k -> new ArrayList<>()).add(booking);
					}
				}
			}
		}
		return seminarBookings;
	}
	public String getRoomNumber() {
		return "101";
	}
	public String[] getTimeSlots() {
		return timeSlots;
	}
	public void modifyBooking() throws InterruptedException, IOException {
		System.out.println("\nCurrent booking details of seminar hall:");
		System.out.println("\nFloor: " + selectedFloor);
		System.out.println("Starting time: " + selectedTime);
		System.out.println("Duration: " + hours);
		System.out.println("Date: " + bookedDate);
		System.out.print("\nDo you want to modify your booking? (yes/no): ");
		if (scanner.next().equalsIgnoreCase("yes")) {
			isModification = true;
			int bookedHours = dailyBookings.get(selectedFloor).getOrDefault(bookedDate, 0);
			Set<String> bookedTimeSlots = bookedTimes.get(selectedFloor).getOrDefault(bookedDate, new HashSet<>());
			bookedHours -= hours;
			dailyBookings.get(selectedFloor).put(bookedDate, bookedHours);
			bookedTimeSlots.remove(selectedTime);
			System.out.println("Rebooking...");
			bookSeminar();
			writeBookingToFile(bookedDate, selectedTime, hours,	isModification);
		}
	}
}