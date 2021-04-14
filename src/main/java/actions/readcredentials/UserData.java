package actions.readcredentials;

public class UserData 
{
	private String username;
    private String password;
    private String subtenant;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSubtenant() {
		return subtenant;
	}
	public void setSubtenant(String subtenant) {
		this.subtenant = subtenant;
	}
}
