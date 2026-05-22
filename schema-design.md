# Schema Design

## MySQL Database Design

Structured and relational data is stored in MySQL because appointments, users, and administrative actions require strong consistency and relationships.

---

### Table: patients

- id: INT, Primary Key, AUTO_INCREMENT
- first_name: VARCHAR(100), NOT NULL
- last_name: VARCHAR(100), NOT NULL
- email: VARCHAR(255), UNIQUE, NOT NULL
- password_hash: VARCHAR(255), NOT NULL
- phone_number: VARCHAR(20), UNIQUE
- date_of_birth: DATE
- created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

Notes:
- Email must remain unique.
- Passwords should never be stored in plain text.

---

### Table: doctors

- id: INT, Primary Key, AUTO_INCREMENT
- first_name: VARCHAR(100), NOT NULL
- last_name: VARCHAR(100), NOT NULL
- specialization: VARCHAR(150), NOT NULL
- email: VARCHAR(255), UNIQUE, NOT NULL
- phone_number: VARCHAR(20), UNIQUE
- availability_status: BOOLEAN DEFAULT TRUE
- created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

Notes:
- Availability supports appointment scheduling.
- Specialization helps search and filtering.

---

### Table: appointments

- id: INT, Primary Key, AUTO_INCREMENT
- patient_id: INT, Foreign Key → patients(id), NOT NULL
- doctor_id: INT, Foreign Key → doctors(id), NOT NULL
- appointment_time: DATETIME, NOT NULL
- duration_minutes: INT DEFAULT 60
- status: ENUM('Scheduled','Completed','Cancelled'), NOT NULL
- created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

Constraints:
- FOREIGN KEY(patient_id) REFERENCES patients(id)
- FOREIGN KEY(doctor_id) REFERENCES doctors(id)

Notes:
- Appointment history should be preserved.
- Prevent overlapping doctor appointments at application level.

---

### Table: admin

- id: INT, Primary Key, AUTO_INCREMENT
- username: VARCHAR(100), UNIQUE, NOT NULL
- email: VARCHAR(255), UNIQUE, NOT NULL
- password_hash: VARCHAR(255), NOT NULL
- role: VARCHAR(50) DEFAULT 'ADMIN'
- created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP

Notes:
- Separate admin accounts improve security and auditing.

---

### Table: doctor_availability

- id: INT, Primary Key, AUTO_INCREMENT
- doctor_id: INT, Foreign Key → doctors(id)
- available_date: DATE NOT NULL
- start_time: TIME NOT NULL
- end_time: TIME NOT NULL

Constraints:
- FOREIGN KEY(doctor_id) REFERENCES doctors(id)

Notes:
- Availability stored separately for flexibility.

---

## MongoDB Collection Design

MongoDB stores flexible document-based data that may evolve over time.

### Collection: prescriptions

```
{
  "_id": "ObjectId('66abc111223344')",
  "appointmentId": 105,
  "patientId": 12,
  "doctorId": 4,

  "diagnosis": {
    "primary": "Seasonal Flu",
    "secondary": [
      "Mild Dehydration"
    ]
  },

  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Twice Daily",
      "durationDays": 5
    },
    {
      "name": "ORS",
      "dosage": "1 packet",
      "frequency": "After meals",
      "durationDays": 3
    }
  ],

  "doctorNotes": "Increase fluid intake and monitor temperature.",

  "attachments": [
    {
      "type": "pdf",
      "url": "/reports/prescription_105.pdf"
    }
  ],

  "followUp": {
    "required": true,
    "days": 7
  },

  "createdAt": "2026-05-22T10:30:00Z"
}
```

Design Notes:

- Prescription structure may evolve frequently → ideal for MongoDB.
- Embedded medication arrays avoid unnecessary joins.
- References to appointmentId, patientId, and doctorId preserve integration with MySQL.
- Attachments and nested notes provide flexibility for future features.
