import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class FileUtils {
	public static String calculateFileHash(File file) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, bytesRead);
			}
			fis.close();
			byte[] hashBytes = digest.digest();

			StringBuilder sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
