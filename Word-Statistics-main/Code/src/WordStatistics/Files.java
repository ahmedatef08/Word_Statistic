package WordStatistics;

import java.util.ArrayList;
import java.io.File;

public class Files{  
    private static ArrayList<File> txtFiles;
    // getfiles function
    // input (URL location -> the location of the dirctory , boolean IncludeSubdirctories -> 0: false 1: true)
    // output (array of File with location and name)
    public Files(){
        txtFiles = new ArrayList<>();
    }
    
    public File[] getFiles(File directory, boolean includeSubdirectories) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    txtFiles.add(file); 
                    //System.out.println(file.getName());
                }
                else if (file.isDirectory() && includeSubdirectories == true) 
                    // Recursively analyze subdirectories
                    getFiles(file, true);
            }
        }
        return (txtFiles.isEmpty()? null : (File[]) txtFiles.toArray(File[]::new));
    }
}
