package exemplo;
import java.util.ArrayList;
import java.util.List;

public class TextRepository {
	private List<TextChunk> chunks=new ArrayList<>();
	private List<TextChunk> foundChunks=new ArrayList<>();
	private int numChunks;
	public int numReceivedResults;

	public TextRepository(String text, String stringToBeFound, int chunkSize) {
		int l = 1;
		for(int i=0; i<text.length(); i+=chunkSize) {
			chunks.add(new TextChunk(l++,text.substring(i, (int)Math.min(i+chunkSize+stringToBeFound.length(),text.length())), 
					i, stringToBeFound));  
			numChunks++;	
		}
	}

	public synchronized TextChunk getChunk() {
			if(chunks.isEmpty()) return null;
			return chunks.remove(0);
	}

	public synchronized void submitResult(TextChunk chunk) {
		numReceivedResults++;
		foundChunks.add(chunk);
		notifyAll();
	}

	public synchronized List<TextChunk> getAllMatches() throws InterruptedException {
		while(numReceivedResults == numChunks)
			wait();
		if(foundChunks.isEmpty())
			return null;
		return foundChunks;

	}
}
