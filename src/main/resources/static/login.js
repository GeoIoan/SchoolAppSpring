$(document).ready(function () {
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        const loginDTO = {
            username: $("#username").val(),
            password: $("#password").val()
        }

        const jsonLoginDTO = JSON.stringify(loginDTO)

        $.ajax({
            url: "http://localhost:8080/api/users/login",
            type: "POST",
            data: jsonLoginDTO,
            contentType: "application/json"
            })
            .done(function (response) {
                console.log(response)
                window.location.href = "http://localhost:8080/main-menu.html"
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 400) {
                    //will handle this case later
                } else if (jqXHR.status === 401) {
                    $("#errorText").show()
                    $("#errorText").text("Λάθος Στοιχεία")
                } else{
                    $("#errorText").show()
                    $("#errorText").text("Πρόβλημα κατα την φόρτωση της σελίδας")
                }
            })
    })
})