import java.util.ArrayList;
import java.util.List;

public class DownloadTasksManager {
	private List<FileBlockRequestMessage> chunks=new ArrayList<>();
	private List<FileBlockRequestMessage> foundChunks=new ArrayList<>();
	private int numChunks;
	private int numReceivedResults;

	public DownloadTasksManager(String text, int chunkSize) {
		for(int i=0; i<text.length(); i+=chunkSize) {
//			chunks.add(new DownloadTasksManager(text.substring(i, (int)Math.min(i+chunkSize,text.length())), 
//					i);
//			numChunks++;	
		}
	}
}
