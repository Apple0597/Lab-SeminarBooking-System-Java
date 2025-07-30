package Labs;
import java.time.LocalDate;
public class BookingInfo {
	LocalDate date;
	String startTime;
	String floor;
	int duration;
	public BookingInfo(LocalDate date, String startTime, String floor,int duration) {
		this.date = date;
		this.startTime = startTime;
		this.floor = floor;
		this.duration = duration;
	}
	public LocalDate getDate() {
		return date;
	}
	public String getStartTime() {
		return startTime;
	}
	public String getFloor() {
		return floor;
	}
	public int getDuration() {
		return duration;
	}
}