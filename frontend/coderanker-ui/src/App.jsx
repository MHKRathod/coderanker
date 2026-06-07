import { useState } from "react";
import "./App.css";
import axios from "axios";
import Editor from "@monaco-editor/react";

const templates = {
  python: `print("Hello World")`,

  java: `public class Main {
    public static void main(String[] args) {

    }
}`,

  cpp: `#include <iostream>
using namespace std;

int main() {

    return 0;
}`
};

function App() {
  const [language, setLanguage] = useState("python");
 const [code, setCode] = useState(templates.python);
  const [output, setOutput] = useState(
  "Welcome to CodeRanker 🚀\nSelect a language and run your code."
);
  const [status, setStatus] = useState("");
const [loading, setLoading] = useState(false);

  

  const runCode = async () => {
  try {
    setLoading(true);
    setStatus("QUEUED");
    setOutput("Running...");

    const submitResponse = await axios.post(
      "http://localhost:8080/submit",
      {
        code,
        language,
      }
    );

    const submissionId = submitResponse.data.split(": ")[1];

    const interval = setInterval(async () => {
      const resultResponse = await axios.get(
        `http://localhost:8080/result/${submissionId}`
      );

      const result = resultResponse.data;

setStatus(result.status);

     if (
  result.status === "COMPLETED" ||
  result.status === "FAILED"
) {
  setOutput(result.output);
  setLoading(false);
  clearInterval(interval);
}
    }, 1000);
  } catch (error) {
    setOutput("Error: " + error.message);
  }
};

const handleLanguageChange = (e) => {
  const lang = e.target.value;

  setLanguage(lang);
  setCode(templates[lang]);
};

  return (
    <div className="container">
      <div className="header">
  <h1>⚡ CodeRanker</h1>
  <p>Secure Online Code Execution Platform</p>
</div>

<div className="toolbar">
      <select
  value={language}
  onChange={handleLanguageChange}
>
        <option value="python">🐍 Python</option>
<option value="java">☕ Java</option>
<option value="cpp">⚡ C++</option>
      </select>

    

   <div className="workspace">

  <div className="editor-section">
    <Editor
      height="600px"
      language={
        language === "cpp"
          ? "cpp"
          : language === "python"
          ? "python"
          : "java"
      }
      value={code}
      onChange={(value) => setCode(value || "")}
      theme="vs-dark"
    />
  </div>

  <div className="output-section">

    <div className={`status ${status.toLowerCase()}`}>
     {status && `Status: ${status}`}
    </div>

    <h3>Output</h3>
      
<button
  className="run-button"
  onClick={runCode}
  disabled={loading}
>
  {loading ? "Running..." : "▶ Run Code"}
</button>


    <pre>{output}</pre>

  </div>

</div>


</div>

<footer className="footer">
  Built with Java, Spring Boot, Docker & React
</footer>
    </div>
  );
}

export default App;