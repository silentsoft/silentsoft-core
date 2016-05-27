package org.silentsoft.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.silentsoft.core.CommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	/**
     * The character set. UTF-8 works good for windows, mac and Umlaute.
     */
    private static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * Reads the specified file and returns the content as a String.
     * 
     * @param file
     * @return
     * @throws IOException thrown if an I/O error occurs opening the file
     */
    public static String readFile(File file) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        BufferedReader reader = Files.newBufferedReader(file.toPath(), CHARSET);

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }

        reader.close();

        return stringBuffer.toString();
    }

    /**
     * Saves the content String to the specified file.
     * 
     * @param content
     * @param file
     * @throws IOException thrown if an I/O error occurs opening or creating the file
     */
    public static void saveFile(String content, File file) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CHARSET);
        writer.write(content, 0, content.length());
        writer.close();
    }
    
    /**
     * Appends the content String to the specified file with new line.
     * @param content
     * @param file
     * @throws IOException thrown if an I/O error occurs appending or creating the file
     */
    public static void appendFile(String content, File file) throws IOException {
    	appendFile(content, file, true);
    }
    
    /**
     * Appends the content String to the specified file with new line option.
     * @param content
     * @param file
     * @param withNewLine
     * @throws IOException
     */
    public static void appendFile(String content, File file, boolean withNewLine) throws IOException {
    	if (withNewLine) {
    		if (file.length() > 0) {
    			content = String.format("%s%s", CommonConst.ENTER, content);
    		}
    	}
    	
    	FileUtils.writeStringToFile(file, content, true);
    }
    
    public static boolean isExists(String fileName) {
    	return new File(fileName).exists();
    }
    
    public static String getName(String fileName) {
    	int lastIndexOfSeparator = fileName.lastIndexOf(File.separator);
    	int lastIndexOfDot = fileName.lastIndexOf(CommonConst.DOT);
    	
    	lastIndexOfDot = (lastIndexOfDot > lastIndexOfSeparator) ? lastIndexOfDot : fileName.length();
    	
    	return fileName.substring(lastIndexOfSeparator+1, lastIndexOfDot);
    }
    
    public static String getExtension(String fileName) {
    	int lastIndexOfSeparator = fileName.lastIndexOf(File.separator);
    	int lastIndexOfDot = fileName.lastIndexOf(CommonConst.DOT);
    	
    	if (lastIndexOfDot != -1 && lastIndexOfDot > lastIndexOfSeparator) {
    		return fileName.substring(fileName.lastIndexOf(CommonConst.DOT)+1, fileName.length());
    	}
    	
    	return "";
    }
    
    /**
     * Count file's total line number.
     * If exception thrown, return -1. 
     * 
     * @param fileName
     * @return
     */
	public static int getTotalLineNumber(String fileName) {
    	int totalLineNumber = -1;
    	
    	LineNumberReader lineNumberReader = null;
    	try {
    		lineNumberReader = new LineNumberReader(new FileReader(fileName));
    		lineNumberReader.skip(Long.MAX_VALUE);
    		totalLineNumber = lineNumberReader.getLineNumber() + 1;
    	} catch (Exception e) {
    		LOGGER.error(e.toString());
    	} finally {
    		if (lineNumberReader != null) {
    			try {
					lineNumberReader.close();
				} catch (IOException e) {
					LOGGER.error(e.toString());
				}
    		}
    	}
    	
    	return totalLineNumber;
    }
	
	/**
	 * Reading lines from start of file to down direction.
	 * @param fileName
	 * @param lines must higher then 0
	 * @return
	 */
	public static String getContentsByLineNumberFromSOF(String fileName, int lines) {
		return getContentsByLineNumber(fileName, 1, lines);
	}
	
	/**
	 * Reading lines from end of file to up direction.
	 * @param fileName
	 * @param lines must higher then 0
	 * @return
	 */
	public static String getContentsByLineNumberFromEOF(String fileName, int lines) {
		int totalLineNumber = getTotalLineNumber(fileName);
		
		return getContentsByLineNumber(fileName, totalLineNumber - lines + 1, totalLineNumber);
	}
	
	/**
	 * Contents reading by line between start and end.
	 * @param fileName
	 * @param start line must higher then 0
	 * @param end
	 * @return
	 */
	public static String getContentsByLineNumber(String fileName, int start, int end) {
		StringBuffer stringBuffer = new StringBuffer();
		
		if (start <= end) {
			LineNumberReader lineNumberReader = null;
			try {
				lineNumberReader = new LineNumberReader(new FileReader(fileName));
				
				for (String line = null; (line = lineNumberReader.readLine()) != null;) {
					int lineNumber = lineNumberReader.getLineNumber();
					if (lineNumber >= start && lineNumber <= end) {
						stringBuffer.append(line);
					} else if (lineNumber > end) {
						break;
					}
				}
			} catch (Exception e) {
	    		LOGGER.error(e.toString());
	    	} finally {
	    		if (lineNumberReader != null) {
	    			try {
						lineNumberReader.close();
					} catch (IOException e) {
						LOGGER.error(e.toString());
					}
	    		}
	    	}
		}
		
		return stringBuffer.toString();
	}
		
	public static boolean clean(File file, Function<File, Boolean> function) {
		return clean(file, function, true);
	}
	
	public static boolean clean(File file, Function<File, Boolean> function, boolean skipIfDenied) {
		boolean result = true;
		
		try {
			Files.walkFileTree(Paths.get(file.getPath()), new FileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dirPath, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
					File visitedFile = filePath.toFile();
					if (function.apply(visitedFile)) {
						try {
							visitedFile.delete();
						} catch (Exception e) {
							if (skipIfDenied == false) {
								throw e;
							}
						}
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path filePath, IOException exc) throws IOException {
					if (skipIfDenied) {
						return FileVisitResult.CONTINUE;
					}
					
					return FileVisitResult.TERMINATE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dirPath, IOException exc) throws IOException {
					File visitedDirectory = dirPath.toFile();
					if (visitedDirectory.list().length == 0) {
						visitedDirectory.delete();
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (Exception e) {
			LOGGER.error("", e);
			
			result = false;
		}
		
		return result;
	}
}
