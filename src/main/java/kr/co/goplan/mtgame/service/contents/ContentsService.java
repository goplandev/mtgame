package kr.co.goplan.mtgame.service.contents;


import kr.co.goplan.mtgame.config.ConfigProperties;
import kr.co.goplan.mtgame.constant.ContentsType;
import kr.co.goplan.mtgame.domain.contents.Contents;
import kr.co.goplan.mtgame.domain.contents.ContentsDto;
import kr.co.goplan.mtgame.domain.file.FileInfo;
import kr.co.goplan.mtgame.repository.contents.ContentsRepository;
import kr.co.goplan.mtgame.repository.file.FileInfoRepository;
import kr.co.goplan.mtgame.util.FileUtilities;
import kr.co.goplan.mtgame.util.ImageUtil;
import kr.co.goplan.mtgame.util.ffmpeg.Thumbnail;
import kr.co.goplan.mtgame.util.paging.Paged;
import kr.co.goplan.mtgame.util.paging.Paging;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
@Setter
@Transactional(readOnly = true)
public class ContentsService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ContentsRepository contentsRepository;
    private final FileInfoRepository fileInfoRepository;

    @Value("#{config['ffmpeg.installFolder']}")
    private String ffmpegInstallFolder;// = "D:/DEVGO/ffmpeg-4.3.2/";

    @Value("#{config['upload.dir']}")
    private String uploadDir;// = "D:/upload/mtgame/";

    @Value("#{config['video.allow']}")
    private String videoAllow;
    @Value("#{config['image.allow']}")
    private String imageAllow;
    @Value("#{config['audio.allow']}")
    private String audioAllow;
    @Value("#{config['ppt.allow']}")
    private String pptAllow;

    public ContentsDto searchById(Long id){
        Contents contents = contentsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("게시물이 존제하지 않습니다."));
        return new ContentsDto(contents);
    }
    public Contents searchByIdContent(Long id){
        Contents contents = contentsRepository.findById(id).get();
        return contents;
    }
    public FileInfo searchByIdFile(Long id){
        FileInfo fileInfo = fileInfoRepository.findById(id).get();
        return fileInfo;
    }
    public List<ContentsDto> searchAllDesc(){
        return contentsRepository.findAllByOrderByIdDesc().stream()
                .map(ContentsDto::new)
                .collect(Collectors.toList());
    }
    public List<ContentsDto> findAllByOrderByIdDescType(ContentsType contentsType){
        return contentsRepository.findAllByOrderByIdDescType(contentsType).stream()
                .map(ContentsDto::new)
                .collect(Collectors.toList());
    }
    public List<ContentsDto> findAllByOrderByIdDescTypeFile(ContentsType contentsType){
        return contentsRepository.findAllByOrderByIdDescTypeFile(contentsType).stream()
                .map(ContentsDto::new)
                .collect(Collectors.toList());
    }
    public Paged<ContentsDto> findAllByOrderByIdDescPage(int pageNumber, int size ){
        PageRequest request = PageRequest.of(pageNumber -1 , size , Sort.Direction.DESC,"id");
        Page<ContentsDto> contentsPage = contentsRepository.findAllByOrderByIdDescPage(request);
        return new Paged<>(contentsPage, Paging.of(contentsPage.getTotalPages(),pageNumber,size));
    }
    public Paged<ContentsDto> findAllByOrderByIdDescTypeFilePage(int pageNumber, int size , ContentsType contentsType){
        PageRequest request = PageRequest.of(pageNumber -1 , size , Sort.Direction.DESC,"id");
        Page<ContentsDto> contentsPage = contentsRepository.findAllByOrderByIdDescTypeFilePage(contentsType,request);
        return new Paged<>(contentsPage, Paging.of(contentsPage.getTotalPages(),pageNumber,size));
    }
    @Transactional
    public void delete(Long id){
        Contents contents = contentsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("게시물이 존제하지 않습니다."));
        contentsRepository.delete(contents);
    }

    @Transactional
    public Long save(Contents contents , List<MultipartFile> files , List<Long> deleteFileList) throws Exception{
        FileUtilities.allowSet(uploadDir,videoAllow,imageAllow,audioAllow,pptAllow);
        Contents saveContents = contentsRepository.save(contents);

        List<FileInfo> fileInfoList = FileUtilities.parseFileInfo(files, saveContents);

        // 파일이 존재할 경우
        if (!fileInfoList.isEmpty()) {
            for (FileInfo fileInfo : fileInfoList) {
                //파일 저장
                fileInfo.setContents(saveContents);
                fileInfoRepository.save(fileInfo);
            }
        }
        // 삭제할 파일이 존재할 경우
        if (!deleteFileList.isEmpty()) {
            fileInfoRepository.deleteByAttachIdList(deleteFileList);
        }
        return saveContents.getId();
    }
    @Transactional
    public Long savefileSeq(Contents contents , List<MultipartFile> files , List<Long> deleteFileList) throws Exception{
        FileUtilities.allowSet(uploadDir,videoAllow,imageAllow,audioAllow,pptAllow);
        List<FileInfo> fileInfoList = FileUtilities.parseFileInfo(files);
        Long upl = 0l;
        if(contents.getId() != null){
            //수정
            if (!fileInfoList.isEmpty()) {
                // 파일이 존재할 경우 기존파일 삭제처리
                FileInfo fileInfo_del = fileInfoRepository.getById(contents.getFileId());
                fileInfo_del.setIsDeleted(true);
                fileInfoRepository.save(fileInfo_del);
                //새로운 파일 등록 처리
                for (FileInfo fileInfo : fileInfoList) {
                    //파일 저장
                    fileInfoRepository.save(fileInfo);
                    //컨텐츠 타입 파일연결
                    contents.setFileInfo(fileInfo);
                    contents.setFileId(fileInfo.getId());
                    contents.setFileName(fileInfo.getOriginalFilename());
                    contents.setContentType(fileInfo.getContentType());
                    contents.setContentTypeStr(fileInfo.getExtension());
                    contentsRepository.save(contents);

                    if(fileInfo.getContentType().equals(ContentsType.Video)){
                        //비디오 파일 입니다. 섬네일 추출
                        Contents targetContents = makeCapture(contents);
                        String captureFile = targetContents.getFileInfo().getName() + "001";
                        Path source = Paths.get(uploadDir + "/" + captureFile);
                        File file = source.toFile();
                        FileInfo fileInfo1 = new FileInfo();

                        fileInfo1.setFileId(targetContents.getFileInfo().getId());
                        fileInfo1.setContents(contents);
                        fileInfo1.setExtension("png");
                        fileInfo1.setFileSize(file.length());
                        fileInfo1.setName(file.getName());
                        fileInfo1.setOriginalFilename(file.getName() + ".png");
                        fileInfo1.setServerFilePath(Paths.get(uploadDir).toString());
                        //fileInfo1.setRegisteredDatetime(targetContents.getRegisteredDatetime());
                        fileInfo1.setFilePath(Paths.get(uploadDir ,file.getName()).toString());
                        fileInfo1.setContentType(ContentsType.Thumbnail);
                        fileInfoRepository.save(fileInfo1);
                    }else if (fileInfo.getContentType().equals(ContentsType.PPT)){
                        // ppt 스토리캡처
                        makeStoryCaptureForPpt(contents);
                    }
                    upl++;
                }
            }else {
                //파일이 없을경우 업데이트
                contentsRepository.save(contents);
            }
            
        }else{
            //등록
            // 파일이 존재할 경우
            if (!fileInfoList.isEmpty()) {
                for (FileInfo fileInfo : fileInfoList) {
                    //컨텐츠 생성
                    Contents contents1 = new Contents(contents);

                    //파일 저장
                    fileInfoRepository.save(fileInfo);

                    //컨텐츠 타입 파일연결
                    contents1.setFileInfo(fileInfo);
                    contents1.setFileId(fileInfo.getId());
                    contents1.setFileName(fileInfo.getOriginalFilename());
                    contents1.setContentType(fileInfo.getContentType());
                    contents1.setContentTypeStr(fileInfo.getExtension());
                    contentsRepository.save(contents1);

                    if(fileInfo.getContentType().equals(ContentsType.Video)){
                        //비디오 파일 입니다. 섬네일 추출
                        Contents targetContents = makeCapture(contents1);
                        String captureFile = targetContents.getFileInfo().getName() + "001";
                        Path source = Paths.get(uploadDir + "/" + captureFile);
                        File file = source.toFile();
                        FileInfo fileInfo1 = new FileInfo();

                        fileInfo1.setFileId(targetContents.getFileInfo().getId());
                        fileInfo1.setContents(contents1);
                        fileInfo1.setExtension("png");
                        fileInfo1.setFileSize(file.length());
                        fileInfo1.setName(file.getName());
                        fileInfo1.setOriginalFilename(file.getName() + ".png");
                        fileInfo1.setServerFilePath(Paths.get(uploadDir).toString());
                        //fileInfo1.setRegisteredDatetime(targetContents.getRegisteredDatetime());
                        fileInfo1.setFilePath(Paths.get(uploadDir ,file.getName()).toString());
                        fileInfo1.setContentType(ContentsType.Thumbnail);
                        fileInfoRepository.save(fileInfo1);
                    }else if (fileInfo.getContentType().equals(ContentsType.PPT)){
                        // ppt 스토리캡처
                        makeStoryCaptureForPpt(contents1);
                    }

                    upl++;
                }
            }
        }

        return upl;
    }


    private Contents makeCapture(Contents contents) {
        Thumbnail thumbnail = new Thumbnail();

        try {
            if (thumbnail.makeCaptures(ffmpegInstallFolder, contents.getFileInfo().getServerFilePath()+"\\", contents.getFileInfo().getName(), 1)) {

                Path file = Paths.get(ffmpegInstallFolder + contents.getFileInfo().getName() + "001");

                //Path movePath = Paths.get(uploadDir + "/capture");
                Path movePath = Paths.get(contents.getFileInfo().getServerFilePath());

                Files.move(file , movePath .resolve(file .getFileName()));

                makeThumbnail(contents.getFileInfo());
            }
            else {
                logger.error("Can not make a preview file");
            }

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return contents;
    }
    private void makeThumbnail(FileInfo fileInfo) {
        try {

            File file = new File(fileInfo.getFilePath() + fileInfo.getFileId() + "001");
            String thumbFileId = "thumb_" + fileInfo.getFileId();

            BufferedImage in = ImageIO.read(file);
            BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = newImage.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();

            // Make Thumbnail
            BufferedImage resizeImage = ImageUtil.shrink(newImage, 145, 145);
            File fileThumb = new File(fileInfo.getServerFilePath() + thumbFileId);
            ImageIO.write(resizeImage, "png", fileThumb);

        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void makeStoryCaptureForPpt(Contents contents) {

        int width = 640;
        int height = 480;
        String format = "png";

        if (contents == null || contents.getFileInfo() == null) {
            return;
        }

        //FileInfo fileInfo = fileInfoRepository.findFileInfoByFileId(contents.getFileInfo().getFileId());
        FileInfo fileInfo = contents.getFileInfo();
        File pptFile = new File(fileInfo.getFilePath());

        try {

            if (pptFile.exists()) {
                XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(pptFile));

                //getting the dimensions and size of the slide
                Dimension pgsize = ppt.getPageSize();
                //java.util.List<XSLFSlide> slide = ppt.getSlides();
                List<XSLFSlide> slide = Arrays.asList(ppt.getSlides());

                if (slide == null || slide.size() == 0) {
                    return;
                }

                Set<FileInfo> storyCaptures = new HashSet<>();

                //String fileId = fileInfoRepository.getNewFileId();

                String thumbFileId = "thumb_" + fileInfo.getName();
                String lightFileId = "light_" + fileInfo.getName();

                for (int i = 0; i < slide.size(); i++) {
                    BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = img.createGraphics();

                    //clear the drawing area
                    graphics.setPaint(Color.white);
                    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                    //render
                    slide.get(i).draw(graphics);

                    FileInfo storyCapture = new FileInfo();
                    //todo
                    int count = i + 1;
                    String captureFileId;

                    if (count < 10) {
                        captureFileId = fileInfo.getName() + "00" + count;
                    }
                    else if (count < 100) {
                        captureFileId = fileInfo.getName() + "0" + count;
                    }
                    else {
                        captureFileId = fileInfo.getName() + count;
                    }

                    storyCapture.setName(captureFileId);
                    storyCapture.setContentType(ContentsType.Thumbnail);
                    storyCapture.setExtension(format);
                    storyCapture.setServerFilePath(Paths.get(uploadDir).toString());
                    storyCapture.setFilePath(Paths.get(uploadDir ,captureFileId).toString());
                    storyCapture.setOriginalFilename(captureFileId+".png");
                    //creating an image file as output
                    FileOutputStream out = new FileOutputStream(storyCapture.getServerFilePath()+"\\" + storyCapture.getName());
                    javax.imageio.ImageIO.write(img, "png", out);
                    out.close();

                    fileInfoRepository.save(storyCapture);

                    if (i == 0) {
                        contents.setPreview(storyCapture);

                        // Make Thumbnail
                        BufferedImage resizeImage = ImageUtil.shrink(img, 145, 145);
                        File fileThumb = new File(fileInfo.getServerFilePath()+"\\" + thumbFileId);
                        ImageIO.write(resizeImage, "png", fileThumb);

                        BufferedImage lightImage = ImageUtil.shrink(img, width, height);
                        File fileLight = new File(fileInfo.getServerFilePath()+"\\" + lightFileId);
                        ImageIO.write(lightImage, "png", fileLight);
                    }

                    storyCaptures.add(storyCapture);

                }

                if (!storyCaptures.isEmpty()) {
                    contents.setCaptures(storyCaptures);
                    contentsRepository.save(contents);
                }

            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

    }
}
