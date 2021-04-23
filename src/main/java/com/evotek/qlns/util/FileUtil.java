/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Filedownload;

/**
 *
 * @author linhlh2
 */
public class FileUtil {

    public static final String PNG = "png";
    public static final String DOC = "doc";
    public static final String DOCX = "docx";
    public static final String PDF = "pdf";
    public static final String JPG = "jpg";
    public static final String JPEG = "jpeg";
    public static final String GIF = "gif";
    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
    public static final String PPT = "ppt";
    public static final String PPTX = "pptx";

    public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>(){{
        put(PNG, "image/png");
        put(DOC, "application/msword");
        put(DOCX, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        put(PDF, "application/pdf");
        put(JPG, "image/jpeg");
        put(JPEG, "image/jpeg");
        put(GIF, "image/gif");
        put(XLS, "application/vnd.ms-excel");
        put(XLSX, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        put(PPT, "application/vnd.ms-powerpoint");
        put(PPTX, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }};

    public static void write(InputStream in, File file) {

        try {
            OutputStream out = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[5120];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            out.close();

            in.close();
        } catch (FileNotFoundException fnfe) {
            _log.error(fnfe.getMessage(), fnfe);
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }
    }

    public static String getTextFromBlob(Blob blob) throws
            FileNotFoundException, SQLException, IOException {
        String text = StringPool.BLANK;

        try {
            if (blob != null) {
                InputStream inStream = blob.getBinaryStream();

                StringWriter writer = new StringWriter();

                IOUtils.copy(inStream, writer, "UTF-8");

                text = writer.toString();
            }
        } catch (FileNotFoundException fnfe) {
            _log.error(fnfe.getMessage(), fnfe);
        } catch (SQLException sqlex) {
            _log.error(sqlex.getMessage(), sqlex);
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }

        return text;
    }

    public static BufferedImage getBufferedImage(Blob blob) {
        if (blob == null) {
            return null;
        }

        BufferedImage buffered = null;

        try {
            InputStream inStream = blob.getBinaryStream();

            buffered = ImageIO.read(inStream);
        } catch (SQLException sqlex) {
            _log.error(sqlex.getMessage(), sqlex);
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }

        return buffered;
    }

    public static BufferedImage getBufferedImage(Media media) {
        if (media == null) {
            return null;
        }

        BufferedImage buffered = null;

        try {
            InputStream inStream = media.getStreamData();

            buffered = ImageIO.read(inStream);
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }

        return buffered;
    }

    public static String getImageBase64String(Blob blob, String type) {
        String imgStr = StringPool.BLANK;

        try {
            InputStream inStream = blob.getBinaryStream();

            imgStr = getImageBase64String(inStream, type);
        } catch (SQLException sqlex) {
            _log.error(sqlex.getMessage(), sqlex);
        }

        return imgStr;
    }

    public static String getImageBase64String(InputStream inStream, String type) {
        String imgStr = StringPool.BLANK;

        try {
            BufferedImage img = ImageIO.read(inStream);

            imgStr = encodeToString(img, type);
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }

        return imgStr;
    }

    public static String getImageBase64String(String url, String type) {
        String imgStr = StringPool.BLANK;

        try {
            BufferedImage img = ImageIO.read(new File(url));

            imgStr = encodeToString(img, type);
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }

        return imgStr;
    }

    public static String getImageSrcBase64String(Blob blob, String type) {
        StringBuilder sb = new StringBuilder();

        sb.append("data:image");
        sb.append(StringPool.SLASH);
        sb.append(type);
        sb.append(";base64,");
        sb.append(getImageBase64String(blob, type));

        return sb.toString();
    }

    public static String getImageSrcBase64String(InputStream inStream,
            String type) {
        StringBuilder sb = new StringBuilder();

        sb.append("data:image");
        sb.append(StringPool.SLASH);
        sb.append(type);
        sb.append(";base64,");
        sb.append(getImageBase64String(inStream, type));

        return sb.toString();
    }

    public static String getImageSrcBase64String(String url,
            String type) {
        StringBuilder sb = new StringBuilder();

        sb.append("data:image");
        sb.append(StringPool.SLASH);
        sb.append(type);
        sb.append(";base64,");
        sb.append(getImageBase64String(url, type));

        return sb.toString();
    }

    /**
     * Encode image to string
     *
     * @param image The image to encode
     * @param type jpeg, bmp, ...
     * @return encoded string
     */
    public static String encodeToString(BufferedImage image, String type) {
        String imgStr = StringPool.BLANK;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);

            byte[] imageBytes = bos.toByteArray();

            imgStr = Base64.encodeBase64String(imageBytes);

            bos.close();
        } catch (IOException ioe) {
            _log.error(ioe.getMessage(), ioe);
        }

        return imgStr;
    }

    public static BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;

        byte[] imageByte;

        try {
            imageByte = Base64.decodeBase64(imageString);

            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);

            image = ImageIO.read(bis);

            bis.close();
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        return image;
    }

