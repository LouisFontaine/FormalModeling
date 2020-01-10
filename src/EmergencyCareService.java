
public class EmergencyCareService {
	
	
	private int newPatients = 0;
	private int patientsInWaitingRoomWithOutPaper = 0;
	private int patientsInWaitingRoomWithPaper = 0;
	private int patientsWaitingIntheirRooms = 0;
	private int nurses = 0;
	private int rooms = 0;
	private int physicians = 0;
	
	
	public EmergencyCareService() {
	}
	
	public EmergencyCareService(int newPatients, int patientsInWaitingRoomWithOutPaper, int patientsInWaitingRoomWithPaper, int patientsWaitingIntheirRooms, int nurses, int rooms, int physicians) {
		this.newPatients = newPatients;
		this.patientsInWaitingRoomWithOutPaper = patientsInWaitingRoomWithOutPaper;
		this.patientsInWaitingRoomWithPaper = patientsInWaitingRoomWithPaper;
		this.patientsWaitingIntheirRooms = patientsWaitingIntheirRooms;
		this.nurses = nurses;
		this.rooms = rooms;
		this.physicians = physicians;
	}
	
	// Add a new patient in the service
	public void addNewPatients() {
		this.newPatients++;
	}
	
	// Add a new room in the service
	public void addRoom() {
		this.rooms++;
	}
	
	// Add a physician in the service
	public void addPhysician() {
		this.physicians++;
	}
	
	// Check in of a patient
	public boolean patientCheckIn(boolean accepted) {
		if (accepted) {
			this.newPatients--;
			System.out.println("A newPatient check in ...");
			System.out.println("The newPatient is accepted ...");
			System.out.println("The newPatient go in the waiting room ...");
			this.patientsInWaitingRoomWithOutPaper++;
		}
		else {
			System.out.println("A newPatient check in ...");
			System.out.println("The newPatient is refused ...");
			System.out.println("A newPatient leave the service ...");
			this.newPatients--;
		}
		return true;
	}
	
	// Patient fill paper
		public boolean patientFillPaper(boolean accepted) {
			System.out.println("The patient process paper ...");
			return true;
		}
	
	// Nurse processing a patient paper, return true if done and false if not done
	public boolean nurseProcessPatientPaper() {
		// TODO semaphore pour les nurses ?
		if (this.nurses > 0) {
			this.nurses--;
			System.out.println("The nurse process the patient paper ...");
			this.patientsInWaitingRoomWithOutPaper--;
			this.patientsInWaitingRoomWithPaper++;
			return true;
		}
		else {
			System.out.println("There is no nurse available ...");
			return false;
		}
	}
	
	// Try to put a patient in a room
	public boolean patientGoToHisRoom() {
		if (this.rooms > 0) {
			this.rooms--;
			this.patientsInWaitingRoomWithPaper--;
			System.out.println("The patient go to his room ...");
			this.patientsWaitingIntheirRooms++;
			this.nurses++;
			return true;
		}
		else {
			System.out.println("There is no room available");
			return false;
		}
	}
	
	// When we try to examine a patient by a physician
	public boolean PhysicianExaminePatient() {
		if (this.physicians > 0) {
			this.physicians--;
			System.out.println("Patient is examined by a physician ...");
			// Attendre un peu de temps
			System.out.println("The patient is cured ...");
			this.physicians++;
			return true;
		}
		else {
			System.out.println("There is no Physician available");
			return false;
		}
	}
	
	// When a patient check out
	public boolean patientCheckOut() {
		System.out.println("The patient check out and leave");
		this.rooms++;
		return true;
	}
	
}
