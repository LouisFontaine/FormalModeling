import java.util.ArrayList;

public class EmergencyCareService {
	
	
	private ArrayList<Patient> newPatients;
	private ArrayList<Patient>  patientsInWaitingRoomWithOutPaper;
	private ArrayList<Patient>  patientsInWaitingRoomWithPaper;
	private ArrayList<Patient> patientsWaitingIntheirRooms;
	private int nurses = 0;
	private int rooms = 0;
	private int physicians = 0;
	
	
	public EmergencyCareService() {
		newPatients = new ArrayList<Patient>();
		patientsInWaitingRoomWithOutPaper = new ArrayList<Patient>();
		patientsInWaitingRoomWithPaper = new ArrayList<Patient>();
		patientsWaitingIntheirRooms = new ArrayList<Patient>();
	}
	
	// Add a new patient in the service
	public void addNewPatient(Patient patient) {
		this.newPatients.add(patient);
	}
	
	// Add a new room in the service
	public void addRoom() {
		this.rooms++;
	}
	
	// Add a new room in the service
		public void addNurse() {
			this.nurses++;
		}
	
	// Add a physician in the service
	public void addPhysician() {
		this.physicians++;
	}
	
	// Check in of a patient,  return true if patient exist, false if not
	public boolean patientCheckIn(Patient patient) {
		
		if(newPatients.contains(patient)) {
			System.out.println(patient + " checks in");
			
			if(!patient.isCured()) {
				System.out.println(patient + " is accepted");
				
				this.newPatients.remove(patient);
				this.patientsInWaitingRoomWithOutPaper.add(patient);
				
				System.out.println(patient + " go in the waiting room");
			}
			else {
				System.out.println(patient + " is refused");
				System.out.println(patient + " leave the service");
				this.newPatients.remove(patient);
			}
			return true;
		}
		else return false;
	}
	
	// Patient fill paper
	public boolean patientFillPaper(Patient patient) {
		System.out.println(patient + " process paper");
		return true;
	}
	
	// Nurse processing a patient paper, return true if done and false if not done
	public boolean nurseProcessPatientPaper(Patient patient) {
		// TODO semaphore pour les nurses ?
		if (this.nurses > 0) {
			this.nurses--;
			
			System.out.println("The nurse process " + patient + "'s paper");
			this.patientsInWaitingRoomWithOutPaper.remove(patient);
			
			this.patientsInWaitingRoomWithPaper.add(patient);
			return true;
		}
		else {
			System.out.println("There is no nurse available");
			return false;
		}
	}
	
	
	// Try to put a patient in a room
	public boolean patientGoToHisRoom(Patient patient) {
		if (this.rooms > 0) {
			this.rooms--;
			this.patientsInWaitingRoomWithPaper.remove(patient);
			System.out.println(patient + " goes to his room");
			this.patientsWaitingIntheirRooms.add(patient);
			this.nurses++;
			return true;
		}
		else {
			System.out.println("There is no room available");
			return false;
		}
	}
	
	
	// When we try to examine a patient by a physician
	public boolean physicianExaminePatient(Patient patient) {
		if (this.physicians > 0) {
			this.physicians--;
			System.out.println(patient + " is examined by a physician");
			patient.cure();
			
			if(patient.isCured()) System.out.println(patient + " is cured");
			else System.out.println(patient + " is not cured");
			
			this.physicians++;
			return true;
		}
		else {
			System.out.println("There is no Physician available");
			return false;
		}
	}
	
	
	// When a patient check out
	public boolean patientCheckOut(Patient patient) {
		System.out.println(patient + " check out and leave");
		this.rooms++;
		return true;
	}
	
}
