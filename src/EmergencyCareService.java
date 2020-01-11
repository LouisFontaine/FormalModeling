import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class EmergencyCareService {
	
	private String ECSName; // Name of the ECS service
	private ResourceProvider resourceProvider; // Store the ressource provider
	private ArrayList<Patient> newPatients; // List of the new patients who don't have check in yet
	private ArrayList<Patient> patientsInWaitingRoomWithOutPaper; // List of all the patient in the waiting room who don't have process the paper
	private ArrayList<Patient> patientsInWaitingRoomWithPaper; // List of all the patient in the waiting room who have process the paper
	private ArrayList<Patient> patientsWaitingIntheirRooms; // List of all the patient waiting in their room who don't have a physician yet
	private Semaphore physiciansSemaphore; // Number of physicians available
	private Semaphore roomsSemaphore; // Number of room available
	private Semaphore nursesSemaphore; // Number of nurse available
	
	// Constructor
	public EmergencyCareService(String ECSName, ResourceProvider resourceProvider) {
		this.ECSName = ECSName;
		this.resourceProvider = resourceProvider;
		this.newPatients = new ArrayList<Patient>();
		this.patientsInWaitingRoomWithOutPaper = new ArrayList<Patient>();
		this.patientsInWaitingRoomWithPaper = new ArrayList<Patient>();
		this.patientsWaitingIntheirRooms = new ArrayList<Patient>();
		this.physiciansSemaphore =  new Semaphore(0);
		this.roomsSemaphore =  new Semaphore(0);
		this.nursesSemaphore =  new Semaphore(0);
		this.launchSendEmptyRoomService();
		this.launchSendNonWorkingPhysicianService();
	}

	// Add a new patient in the service and make it take in chanrge by the ECS
	public boolean addNewPatient(Patient patient) throws InterruptedException {
		System.out.println("[" + this.ECSName + "] " + patient + " arrived at the hospital");
		this.newPatients.add(patient);
		this.patientCheckIn(patient);
    	this.patientFillPaper(patient);
    	this.nurseProcessPatientPaper(patient);	    	
    	this.patientGoToHisRoom(patient);
    	this.physicianExaminePatient(patient);
    	this.patientCheckOut(patient);
    	return true;
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
			System.out.println("[" + this.ECSName + "] " + patient + " checked in");
			
			if(!patient.isCured()) {
				System.out.println("[" + this.ECSName + "] " + patient + " was accepted");
				
				this.newPatients.remove(patient);
				this.patientsInWaitingRoomWithOutPaper.add(patient);
				
				System.out.println("[" + this.ECSName + "] " + patient + " went in the waiting room");
			}
			else {
				System.out.println("[" + this.ECSName + "] " + patient + " was refused");
				System.out.println("[" + this.ECSName + "] " + patient + " left the service");
				this.newPatients.remove(patient);
			}
			return true;
		}
		else return false;
	}
	
	// Patient fill paper
	public boolean patientFillPaper(Patient patient) {
		System.out.println("[" + this.ECSName + "] " + patient + " proced paper");
		return true;
	}
	
	// Nurse processing a patient paper, return true if done and false if not done
	public boolean nurseProcessPatientPaper(Patient patient) throws InterruptedException {
		
		// Wait for a nurse
		while(!this.nursesSemaphore.tryAcquire()) {
			System.out.println("[" + this.ECSName + "] " + patient + " is waiting for a nurse");
			
			// Wait 2 secondes before try to redemand for a physician
			Thread.sleep(2 * 1000);
		}
		
		System.out.println("[" + this.ECSName + "] " + "A nurse arrived for " + patient);
		System.out.println("[" + this.ECSName + "] " + "The nurse proced " + patient + "'s paper");
		this.patientsInWaitingRoomWithOutPaper.remove(patient);
		
		this.patientsInWaitingRoomWithPaper.add(patient);
		
		return true;
	}
	
	
	// Try to put a patient in a room
	public boolean patientGoToHisRoom(Patient patient) throws InterruptedException {
		
		System.out.println("[" + this.ECSName + "] " + patient + " is waiting for a room");
		
		// Wait for a nurse
		while(!this.roomsSemaphore.tryAcquire()) {
			
			this.askRoomToProvider();
			
			// Wait 1 secondes before try to redemand for a room
			Thread.sleep(1000);
		}
		
		this.patientsInWaitingRoomWithPaper.remove(patient);
		System.out.println("[" + this.ECSName + "] " + patient + " went to his room");
		this.patientsWaitingIntheirRooms.add(patient);
		
		// Release nurse
		this.nursesSemaphore.release();
		
		return true;
	}
	
	
	private void askRoomToProvider() {
		Thread askRoom = new Thread() { 
			
		    @Override 
		    public void run() {
		    	
		    	try {
		    		//System.out.println("[" + ECSName + "] " + "is Asking Ressource Provider for a room");
		    		
					if (resourceProvider.giveRoom(ECSName)) {
						
						// Add a new room to the service
						roomsSemaphore.release();
						//System.out.println("[" + ECSName + "] " + "Room received from [Ressource provider]");
					}
					else {
						//System.out.println("[" + ECSName + "] " + "Ressource Provider couldn't get a room");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	
		    } 
		};
		askRoom.start();
		
	}

	// When we try to examine a patient by a physician
	public boolean physicianExaminePatient(Patient patient) throws InterruptedException {
		
		System.out.println("[" + this.ECSName + "] " + patient + " is waiting for a physician");
		
		// Wait for a physician
		while(!this.physiciansSemaphore.tryAcquire()) {
			
			
			this.askPhysicianToProvider();
			
			// Wait 2 secondes before try to redemand for a physician
			Thread.sleep(2 * 1000);
		}
		
		System.out.println("[" + this.ECSName + "] " + patient + " is examined by a physician");
		
		// Time to examine a patient
		Thread.sleep(3 * 1000);
		patient.cure();
		
		if(patient.isCured()) System.out.println("[" + this.ECSName + "] " + patient + " is cured");
		else System.out.println("[" + this.ECSName + "] " + patient + " is not cured");
		
		// Release physician
		this.physiciansSemaphore.release();
		
		return true;
	}
	
	
	private void askPhysicianToProvider() {
		Thread askPhysician = new Thread() { 
			
		    @Override 
		    public void run() {
		    	
		    	try {
		    		// System.out.println("[" + ECSName + "] " + "is Asking Ressource Provider for a physician");
		    		
					if (resourceProvider.givePhysician(ECSName)) {
						
						// Add a new room to the service
						physiciansSemaphore.release();
						// System.out.println("[" + ECSName + "] " + "Physician received from [Ressource provider]");
					}
					else {
						// System.out.println("[" + ECSName + "] " + "Ressource Provider couldn't get a Physician");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    	
		    } 
		};
		askPhysician.start();
		
	}

	// When a patient check out
	public boolean patientCheckOut(Patient patient) {
		System.out.println("[" + this.ECSName + "] " + patient + " checked out and leave");
		this.patientsWaitingIntheirRooms.remove(patient);
		this.roomsSemaphore.release();
		return true;
	}
	
	// Async function that will permenently check and send free rooms when there is no patient in the service
	public void launchSendEmptyRoomService() {
		Thread SendEmptyRoomService = new Thread() { 
			
		    @Override 
		    public void run() {
		    	
		    	
		    	while(true) {
		    		// Check and send empty rooms to the provider every second 
		    		try {
		    			
						Thread.sleep(1000);
						
						/*
						 * If there is no patient in the service, 
						 * it means that we cans send a room to the ressource provider
						 */
						if ((newPatients.size() + patientsInWaitingRoomWithOutPaper.size() + patientsInWaitingRoomWithPaper.size() 
							+ patientsWaitingIntheirRooms.size()) == 0) {
							
							// We take a room from this service
							roomsSemaphore.acquire();
							System.out.println("[" + ECSName + "] Sent a room to [Ressource provider]");
							
							// And we send this room to the ressource provider
							resourceProvider.receiveRoom(ECSName);
							
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}
		    	
		    } 
		};
		SendEmptyRoomService.start();
	}
	
	// Async function that will permenently check and send non occupied physicians when there is no patient in the service
	private void launchSendNonWorkingPhysicianService() {
		Thread SendEmptyRoomService = new Thread() { 
			
		    @Override 
		    public void run() {
		    	
		    	
		    	while(true) {
		    		// Check and send empty rooms to the provider every second 
		    		try {
		    			
						Thread.sleep(1000);
						
						/*
						 * If there is no patient in the service, 
						 * it means that we cans send a room to the ressource provider
						 */
						if ((newPatients.size() + patientsInWaitingRoomWithOutPaper.size() + patientsInWaitingRoomWithPaper.size() 
							+ patientsWaitingIntheirRooms.size()) == 0) {
							
							// We take a room from this service
							physiciansSemaphore.acquire();
							System.out.println("[" + ECSName + "] Sent a physician to [Ressource provider]");
							
							// And we send this room to the ressource provider
							resourceProvider.receivePhysician(ECSName);
							
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}
		    	
		    } 
		};
		SendEmptyRoomService.start();
		
	}
	
}
