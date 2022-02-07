import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LineCount extends DirectoryProcessor {
	private int _totalLineCount;

	public LineCount(String directory, String pattern, boolean recurse) throws IOException {
		super(directory, pattern, recurse);
		_totalLineCount = 0;
	}

	public void processFileContents(File file) {
		try {
			Reader reader = new BufferedReader(new FileReader(file));
			int curLineCount = 0;
			try {
				curLineCount = 0;
				Scanner input = new Scanner(reader);
				while (input.hasNextLine()) {
					String line = input.nextLine();
					++curLineCount;
					++_totalLineCount;
				}
			}
			finally {
				System.out.println(curLineCount + "  " + file);
				reader.close();
			}
		}
		catch (IOException e) {
			System.out.println("File " + file + " is unreadable");
		}
	}

	public String results() {
		return "TOTAL: " + _totalLineCount;
	}
	
	public static void main(String[] args) throws IOException {
		String directory = "";
		String pattern = "";
		boolean recurse = false;
		
		if (args.length == 2) {
			recurse = false;
			directory = args[0];
			pattern = args[1];
		}
		else if (args.length == 3 && args[0].equals("-r")) {
			recurse = true;
			directory = args[1];
			pattern = args[2];
		}
		else {
			usage();
			return;
		}
		
		LineCount lineCounter = new LineCount(directory, pattern, recurse);
		lineCounter.doIt();
		lineCounter.results();
	}
	
	private static void usage() {
		System.out.println("USAGE: java LineCount {-r} <dir> <file-pattern>");
	}

}
