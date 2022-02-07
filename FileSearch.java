import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileSearch extends DirectoryProcessor {
	private Matcher _searchMatcher;
	private int _totalMatches;
	
	public FileSearch(String dirName, String filePattern, String searchPattern, boolean recurse) throws IOException {
		super(dirName, filePattern, recurse);
		_searchMatcher = Pattern.compile(searchPattern).matcher("");
		_totalMatches = 0;

	}
	
	public void processFileContents(File file) {
		try {
			int curMatches = 0;

			InputStream data = new BufferedInputStream(new FileInputStream(file));
			try {
				Scanner input = new Scanner(data);
				while (input.hasNextLine()) {
					String line = input.nextLine();

					_searchMatcher.reset(line);
					if (_searchMatcher.find()) {
						if (++curMatches == 1) {
							System.out.println("");
							System.out.println("FILE: " + file);
						}

						System.out.println(line);
						++_totalMatches;
					}
				}
			}
			finally {
				data.close();

				if (curMatches > 0) {
					System.out.println("MATCHES: " + curMatches);
				}
			}
		}
		catch (IOException e) {
			System.out.println("File " + file + " is unreadable");
		}
	}

	public String results() {
		return "TOTAL MATCHES: " + _totalMatches;
	}
	
	public static void main(String[] args) throws IOException {
		
		String dirName = "";
		String filePattern = "";
		String searchPattern = "";
		boolean recurse = false;
		
		if (args.length == 3) {
			recurse = false;
			dirName = args[0];
			filePattern = args[1];
			searchPattern = args[2];
		}
		else if (args.length == 4 && args[0].equals("-r")) {
			recurse = true;
			dirName = args[1];
			filePattern = args[2];
			searchPattern = args[3];
		}
		else {
			usage();
			return;
		}
		
		FileSearch fileSearch = new FileSearch(dirName, filePattern, searchPattern, recurse);
		fileSearch.doIt();
		fileSearch.results();
	}
	
	private static void usage() {
		System.out.println("USAGE: java FileSearch {-r} <dir> <file-pattern> <search-pattern>");
	}

}