    public static String getOrCreateFolder(String rootDirName, String newDirName) {
        String tempDirPath = StaticUtil.SYSTEM_STORE_FILE_DIR;

        StringBuilder sb = new StringBuilder();

        sb.append(tempDirPath);
        sb.append(StringPool.SLASH);
        sb.append(rootDirName);
        if (newDirName != null) {
            sb.append(StringPool.SLASH);
            sb.append(newDirName);
        }
        
        File newDir = new File(sb.toString());

        if (!newDir.exists()
                && !newDir.mkdirs()) {
            _log.warn("Khong the tao duoc thu muc: " + newDirName);

            return null;
        }

        if (!newDir.isDirectory()) {
            if (!newDir.delete()
                    || !newDir.mkdir()) {
                _log.warn("Khong the tao duoc thu muc: " + newDirName);

                return null;
            }
        }

        return sb.toString();
    }

    public static boolean write(byte[] data, String dirPath, String fileName)
            throws Exception {
        boolean success = false;

        FileOutputStream out = null;

        try {
            out = new FileOutputStream(dirPath + StringPool.SLASH + fileName);

            out.write(data);

            success = true;
        } catch (Exception ex) {
            _log.error("Khong the tao file: " + fileName);

            throw ex;
        } finally {
            try {
                out.close();
            } catch (IOException ioe) {
                _log.error(ioe.getMessage(), ioe);
            }
        }

        return success;
    }

    public static boolean write(Media media, String dirPath, String fileName)
            throws Exception {
        if (media.isBinary()) {
            return write(media.getByteData(), dirPath, fileName);
        }

        byte[] data = IOUtils.toByteArray(media.getReaderData());

        return write(data, dirPath, fileName);

    }

    public static boolean remove(String rootFolder, String folder,
            String fileName) {
        String tempDirPath = StaticUtil.SYSTEM_STORE_FILE_DIR;

        StringBuilder sb = new StringBuilder();

        sb.append(tempDirPath);
        sb.append(StringPool.SLASH);
        sb.append(rootFolder);
        sb.append(StringPool.SLASH);
        sb.append(folder);
        sb.append(StringPool.SLASH);
        sb.append(fileName);

        File file = new File(sb.toString());

//        if(!file.exists() || !file.delete()){
        if (!file.exists()) {
            return true;
        }

        if (!file.delete()) {
            _log.warn("Khong the xoa duoc file: " + fileName);

            return false;
        }

        return true;
    }

    public static void download(File file)
            throws FileNotFoundException {
        download(file, file.getName());
    }

    public static void download(File file, String fileName)
            throws FileNotFoundException {
        InputStream inp = null;

        try {
            inp = new FileInputStream(file);

            Filedownload.save(inp,
                    new MimetypesFileTypeMap().getContentType(file),
                    fileName);
        } catch (FileNotFoundException fnfe) {
            _log.error(fnfe.getMessage(), fnfe);
        }
    }

    public static String getAutoIncrementName(String folder, String fileName) {
        Pattern p = Pattern.compile("(.*?)_(\\d+)?(\\..*)?");

        while (new File(folder + StringPool.SLASH + fileName).exists()) {
            Matcher m = p.matcher(fileName);

            if (!m.matches()) {
                break;
            }

            StringBuilder sb = new StringBuilder();

            //group 1 is the prefix, group 2 is the number, group 3 is the suffix
            sb.append(m.group(1));
            sb.append(StringPool.UNDERLINE);

            if (Validator.isNull(m.group(2))) {
                sb.append(1);
            } else {
                sb.append(Integer.parseInt(m.group(2)) + 1);
            }

            if (Validator.isNull(m.group(3))) {
                sb.append(StringPool.BLANK);
            } else {
                sb.append(m.group(3));
            }

            fileName = sb.toString();
        }

        return fileName;
    }

    public static boolean isValidFileExtension(String extension,
            List<String> allowExts) {

        return allowExts.contains(extension.toLowerCase());
    }

    public static boolean isValidFileExtension(String extension,
            String[] allowExts) {
        List<String> exts = Arrays.asList(allowExts);

        return isValidFileExtension(extension, exts);
    }

    public static boolean isValidMaxSize(int size, Long maxSize) {
        if (Validator.isNull(maxSize)) {
            return true;
        }

        return size <= maxSize.intValue();
    }

    public static Long getKilobyte(Long sizeB) {
        return sizeB / (1024);
    }

    public static Long getMegabyte(Long sizeB) {
        return getKilobyte(sizeB) / 1024;
    }

    public static String getFileExtension(String fileName){
        if(Validator.isNull(fileName)){
            return null;
        }

        String[] splits = fileName.split("\\.(?=[^\\.]+$)");

        if(splits.length < 2) {
            return null;
        }

        return splits[1];
    }

    private static final Logger _log = LogManager.getLogger(FileUtil.class);
}
