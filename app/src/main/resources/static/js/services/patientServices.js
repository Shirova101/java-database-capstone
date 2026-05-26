/* patientServices.js
   Handles Patient-related API communication
*/

import { API_BASE_URL } from "../config/config.js";

/* Base endpoint */
const PATIENT_API = `${API_BASE_URL}/patient`;


/*
--------------------------------
Patient Signup
POST /patient

Input:
{
  name,
  email,
  password
}

Returns:
{
  success,
  message
}
--------------------------------
*/
export async function patientSignup(data) {
    try {

        const response = await fetch(
            PATIENT_API,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(data)
            }
        );

        const result = await response.json();

        if (!response.ok) {
            throw new Error(
                result.message
            );
        }

        return {
            success: true,
            message: result.message
        };

    } catch (error) {

        console.error(
            "patientSignup:",
            error
        );

        return {
            success: false,
            message: error.message
        };
    }
}



/*
--------------------------------
Patient Login

POST /patient/login

Returns:
Fetch response
--------------------------------
*/
export async function patientLogin(data) {

    console.log(
        "patientLogin:",
        data
    );

    return fetch(

        `${PATIENT_API}/login`,

        {
            method: "POST",

            headers: {
                "Content-Type":
                    "application/json"
            },

            body:
                JSON.stringify(
                    data
                )
        }

    );

}



/*
--------------------------------
Get Logged Patient

GET /patient/{token}

Returns:
patient
or
null
--------------------------------
*/
export async function getPatientData(token) {

    try {

        const response =
            await fetch(

                `${PATIENT_API}/${token}`

            );

        const data =
            await response.json();


        if (response.ok) {

            return data.patient;

        }

        return null;

    } catch (error) {

        console.error(
            "getPatientData:",
            error
        );

        return null;

    }

}



/*
--------------------------------
Get Patient Appointments

Used by:
- Doctor Dashboard
- Patient Dashboard

GET:

/patient/id/user/token

Returns:
appointments[]
--------------------------------
*/
export async function getPatientAppointments(
    id,
    token,
    user
) {

    try {

        const response =
            await fetch(

                `${PATIENT_API}/${id}/${user}/${token}`

            );

        const data =
            await response.json();


        console.log(
            data.appointments
        );


        if (response.ok) {

            return (
                data.appointments
            );

        }

        return null;

    } catch (error) {

        console.error(
            "getPatientAppointments:",
            error
        );

        return null;

    }

}




/*
--------------------------------
Filter Appointments

GET:

filter/
condition/
name/
token

Returns:

{
 appointments:[]
}
--------------------------------
*/
export async function filterAppointments(
    condition,
    name,
    token
) {

    try {

        const response =
            await fetch(

                `${PATIENT_API}/filter/${condition}/${name}/${token}`,

                {
                    method: "GET",

                    headers: {
                        "Content-Type":
                            "application/json"
                    }
                }

            );


        if (!response.ok) {

            console.error(
                "Filter request failed"
            );

            return {
                appointments: []
            };

        }


        return await response.json();


    } catch (error) {

        console.error(
            "filterAppointments:",
            error
        );

        alert(
            "Something went wrong!"
        );

        return {
            appointments: []
        };

    }

}
