import java.util.concurrent.Semaphore;

public class ResourceProvider {
	private Semaphore rooms;
	private Semaphore physicians;
	private Semaphore isRespondingToRequest;
	
	
	public ResourceProvider() {
		this.isRespondingToRequest =  new Semaphore(1, true); // Used to block multiple transaction at the same time
		this.rooms = new Semaphore(0, true); // Number of rooms available
		this.physicians = new Semaphore(0, true); // Number of physicians available
	}
	
	public void receiveRoom(String senderName) throws InterruptedException {
		isRespondingToRequest.acquire();
		this.rooms.release();
		System.out.println("[Resource provider] Received a room from [" + senderName + "]");
		isRespondingToRequest.release();
	}
	
	public void receivePhysician() {
		
	}
	
	// Return true if the provider give a room to the receiver, false if not
	public boolean giveRoom(String receiverName) throws InterruptedException {
		isRespondingToRequest.acquire();
		
		// Deletea a room frome the list of available rooms
		if (rooms.tryAcquire()) {
			System.out.println("[Resource provider] Sent a room to [" + receiverName + "]");
			isRespondingToRequest.release();
			return true;
		}
		else {
			System.out.println("[Resource provider] Couldn't send a room to [" + receiverName + "]");
			isRespondingToRequest.release();
			return false;
		}
		
		
		
	}
	
	// Return true if the provider give a physician to the service, false if not
	public boolean givePhysician() {
		return true;
	}
}
