package Visual;

import java.io.File;

public class PathUtils {
    public static String getJarDir() {
        try {
            return new File(
                PathUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            ).getParent();
        } catch (Exception e) {
            return new File("").getAbsolutePath();
        }
    }
}
