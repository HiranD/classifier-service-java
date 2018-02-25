package OpenNLPApproach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassifierDataUtil {
	private static final Logger log = LoggerFactory.getLogger(ClassifierDataUtil.class);

	public static InputStream readClassifierSourceData(String trainingDataDir) throws IOException {
		Collection<String> fileNames = FileUtil.getDirectories(trainingDataDir);
		ByteArrayOutputStream trainingBaos = new ByteArrayOutputStream();

		long lowestContentLength = Long.MAX_VALUE;

		// count lowest content length
		for (String discipline : fileNames) {
			Collection<String> soureLines = FileUtil.readFilesAsList(trainingDataDir + "/" + discipline);
			long contentLength = 0;

			for (String line : soureLines) {
				contentLength += line.length();
			}

			if (contentLength < lowestContentLength) {
				lowestContentLength = contentLength;
			}
		}

		log.info("###      Max Content Size: " + lowestContentLength + "      ###");

		for (String discipline : fileNames) {
			log.info("Reading classifier source data for the discipline: " + discipline);
			Collection<String> soureLines = FileUtil.readFilesAsList(trainingDataDir + "/" + discipline);
			long contentLength = 0;
			Map<String, Integer> conceptCountMap = new HashMap<String, Integer>();

			for (String line : soureLines) {

				contentLength += line.length();
				line = line.replaceAll("\\w*\\d\\w*", "");

					if (line.indexOf(discipline) == 0) {
						trainingBaos.write((line + "\n").getBytes());
					} else {
						trainingBaos.write((discipline + " " + line + "\n").getBytes());
					}

				if (contentLength > lowestContentLength) {
					break;
				}
			}

			Map<String, Integer> sotedMap = MapUtil.sortByValueOrder(conceptCountMap, MapUtil.SORT_ORDER_DESC);
		}

		InputStream is = new ByteArrayInputStream(trainingBaos.toByteArray());
		return is;
	}
}
