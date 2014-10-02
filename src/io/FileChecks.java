package io;

import java.io.File;

/**
 * Created by wwadewitte on 10/2/14.
 */
public class FileChecks {

    public static long folderSize(File directory) {
        if(directory == null) {
            return 0;
        }
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

}
