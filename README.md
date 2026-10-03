# IT Support — Ticket Management System

---

## Περιγραφή | Description

**GR:** Ένα σύστημα διαχείρισης αιτημάτων τεχνικής υποστήριξης με σύστημα εγγραφής/σύνδεσης χρηστών, δημιουργία tickets και επικοινωνία μεταξύ χρηστών και IT Staff .

**EN:** A helpdesk ticket management system with user authentication, ticket creation and communication between users and IT Staff .

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.x |
| Frontend | Thymeleaf, HTML, CSS |
| Database | MySQL 8 |
| Security | Spring Security (BCrypt) |
| ORM | Spring Data JPA / Hibernate |
| Build | Maven |

---

## Αρχιτεκτονική | Architecture

```
Entity → Repository → Service → Controller → Thymeleaf View
```

```
src/main/java/com/itsupport/app/
├── model/         → User, Ticket, Comment
├── repository/    → JPA Repositories
├── service/       → Business Logic
├── controller/    → MVC Controllers
├── security/      → Spring Security Config
└── config/        → Data Initializer
```

---

## Εγκατάσταση | Installation

### Προαπαιτούμενα | Prerequisites
- Java 21
- Maven
- MySQL 8

### Βήματα | Steps

**1. Clone το repository**
```bash
git clone https://github.com/pannteros/itsupport.git
cd itsupport
```

**2. Ρύθμιση βάσης | Database setup**

Δεν χρειάζεται manual δημιουργία — το Spring Boot δημιουργεί αυτόματα τη βάση.

Άλλαξε το password στο `src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

**3. Build & Run**
```bash
mvn clean install
mvn spring-boot:run
```

**4. Άνοιξε τον browser | Open browser**
```
http://localhost:8080
```

---

## Default Accounts

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| User | user | user123 |

*(Δημιουργούνται αυτόματα κατά την εκκίνηση / Created automatically on startup)*

---

## Σελίδες | Pages

| URL | Περιγραφή | Πρόσβαση |
|-----|-----------|----------|
| `/tickets` | Τα tickets μου | USER |
| `/tickets/new` | Νέο ticket | USER |
| `/tickets/{id}` | Λεπτομέρειες ticket | USER |
| `/auth/login` | Σύνδεση | Όλοι |
| `/auth/register` | Εγγραφή | Όλοι |
| `/admin` | Dashboard | ADMIN |
| `/admin/tickets` | Όλα τα tickets | ADMIN |
| `/admin/tickets/{id}` | Διαχείριση ticket | ADMIN |
| `/admin/users` | Διαχείριση χρηστών | ADMIN |

---

## Βάση Δεδομένων | Database Schema

```
users    → id, username, email, password, full_name, department, role
tickets  → id, title, description, priority, status, category, created_at, created_by, assigned_to
comments → id, content, created_at, ticket_id, author_id
```

---

## Features

- Εγγραφή & Σύνδεση χρηστών (Spring Security + BCrypt)
- Δύο ρόλοι: USER και ADMIN
- Δημιουργία ticket με τίτλο, περιγραφή, κατηγορία και προτεραιότητα
- Κατηγορίες: Network, Hardware, Software
- Προτεραιότητες: Low, Medium, High
- Status tickets: Open, In Progress, Resolved, Closed
- Επικοινωνία USER ↔ IT Staff μέσω comments
- Admin: διαχείριση όλων των tickets, αλλαγή status, ανάληψη ticket
- Dashboard με στατιστικά tickets
- Αυτόματη δημιουργία βάσης & demo δεδομένων
