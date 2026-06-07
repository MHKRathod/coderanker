# ⚡ CodeRanker

A secure online code execution platform that allows users to write, compile, and execute code in multiple programming languages through an isolated Docker-based sandbox environment.

---

## 🚀 Features

* Execute code in **Python, Java, and C++**
* Dynamic code compilation and execution
* Docker-based sandboxing for secure execution
* Runtime isolation using containers
* Compilation error handling
* Runtime exception handling
* Infinite loop protection using execution timeout
* Asynchronous code execution using Spring Boot `@Async`
* Submission tracking with MySQL persistence
* REST APIs for code submission and result retrieval
* Monaco Editor integration for a VS Code-like coding experience

---

## 🛠 Tech Stack

### Backend

* Java
* Spring Boot
* Spring Data JPA
* MySQL

### Frontend

* React
* Vite
* Monaco Editor
* Axios

### Infrastructure

* Docker

---

## 🏗 Architecture

```text
React Frontend
       │
       ▼
Spring Boot REST API
       │
       ▼
Execution Service (@Async)
       │
       ▼
Docker Container
       │
       ▼
Code Compilation & Execution
       │
       ▼
Store Results in MySQL
       │
       ▼
Frontend Polling for Results
```

---

## ⚙️ Execution Flow

1. User writes code in the Monaco Editor.
2. User selects a programming language.
3. Frontend sends code to the backend via REST API.
4. Backend creates a submission record in MySQL.
5. Execution starts asynchronously.
6. Code runs inside an isolated Docker container.
7. Output, errors, and execution status are stored in the database.
8. Frontend polls for execution status and displays the result.

---

## 🔒 Security Features

### Docker Sandboxing

User code is executed inside isolated Docker containers instead of directly on the host machine.

### Timeout Protection

Long-running or infinite-loop programs are automatically terminated after a configurable timeout.

### Runtime Isolation

Each execution runs in a clean environment, preventing interference with the application server.

### Error Handling

Compilation errors and runtime exceptions are captured and returned safely to the user.

---

## 📌 Supported Languages

| Language | Supported |
| -------- | --------- |
| Python   | ✅         |
| Java     | ✅         |
| C++      | ✅         |

---

## 📷 Screenshots

### Code Editor

*Add screenshot here*

### Code Execution

*Add screenshot here*

### Output Console

*Add screenshot here*

---

## 🔮 Future Enhancements

* Custom Input (stdin) support
* Authentication and user accounts
* Execution history dashboard
* Rate limiting
* Redis-based execution queue
* Kubernetes-based container orchestration
* Multi-language support expansion

---

## ▶️ Running Locally

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 🎯 Key Backend Concepts Demonstrated

* Process Management
* Secure Code Execution
* Docker Containerization
* Concurrency Handling
* Asynchronous Processing
* Runtime Isolation
* REST API Development
* Error Handling
* Database Persistence
* System Design Fundamentals

---

## 👨‍💻 Author

Hari Krishna Rathod

Built as a backend engineering project to explore secure code execution, containerization, process management, and scalable execution workflows.
