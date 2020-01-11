
public class Main {

	public static void main(String[] args) {
		    	    	
    	try {
    		
    		// Create a RessourceProvider
    		ResourceProvider resourceProvider = new ResourceProvider();
    		
    		// Create an emergency care service with 1 nurse, 1 room and 1 physician
	    	EmergencyCareService service1 = new EmergencyCareService("Service 1", resourceProvider);
	    	service1.addNurse();
	    	service1.addRoom();
	    	service1.addRoom();
	    	service1.addRoom();
	    	service1.addPhysician();
	    	
	    	// Create an emergency care service with 2 nurse, 0 room and 1 physician
	    	EmergencyCareService service2 = new EmergencyCareService("Service 2", resourceProvider);
	    	service2.addNurse();
	    	service2.addNurse();
	    	service2.addPhysician();
	    	
	    	// Create 2 patients
	    	
	    	Patient patient1 = new Patient("Louis", false);
	    	Patient patient2 = new Patient("Jérémie", false);
	    	Patient patient3 = new Patient("Claude", false);
	    	Patient patient4 = new Patient("Philipe", false);
	    	
	    	// Async functions
	    	Thread patient1Admission = new Thread() { 
				
			    @Override 
			    public void run() {
			    	
			    	try {
			    		patient1.joinHospital(service1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    	
			    } 
			};
			Thread patient2Admission = new Thread() { 
				
			    @Override 
			    public void run() {
			    	
			    	try {
			    		patient2.joinHospital(service1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    	
			    } 
			};
			// Async functions
	    	Thread patient3Admission = new Thread() { 
				
			    @Override 
			    public void run() {
			    	
			    	try {
			    		patient3.joinHospital(service2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    	
			    } 
			};
			Thread patient4Admission = new Thread() { 
				
			    @Override 
			    public void run() {
			    	
			    	try {
			    		patient4.joinHospital(service2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    	
			    } 
			};
			
			// Patient 1 join service 1
			patient1Admission.start();
			// Patient 1 join service 1
			patient2Admission.start();
			// Patient 1 join service 2
			patient3Admission.start();
			// Patient 1 join service 2
			patient4Admission.start();
	    	
    	}
    	catch(Exception err) {
    		System.out.println(err);
    	}
	}
}
