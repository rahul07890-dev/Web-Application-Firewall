# Web Application Firewall (WAF)

## Overview
A basic Java-based Web Application Firewall (WAF) prototype that filters HTTP requests, detects malicious inputs, and logs security events. This application also integrates email alerting and request logging functionality.

## Technologies Used
- Java: For implementing the WAF’s core logic.
- Servlets & JSP: For handling HTTP requests and responses.
- Regex & Pattern Matching: For identifying malicious content in requests.
- web.xml: For servlet filter configuration.

---

## Directory Structure
```
WebApplicationFirewall/
├── Web Pages/
│   ├── META-INF/
│   ├── WEB-INF/
│   ├── index.html
│   ├── mail_failure.jsp
│   ├── mail_success.jsp
│   └── requestLogs.jsp
├── Source Packages/
│   └── com/
│       └── myapp/
│           └── webappfirewall/
│               ├── resources/
│               │   ├── AlertServlet.java
│               │   ├── SecurityFilter.java
│               │   └── TestServlet.java
│               └── utils/
│                   └── DBLogger.java
├── Test Packages/
│   └── <default package>/
├── Libraries/
│   ├── mysql-connector-j-9.1.0.jar
│   ├── mysql-connector-j-8.3.0.jar
│   ├── jakarta.mail-api-2.1.2.jar
│   ├── jakarta.mail-2.0.1.jar
│   ├── jakarta.activation-api-2.1.1.jar
│   └── jakarta.activation-2.0.1.jar
├── Test Libraries/
│   └── Apache Tomcat or TomEE (embedded)
└── Configuration Files/


```

##  Requirements

- **JDK 20**
- **Apache Tomcat or TomEE** (configured as your server)
- **MySQL database** (for logging suspicious activities)
- Compatible IDE: **NetBeans** recommended

---

##  Dependencies

Ensure the following JARs are available in your `Libraries`:
- MySQL Connector (`mysql-connector-j-*.jar`)
- Jakarta Mail API (`jakarta.mail-api`, `jakarta.mail`)
- Jakarta Activation (`jakarta.activation-api`, `jakarta.activation`)

---

##  How It Works

### Core Components:

- **SecurityFilter.java**  
  Intercepts incoming requests and performs basic filtering/suspicious pattern detection.

- **AlertServlet.java**  
  Sends alerts via email when a potential threat is detected.

- **DBLogger.java**  
  Handles logging of suspicious activity into a MySQL database.

- **requestLogs.jsp**  
  Displays logs from the database in a user-friendly format.

- **mail_success.jsp / mail_failure.jsp**  
  Confirmation views for alert email status.

- **TestServlet.java**  
  Simple test servlet to verify functionality.

---
## Setup Instructions

1. **Clone this repo** or download the project folder.

```bash

git clone https://github.com/rahul07890-dev/Web-Application-Firewall.git

```
2. **Database Setup**:
    - Create a MySQL database and a table for logging.
    - Update DB credentials inside `DBLogger.java`.

3. **Configure Mail Settings**:
    - Set your SMTP settings inside `AlertServlet.java`.

4. **Deploy to Tomcat/TomEE**:
    - Build and deploy the project using NetBeans or manually drop the WAR into Tomcat’s `webapps` folder.

5. **Run**:
    - Access `index.html` via `http://localhost:8080/WebApplicationFirewall/`.

---

##  Features

- Servlet Filter-based request scanning
- Database logging of suspicious activity
- Email alerts for potential threats
- Basic JSP interface for log viewing

---

##  TODO / Improvements

- Add more robust pattern detection
- Integrate CAPTCHA
- Role-based access for log viewers
- Log sanitization and encoding

---

## Contribution

Pull requests are welcome. For major changes, please open an issue first to discuss what you’d like to change.

---

## License
This project is licensed under the Apache License 2.0. See the LICENSE file for details.

---

## Acknowledgments
- Java Documentation: For guidance on servlet programming and request handling.
- Open-Source Community: For tools and inspiration to build secure applications.
