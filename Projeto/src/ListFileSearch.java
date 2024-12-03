import java.io.Serializable;
import java.util.List;

public class ListFileSearch implements Serializable{
	private static List<FileSearchResult> fileList;
	
	public ListFileSearch(List<FileSearchResult> fileList) {
		this.fileList = fileList;
	}

	public static List<FileSearchResult> getFileList() {
		return fileList;
	}
	
	
}
