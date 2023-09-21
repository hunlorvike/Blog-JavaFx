package hung.pj.login.ultis;

import java.io.File;

public class ImageFileUtil {

    public static boolean isImageFile(File file) {
        String extension = getFileExtension(file.getName()).toLowerCase();
        // Kiểm tra các phần mở rộng của tệp ảnh được chấp nhận
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")
                || extension.equals("bmp") || extension.equals("gif")
                || extension.equals("tiff") || extension.equals("tif")
                || extension.equals("ico") || extension.equals("webp")
                || extension.equals("svg") || extension.equals("jp2")
                || extension.equals("pcx") || extension.equals("ppm")
                || extension.equals("pgm") || extension.equals("pnm")
                || extension.equals("exif");
    }

    public static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex >= 0) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }
}

