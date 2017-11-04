package OpenNLPApproach;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileUtil {

	private enum StringCase {
		ALL, LOWERCASE, UPPCASE
	}

	public static BufferedReader loadFileForRead(String fileName) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		return br;

	}

	public static boolean isFileOrDirectoryExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	public static Collection<String> getFiles(String folderName) {
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();
		List<String> fileNames = new ArrayList<String>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				fileNames.add(listOfFiles[i].getName());
			}
		}

		Collections.sort(fileNames);
		return fileNames;
	}

	public static Collection<String> getDirectories(String folderName) {
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();
		List<String> fileNames = new ArrayList<String>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				fileNames.add(listOfFiles[i].getName());
			}
		}

		Collections.sort(fileNames);
		return fileNames;
	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static String readFile(String path) {
		try {
			return readFile(path, Charset.defaultCharset());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<String> readFileAsList(String fileName, StringCase stringCase) {
		List<String> col = new ArrayList<>();
		BufferedReader br = loadFileForRead(fileName);
		String line;

		try {
			while ((line = br.readLine()) != null) {
				if (stringCase == StringCase.LOWERCASE) {
					line = new String(line.toLowerCase());
				} else if (stringCase == StringCase.UPPCASE) {
					line = new String(line.toUpperCase());
				}

				col.add(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return col;
	}

	public static List<String> readFilesAsList(String DataDir) {
		List<String> content = new ArrayList<>();
		Collection<String> fileNames = getFiles(DataDir);
		for (String file : fileNames){
			content.addAll(readFileAsList(DataDir + "/"+ file, StringCase.ALL));
		}
		return content;
	}

	public static List<String> readFileAsList(String fileName) {
		return readFileAsList(fileName, StringCase.ALL);
	}

	public static Collection<String> readFileAsListLowerCase(String fileName) {
		return readFileAsList(fileName, StringCase.LOWERCASE);
	}
}