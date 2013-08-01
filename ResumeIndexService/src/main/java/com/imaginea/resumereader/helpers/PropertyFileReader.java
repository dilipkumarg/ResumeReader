package com.imaginea.resumereader.helpers;

import java.io.*;
import java.text.ParseException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;

public class PropertyFileReader {
    private final Properties propResumeDir, propTimeStamp;
    private final String FILE_NAME = "config.properties";
    private final String TIME_STAMP_NAME = "timestamp.properties";
    private final String RESUME_DIR_PATH = "resumeDir";
    private final String LAST_TIME_STAMP = "lastTimeStamp";
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    public PropertyFileReader() throws IOException {
        propResumeDir = new Properties();
        propTimeStamp = new Properties();
        try {
            propResumeDir.load(new FileInputStream(FILE_NAME));
        } catch (FileNotFoundException fne) {
            LOGGER.log(Level.WARNING,
                    "Properties File not exists, creating new propResumeDir file with default values");
            this.setResumeDirPath("Resumes");
        }
    }

    /**
     * returns index directory path
     *
     * @return indexDirPath
     * @throws FileDirectoryEmptyException
     */
    public String getIndexDirPath() throws FileDirectoryEmptyException {
        return getResumeDirPath() + File.separator + ".index";
    }

    public void setResumeDirPath(String fileDir) throws IOException {
        propResumeDir.setProperty(RESUME_DIR_PATH, fileDir);
        propResumeDir.store(new FileOutputStream(FILE_NAME),
                "Resume Directory updated");
    }

    /**
     * returns file directory path
     *
     * @return fileDirPath
     * @throws FileDirectoryEmptyException
     */

    public String getResumeDirPath() throws FileDirectoryEmptyException {
        String fileDirPath = propResumeDir.getProperty(RESUME_DIR_PATH).trim();
        if (fileDirPath.isEmpty()) {
            throw new FileDirectoryEmptyException(
                    "File Directory Path is Empty");
        }
        return fileDirPath;
    }

    private String getTImeStampFilePath() throws FileDirectoryEmptyException {
        return getIndexDirPath() + File.separator + TIME_STAMP_NAME;
    }

    public void setLastTimeStamp(long millSeconds)
            throws IOException, FileDirectoryEmptyException {
        File indexDirectory = new File(getIndexDirPath());
        if (!indexDirectory.exists()) {
            indexDirectory.mkdirs();
        }
        propTimeStamp.setProperty(LAST_TIME_STAMP, Long.toString(millSeconds));
        propTimeStamp.store(new FileOutputStream(getTImeStampFilePath()),
                "Last TimeStamp updated");

    }

    /**
     * returns last time stamp
     *
     * @return prevTimeStamp
     * @throws ParseException
     */
    public long getLastTimeStamp() throws FileDirectoryEmptyException, IOException {
        try {
            propTimeStamp.load(new FileInputStream(this.getTImeStampFilePath()));
        } catch (FileNotFoundException fne) {
            LOGGER.log(Level.INFO, "Time Stamp File not exists on the current resume directory. Creating default time stamp file");
            this.setLastTimeStamp(0);
        }
        String dateString = propTimeStamp.getProperty(LAST_TIME_STAMP);
        return Long.parseLong(dateString, 10);
    }
}