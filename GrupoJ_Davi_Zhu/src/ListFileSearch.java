import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class ListFileSearch implements Serializable {
	private List<FileSearchResult> fileList;
	private String originPort;

	public ListFileSearch(List<FileSearchResult> fileList, String originPort) {
		this.fileList = fileList;
		this.originPort = originPort;
	}

	public List<FileSearchResult> getFileList() {
		return fileList;
	}

	public String getOriginPort() {
		return originPort;
	}
}
