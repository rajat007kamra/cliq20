package actions.readcredentials;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import core.db.CommonConstants;
import core.db.DataExtractor;

/**
 * @Summary This class is reading login credentials from UserConfig.yaml file
 */

public class UserConfigYamlReader 
{
	private static final UserConfigYamlReader instance = new UserConfigYamlReader();
	final static Logger logger = Logger.getLogger(UserConfigYamlReader.class);
	// OuterKey - URL, InnerKey - UserName
	private Map<String, Map<String, UserData>> testConfigUsers = new HashMap<>();
	
	public static UserConfigYamlReader getInstance()
	{
		return instance;
	}
	
	private UserConfigYamlReader()
	{
		loadYAMLUserDetails();
	}
	
	/**
	 * @Summary This method return UserData for a given URL and User Name - Functional use case
	 * @param url, userName
	 */
	public UserData getUserData(String url, String userName)
	{
		if (!DataExtractor.isEmpty(testConfigUsers.get(url)))
		{
			return testConfigUsers.get(url).get(userName);
		}
		return null;
	}
	
	/**
	 * @Summary This method return a random UserData for a given URL - Generic use case
	 * @param url
	 */
	public UserData getGenericUserData(String url)
	{
		if (!DataExtractor.isEmpty(testConfigUsers.get(url)))
		{
			return testConfigUsers.get(url).values().iterator().next();
		}
		return null;
	}

	/**
	 * @Summary This method loading TestConfig.yaml file data into TestConfig file
	 */
	private void loadYAMLUserDetails() {

		Yaml yaml = new Yaml(new Constructor(UserConfig.class));
	    InputStream inputStream = this.getClass()
	      .getClassLoader()
	      .getResourceAsStream(CommonConstants.yamlFileName);
	    
	    UserConfig environments = yaml.load(inputStream);
	    try
	    {
	    	for(EnvironmentUsersData envData : environments.getEnvironment() ) 
		    {	    	
		    	testConfigUsers.put(envData.getUrl(), getUsersMap(envData));	    	
		    } 
	    }
	    catch (Exception e)
	    {
	    	logger.error(e);
	    	logger.info("YAMLREAD-ERROR");
	    }
	}
	
	private Map<String, UserData> getUsersMap(EnvironmentUsersData envData) 
	{
		Map<String, UserData> userMap = new HashMap<String, UserData>();
		for (UserData userData : envData.getUserData())
    	{
			userMap.put(userData.getUsername(), userData);
    	}
		return userMap;
	}

//	public static void main(String[] args)
//	{
//		System.out.println(TestConfigYamlReader.getInstance().getUserData("https://qabin-c-test.mezocliq.com/ql", "Qabin.LOGIN.IND.USER.001").getPassword());
//		System.out.println(TestConfigYamlReader.getInstance().getUserData("https://qanon-b.mezocliq.com/mezocliq/looqs.html", "Qanon.UI.IND.USER.001").getPassword());
//	}
}