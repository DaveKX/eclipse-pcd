import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class ListFileSearch implements Serializable {
	private List<FileSearchResult> fileList;

	public ListFileSearch(List<FileSearchResult> fileList) {
		this.fileList = fileList;
	}

	public List<FileSearchResult> getFileList() {
		return fileList;
	}

}
