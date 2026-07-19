# 📖 API Technical Specification

This document details the functional REST endpoints exposed by the Online Complaint Management System.

## 🔑 Authentication Endpoints

### 1. User Authentication
* **Endpoint:** `POST /api/auth/login`
* **Payload:**
```json
{
  "email": "user@example.com",
  "password": "user"
}

Response (200 OK): Returns an access token required for the X-Auth header on all protected resources.

📋 Complaint Lifecycle Endpoints
2. File a New Complaint
Endpoint: POST /api/complaints

Header: X-Auth: <USER_TOKEN>

Response (200 OK): Instantiates a complaint with state OPEN.

3. Assign Complaint (Admin Only)
Endpoint: PATCH /api/complaints/{id}/assign

Header: X-Auth: <ADMIN_TOKEN>

Payload: {"assigneeId": 101}

Response (200 OK): Transitions state to IN_PROGRESS.

4. Resolve Complaint (Admin Only)
Endpoint: PATCH /api/complaints/{id}/status

Header: X-Auth: <ADMIN_TOKEN>

Payload: {"status": "RESOLVED"}

5. Submit User Feedback Comment
Endpoint: POST /api/complaints/{id}/comments

Header: X-Auth: <USER_TOKEN>

6. Close Ticket
Endpoint: PATCH /api/complaints/{id}/status

Header: X-Auth: <USER_TOKEN>

Payload: {"status": "CLOSED"}


---

### 🏁 Final System Check
Excellent! With these files created, your entire workspace structure is fully documented:
* `logs/` ✅ (Populated)
* `outputs/` ✅ (Populated)
* `data/` ✅ (Populated)
* `docs/` ✅ (Populated)

Is there anything else left to configure, or are you ready to run a final check on your project?