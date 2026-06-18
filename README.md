# 🏥 Smart Healthcare Management System

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen)
![React](https://img.shields.io/badge/React-18-blue)
![JWT](https://img.shields.io/badge/JWT-Security-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-lightgrey)
![Tailwind](https://img.shields.io/badge/Tailwind-CSS-38B2AC)

A full‑stack web application designed to digitize a healthcare facility's operations. It supports **four roles** (Admin, Doctor, Receptionist, Patient) with secure authentication, appointment scheduling, medical records, prescriptions, billing, PDF reports, and email notifications.

---

## 📋 Features

- ✅ **Role‑Based Access Control** – JWT authentication with Spring Security.
- ✅ **Patient Registration & Management** – CRUD operations, profile management.
- ✅ **Doctor Management** – Specialization, availability, profile updates.
- ✅ **Appointment Scheduling** – Book, cancel, filter by patient/doctor.
- ✅ **Medical Records & Prescriptions** – Add diagnoses and prescribe medicines.
- ✅ **Billing** – Create bills, mark as paid, track status.
- ✅ **PDF Reports** – Download patient medical reports.
- ✅ **Email Reminders** – Automatic notifications via Mailtrap (SMTP).
- ✅ **Dashboard with Charts** – Recharts for appointment statistics.
- ✅ **Swagger API Documentation** – All endpoints documented and testable.
- ✅ **Layered Architecture** – Spring Boot service/controller/repository pattern.
- ✅ **Validation & Exception Handling** – Global error handling with proper HTTP status codes.

---

## 🏗️ Tech Stack

| Layer          | Technology                                          |
|----------------|-----------------------------------------------------|
| Backend        | Java 17, Spring Boot 3.4.1, Spring Security, Spring Data JPA |
| Security       | JWT (jjwt), BCrypt, Role‑Based Access Control      |
| Database       | MySQL 8.0                                           |
| Frontend       | React 18, React Router, Axios, Tailwind CSS, Recharts, React‑Toastify |
| Email          | Spring Mail + Mailtrap SMTP                         |
| PDF            | OpenPDF (LibrePDF)                                  |
| API Docs       | Swagger (SpringDoc OpenAPI)                         |
| Build Tool     | Maven                                               |

---

## 🚀 Getting Started

### Prerequisites
- Java 17
- Node.js 16+
- MySQL 8.0
- Maven

### Backend Setup
1. Clone the repository.
2. Open `healthcare-backend` in IntelliJ.
3. Update `src/main/resources/application.yml` with your MySQL credentials and Mailtrap details.
4. Run `HealthcarebackendApplication.java`.
5. Swagger UI → `http://localhost:8080/swagger-ui.html`

### Frontend Setup
1. Navigate to `smart-health-frontend`.
2. Run `npm install`.
3. Start with `npm start`.
4. Open `http://localhost:3000`.

### Default Admin Credentials
- **Username:** admin  
- **Password:** admin123  
*(Register an Admin user first via the register page.)*

---

## 📡 API Documentation

All REST endpoints are documented with Swagger.  
After starting the backend, open:  
`http://localhost:8080/swagger-ui.html`

---

## 📄 Report & Documentation

A complete project report (SRS, user manual, technical manual, diagrams) is included in the `docs/` folder.

---

## 📌 Future Enhancements

- [ ] Docker containerization
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] AI‑powered symptom checker
- [ ] Mobile app (React Native)
- [ ] Audit logging

---

## 📧 Contact

**Malik Muhammad Uzair Zareef**  
[LinkedIn](www.linkedin.com/in/ozair-malik-43b156294) | [Email](mailto:uzairzareef001@gmail.com)

---

⭐ If you find this project useful, please give it a star!
*(Add your screenshots here. Upload them to the repo and link them with `![alt](url)`)*

Example:
