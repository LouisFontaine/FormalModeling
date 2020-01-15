import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		    	    	
    	try {
    		
    		// Create a RessourceProvider
    		ResourceProvider resourceProvider = new ResourceProvider();
    		
    		// Create an emergency care service 
	    	EmergencyCareService service1 = new EmergencyCareService("Service 1", resourceProvider);
	    	service1.addNurse();
	    	service1.addNurse();
	    	service1.addRoom();
	    	service1.addRoom();
	    	service1.addPhysician();
	    	service1.addPhysician();
	    	
	    	// Create an emergency care service
	    	EmergencyCareService service2 = new EmergencyCareService("Service 2", resourceProvider);
	    	service2.addNurse();
	    	service2.addNurse();
	    	service2.addNurse();
	    	
	    	// Create 2 arrays for each service
	    	ArrayList<Patient> patientsService1 = new ArrayList<Patient>();
	    	ArrayList<Patient> patientsService2 = new ArrayList<Patient>();
	    	
	    	// Add patients to the service array
	    	patientsService1.add(new Patient("Louis", false));
	    	patientsService1.add(new Patient("Jérémie", false));
	    	patientsService2.add(new Patient("Claude", false));
	    	patientsService2.add(new Patient("Philipe", false));
	    	patientsService2.add(new Patient("Jean", false));
	    	patientsService2.add(new Patient("Pierre", false));
	    	
	    	// Create an thread array
	    	ArrayList<Thread> patientsThreads = new ArrayList<Thread>();
	    	
	    	// Create threads and add them to the thread array
	    	patientsService1.forEach((patient) -> 
	    		patientsThreads.add(
    				new Thread() { 
    					
    				    @Override 
    				    public void run() {
    				    	
    				    	try {
    				    		patient.joinHospital(service1);
    						} catch (InterruptedException e) {
    							e.printStackTrace();
    						}
    				    	
    				    } 
    				}
	    		)
	    	);
	    	patientsService2.forEach((patient) -> 
	    		patientsThreads.add(
					new Thread() { 
						
					    @Override 
					    public void run() {
					    	
					    	try {
					    		patient.joinHospital(service2);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
					    	
					    } 
					}
	    		)
	    	);
	    	
	    	// Start the threads
	    	patientsThreads.forEach((patientThread) -> patientThread.start());
	    	
    	}
    	catch(Exception err) {
    		System.out.println(err);
    	}
	}
}