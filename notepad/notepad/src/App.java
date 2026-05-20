// RUN: java --module-path "C:\javafx-sdk-21.0.2\lib" --add-modules javafx.controls -cp bin App

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import notepad.FileManager;  
import notepad.GUI; 

public class App extends Application {
    private GUI uiController;
    
    public void start(Stage primaryStage) throws Exception {
        
        MenuBar menubar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem exit = new MenuItem("Exit");
        file.getItems().addAll(newItem, open, save, saveAs, exit);

        Menu edit = new Menu("Edit");
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        edit.getItems().addAll(cut, copy, paste);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().addAll(about);
        menubar.getMenus().addAll(file, edit, help);

        TextArea text = new TextArea();
        
       
        uiController = new GUI(text, menubar);
        
        
        newItem.setOnAction(e -> uiController.newFile());
        open.setOnAction(e -> uiController.openFile());
        save.setOnAction(e -> uiController.saveFile());
        saveAs.setOnAction(e -> uiController.saveAsFile());
        exit.setOnAction(e -> uiController.exitApp(primaryStage));
        
        cut.setOnAction(e -> text.cut());
        copy.setOnAction(e -> text.copy());
        paste.setOnAction(e -> text.paste());
        about.setOnAction(e -> uiController.showAboutDialog());
         
        BorderPane layout = new BorderPane();
        layout.setTop(menubar);
        layout.setCenter(text);
        
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Notepad ");
        primaryStage.show();
    }
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}