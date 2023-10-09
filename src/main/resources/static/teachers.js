$(document).ready(function () {
    const error = new URLSearchParams(window.location.search).get("error")
    const teachersList = JSON.parse(new URLSearchParams(window.location.search).get("teachersList"))
    const teachersTable = $("#teachersTable");

    if(error) {
        $('.error').show();

        $('.error').text(error);
    }

    teachersList.forEach(teacher => {
        const teachersId = teacher.id
        const teachersRow = $("<tr>")
            .append($("<td>").text(teacher.id))
            .append($("<td>").text(teacher.firstname))
            .append($("<td>").text(teacher.lastname))
            .append($("<td>").text(teacher.ssn))
            .append($("<td>").text(teacher.username))
            .append($("<td>").text(teacher.speciality))
            .append($("<td>").html(`<a href="http://localhost:8080/update.html?teacher=${encodeURIComponent(JSON.stringify(teacher))}">Update</a>`))
            .append($("<td>").html(`<button class="delete-button" data-teacher-id="${teacher.id}">Delete</button>`));

        teachersTable.append(teachersRow);
    });

    $(".delete-button").click(function() {
        const teacherId = $(this).data("teacher-id");
        const confirmation = confirm("Are you sure you want to delete?");

        if (confirmation) {
            window.location.href = `http://localhost:8080/delete.html?id=${teacherId}`;
        }
    });
});
