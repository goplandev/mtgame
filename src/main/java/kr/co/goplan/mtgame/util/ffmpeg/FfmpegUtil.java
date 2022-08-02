package kr.co.goplan.mtgame.util.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Provides helper methods for working with ffmpeg
 *
 * @author shaines
 */
public class FfmpegUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String ffmpegCommand;

    public FfmpegUtil(String ffmpegCommand) {
        this.ffmpegCommand = ffmpegCommand;
    }

    /**
     * Helper method that executes ffmpeg against the specified file, parses the
     * results, and returns a map that contains key metrics about the file
     *
     * @param filename
     *            The name of the file to query
     *
     * @return A map that contains interesting fields
     */
    protected Map<String, String> executeInquiry(String filename) {
        System.out.println("Execute Inquiry for file: " + filename);
        Map<String, String> fieldMap = new HashMap<String, String>();
        BufferedReader input = null;
        BufferedReader error = null;
        try {
            // Build the command line
            StringBuilder sb = new StringBuilder();
            sb.append(ffmpegCommand);
            sb.append(" -i ");
            sb.append(filename);

            // Execute the command
            System.out.println("Command line: " + sb);
            Process p = Runtime.getRuntime().exec(sb.toString());

            // Read the response
            input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // Parse the input stream
            String line = input.readLine();
            System.out.println("ffmpeg execution of: " + filename);
            while (line != null) {
                //System.out.println("\t***" + line);
                line = input.readLine();
            }


            // Parse the error stream
            line = error.readLine();
            //System.out.println("Error Stream: " + filename);
            while (line != null) {
                // Handle the line
                if (line.startsWith("FFmpeg version")) {
                    // Handle the version line:
                    // FFmpeg version 0.6.2-4:0.6.2-1ubuntu1, Copyright (c)
                    // 2000-2010 the Libav developers
                    String version = line.substring(15, line.indexOf(", Copyright", 16));
                    fieldMap.put("version", version);
                } else if (line.contains("Duration:")) {
                    // Handle Duration line:
                    // Duration: 00:42:53.59, start: 0.000000, bitrate: 1136
                    // kb/s
                    String duration = line.substring(line.indexOf("Duration: ") + 10, line.indexOf(", start:"));
                    fieldMap.put("duration", duration);

                    String bitrate = line.substring(line.indexOf("bitrate: ") + 9);
                    fieldMap.put("bitrate", bitrate);
                } else if((line.contains("Stream #")) && (line.contains("Video:")))  //
                {
                    String resolution = line;

                    String width = resolution.substring(0, resolution.lastIndexOf("x"));
                    String height = resolution.substring(resolution.lastIndexOf("x") + 1);
                    width = width.substring(width.lastIndexOf(" ")).trim();
                    height = height.substring(0, height.indexOf(" ")).trim();

                    fieldMap.put("width", width);
                    fieldMap.put("height", height.replace(",","")); //height에 , 가 붙어있어서 제거함. 2017.11.10
                }

                // Read the next line
                //System.out.println("\t***" + line);
                line = error.readLine();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if(null != input) {
                    input.close();
                }
            } catch(Exception e) {
            }

            try {
                if(null != error) {
                    error.close();
                }
            } catch(Exception e) {
            }
        }

        // Debug: dump fields:
        //		System.out.println("Fields:");
        //		for (String field : fieldMap.keySet()) {
        //			System.out.println("\t" + field + " = " + fieldMap.get(field));
        //		}

        return fieldMap;
    }

    /**
     * Returns a String containing the duration of the specified file
     *
     * @param filename
     *            The name of the file for which to retrieve the duration
     *
     * @return A String that contains the duration of the file
     */
    public Map<String, String> getResolution(String filename) {

        return executeInquiry(filename);
    }


    public String getDuration(String filename) {
        Map<String, String> fieldMap = executeInquiry(filename);
        if (fieldMap.containsKey("duration")) {
            return fieldMap.get("duration");
        }
        return "0:00:00";
    }

    public int getIntDuration(String filename) {

        String duration = getDuration(filename);

        String[] dur = duration.split(":");

        int hours = Integer.parseInt(dur[0]);
        int minutes = Integer.parseInt(dur[1]);
        int seconds = 0;

        int includingSeconds = 2;

        if(dur.length > includingSeconds) {
            seconds = (int)(Float.parseFloat(dur[2]));
        }

		/*
		int hours = Integer.parseInt(duration.substring(0, 2));
		int minutes = Integer.parseInt(duration.substring(3, 5));
		int seconds = Integer.parseInt(duration.substring(6, 8));
		 */

        return hours * 3600 + minutes * 60 + seconds;
    }

    /**
     * Generates thumbnails for the specified video file to the specified
     * directory
     *
     * Example taken from:
     * http://blog.prashanthellina.com/2008/03/29/creating-video
     * -thumbnails-using-ffmpeg/
     *
     * @param filename
     *            The name of the file from which to create the thumbnails
     * @param dir
     *            The directory to which to create the thumbnails
     * @param width
     *            The width of the thumbnail
     * @param height
     *            The height of the thumbnail
     * @param count
     *            The number of thumbnails to generate
     */
    public boolean generateThumbnails(String path, String filename, File dir, int width, int height, int count) {
        // The following example shows how to generate thumbnails at seconds 4,
        // 8, 12, and 16
        // ffmpeg -itsoffset -4 -i test.avi -vcodec mjpeg -vframes 1 -an -f
        // rawvideo -s 320x240 test.jpg
        // ffmpeg -itsoffset -8 -i test.avi -vcodec mjpeg -vframes 1 -an -f
        // rawvideo -s 320x240 test.jpg
        // ffmpeg -itsoffset -12 -i test.avi -vcodec mjpeg -vframes 1 -an -f
        // rawvideo -s 320x240 test.jpg
        // ffmpeg -itsoffset -16 -i test.avi -vcodec mjpeg -vframes 1 -an -f
        // rawvideo -s 320x240 test.jpg

        String fullname = path + filename;

        // Have to compute seconds, format is: 00:42:53.59
        //String duration = getDuration(fullname);
        //int hours = Integer.parseInt(duration.substring(0, 2));
        //int minutes = Integer.parseInt(duration.substring(3, 5));
        //int seconds = Integer.parseInt(duration.substring(6, 8));
        int totalSeconds = getIntDuration(fullname);
        //System.out.println("Total Seconds: " + totalSeconds);

        // Create the thumbnails
        String shortFilename = filename;
        if (filename.indexOf(File.separator) != -1) {
            // Strip the path
            shortFilename = filename.substring(filename.lastIndexOf(File.separator) + 1);
        }
        if (shortFilename.indexOf(".") != -1) {
            // Strip extension
            shortFilename = shortFilename.substring(0, shortFilename.lastIndexOf("."));
        }

        // Define a shift in seconds
        int shift = 4;

        // The step is the number of seconds to step between thumbnails
        int step = totalSeconds / count;

        ExecutorService exec = Executors.newSingleThreadExecutor();

        for (int index = 0; index < count; index++) {
            // Build the command
            String nextStep = Integer.toString(shift + (index * step));

            int intNextStep = Integer.parseInt(nextStep);

            if(intNextStep > totalSeconds)
            {
                int diff = intNextStep - totalSeconds;
                nextStep = Integer.toString(totalSeconds - diff);

            }else if(intNextStep == totalSeconds)
            {
                nextStep = Integer.toString(totalSeconds - 1);
            }

//			System.out.println("totalSeconds : " +totalSeconds);
//
//			System.out.println("nextStep : " + nextStep);

            List<String> command = new ArrayList<String>();
            command.add(ffmpegCommand);
            command.add("-ss");
            command.add(nextStep);
//			command.add(Integer.toString(shift + (index * step)));
            command.add("-i");
            command.add(fullname);
            command.add("-vcodec");
            command.add("mjpeg");
            command.add("-vframes");
            command.add("2");
            command.add("-an");
            command.add("-f");
            command.add("rawvideo");
            command.add("-threads");
            command.add("4");
            command.add("-vf");
            command.add("scale=640:-1");
            command.add(dir.getAbsolutePath() + File.separator + shortFilename + getFileSubNameByIndex(index + 1));

            Future<Boolean> future = exec.submit(new CommandExecuter(command));

            try {
                if(!future.get(10, TimeUnit.SECONDS)){
                    //logger.error("thumbnail capture failed");
                    exec.shutdown();
                    return false;
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                //logger.error(e.getMessage());
                exec.shutdown();
                return false;
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                //logger.error(e.getMessage());
                exec.shutdown();
                return false;
            } catch (TimeoutException e) {
                // TODO Auto-generated catch block
                //logger.error(e.getMessage());
                exec.shutdown();
                return false;
            }
        }

        exec.shutdown();

        return true;
    }

    private String getFileSubNameByIndex(int i) {

        String result = "";

        int twoDigit = 10;
        int threeDigit = 100;

        if(i < twoDigit) {
            result  = "00" + i;
        }
        else if (twoDigit <= i && i < threeDigit) {
            result = "0" + i;
        }
        else {
            result = String.valueOf(i);
        }

        return result;
    }

    public String getFfmpegCommand() {
        return ffmpegCommand;
    }

    public void setFfmpegCommand(String ffmpegCommand) {
        this.ffmpegCommand = ffmpegCommand;
    }

    private class CommandExecuter implements Callable<Boolean>{
        private final List<String> command;

        public CommandExecuter(List<String> command) {
            // TODO Auto-generated constructor stub
            this.command = command;
        }

        @Override
        public Boolean call() throws Exception {
            // TODO Auto-generated method stub
            Process p = null;

            try {
                // Execute the command
                ProcessBuilder pb = new ProcessBuilder();
                pb.command(command);

                pb.redirectErrorStream(true);
                p = pb.start();

                // Detach from the process
                p.getOutputStream().close();

                exhaustInputStream(p.getInputStream());
                //exhaustInputStream(p.getErrorStream());
                return true;

            } catch (Exception e) {
                //logger.error(e.getMessage());
                return false;
            }
        }
    }

    private void exhaustInputStream(final InputStream is) {

        try{
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(is));
            String output = bufReader.readLine();
            while(output != null){
                output = bufReader.readLine();
            }
            bufReader.close();
            is.close();
        } catch (IOException e) {
            //logger.error(e.getMessage());
        }
    }
}
