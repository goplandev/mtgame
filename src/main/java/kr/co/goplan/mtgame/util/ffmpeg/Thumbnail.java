package kr.co.goplan.mtgame.util.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

public class Thumbnail {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean makeCaptures(String ffmpegInstallFolder, String videoFileFolderStr, String videoFileNameStr) throws Exception {
        return makeCaptures(ffmpegInstallFolder, videoFileFolderStr, videoFileNameStr, 9);
    }

    public boolean makeCaptures(String ffmpegInstallFolder, String videoFileFolderStr, String videoFileNameStr, int captureCount) throws Exception {

        try {

            FfmpegUtil ffmpeg = new FfmpegUtil( ffmpegInstallFolder + "ffmpeg" );

            return ffmpeg.generateThumbnails(videoFileFolderStr, videoFileNameStr, new File( ffmpegInstallFolder ), 480, 480, captureCount);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    public int getPlayTime(String ffmpegInstallFolder, String videoFileFolderStr, String videoFileNameStr) throws Exception {

        try {

            String videoFullPath = videoFileFolderStr + videoFileNameStr;

            FfmpegUtil ffmpeg = new FfmpegUtil( ffmpegInstallFolder + "ffmpeg" );

            return ffmpeg.getIntDuration(videoFullPath);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }

    }

    public Map<String, String> getVideoResolution(String ffmpegInstallFolder, String videoFileFolderStr, String videoFileNameStr) throws Exception {

        try {

            String videoFullPath = videoFileFolderStr + videoFileNameStr;

            FfmpegUtil ffmpeg = new FfmpegUtil( ffmpegInstallFolder + "ffmpeg" );

            return ffmpeg.getResolution(videoFullPath);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }

    }

    public boolean isNumber( String input )
    {
        try
        {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e)
        {
            return false;
        }
    }
}
