package actions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.log4j.Logger;

// TODO Usage is not known and will be deleted by Rajat
public class SystemEnvVar {
	final static Logger logger = Logger.getLogger(SystemEnvVar.class);
	public static void main(String[] args) throws IOException {

		ProcessBuilder pb = new ProcessBuilder("CMD", "/C", "SET");
		Map<String, String> env = pb.environment();
		env.put("Suite", "testng.xml");
		Process p = pb.start();
		InputStreamReader isr = new InputStreamReader(p.getInputStream());
		char[] buf = new char[1024];
		while (!isr.ready()) {
			;
		}
		while (isr.read(buf) != -1) {
			logger.info(buf);
		}
	}
}
