package notepad;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FilenameFilter;

public class FileManager {
    private String dataFolderPath;
    public FileManager(){
        this.dataFolderPath = "src/data/";
        File dataFolder = new File(dataFolderPath);
        if(!dataFolder.exists()){
            dataFolder.mkdirs();
            System.out.println("Created folder: " + dataFolderPath);
        }
    }
    
    public boolean saveToFile(String fileName, String content){
        if(!fileName.endsWith(".txt")){
            fileName = fileName + ".txt";
        }
        BufferedWriter bufferedWriter = null;
        try{
            File file = new File(dataFolderPath + fileName);
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(content);
            System.out.println("File saved: " + fileName);
            return true;
        } catch(IOException e){
            System.err.println(e.getMessage());
            return false;
        } finally{
            try {
                if(bufferedWriter != null){
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    public String openFile(String fileName){
        if(!fileName.endsWith(".txt")){
            fileName = fileName + ".txt";
        }
        BufferedReader bufferedReader = null;
        StringBuilder content = new StringBuilder();
        try{
            File file = new File(dataFolderPath + fileName);
            if(!file.exists()){
                return "";
            }
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while((line = bufferedReader.readLine()) != null){
                content.append(line).append("\n");
            }
            System.out.println("File opened: " + fileName);
            return content.toString();
        } catch(IOException e){
            System.err.println(e.getMessage());
            return "";
        } finally{
            try {
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    public List<String> getAllFiles(){
        List<String> fileList = new ArrayList<>();
        File dataFolder = new File(dataFolderPath);
        if(dataFolder.exists() && dataFolder.isDirectory()){
            File[] files = dataFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            });
            if(files != null){
                for(File file: files){
                    fileList.add(file.getName());
                }
            }
        }
        return fileList;
    }
    
    public boolean fileExists(String fileName){
        if(!fileName.endsWith(".txt")){
            fileName = fileName + ".txt";
        }
        File file = new File(dataFolderPath + fileName);
        return file.exists();
    }
    
    public String generateUniqueFileName(){
        int counter = 1;
        String baseName = "untitled";
        String fileName = baseName + counter + ".txt";
        while (fileExists(fileName)){
            counter++;
            fileName = baseName + counter + ".txt";
        }
        return fileName;
    }
}