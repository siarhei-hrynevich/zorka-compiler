package com.flex.compiler.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    public static final String sourceFileExtension = ".f";

    public static File getFile(String packageName) {
        return new File(packageName + sourceFileExtension);
    }

    public static String getPackage(File file) {
        return file.getPath().replace(file.getName(), "") + File.separator + FileUtils.removeExtension(file.getName());
    }

    public static String createPath(List<String> path) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < path.size() - 1; i++) {
            builder.append(path.get(i));
            builder.append(File.separator);
        }
        builder.append(path.get(path.size() - 1));
        builder.append(sourceFileExtension);
        return builder.toString();
    }

    public static String removeExtension(String name) {
        String fileName = "";

        try {
            if (name != null) {
                fileName = name.replaceFirst("[.][^.]+$", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public static String getFileNameWithoutExtension(File file) {
        String fileName = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                fileName = removeExtension(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static String changeExtension(String name, String extension) {
        return name.replaceFirst("[.][^.]+$", extension);
    }

    public static void createFolders(List<String> folders, String dir) {
        StringBuilder path = new StringBuilder(dir);
        for (int i = 0; i < folders.size(); i++) {
            createFolder(path.toString());
            path.append(File.separator);
            path.append(folders.get(i));
        }
    }

    public static void createFolder(String dir) {
        File file = new File(dir);
        if (!file.isDirectory())
            file.mkdir();
    }
}