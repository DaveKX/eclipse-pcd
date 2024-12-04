import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListFileSearch implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<FileSearchResult> fileList;

	public ListFileSearch(List<FileSearchResult> fileList) {
		this.fileList = new ArrayList<>(fileList);
	}

	public List<FileSearchResult> getFileList() {
		return fileList;
	}

}
