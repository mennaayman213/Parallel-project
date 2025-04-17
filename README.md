# 🚀 Parallel Customer Request Processing (Java Swing + Multithreading)

## 📌 Overview
This project demonstrates how to process multiple customer requests **in parallel** using Java's multithreading capabilities and a graphical interface built with **Swing**. It uses a **fixed thread pool** to manage resources efficiently and simulate realistic task durations.

> Ideal for learning multithreading, GUI design, and concurrent task handling in Java.

---

## 🧠 Features
- 💬 **User Input** – Choose how many customer requests to process.
- ⚙ **Thread Pool (Fixed)** – 5 threads working simultaneously.
- 🧵 **Simulated Task Time** – Each request takes a random time (1–5 seconds).
- 🪟 **Java Swing GUI** – Clean interface with real-time log output.
- 📥 **Scalable Design** – Ready for integration with files (CSV) or databases.
  
---

## 🛠 Technologies Used
- Java 17+
- Swing (for GUI)
- ExecutorService (Thread Pool)
- java.time (for timestamps)

---

## 🔧 How to Run

### 🧑‍💻 Prerequisites
- Java JDK 17 or later
- Any IDE (NetBeans, IntelliJ, Eclipse)

### ▶️ Run Steps
1. Clone this repo or download the source.
2. Open the project in your IDE.
3. Compile and run `ParallelProcessingGUI.java`.
4. Enter number of requests and click **Start Processing**.

---

## 💡 Future Enhancements
- 📁 Load customer requests from a CSV file.
- 💽 Store results into a database.
- 📊 Visualize processing stats (completion time, active threads).
- 🔁 Add Spring Batch support for large-scale batch operations.

---

## 🤝 Contribution
Feel free to fork the repo and submit pull requests for improvements!

---

## 🧑‍💻 Author
Developed by mina tawfik, mostafa sherif, mennatullah ayman, manar ghareeb

---

## 📜 License
This project is licensed under the MIT License.
