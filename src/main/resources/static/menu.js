$(document).ready(function () {
    $.ajax({
        url:'http://localhost:8080/api/teachers/options',
        type: "get",
        dataType: "json",
        contentType: "application/json"
    })
        .done(function (response) {
                populateComboboxes(response.usernames, response.specialities);
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            const redirectURL = `main-menu.html?error=true`;
            window.location.href = redirectURL;
        });

    function populateComboboxes(usernames, specialities) {
        const usernamesMap = usernames;
        const specialitiesMap = specialities;
        const usernamesSelect = $("#username");
        const specialitiesSelect = $("#speciality");

        for (const key in usernamesMap) {
            if (usernamesMap.hasOwnProperty(key)) {
                const value = usernamesMap[key];

                const option = document.createElement("option");
                option.value = key;
                option.text = value;

                usernamesSelect.append(option);
            }
        }

        for (const key in specialitiesMap) {
            if (specialitiesMap.hasOwnProperty(key)) {
                const value = specialitiesMap[key];

                const option = document.createElement("option");
                option.value = key;
                option.text = value;

                specialitiesSelect.append(option);
            }
        }
    }

    $("#search-form").submit(function (event) {
        event.preventDefault();

        const lastname = $("#search-lastname").val().trim()

        $.ajax({
            url: `http://localhost:8080/api/teachers?lastname=${lastname}`,
            type: "GET"
        })
        .done(function (response) {
                const teachersList = encodeURIComponent(JSON.stringify(response));
                window.location.href=`http://localhost:8080/teachers.html?teachersList=${teachersList}`;
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
           if (jqXHR.status === 400) {
               $('.not-found-error').show()
           } else {
               window.location.href = "http://localhost:8080/main-menu.html?error=true"
           }
        })
    })

    $("#insert-form").submit(function (event) {
        event.preventDefault();
        $(".error-message").hide()

        const formData = {
            lastname: $("#lastname").val().trim(),
            firstname: $("#firstname").val().trim(),
            ssn: $("#ssn").val().trim(),
            user: $("#username").val().trim(),
            speciality: $("#speciality").val().trim(),
        };

        const formDataJSON = JSON.stringify(formData);

        $.ajax({
            url: "http://localhost:8080/api/teachers",
            type: "POST",
            data: formDataJSON,
            dataType: "json",
            contentType: "application/json"
        })
            .done(function (response) {
                    const insertedTeacher = response;
                    const url = `http://localhost:8080/inserted.html?lastname=${encodeURIComponent(insertedTeacher.lastname)}&firstname=${encodeURIComponent(insertedTeacher.firstname)}&ssn=${encodeURIComponent(insertedTeacher.ssn)}&username=${encodeURIComponent(insertedTeacher.username)}&speciality=${encodeURIComponent(insertedTeacher.speciality)}`;
                    window.location.href = url;
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 400) {
                    const responseJSON = jqXHR.responseJSON;
                    if (responseJSON) {
                        errorHandling(responseJSON);
                    }
                } else{
                    window.location.href = "http://localhost:8080/main-menu.html?error=true"
                }
            });
    });

    function errorHandling(errorsMap) {
        for (const key in errorsMap) {
            if (errorsMap.hasOwnProperty(key)) {
                const error = errorsMap[key];
                const errorField = $(`.${key}-error`);

                errorField.text(error).show();
            }
        }
    }
})