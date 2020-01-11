import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class EmergencyCareService {
	
	
	private ArrayList<Patient> newPatients;
	private ArrayList<Patient>  patientsInWaitingRoomWithOutPaper;
	private ArrayList<Patient>  patientsInWaitingRoomWithPaper;
	private ArrayList<Patient> patientsWaitingIntheirRooms;
	private Semaphore physiciansSemaphore;
	private Semaphore roomsSemaphore;
	private Semaphore nursesSemaphore;
	
	
	public EmergencyCareService() {
		this.newPatients = new ArrayList<Patient>();
		this.patientsInWaitingRoomWithOutPaper = new ArrayList<Patient>();
		this.patientsInWaitingRoomWithPaper = new ArrayList<Patient>();
		this.patientsWaitingIntheirRooms = new ArrayList<Patient>();
		this.physiciansSemaphore =  new Semaphore(0);
		this.roomsSemaphore =  new Semaphore(0);
		this.nursesSemaphore =  new Semaphore(0);
	}
	
	// Add a new patient in the service
	public boolean addNewPatient(Patient patient) {
		System.out.println(patient + " arrived at the hospital");
		return this.newPatients.add(patient);
	}
	
	// Add a new room in the service
	public void addRoom() {
		this.roomsSemaphore.release();
	}
	
	// Add a new room in the service
	public void addNurse() {
		this.nursesSemaphore.release();
	}
	
	// Add a physician in the service
	public void addPhysician() {
		this.physiciansSemaphore.release();
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
	public boolean nurseProcessPatientPaper(Patient patient) throws InterruptedException {
		// Wait for a nurse
		while(!this.nursesSemaphore.tryAcquire()) {
			System.out.println(patient + " is waiting for a nurse");
			// Wait 2 secondes before try to redemand for a physician
			Thread.sleep(2 * 1000);
		}
		
		System.out.println("The nurse process " + patient + "'s paper");
		this.patientsInWaitingRoomWithOutPaper.remove(patient);
		
		this.patientsInWaitingRoomWithPaper.add(patient);
		
		return true;
	}
	
	
	// Try to put a patient in a room
	public boolean patientGoToHisRoom(Patient patient) throws InterruptedException {
		// Wait for a nurse
		while(!this.roomsSemaphore.tryAcquire()) {
			System.out.println(patient + " is waiting for a room");
			// Wait 2 secondes before try to redemand for a physician
			Thread.sleep(2 * 1000);
		}
		
		this.patientsInWaitingRoomWithPaper.remove(patient);
		System.out.println(patient + " goes to his room");
		this.patientsWaitingIntheirRooms.add(patient);
		
		// Release nurse
		this.nursesSemaphore.release();
		
		return true;
	}
	
	
	// When we try to examine a patient by a physician
	public boolean physicianExaminePatient(Patient patient) throws InterruptedException {
		
		// Wait for a physician
		while(!this.physiciansSemaphore.tryAcquire()) {
			System.out.println(patient + " is waiting for a physician");
			// Wait 2 secondes before try to redemand for a physician
			Thread.sleep(2 * 1000);
		}
		
		System.out.println(patient + " is examined by a physician");
		
		// Time to examine a patient
		Thread.sleep(3 * 1000);
		patient.cure();
		
		if(patient.isCured()) System.out.println(patient + " is cured");
		else System.out.println(patient + " is not cured");
		
		// Release physician
		this.physiciansSemaphore.release();
		
		return true;
	}
	
	
	// When a patient check out
	public boolean patientCheckOut(Patient patient) {
		System.out.println(patient + " check out and leave");
		this.roomsSemaphore.release();
		return true;
	}
	
}
