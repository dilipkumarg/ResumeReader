/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 12/8/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */

resumeReader.Configuration = function () {
    "use strict";
    function loadConfigValues() {
        $.ajax({type: "get",
            url: resumeReader.url.config,
            success: function (response) {
                var values = jQuery.parseJSON(response),
                    ids = resumeReader.ids;
                $("#" + ids.txtResumeDir).val(values.resumeDir);
                $("#" + ids.txtEmployeeList).val(values.employeeListFile);

                // enabling submit button
                $(".inputBox").change(function () {
                    $("#btnConfigUpdate").prop("disabled", false);
                });

                //$("#keyInputModal").modal("show");
            },
            error: function (xhr) {
                printErrorAlert(xhr);
            }
        });
    }

    function printErrorAlert(xhr) {
        $("#alertText").html("An error occured:" + xhr.status + "<br />" + xhr.statusText +
            "<br/>" + xhr.responseText);
        var alertBox = $("#alertBox");
        alertBox.removeClass("hide");
        alertBox.removeClass("alert-success");
        alertBox.addClass("alert-error");
    }

    function printSuccessAlert(response) {
        $("#alertText").text(response);
        var alertBox = $("#alertBox");
        alertBox.removeClass("hide");
        alertBox.removeClass("alert-error");
        alertBox.addClass("alert-success");
    }

    function updateConfiguration() {
        var urlParam = resumeReader.urlParams,
            ids = resumeReader.ids,
            btnUpdate = $("#btnConfigUpdate"),
            prevText = btnUpdate.text(),
            resumeDir = $("#" + ids.txtResumeDir),
            employeeFile = $("#" + ids.txtEmployeeList);
        // building data object, based on user requirements.
        //var userData = {};
        //alert($("#" + ids.btnResumeToggle).active);

        $.ajax({type: "post",
            url: resumeReader.url.config,
            data: { resumeDir: resumeDir.val(),
                employeeFile: employeeFile.val(),
                securityKey: prompt("Please enter security key")
            },
            beforeSend: function (xhr) {
                btnUpdate.text("please wait..");
            },
            success: function (response) {
                printSuccessAlert(response);
            },
            error: function (xhr) {
                printErrorAlert(xhr);
                loadConfigValues();
            },
            complete: function (xhr, status) {
                btnUpdate.text(prevText);
                btnUpdate.prop("disabled", "true");
                $(".inputBox").prop("disabled", "true");
            }
        });
    }

    function updateIndex(e, securityKey) {
        if (typeof securityKey == 'undefined' || securityKey == null ||securityKey === "") {
            // doing nothing on empty security key.
            return;
        }
        var prevText = e.target.innerHTML,
            progressBarDiv = $("#progressBarDiv"),
            progressBar = $("#progressBar"),
            interval;
        $.ajax({type: "post",
            url: resumeReader.url.update,
            data: { cleanUpdate: $("#" + e.target.id).attr("data-value"),
                securityKey: securityKey
            },
            beforeSend: function (xhr) {
                $("#" + e.target.id).button("loading");
                progressBarDiv.removeClass("hide");
                var i = 0;
                interval = setInterval(function () {
                    if (i < 90) {
                        progressBar.css("width", i + "%");
                        i++;
                    }
                }, 100);
            },
            success: function (response) {
                printSuccessAlert(response);
            },
            error: function (xhr) {
                printErrorAlert(xhr);
            },
            complete: function (xhr, status) {
                progressBar.css("width", "100%");
                clearInterval(interval);
                $("#" + e.target.id).button("reset");
                // closing progress bar after 1 sec
                setTimeout(function () {
                    progressBarDiv.addClass("hide");
                    progressBar.css("width", "0%");
                }, 1000);
            }
        });
    }

    return {
        loadConfigValues: function () {
            return loadConfigValues();
        },
        updateIndex: function (e, cleanUpdate) {
            return updateIndex(e, cleanUpdate);
        },
        updateConfiguration: function () {
            return updateConfiguration();
        }

    }
}();