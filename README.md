# 🚀 Parallel Customer Request Processing (Java Swing + Multithreading)

## 📌 Overview
This project demonstrates how to handle multiple customer requests **in parallel** using Java’s multithreading capabilities and a GUI built with **Swing**. It simulates real-time task durations and utilizes a fixed thread pool to manage resources efficiently.

Ideal for learning about:
- Multithreading
- GUI development with Swing
- Concurrent task handling

---

### 🖼️ Screenshot

### ✅ Before Start
![Before Start](screenshot-start.png)

### 🔄 During Processing
![During](screenshot-during.png)

### ✅ All Tasks Completed
![Completed](screenshot-complete.png)



---

## 🧠 Features
- 💬 **User Input** – Specify the number of customer requests to process.
- ⚙️ **Thread Pool (Fixed)** – Uses 5 worker threads for concurrent execution.
- ⏳ **Simulated Task Duration** – Each task randomly lasts 1–5 seconds.
- 🪟 **Java Swing GUI** – User-friendly interface with real-time log output.
- 📥 **Scalable Design** – Ready for future integrations (e.g., files, databases).

---

## 🛠 Technologies Used
- Java 17+
- Swing (Java GUI Framework)
- `ExecutorService` (Thread Pool)
- `java.time` (for timestamp formatting)

---

## ▶️ How to Run

### 🔧 Prerequisites
- Java JDK 17 or later installed
- Any Java IDE (NetBeans, IntelliJ IDEA, Eclipse)

### 📦 Run Instructions
1. Clone this repository or download the source code.
2. Open the project in your preferred IDE.
3. Compile and run the `ParallelProcessingGUI.java` file.
4. Enter the number of requests to process.
5. Click **Start Processing** and watch tasks being executed in parallel!

---

## 💡 Future Enhancements
- 📁 Load requests from a **CSV file**.
- 💽 Store results into a **database**.
- 📊 Add **visual stats** (e.g., task duration, active threads).
- 🔁 Integrate with **Spring Batch** for large-scale operations.

---

## 🤝 Contribution
Contributions are welcome!  
Fork the repo and submit a pull request with your enhancements or bug fixes.

---

## 👨‍💻 Authors
Developed by:
- Mina Tawfik  
- Mostafa Sherif  
- Mennatullah Ayman  
- Manar Ghareeb  

---

## 📜 License
This project is licensed under the **MIT License**.
