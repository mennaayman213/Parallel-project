# 📌 Parallel Customer Request Processing in Java

## 📖 Project Description
This project demonstrates **parallel processing of customer requests** stored in a **CSV file or database** using **Multi-threading** in Java. Each request runs in a **separate thread**, allowing multiple requests to be processed simultaneously, improving performance and reducing response time.

Additionally, this project explores **Batch Processing with Spring** to handle large-scale data processing efficiently. The batch processing feature enables **ETL (Extract, Transform, Load) operations**, making it suitable for data analysis and enterprise applications.

## 🛠 Technologies Used
- **Java** – Primary programming language.
- **Multithreading** – For parallel execution of requests.
- **Thread Pool (ExecutorService)** – For efficient resource management.
- **File Handling & Database Access (JDBC / JPA)** – For reading and writing customer requests.
- **Spring Batch** – For large-scale batch processing and ETL operations.

## ⚙️ How to Run the Project
### 1️⃣ Prerequisites
- **Java Development Kit (JDK) 19** or later.
- **NetBeans / IntelliJ IDEA / Eclipse** or any Java-compatible IDE.
- **MySQL / PostgreSQL / Any Database (if using DB Processing).**
- **Spring Framework & Spring Boot (if using Batch Processing).**

### 2️⃣ Execution Steps
#### Multi-threading Execution
1. Open the project in your preferred IDE.
2. Ensure the `customer_requests.csv` file or database is properly configured.
3. Compile and run the Java program using:
   ```sh
   javac Main.java
   java Main
   ```
4. The program will read customer requests and process them in parallel.

#### Batch Processing Execution (Spring Batch)
1. Set up **Spring Boot** project with **Spring Batch** dependencies.
2. Configure a **Job**, **Step**, and **Tasklet** to process requests in batches.
3. Run the Spring Boot application to process customer requests in batch mode.

## 🚀 How It Works
### Multi-threading Approach
1. **Read requests from a CSV file or database.**
2. **Distribute requests across multiple threads.**
3. **Each thread processes a request independently.**
4. **Threads synchronize results and write back to the file/database.**
5. **Shutdown the Thread Pool after processing all requests.**

### Batch Processing Approach
1. **Spring Batch reads the requests** from a CSV file or database.
2. **Processes them in defined chunks** (batch size configurable).
3. **Each batch is handled sequentially** using a Job and Step execution model.
4. **Results are stored** in a database or written to an output file.

## 🔥 Expected Output
```
🔹 Processing request: Request 1 in thread pool-1-thread-1
🔹 Processing request: Request 2 in thread pool-1-thread-2
🔹 Processing request: Request 3 in thread pool-1-thread-3
✅ Request 3 processing completed.
✅ Request 1 processing completed.
✅ Request 2 processing completed.
```
⚠ **Note**: Output order may vary due to parallel execution.

For **Batch Processing**, output logs will show job execution details, including:
```
[INFO] Job started: CustomerRequestJob
[INFO] Processing batch: 1
[INFO] Processed request ID: 101
[INFO] Processed request ID: 102
[INFO] Job completed successfully.
```




