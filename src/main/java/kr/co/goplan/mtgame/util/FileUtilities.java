package kr.co.goplan.mtgame.util;

import kr.co.goplan.mtgame.config.ConfigProperties;
import kr.co.goplan.mtgame.constant.ContentsType;
import kr.co.goplan.mtgame.domain.board.Board;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.file.FileInfo;
import kr.co.goplan.mtgame.exception.FileException;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kr.co.goplan.mtgame.config.ConfigProperties.*;


@Component
public class FileUtilities {
    // Paths.get()으로 운영체제에 따라서 다른 파일구분자 처리
    //public final static String rootPath = Paths.get("D:", "DEVGO", "WebProjectSp", "mtgame", "src", "main", "resources", "templates", "upload").toString();
    public static String rootPath = Paths.get("D:",  "upload",  "mtgame").toString();

    public static String[] videoAllows;

    public static String[] imageAllows;

    public static String[] audioAllows;

    public static String[] pptAllows;

    public static void allowSet(String rootPath1 ,String videoAllow, String imageAllow,String audioAllow, String pptAllow ){
        rootPath = rootPath1;
        videoAllows = videoAllow.split(",");
        imageAllows = imageAllow.split(",");
        audioAllows = audioAllow.split(",");
        pptAllows = pptAllow.split(",");

    }
    /**
     *
     * @return
     * @throws Exception
     * 단일컨테츠 파일 생성 일 경우 FileInfo Entity 형태로 파싱
     */
    public static FileInfo parseFileInfo(MultipartFile multipartFile) throws Exception {

        // 파일 업로드 경로 생성
        //String savePath = Paths.get(rootPath, "files").toString();
        String savePath = Paths.get(rootPath).toString();

        if (!new File(savePath).exists())
        {
            try {
                new File(savePath).mkdir();
            }
            catch (Exception e)
            { e.getStackTrace();
            }
        }

            String origFilename = multipartFile.getOriginalFilename();
            String filename = MD5Generator(FilenameUtils.getBaseName(origFilename)).toString();
            String extension = FilenameUtils.getExtension(origFilename);
            String filePath = Paths.get(savePath, filename).toString();

            //FileInfo FileInfo = new FileInfo(content, multipartFile.getOriginalFilename(), filename, filePath, multipartFile.getSize());
            FileInfo fileInfo = new FileInfo( multipartFile.getOriginalFilename(), filename, filePath, multipartFile.getSize(),extension,savePath );
            fileInfo.setContentType(checkContentType(multipartFile));

            try {
                File file = new File(filePath);
                multipartFile.transferTo(file);
                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            } catch (IOException e) {
                throw new FileException("[" + multipartFile.getOriginalFilename() + "] failed to save file...");
            } catch (Exception e) {
                throw new FileException("[" + multipartFile.getOriginalFilename() + "] failed to save file...");
            }
        //}
        return fileInfo;
    }
    /**
     *
     * @param multipartFiles
     * @return
     * @throws Exception
     * 멀티컨테츠 파일 생성 일 경우 FileInfo Entity 형태로 파싱
     */
    public static List<FileInfo> parseFileInfo(List<MultipartFile> multipartFiles) throws Exception {
        // 파일이 첨부되지 않았을 경우
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return Collections.emptyList();
        }

        // 파일 업로드 경로 생성
        String savePath = Paths.get(rootPath).toString();
        if (!new File(savePath).exists())
        {
            try {
                new File(savePath).mkdir();
            }
            catch (Exception e)
            { e.getStackTrace();
            }
        }

