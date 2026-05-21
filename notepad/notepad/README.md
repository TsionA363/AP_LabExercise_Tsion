````markdown
# Notepad Application

##  Overview

This project is a simple desktop text editor built using **JavaFX** and Java file handling.  
The application follows a modular structure that separates the user interface from file management logic.

---

# System Architecture

The application is divided into three main components:

- `App.java`
- `GUI.java`
- `FileManager.java`

These components work together to manage the editor interface and local file storage.

---

# Components

## App.java

### Purpose
Acts as the application entry point.

### Main Functions

- Launches the JavaFX application
- Creates the main layout using `BorderPane`
- Adds:
  - `MenuBar`
  - `TextArea`
- Connects menu actions to controller methods

Example:

```java
newItem.setOnAction(e -> uiController.newFile());
```

---

## GUI.java

### Purpose
Controls the user interface and application state.

### Main Functions

- Tracks unsaved changes using:

```java
isModified
```

- Updates the window title dynamically
- Displays confirmation alerts before closing or opening files
- Handles:
  - New File
  - Open File
  - Save
  - Save As

- Listens for text changes using:

```java
textArea.textProperty().addListener(...)
```

---

## FileManager.java

### Purpose
Handles file storage and reading operations.

### Main Functions

- Creates the `src/data/` folder automatically
- Reads and writes text files using:
  - `BufferedReader`
  - `BufferedWriter`

- Filters only `.txt` files
- Generates unique filenames such as:
  - `untitled1.txt`
  - `untitled2.txt`

- Checks file existence before saving

---

# Application Workflow

| Action | Behavior |
|---|---|
| Open/New File | Checks for unsaved changes |
| Save | Writes content to local storage |
| Typing | Marks document as modified |
| Open Existing File | Loads `.txt` content into editor |

---

# Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core programming |
| JavaFX | Graphical User Interface |
| File I/O | Reading and writing files |
| OOP | Modular application design |

---




````
