
public class Patient {
	private String name;
	private boolean cured;
	
	public Patient(String name, boolean cured) {
		this.name = name;
		this.cured = cured;
	}
	
	public boolean isCured() {
		return cured;
	}
	
	public boolean cure() {
		return this.cured = true;
	}
	
	@Override
    public String toString() { 
        return String.format(this.name); 
    }
}
