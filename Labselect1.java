package Labs;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
public class Labselect1 {
	private final String[] labs = {"Lab1", "Lab2", "Lab3", "Lab4","Lab5"};
	private final String[] timeSlots = {"8:40AM - 11:10AM", "9:30AM - 12:10PM", "10:20AM - 1:00PM", "2:00PM - 4:30PM"};
	private final String[][] roomNumbers = {
		{"101", "102", "103", "104", "105"},
		{"201", "202", "203", "204", "205"},
		{"301", "302", "303", "304", "305"},
		{"401", "402", "403", "404", "405"}
	};
	private final Scanner sc = new Scanner(System.in);
	private String bookedFloor = "null";
	private String bookedLab = "null";
	private String bookedTime = "null";
	private LocalDate bookedDate;
	private Map<LocalDate, Map<String, boolean[]>> bookings = new HashMap<>();
	private boolean isModification;
	public void bookLab() throws InterruptedException, IOException {
		System.out.println("--------------------------------------------------------------");
		System.out.println("\t\tLAB BOOKINGS");
		System.out.println("--------------------------------------------------------------\n");
		DateSelect dateSelect = new DateSelect();
		bookedDate = dateSelect.selectBookingDate();
		bookedFloor = reserve();
		String[] labDetails = selectLab(bookedFloor, bookedDate);
		bookedLab = labDetails[0];
		String roomNumber = labDetails[1];
		bookedTime = slot(bookedLab, bookedFloor, bookedDate);
		displayBookingConfirmation(roomNumber);
		writeBookingToFile(roomNumber,isModification);
	}
	public String reserve() {
		System.out.println("Select your floor:");
		String[] floors = {"Floor 1", "Floor 2", "Floor 3", "Floor 4"};
		for (int i = 0; i < floors.length; i++) {
			System.out.println("\t" + (i + 1) + "\t" + floors[i]);
		}
		System.out.print("Enter your choice: ");
		int choice = sc.nextInt();
		if (choice < 1 || choice > floors.length) {
			System.out.println("Invalid floor choice. Please try again.");
			return reserve();
		}
		return String.valueOf(choice);
	}
	public String slot(String lab, String floor, LocalDate date) {
		System.out.println("Labs times are fixed and are here...");
		for (int i = 0; i < timeSlots.length; i++) {
			System.out.printf("\t%d\t%s%n", (i + 1), timeSlots[i]);
		}
		int choice = -1;
		while (true) {
			System.out.print("Enter the time: ");
			try {
				choice = sc.nextInt();
				if (choice < 1 || choice > timeSlots.length) {
					System.out.println("Wrong choice selected...! Please enter a number between 1 and " + timeSlots.length);
				} else if (isSlotBooked(lab, floor, date, choice - 1)) {
					System.out.println("Error: This slot is already booked for the selected lab. Please choose a different slot.");
				} else {
					bookSlot(lab, floor, date, choice - 1);
				break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input! Please enter a valid number.");
			sc.next(); // Clear invalid input
			}
		}
		return timeSlots[choice - 1];
	}
	public String[] selectLab(String floor, LocalDate date) {
		int floorIndex = Integer.parseInt(floor) - 1;
		System.out.println("\nLabs on Floor " + floor + ":");
		Map<String, boolean[]> floorBookings = bookings.getOrDefault(date, new HashMap<>());
		for (int i = 0; i < labs.length; i++) {
			String lab = labs[i];
			boolean[] slots = floorBookings.getOrDefault(lab, new boolean[timeSlots.length]);
			boolean isFullyBooked = true;
			for (boolean slot : slots) {
				if (!slot) {
				isFullyBooked = false;
				break;
				}
			}
			String availabilityStatus = isFullyBooked ? "Not Available" : "Available";
			System.out.printf("\t%d\t%s (Room %s) %s%n", (i + 1), lab, roomNumbers[floorIndex][i], availabilityStatus);
		}
		int opt = -1;
		while (true) {
			System.out.print("Select a lab: ");
			try {
				opt = sc.nextInt();
				if (opt < 1 || opt > labs.length) {
					System.out.println("Invalid choice! Please select again.");
				} else {
					String selectedLab = labs[opt - 1];
					boolean[]slots=floorBookings.getOrDefault(selectedLab, new boolean[timeSlots.length]);
					boolean isFullyBooked = true;
					for (boolean slot : slots) {
						if (!slot) {
							isFullyBooked = false;
							break;
						}
					}
					if (isFullyBooked) {
					System.out.println("Error: The selected lab is fully booked. Please choose another lab.");
					} else {
						return new String[]{selectedLab, roomNumbers[floorIndex][opt - 1]};
					}
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input! Please enter a valid	number.");
				sc.next();
			}
		}
	}
	private boolean isSlotBooked(String lab, String floor, LocalDate date, int slotIndex) {
		Map<String, boolean[]> floorBookings = bookings.getOrDefault(date, new HashMap<>());
		boolean[] slots = floorBookings.getOrDefault(lab, new boolean[timeSlots.length]);
		return slots[slotIndex];
	}
	private void bookSlot(String lab, String floor, LocalDate date, int slotIndex) {
		bookings.putIfAbsent(date, new HashMap<>());
		bookings.get(date).putIfAbsent(lab, new boolean[timeSlots.length]);
		bookings.get(date).get(lab)[slotIndex] = true;
	}
	private void displayBookingConfirmation(String roomNumber) throws InterruptedException {
		System.out.println("\nYour Allotment is getting	ready.............");
		Thread.sleep(1000);
		System.out.println("\n\t\t____________________________________________");
		System.out.println("\t\t\t\tLAB BOOKING");
		System.out.println("\t\t____________________________________________");
		System.out.printf("\n\t\tFloor\t\t: %s%n", bookedFloor);
		System.out.printf("\t\tLab\t\t: %s%n", bookedLab);
		System.out.printf("\t\tRoom\t\t: %s%n", roomNumber);
		System.out.printf("\t\tLab time\t: %s%n", bookedTime);
		System.out.printf("\t\tDate\t\t: %s%n", bookedDate);
		System.out.println("\n\t\tThanks for booking");
		System.out.println("\t\tHappy learning.....");
		System.out.println("\t\t____________________________________________");
	}
	private void writeBookingToFile(String roomNumber, boolean isModification) throws IOException {
		try (FileWriter writer = new FileWriter("LabInfo.txt", true))
		{ 
			writer.write("________________________________________________\n");
			writer.write("\t\tLAB BOOKING");
			writer.write("\n______________________________________________\n");
			writer.write("Floor\t\t: " + bookedFloor + "\n");
			writer.write("Lab\t\t: " + bookedLab + "\n");
			writer.write("Room\t\t: " + roomNumber + "\n");
			writer.write("Slot\t\t: " + bookedTime + "\n");
			writer.write("Date\t\t: " + bookedDate + "\n");
			if (isModification) {
				writer.write("** This is a modification of the previous booking **\n");
			}
			writer.write("\n_____________________________________________\n\n");
		}
	}
	public void modifyBooking() throws InterruptedException, IOException {
		System.out.println("\nCurrent booking details:");
		System.out.println("Floor: " + bookedFloor);
		System.out.println("Lab: " + bookedLab);
		System.out.println("Time: " + bookedTime);
		System.out.println("Date: " + bookedDate);
		System.out.print("\nDo you want to modify your booking?	(yes/no): ");
		if (sc.next().equalsIgnoreCase("yes")) {
			isModification = true;
			removeBooking(bookedDate, bookedLab, bookedTime, bookedFloor);
			bookedDate = new DateSelect().selectBookingDate();
			bookedFloor = reserve();
			String[] labDetails = selectLab(bookedFloor, bookedDate);
			bookedLab = labDetails[0];
			String roomNumber = labDetails[1];
			bookedTime = slot(bookedLab, bookedFloor, bookedDate);
			displayBookingConfirmation(roomNumber);
			writeBookingToFile(roomNumber, isModification);
		} else {
			System.out.println("No changes made to your booking.");
		}
	}
	public Map<LocalDate, Map<String, boolean[]>> getBookings() {
		return bookings;
	}
	public String[] getTimeSlots() {
		return timeSlots;
	}
	public String getFloorNumber(String lab) {
		for (int floorIndex = 0; floorIndex < roomNumbers.length; floorIndex++) {
			for (int labIndex = 0; labIndex < labs.length; labIndex++) {
				if (labs[labIndex].equals(lab)) {
					return String.valueOf(floorIndex+1);
				}
			}
		}
		return "Unknown";
	}
	public String getRoomNumber(String lab) {
		for (int floorIndex = 0; floorIndex < roomNumbers.length; floorIndex++) {
			for (int labIndex = 0; labIndex < labs.length; labIndex++) {
				if (labs[labIndex].equals(lab)) {
					return roomNumbers[floorIndex][labIndex];
				}
			}
		}
		return "Unknown";
	}
	private void removeBooking(LocalDate date, String lab, String time, String floor) {
		Map<String, boolean[]> floorBookings = bookings.getOrDefault(date, new HashMap<>());
		boolean[] slots = floorBookings.getOrDefault(lab, new boolean[timeSlots.length]);
		for (int i = 0; i < timeSlots.length; i++) {
			if (timeSlots[i].equals(time)) {
				slots[i] = false;
			break;
			}
		}
	}
}