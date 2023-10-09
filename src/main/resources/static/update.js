
$(document).ready(function () {
    $.ajax({
        url: 'http://localhost:8080/api/teachers/options',
        type: 'get',
        dataType: 'json',
        contentType: 'application/json'
    })
        .done(function (response) {
            const usernameMap = response.usernames;
            const specialitiesMap = response.specialities;

            populateComboboxes(usernameMap, specialitiesMap);
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            const redirectURL = 'http://localhost:8080/main-menu.html?error=true';
            window.location.href = redirectURL;
        });

    const urlParams = new URLSearchParams(window.location.search);
    const teacher = JSON.parse(urlParams.get('teacher'));
    const idParam = teacher.id;
    const firstnameParam = teacher.firstname;
    const lastnameParam = teacher.lastname;
    const ssnParam = teacher.ssn;
    const usernameParam = teacher.username;
    const specialityParam = teacher.speciality;

    $('#tid').val(idParam);
    $('#firstname').val(firstnameParam);
    $('#lastname').val(lastnameParam);
    $('#ssn').val(ssnParam);

    function addOption(select, value, text, selected) {
        const option = $('<option>');
        option.val(value);
        option.text(text);
        if (selected) {
            option.prop('selected', true);
        }
        select.append(option);
    }

    function populateComboboxes(usernameMap, specialitiesMap) {
        const usernameSelect = $('#username');
        const specialitySelect = $('#speciality');

        for (const [key, value] of Object.entries(usernameMap)) {
            addOption(usernameSelect, key, value, value === usernameParam);
        }

        for (const [key, value] of Object.entries(specialitiesMap)) {
            addOption(specialitySelect, key, value, value === specialityParam);
        }
    }

    $("#updateTeacherForm").submit(function (event) {
        event.preventDefault();
        $(".error-message").hide()

        const formData = {
            id: $("#tid").val(),
            lastname: $("#lastname").val().trim(),
            firstname: $("#firstname").val().trim(),
            ssn: $("#ssn").val().trim(),
            user: $("#username").val(),
            speciality: $("#speciality").val(),
        };

        console.log(formData)

        const formDataJSON = JSON.stringify(formData);

        $.ajax({
            url: "http://localhost:8080/api/teachers",
            type: "PUT",
            data: formDataJSON,
            dataType: "json",
            contentType: "application/json"
        })
        .done(function (response) {
            const updatedTeacher = response;
            const url = `http://localhost:8080/updated.html?id=${encodeURIComponent(updatedTeacher.id)}&lastname=${encodeURIComponent(updatedTeacher.lastname)}&firstname=${encodeURIComponent(updatedTeacher.firstname)}&ssn=${encodeURIComponent(updatedTeacher.ssn)}&username=${encodeURIComponent(updatedTeacher.username)}&speciality=${encodeURIComponent(updatedTeacher.speciality)}`;
            window.location.href = url;
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 400) {
                const responseJSON = jqXHR.responseJSON;
                if (responseJSON) {
                    errorHandling(responseJSON);
                }
            } else {
                window.location.href = "http://localhost:8080/main-menu.html?error=true"
            }
        });
    });

    function errorHandling(errorsMap) {
        // Iterate through the error map
        for (const key in errorsMap) {
            if (errorsMap.hasOwnProperty(key)) {
                const error = errorsMap[key];
                const errorField = $(`.${key}-error`);

                errorField.text(error).show();
            }
        }
    }
});

