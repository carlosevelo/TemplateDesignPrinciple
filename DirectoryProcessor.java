import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class DirectoryProcessor {
    private String _directory;
    private boolean _recurse;
    private Matcher _fileMatcher;

    public DirectoryProcessor (String directory, String pattern, boolean recurse) throws IOException {
        _directory = directory;
        _fileMatcher = Pattern.compile(pattern).matcher("");
        _recurse = recurse;
    }

    public void doIt() throws IOException {
        processDirectory(new File(_directory));
    }

    private void processDirectory(File dir) throws IOException {
        if (dir.isDirectory())
        {
            if (dir.canRead())
            {
                for (File file : dir.listFiles()) {
                    if (file.isFile()) {
                        if (file.canRead()) {
                            processFile(file);
                        }
                        else {
                            System.out.println("File " + file + " is unreadable");
                        }
                    }
                }

                if (_recurse) {
                    for (File file : dir.listFiles()) {
                        if (file.isDirectory()) {
                            processFile(file);
                        }
                    }
                }
            }
            else {
                System.out.println("Directory " + dir + " is unreadable");
            }
        }
        else {
            System.out.println(dir + " is not a directory");
        }
    }

    String getFileName(File file) {
        try {
            return file.getCanonicalPath();
        }
        catch (IOException e) {
            return "";
        }
    }

    void processFile(File file) throws IOException {
        String filename = getFileName(file);
        _fileMatcher.reset(filename);

        if (_fileMatcher.find()) {
            processFileContents(file);

        }
    }

    abstract void processFileContents(File file) throws IOException;
    abstract String results();
}
