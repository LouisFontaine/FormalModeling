
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
	    	service1.addPhysician();
	    	service1.addPhysician();
	    	
	    	// Create an emergency care service with 4 nurse, 0 room and 0 physician
	    	EmergencyCareService service2 = new EmergencyCareService("Service 2", resourceProvider);
	    	service2.addNurse();
	    	service2.addNurse();
	    	service2.addNurse();
	    	service2.addNurse();
	    	
	    	// Create 4 patients
	    	
	    	Patient patient1 = new Patient("Louis", false);
	    	Patient patient2 = new Patient("Jérémie", false);
	    	Patient patient3 = new Patient("Claude", false);
	    	Patient patient4 = new Patient("Philipe", false);
	    	Patient patient5 = new Patient("Jean", false);
	    	Patient patient6 = new Patient("Pierre", false);
	    	
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
			Thread patient5Admission = new Thread() { 
				
			    @Override 
			    public void run() {
			    	
			    	try {
			    		patient5.joinHospital(service2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    	
			    } 
			};
			Thread patient6Admission = new Thread() { 
				
			    @Override 
			    public void run() {
			    	
			    	try {
			    		patient6.joinHospital(service2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    	
			    } 
			};
			// Patient 1 join service 1
			patient1Admission.start();
			// Patient 2 join service 1
			patient2Admission.start();
			// Patient 3 join service 2
			patient3Admission.start();
			// Patient 4 join service 2
			patient4Admission.start();
			// Patient 5 join service 2
			patient5Admission.start();
			// Patient 6 join service 2
			patient6Admission.start();
	    	
    	}
    	catch(Exception err) {
    		System.out.println(err);
    	}
	}
}
