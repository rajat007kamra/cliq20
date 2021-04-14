package actions.readcredentials;

import java.util.List;

public class EnvironmentUsersData 
{
	private String url;
    private List<UserData> userData;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<UserData> getUserData() {
		return userData;
	}
	public void setUserData(List<UserData> userData) {
		this.userData = userData;
	}
}
