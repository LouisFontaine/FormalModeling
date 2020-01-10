
public class Main {

	public static void main(String[] args) {
		
		// Create a new hospital
		// create a patient in the hospital
		// create a second patient in the hospital
		// The first patient check in
		// The second patient check in
		
		Thread tache = new Thread() { 
			  
		    @Override 
		    public void run() { 
		    	// Create an emergency care service with 1 nurse, 1 room and 1 physician
		    	EmergencyCareService service1 = new EmergencyCareService();
		    	service1.addNurse();
		    	service1.addRoom();
		    	service1.addPhysician();
		    	Patient patient1 = new Patient("Louis", true);
		    	Patient patient2 = new Patient("Jérémie", false);
		    	
		    	
		    	service1.addNewPatient(patient1);
		    	service1.addNewPatient(patient2);
		    	
		    	service1.patientCheckIn(patient1);
		    	
		    	service1.patientCheckIn(patient2);
		    	service1.patientFillPaper(patient2);
		    	service1.nurseProcessPatientPaper(patient2);	    	
		    	service1.patientGoToHisRoom(patient2);
		    	service1.physicianExaminePatient(patient2);
		    	service1.patientCheckOut(patient2);
		    	
		    } 
		};
		
		tache.start();
	}

}
