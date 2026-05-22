# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

---

## Admin User Stories

### User Story 1
**Title:**  
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**  
1. Admin can enter username and password.
2. System validates credentials.
3. Successful login redirects to dashboard.

**Priority:** High  
**Story Points:** 3  

**Notes:**  
- Invalid credentials should display an error.

---

### User Story 2
**Title:**  
_As an admin, I want to log out of the portal, so that system access remains protected._

**Acceptance Criteria:**  
1. Logout button is visible.
2. Session is terminated.
3. User is redirected to login page.

**Priority:** High  
**Story Points:** 2  

**Notes:**  
- Sessions should expire securely.

---

### User Story 3
**Title:**  
_As an admin, I want to add doctors to the portal, so that doctors can manage appointments._

**Acceptance Criteria:**  
1. Admin can enter doctor details.
2. Doctor profile is stored.
3. Confirmation message appears.

**Priority:** High  
**Story Points:** 5  

**Notes:**  
- Validate required fields.

---

### User Story 4
**Title:**  
_As an admin, I want to delete doctor profiles from the portal, so that outdated accounts are removed._

**Acceptance Criteria:**  
1. Admin selects doctor profile.
2. Confirmation is required.
3. Profile is deleted successfully.

**Priority:** Medium  
**Story Points:** 3  

**Notes:**  
- Prevent accidental deletion.

---

### User Story 5
**Title:**  
_As an admin, I want to run a stored procedure in MySQL CLI to retrieve appointments per month, so that I can track platform usage._

**Acceptance Criteria:**  
1. Stored procedure executes successfully.
2. Monthly statistics are displayed.
3. Results are accurate.

**Priority:** Medium  
**Story Points:** 4  

**Notes:**  
- Handle missing appointment data.

---

# Patient User Stories

### User Story 6
**Title:**  
_As a patient, I want to view doctors without logging in, so that I can explore options._

**Acceptance Criteria:**  
1. Doctor list loads publicly.
2. Profiles show specialization.
3. Navigation remains available.

**Priority:** Medium  
**Story Points:** 2  

**Notes:**  
- Protect private doctor data.

---

### User Story 7
**Title:**  
_As a patient, I want to sign up using email and password, so that I can book appointments._

**Acceptance Criteria:**  
1. Registration form accepts inputs.
2. Validation occurs.
3. Account is created.

**Priority:** High  
**Story Points:** 5  

**Notes:**  
- Prevent duplicate emails.

---

### User Story 8
**Title:**  
_As a patient, I want to log into the portal, so that I can manage bookings._

**Acceptance Criteria:**  
1. Login form works.
2. Credentials are verified.
3. Dashboard loads.

**Priority:** High  
**Story Points:** 3  

**Notes:**  
- Lock after repeated failures.

---

### User Story 9
**Title:**  
_As a patient, I want to log out, so that my account remains secure._

**Acceptance Criteria:**  
1. Logout option is available.
2. Session is terminated.
3. Login screen appears.

**Priority:** Medium  
**Story Points:** 2  

**Notes:**  
- Clear authentication tokens.

---

### User Story 10
**Title:**  
_As a patient, I want to book an hour-long appointment, so that I can consult with a doctor._

**Acceptance Criteria:**  
1. Available slots are shown.
2. Appointment is saved.
3. Confirmation appears.

**Priority:** High  
**Story Points:** 5  

**Notes:**  
- Prevent overlapping bookings.

---

### User Story 11
**Title:**  
_As a patient, I want to view upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**  
1. Upcoming appointments load.
2. Dates are visible.
3. Appointment details display correctly.

**Priority:** Medium  
**Story Points:** 3  

**Notes:**  
- Sort appointments chronologically.

---

# Doctor User Stories

### User Story 12
**Title:**  
_As a doctor, I want to log into the portal, so that I can manage appointments._

**Acceptance Criteria:**  
1. Login succeeds.
2. Dashboard loads.
3. Session persists.

**Priority:** High  
**Story Points:** 3  

**Notes:**  
- Secure authentication.

---

### User Story 13
**Title:**  
_As a doctor, I want to log out, so that my data remains protected._

**Acceptance Criteria:**  
1. Logout terminates session.
2. Redirect occurs.
3. Session expires.

**Priority:** High  
**Story Points:** 2  

**Notes:**  
- Prevent unauthorized reuse.

---

### User Story 14
**Title:**  
_As a doctor, I want to view my appointment calendar, so that I stay organized._

**Acceptance Criteria:**  
1. Calendar loads.
2. Appointments appear.
3. Filters work.

**Priority:** High  
**Story Points:** 5  

**Notes:**  
- Show appointment status.

---

### User Story 15
**Title:**  
_As a doctor, I want to mark my unavailability, so that patients see only available slots._

**Acceptance Criteria:**  
1. Doctor selects unavailable dates.
2. Calendar updates.
3. Booking is blocked.

**Priority:** High  
**Story Points:** 4  

**Notes:**  
- Existing appointments remain unaffected.

---

### User Story 16
**Title:**  
_As a doctor, I want to update my specialization and contact information, so that patients see accurate information._

**Acceptance Criteria:**  
1. Doctor edits profile.
2. Changes are saved.
3. Updated data is displayed.

**Priority:** Medium  
**Story Points:** 3  

**Notes:**  
- Validate contact details.

---

### User Story 17
**Title:**  
_As a doctor, I want to view patient details for upcoming appointments, so that I can prepare beforehand._

**Acceptance Criteria:**  
1. Appointment details load.
2. Patient information is visible.
3. Data is accurate.

**Priority:** Medium  
**Story Points:** 4  

**Notes:**  
- Respect privacy rules.
