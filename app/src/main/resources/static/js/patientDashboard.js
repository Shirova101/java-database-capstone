/* patientDashboard.js */

import { createDoctorCard } from "../components/doctorCard.js";
import { openModal } from "../components/modals.js";
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import { patientSignup, patientLogin } from "./services/patientServices.js";

// Initial page load
document.addEventListener("DOMContentLoaded", () => {
    loadDoctorCards();
    bindEvents();
});

// Attach UI event listeners
function bindEvents() {
    document.getElementById("patientSignup")
        ?.addEventListener("click", () => openModal("patientSignup"));

    document.getElementById("patientLogin")
        ?.addEventListener("click", () => openModal("patientLogin"));

    document.getElementById("searchBar")
        ?.addEventListener("input", filterDoctorsOnChange);

    document.getElementById("filterTime")
        ?.addEventListener("change", filterDoctorsOnChange);

    document.getElementById("filterSpecialty")
        ?.addEventListener("change", filterDoctorsOnChange);
}

// Fetch and display doctors
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Failed to load doctors:", error);
    }
}

// Shared doctor renderer
function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";

    if (!doctors?.length) {
        contentDiv.innerHTML =
            "<p>No doctors found with the given filters.</p>";
        return;
    }

    doctors.forEach(doctor => {
        contentDiv.appendChild(
            createDoctorCard(doctor)
        );
    });
}

// Search + filter doctors
async function filterDoctorsOnChange() {
    try {
        const name =
            document.getElementById("searchBar")
                ?.value.trim() || null;

        const time =
            document.getElementById("filterTime")
                ?.value || null;

        const specialty =
            document.getElementById("filterSpecialty")
                ?.value || null;

        const response =
            await filterDoctors(
                name,
                time,
                specialty
            );

        renderDoctorCards(
            response.doctors || []
        );

    } catch (error) {
        console.error(
            "Filtering failed:",
            error
        );

        alert(
            "An error occurred while filtering doctors."
        );
    }
}

// Patient signup
window.signupPatient = async function () {
    try {
        const patient = {
            name:
                document.getElementById("name").value,

            email:
                document.getElementById("email").value,

            password:
                document.getElementById("password").value,

            phone:
                document.getElementById("phone").value,

            address:
                document.getElementById("address").value
        };

        const result =
            await patientSignup(patient);

        if (result.success) {
            alert(result.message);

            document.getElementById("modal")
                .style.display = "none";

            window.location.reload();
            return;
        }

        alert(result.message);

    } catch (error) {
        console.error(
            "Signup failed:",
            error
        );

        alert(
            "Signup failed."
        );
    }
};

// Patient login
window.loginPatient = async function () {
    try {
        const credentials = {
            email:
                document.getElementById("email").value,

            password:
                document.getElementById("password").value
        };

        const response =
            await patientLogin(
                credentials
            );

        if (response.ok) {
            const result =
                await response.json();

            localStorage.setItem(
                "token",
                result.token
            );

            selectRole(
                "loggedPatient"
            );

            window.location.href =
                "/pages/loggedPatientDashboard.html";

            return;
        }

        alert(
            "Invalid credentials!"
        );

    } catch (error) {
        console.error(
            "Login failed:",
            error
        );

        alert(
            "Failed to login."
        );
    }
};