        List<FileInfo> fileList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String origFilename = multipartFile.getOriginalFilename();
            if (origFilename == null || "".equals(origFilename))
                continue;
            String filename = MD5Generator(FilenameUtils.getBaseName(origFilename)).toString();
            String extension = FilenameUtils.getExtension(origFilename);
            String filePath = Paths.get(savePath, filename).toString();
            //FileInfo FileInfo = new FileInfo(content, multipartFile.getOriginalFilename(), filename, filePath, multipartFile.getSize());
            FileInfo fileInfo = new FileInfo( multipartFile.getOriginalFilename(), filename, filePath, multipartFile.getSize(),extension ,savePath);
            fileInfo.setContentType(checkContentType(multipartFile));
            fileList.add(fileInfo);

            try {
                File file = new File(filePath);
                multipartFile.transferTo(file);
                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            } catch (IOException e) {
                throw new FileException("[" + multipartFile.getOriginalFilename() + "] failed to save file...");
            } catch (Exception e) {
                throw new FileException("[" + multipartFile.getOriginalFilename() + "] failed to save file...");
            }
        }
        return fileList;
    }
    /**
     *
     * @param multipartFiles 컨텐츠일경우
     * @param content
     * @return
     * @throws Exception
     */
    public static List<FileInfo> parseFileInfo(List<MultipartFile> multipartFiles, Contents content) throws Exception {
        // 파일이 첨부되지 않았을 경우
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return Collections.emptyList();
        }

        // 파일 업로드 경로 생성
        String savePath = Paths.get(rootPath).toString();
        if (!new File(savePath).exists())
        {
            try {
                new File(savePath).mkdir();
            }
            catch (Exception e)
            { e.getStackTrace();
            }
        }

        List<FileInfo> fileList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String origFilename = multipartFile.getOriginalFilename();
            if (origFilename == null || "".equals(origFilename))
                continue;
            String filename = MD5Generator(FilenameUtils.getBaseName(origFilename)).toString();
            String extension = FilenameUtils.getExtension(origFilename);
            String filePath = Paths.get(savePath, filename).toString();
            //FileInfo FileInfo = new FileInfo(content, multipartFile.getOriginalFilename(), filename, filePath, multipartFile.getSize());
            FileInfo fileInfo = new FileInfo( multipartFile.getOriginalFilename(), filename, filePath, multipartFile.getSize(),extension ,savePath);
            fileInfo.setContentType(checkContentType(multipartFile));
            fileList.add(fileInfo);

            try {
                File file = new File(filePath);
                multipartFile.transferTo(file);
                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            } catch (IOException e) {
                throw new FileException("[" + multipartFile.getOriginalFilename() + "] failed to save file...");
            } catch (Exception e) {
                throw new FileException("[" + multipartFile.getOriginalFilename() + "] failed to save file...");
            }
        }
        return fileList;
    }
    /**
     *
     * @param multipartFiles 보드일경우
     * @param board
     * @return
     * @throws Exception
     */
    public static List<FileInfo> parseFileInfo(List<MultipartFile> multipartFiles, Board board) throws Exception {
        // 파일이 첨부되지 않았을 경우
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return Collections.emptyList();
        }

        // 파일 업로드 경로 생성
        String savePath = Paths.get(rootPath).toString();
        if (!new File(savePath).exists())
        {
            try {
                new File(savePath).mkdir();
            }
            catch (Exception e)
            { e.getStackTrace();
            }
        }

        List<FileInfo> fileList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String origFilename = multipartFile.getOriginalFilename();
            if (origFilename == null || "".equals(origFilename))
                continue;
            String filename = MD5Generator(FilenameUtils.getBaseName(origFilename)).toString();
            String extension = FilenameUtils.getExtension(origFilename);
            String filePath = Paths.get(savePath, filename).toString();
            //FileInfo FileInfo = new FileInfo(content, multipartFile.getOriginalFilename(), filename, filePath, multipartFile.getSize());
            FileInfo FileInfo = new FileInfo(board, multipartFile.getOriginalFilename(), filename, filePath, multipartFile.getSize(),extension,savePath, board.getBoardDivision());

            fileList.add(FileInfo);

            try {
                File file = new File(filePath);
                multipartFile.transferTo(file);
                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            } catch (IOException e) {
                throw new FileException("[" + multipartFile.getOriginalFilename() + "] failed to save file...");
            } catch (Exception e) {
                throw new FileException("[" + multipartFile.getOriginalFilename() + "] failed to save file...");
            }
        }
        return fileList;
    }
    /** * 다운로드 받을 파일 생성 * * @param attach */
    public static File getDownloadFile(FileInfo attach) {
        return new File(Paths.get(rootPath).toString(), attach.getName());
    }

    /** * 파일명 중복 방지를 위해 MD5(128비트 암호화 해시 함수) 파일명 생성 * * @param input */
    public static String MD5Generator(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        MessageDigest mdMD5 = MessageDigest.getInstance("MD5");
        mdMD5.update(input.getBytes("UTF-8"));
        byte[] md5Hash = mdMD5.digest();
        StringBuilder hexMD5hash = new StringBuilder();
        for(byte b : md5Hash) { String hexString = String.format("%02x", b);
            hexMD5hash.append(hexString);
        }
        return hexMD5hash.toString();
    }
    /**
     * MediaType 생성
     * @param filename
     */
    public static MediaType getMediaType(String filename) {
        String contentType = FilenameUtils.getExtension(filename);
        MediaType mediaType = null;
        if (contentType.equals("png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (contentType.equals("jpeg") || contentType.equals("jpg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (contentType.equals("gif")) {
            mediaType = MediaType.IMAGE_GIF;
        } else if (contentType.equals("mp4") || contentType.equals("wmv")|| contentType.equals("avi")) {
            mediaType = MediaType.parseMediaType("video/mp4");
        } else if (contentType.equals("zip")) {
            mediaType = MediaType.parseMediaType("application/zip");
        } else if (contentType.equals("pdf")) {
            mediaType = MediaType.parseMediaType("application/pdf");
        } else if (contentType.equals("ppt") || contentType.equals("pptx")) {
            mediaType = MediaType.parseMediaType("application/vnd.ms-powerpoint");
        } else if (contentType.equals("doc")) {
            mediaType = MediaType.parseMediaType("application/msword");
        }
        return mediaType;
    }
    public static ContentsType checkContentType(MultipartFile file) {

        ContentsType contentsType = getContentsTypeByExtension(getFileExtension(file.getOriginalFilename()));

        return contentsType;
    }
    public static ContentsType getContentsTypeByExtension(String extension) {

        if(isVideo(extension)) {
            return ContentsType.Video;
        }

        if(isImage(extension)) {
            return ContentsType.Image;
        }

        if(isAudio(extension)) {
            return ContentsType.Audio;
        }

        if(isPpt(extension)) {
            return ContentsType.PPT;
        }

        return ContentsType.ETC;
    }
    public static String getFileExtension(FileInfo fileInfo) {
        return getFileExtension(fileInfo.getName());
    }

    public static String getFileExtension(String fileName) {
        String extension = "";

        if (fileName != null && !fileName.isEmpty()) {

            int i = fileName.lastIndexOf('.');
            int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

            if (i > p) {
                extension = fileName.substring(i + 1);
            }
        }

        return extension;
    }
    private static Boolean isVideo(String extension) {
        ConfigProperties configProperties = new ConfigProperties();

        return isMatchedWithStringArray(videoAllows, extension);
    }

    private static Boolean isImage(String extension) {
        return isMatchedWithStringArray(imageAllows, extension);
    }

    private static Boolean isAudio(String extension) {
        return isMatchedWithStringArray(audioAllows, extension);
    }

    private static Boolean isPpt(String extension) {
        return isMatchedWithStringArray(pptAllows, extension);
    }

    private static Boolean isMatchedWithStringArray(String[] allowed, String extension) {

        for(String ext : allowed) {
            if(extension.equalsIgnoreCase(ext.trim())){
                return true;
            }
        }

        return false;
    }
}
