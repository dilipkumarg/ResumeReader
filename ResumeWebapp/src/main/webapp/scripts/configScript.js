/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 12/8/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */

$(document).ready(function () {
    $.ajax({type: "get",
        url: resumeReader.url.config,
        success: function (response) {
            var values = jQuery.parseJSON(response),
                ids = resumeReader.ids;
            $("#" + ids.txtResumeDir).val(values.resumeDir);
            $("#" + ids.txtEmployeeList).val(values.employeeListFile);

            // enabling text boxes
            $(".enableTextDiv").dblclick(function (evt) {
                var target = this.getAttribute("target");
                $(target).prop("disabled", false).focus();
            });

            // enabling submit button
            $(".inputBox").change(function () {
                $("#btnConfigUpdate").prop("disabled", false);
            });
        },
        error: function (xhr) {
            alert("Failed to load the configuration details. /n" + xhr.status + "/n" + xhr.statusText);
        }
    })
});

function updateConfiguration() {
    var urlParam = resumeReader.urlParams,
        ids = resumeReader.ids,
        btnUpdate = $("#btnConfigUpdate"),
        prevText = btnUpdate.text();
    $.ajax({type: "post",
        url: resumeReader.url.config,
        data: { resumeDir: $("#" + ids.txtResumeDir).val(),
            employeeFile: $("#" + ids.txtEmployeeList).val()
        },
        beforeSend: function (xhr) {
            btnUpdate.text("please wait..");
        },
        success: function (response) {
            $("#alertText").text(response);
            var alertBox = $("#alertBox");
            alertBox.removeClass("hide");
            alertBox.addClass("alert-success");
        },
        error: function (xhr) {
            $("#alertText").html("An error occured:" + xhr.status + "<br />" + xhr.statusText);
            var alertBox = $("#alertBox");
            alertBox.removeClass("hide");
            alertBox.addClass("alert-error");
        },
        complete: function (xhr, status) {
            btnUpdate.text(prevText);
            btnUpdate.prop("disabled", "true");
            $(".inputBox").prop("disabled", "true");
        }
    });
}
function updateIndex(e, cleanUpdate) {
    var prevText = e.target.innerHTML,
        progressBarDiv = $("#progressBarDiv"),
        progressBar = $("#progressBar");
    $.ajax({type: "post",
        url: resumeReader.url.update,
        data: { cleanUpdate: cleanUpdate
        },
        beforeSend: function (xhr) {
            e.target.innerHTML = "please wait..";
            progressBarDiv.removeClass("hide");
            var i = 0;
            setInterval(function () {
                if (i < 80) {
                    progressBar.css("width", i + "%");
                    i++;
                }
            }, 100);
        },
        success: function (response) {
            $("#alertText").text(response);
            var alertBox = $("#alertBox");
            alertBox.removeClass("hide");
            alertBox.addClass("alert-success");
        },
        error: function (xhr) {
            $("#alertText").html("An error occured:" + xhr.status + "<br />" + xhr.statusText);
            var alertBox = $("#alertBox");
            alertBox.removeClass("hide");
            alertBox.addClass("alert-error");
        },
        complete: function (xhr, status) {
            progressBar.css("width", "100%");
            e.target.innerHTML = prevText;
            progressBarDiv.addClass("hide");
            progressBar.css("width", "0%");
        }
    });
}