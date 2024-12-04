package exemplo;
import java.util.ArrayList;
import java.util.List;

public class TextChunk {
	public final int id;
	public final String text;
	private final int initialPos;
	public final String stringToBeFound;
	private List<Integer> foundPos = new ArrayList<>();

	public TextChunk(int id, String text, int initialPos, String stringToBeFound) {
		super();
		this.id = id;
		this.text = text;
		this.initialPos = initialPos;
		this.stringToBeFound = stringToBeFound;
	}

	public int getInitialPos() {
		return initialPos;
	}

	public List<Integer> getFoundPos() {
		return foundPos;
	}

	public void addFoundPos(int pos) {
		foundPos.add(pos);
	}
	@Override
	public String toString() {
		List<String> subText = new ArrayList();
		for(int i : foundPos) {
			subText.add(text.substring(Math.max(i-3,0), i+7));
		}
		return "TextChunk [id="+id+ ", subtext=" + subText + ", initialPos=" + initialPos + ", stringToBeFound=" + stringToBeFound
				+ ", foundPos=" + foundPos + "]";
	}
}
