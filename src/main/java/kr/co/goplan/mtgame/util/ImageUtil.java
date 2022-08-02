package kr.co.goplan.mtgame.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    private static final int BASE_PIXEL = 640;

    public static BufferedImage getImage(MultipartFile resource) throws IOException {
        InputStream is = resource.getInputStream();
        try {
            return ImageIO.read(is);
        } finally {
            is.close();
        }
    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage rv = new BufferedImage(width, height, image.getType());
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Graphics2D graphics2D = ge.createGraphics(rv);
        //graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        return rv;
    }


    public static BufferedImage scale(BufferedImage source, int sourceDpi, int destDpi) {
        if(sourceDpi == destDpi) {
            return null;
        }
        int width = (int) (source.getWidth() * (double) destDpi / sourceDpi);
        int height = (int) (source.getHeight() * (double) destDpi / sourceDpi);

        return resize(source, width, height);
    }

    public static BufferedImage scaleAndShrink(BufferedImage source, int sourceDpi, int destDpi, double percentage) {
        if(sourceDpi == destDpi) {
            return null;
        }
        int width = (int) (source.getWidth() * (double) destDpi / sourceDpi * percentage);
        int height = (int) (source.getHeight() * (double) destDpi / sourceDpi * percentage);

        return resize(source, width, height);
    }

    public static BufferedImage shrink(BufferedImage image, int maxWidth, int maxHeight) {
        if (maxWidth == -1 && maxHeight == -1) {
            return image;
        }

        int width  = image.getWidth();
        int height = image.getHeight();

        double xScale = maxWidth == -1 ? 1.0 : (double) width / (double) maxWidth;
        double yScale = maxHeight == -1 ? 1.0 : (double) height / (double) maxHeight;

        if (xScale > 1 && xScale >= yScale) {
            width /= xScale;
            height /= xScale;
        }
        else if (yScale > 1 && yScale >= xScale) {
            width /= yScale;
            height /= yScale;
        }

        return resize(image, width, height);
    }

	/*
	 * 이미지 데이터로부터 썸네일 파일을 생성한다.
	 * @param strImageData data:image/png;base64 형식의 데이터
	 * @return Thumbnail BufferedImage
	public static BufferedImage createThumbNail(String strImageData) {
		BufferedImage originalImage = null;
		BufferedImage thumbNail = null;

		try {
			byte[] decodedBytes = DatatypeConverter.parseBase64Binary(extractDataFromImage(strImageData));

			originalImage = ImageIO.read(new ByteArrayInputStream(decodedBytes));

			int oldWidth  = originalImage.getWidth();
			int oldHeight = originalImage.getHeight();
			int newWidth  = 0;
			int newHeight = 0;

			float ratio = 0.0f;
			//넓은쪽의 픽셀 수가 기준 픽셀 이하일 경우는 리사이징 하지 않는 것도 검토 필요...
			if(oldWidth >= oldHeight) {
				ratio     = BASE_PIXEL / (float)oldWidth;
				newWidth  = BASE_PIXEL;
				newHeight = Math.round(oldHeight * ratio);
			} else {
				ratio     = BASE_PIXEL / (float)oldHeight;
				newWidth  = Math.round(oldWidth * ratio);
				newHeight = BASE_PIXEL;
			}

			thumbNail = getScaledImage(originalImage, newWidth, newHeight);
		} catch(Exception e) {
			logger.error(e.getMessage());
		} finally {
			if(null != originalImage) {
				originalImage.flush();
			}
			if(null != thumbNail) {
				thumbNail.flush();
			}
		}

		return thumbNail;
	}
	 */

    /**
     * 이미지 데이터로부터 썸네일 파일을 생성한다.
     * @param resource multipart file
     * @return Thumbnail BufferedImage
     */
    public static BufferedImage createThumbNail(MultipartFile resource) {
        BufferedImage originalImage = null;
        BufferedImage thumbNail = null;

        try {
            originalImage = getImage(resource);

            int oldWidth  = originalImage.getWidth();
            int oldHeight = originalImage.getHeight();
            int newWidth  = 0;
            int newHeight = 0;

            float ratio = 0.0f;
            //넓은쪽의 픽셀 수가 기준 픽셀 이하일 경우는 리사이징 하지 않는 것도 검토 필요...
            if(oldWidth >= oldHeight) {
                ratio     = BASE_PIXEL / (float)oldWidth;
                newWidth  = BASE_PIXEL;
                newHeight = Math.round(oldHeight * ratio);
            } else {
                ratio     = BASE_PIXEL / (float)oldHeight;
                newWidth  = Math.round(oldWidth * ratio);
                newHeight = BASE_PIXEL;
            }

            thumbNail = getScaledImage(originalImage, newWidth, newHeight);
        } catch(Exception e) {
            logger.error("ERROR", e);
        } finally {
            if(null != originalImage) {
                originalImage.flush();
            }
            if(null != thumbNail) {
                thumbNail.flush();
            }
        }

        return thumbNail;
    }

	/*
	 * 이미지로부터 데이터 항목을 추출한다.
	 * @param imgData base64 png data
	 * @return png data string
	private static String extractDataFromImage(String imgData) {
		String result = "";

		String[] arrayFirst = imgData.split(";");
		if(arrayFirst.length > 1) {
			String[] arraySecond = arrayFirst[1].split(",");

			if(arraySecond.length > 1) {
				result = arraySecond[1];
			}
		}
		return result;
	}
	 */

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private static BufferedImage getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

	/*
	public static void main (String[] args){
		// remove data:image/png;base64, and then take rest sting
		String imgData = "data:image/png;base64,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";

		ThumbnailCretaorService conv = new ThumbnailCretaorService();
		String img64 = conv.extractDataFromImage(imgData);
		conv.setBasePixel(200);
		int result = conv.createThumbNail(img64);

		System.out.println(result == 0 ? "썸네일 완성!!!" : "실패!!!");
	}
	*/
}
