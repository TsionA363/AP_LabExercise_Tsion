package notepad;

import java.io.File;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class GUI {
    private TextArea textArea;
    private FileManager fileManager;
    private String currentFileName;
    private boolean isModified = false;
    private MenuBar menuBar;
    
    public GUI(TextArea textArea, MenuBar menuBar){
        this.textArea = textArea;
        this.menuBar = menuBar;
        this.fileManager = new FileManager();
        this.currentFileName = null;
        this.textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            isModified = true;
            updateWindowTitle();
        });
    }
    
    public void newFile(){
        if(checkForUnsavedChanges()){
            textArea.clear();
            currentFileName = null;
            isModified = false;
            updateWindowTitle();
        }
    }
    
    public void openFile(){
        if(checkForUnsavedChanges()){
            java.util.List<String> files = fileManager.getAllFiles();
            if(files.isEmpty()){
                showAlert("No saved files found", "Information");
                return;
            }
            ChoiceDialog<String> dialog = new ChoiceDialog<>(files.get(0), files);
            dialog.setTitle("Open File");
            dialog.setHeaderText("Select a file to open");
            dialog.setContentText("Files in data folder: ");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(fileName -> {
                String content = fileManager.openFile(fileName);
                textArea.setText(content);
                currentFileName = fileName;
                isModified = false;
                updateWindowTitle();
            });
        }
    }
    
    public void saveFile(){
        if(currentFileName == null){
            saveAsFile();
        } else {
            if(fileManager.saveToFile(currentFileName, textArea.getText())){
                isModified = false;
                updateWindowTitle();
                showAlert("File saved: " + currentFileName, "Information");
            } else {
                showAlert("Failed to save file", "Error");
            }
        }
    }
    
    public void saveAsFile(){
        TextInputDialog dialog = new TextInputDialog("Untitled.txt");
        dialog.setTitle("Save File As");
        dialog.setContentText("File name: ");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(fileName -> {
            if(fileName.trim().isEmpty()){
                showAlert("File name cannot be empty", "Error");
                return;
            }
            if(fileManager.saveToFile(fileName, textArea.getText())){
                currentFileName = fileName.endsWith(".txt") ? fileName : fileName + ".txt";
                isModified = false;
                updateWindowTitle();
                showAlert("File saved as: " + currentFileName, "Information");
            } else {
                showAlert("Failed to save file", "Error");
            }
        });
    }
    
    private boolean checkForUnsavedChanges(){
        if(isModified){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes");
            alert.setContentText("Do you want to save the file?");
            ButtonType save = new ButtonType("Save");
            ButtonType discard = new ButtonType("Discard");
            ButtonType cancel = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(save, discard, cancel);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()){
                if(result.get() == save){
                    saveFile();
                    return true;
                } else if(result.get() == discard){
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void exitApp(Stage stage){
        if(checkForUnsavedChanges()){
            Platform.exit();
        }
    }
    
    public void updateWindowTitle(){
        Stage stage = (Stage) textArea.getScene().getWindow();
        String title = "Notepad";
        if(currentFileName != null){
            title += " - " + currentFileName;
        } else {
            title += " - No Document";
        }
        if(isModified){
            title += "*";
        }
        stage.setTitle(title);
    }
    
    public void showAboutDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Notepad");
        alert.setHeaderText("Simple Notepad Application");
        alert.setContentText("Version 1.0\n\n" +
                            "A simple notepad application built with JavaFX.\n" +
                            "Files are stored in: src/data/\n\n" +
                            "Features:\n" +
                            "• Create, open, save\n" +
                            "• Automatic save folder (data/)\n" +
                            "• Unsaved changes warning");
        alert.showAndWait();
    }
    
    private void showAlert(String message, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}