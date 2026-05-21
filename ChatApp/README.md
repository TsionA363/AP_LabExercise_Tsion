
## 📖 Introduction

This project is a **multi-user desktop chat application** developed using:

- Java
- JavaFX
- Socket Programming
- MySQL

The system follows a **Client-Server Architecture** that enables real-time communication between multiple connected users.

---

#  System Architecture

The application is divided into four major components:

- **Client**
- **Server**
- **GUI**
- **Database**

These components work together to send, receive, display, and store chat messages.

---

#  Components

##  Client.java & Message.java

### Purpose
Handles communication between the user and the server.

### Main Functions

- Establishes a socket connection using:

```java
new Socket(host, port);
```

- Sends text and file notification messages
- Receives messages from the server
- Runs a background thread for continuous listening
- Uses the `Message` interface callback:

```java
onMessageReceived(response);
```

---

## Server.java

### Purpose
Controls all connected clients and manages message broadcasting.

### Main Functions

- Uses `ServerSocket` to accept client connections
- Creates a separate `ClientHandler` thread for each user
- Stores connected users using:

```java
Collections.synchronizedSet(...)
```

- Broadcasts messages to all clients
- Saves messages into the MySQL database
- Sends previous chat history to newly connected users

---

## GUI.java

### Purpose
Provides the graphical user interface using JavaFX.

### Main Functions

- Displays login and chat windows
- Allows users to:
  - Join the chat
  - Send messages
  - Upload files

- Safely updates the JavaFX UI using:

```java
Platform.runLater(() -> {
    // UI updates
});
```

- Parses message packets using:

```java
split("\\|");
```

---

##  SQL Database

### Purpose
Stores chat history permanently.

### Database Table

The `messages` table stores:

| Column | Description |
|---|---|
| Username | Sender name |
| Message Content | Chat message |
| File Status | Indicates file messages |
| Timestamp | Time sent |

This allows old messages to be loaded when new users join the chat.

---

# Custom Messaging Protocol

The system uses a pipe-separated (`|`) message format.

| Packet Format | Description |
|---|---|
| `MSG\|text` | Normal text message |
| `FILE\|filename` | File notification |
| `HIST_MSG\|user\|text` | Old text message from database |
| `HIST_FILE\|user\|file` | Old file notification from database |

---

#  Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core application development |
| JavaFX | Graphical User Interface |
| Socket Programming | Network communication |
| MySQL | Data persistence |
| Multithreading | Multiple client handling |

---

# Compilation Command

```bash
java --module-path "C:\javafx-sdk-21.0.2\lib" \
--add-modules javafx.controls \
-cp ".;lib/mysql-connector.jar" GUI
```

---

