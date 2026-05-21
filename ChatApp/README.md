```markdown
# Java Chat Application: Architecture & Code Breakdown

This document provides a technical overview of the multi-user desktop chat application. The system is built using a **Client-Server architecture** utilizing raw network sockets for real-time communication, **JavaFX** for the desktop graphical user interface, and **MySQL** for long-term data persistence.

---

##  System Architecture

The application layout relies on a decoupled, event-driven flow to pass data back and forth across the network layer:

##  Component 

###  Client.java` & Message.java
* **Connection Management:** Establishes a raw TCP connection to the backend using `new Socket(host, port)`. It acts as the low-level wrapper around the network's input and output streams.
* **Background Listener Loop:** Spawns a dedicated background `Thread` instantly upon connection. This thread continuously listens for incoming network data (`in.readLine()`) so that the primary user interface never freezes up.
* **Decoupled Interface:** Uses the `Message` callback interface. Whenever a new network packet arrives, it fires `onMessageReceived(response)` to safely bubble the raw text up to the desktop window.

### 'Server.java`
* **Multi-Threaded Handling:** Runs an infinite execution loop listening for incoming handshakes via a `ServerSocket`. For every new user that connects, it spawns an isolated `ClientHandler` thread.
* **State Management:** Manages all active user connections simultaneously using a thread-safe synchronized wrapper (`Collections.synchronizedSet`).
* **Database logging & Routing:** When any user transmits a message, the server catches it, logs the contents permanently into a MySQL database table, packages it into a structured text packet, and loops through the active list to broadcast it out to everyone else.

### `GUI.java`
* **Visual Form Layouts:** Built entirely with JavaFX controls. It boots into a lightweight Login scene (`VBox`) to capture a username. Once the user clicks **Join Group**, it swaps the application stage out for a spacious multi-panel chat grid (`BorderPane`).
* **Safe Threading (`Platform.runLater`):** Implements the `handleIncomingMessage` method to process incoming engine traffic. Because UI frameworks explicitly forbid background threads from modifying visual nodes, it wraps layout appending routines inside a `Platform.runLater()` call to execute them safely on the primary JavaFX Application Thread.
* **String Parsing:** Automatically handles incoming structured text packets (e.g., `MSG|Alice|Hello`) by splitting them on the pipe character (`split("\\|")`) to determine exactly how to render the message elements visually.

###`SQL`
* **Long-Term Persistence:** Defines a standard structured `messages` table. This allows the server's `sendChatHistory()` method to select, sort, and pipe chronological historical text back to any brand-new user the moment they register and join the server room.

---

## 3. Custom Network Messaging Protocol

To prevent text collisions and keep structural parsing simple, data packets are serialized into strings using a custom pipe-delimited (`|`) formatting standard:

| Packet Structure Prefix | Direction | Data Context & System Action |
| :--- | :--- | :--- |
| `MSG\|[content]` | Client $\rightarrow$ Server | A live, outgoing text string waiting to be broadcasted to the room. |
| `FILE\|[filename]` | Client $\rightarrow$ Server | A notification informing the channel that a user shared a local file via a `FileChooser`. |
| `HIST_MSG\|[user]\|[content]` | Server $\rightarrow$ Client | An archival text entry pulled from the database to catch up a newly joined user. |
| `HIST_FILE\|[user]\|[content]` | Server $\rightarrow$ Client | An archival file notification entry pulled from the database history layer. |

---

> **Compilation Note:** To compile and run the user interface successfully, you must explicitly link your local JavaFX SDK modules and include the target MySQL connector driver archive files directly in your runtime classpath:
> ```bash
> java --module-path "C:\javafx-sdk-21.0.2\lib" --add-modules javafx.controls -cp ".;lib/mysql-connector.jar" GUI
> ```

```
