/**
 * Created with JetBrains WebStorm. User: dilip Date: 12/8/13 Time: 2:33 PM To
 * change this template use File | Settings | File Templates.
 */

resumeReader.Configuration = function () {
    "use strict";
    var ids = resumeReader.ids;

    function loadConfigValues() {
        $.ajax({type: "get",
            url: resumeReader.url.config,
            success: function (response) {
                var values = jQuery.parseJSON(response);
                $("#" + ids.txtResumeDir).val(values.resumeDir);
                $("#" + ids.txtEmployeeList).val(values.employeeListFile);

                // enabling submit button
                $(".inputBox").change(function () {
                    $("#" + ids.btnConfigUpdate).prop("disabled", false);
                });
            },
            error: function (xhr) {
                printErrorAlert(xhr);
            }
        });
    }

    function printErrorAlert(xhr) {
        $("#alertText").html("An error occured:" + xhr.status + "<br />" + xhr.statusText +
            "<br/>" + xhr.responseText);
        var alertBox = $("#" + ids.alertBox);
        alertBox.removeClass("hide");
        alertBox.removeClass("alert-success");
        alertBox.addClass("alert-error");
    }

    function printSuccessAlert(response) {
        $("#alertText").text(response);
        var alertBox = $("#" + ids.alertBox);
        alertBox.removeClass("hide");
        alertBox.removeClass("alert-error");
        alertBox.addClass("alert-success");
    }

    function doUpdateConfig(userData, btnUpdate, securityKey) {
        var prevText = btnUpdate.text();
        $.ajax({type: "post",
            url: resumeReader.url.config,
            data: userData,
            beforeSend: function (xhr) {
                // adding security key
                xhr.setRequestHeader(resumeReader.urlParams.securityKey, securityKey);
                btnUpdate.text("please wait..");
            },
            success: function (response) {
                printSuccessAlert(response);
                loadConfigValues();
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

    function updateConfiguration() {
        var btnUpdate = $("#" + ids.btnConfigUpdate),
            resumeDir = $("#" + ids.txtResumeDir),
            employeeFile = $("#" + ids.txtEmployeeList);
        // building data object, based on user requirements.
        var userData = {
            resumeDir: (resumeDir.attr("disabled") === "disabled") ? "" : resumeDir.val(),
            employeeFile: (employeeFile.attr("disabled") === "disabled") ? "" : employeeFile.val()
        };
        // updating if and only if something is changed
        if (userData.resumeDir !== "" || userData.employeeFile !== "") {
            $('body').myPrompt({}, function (result) {
                // checking if user pressed ok or not
                if (result !== null) {
                    doUpdateConfig(userData, btnUpdate, result);
                }
            });
        } else {
            $("#alertText").html("Text boxes are in disabled state, so nothing to update..!");
            var alertBox = $("#alertBox");
            alertBox.removeClass("hide");
            alertBox.removeClass("alert-success");
            alertBox.addClass("alert-error");
            loadConfigValues();
        }
    }


    function doUpdate(userData, button, securityKey) {
        var progressBarDiv = $("#" + ids.progressBarDiv),
            progressBar = $("#" + ids.progressBar),
            interval;
        $.ajax({type: "post",
            url: resumeReader.url.update,
            data: userData,
            beforeSend: function (xhr) {
                // adding security key
                xhr.setRequestHeader(resumeReader.urlParams.securityKey, securityKey);
                button.button("loading");
                progressBarDiv.removeClass("hide");
                var i = 0;
                // for showing progress bar.
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
                button.button("reset");
                // closing progress bar after 1 sec
                setTimeout(function () {
                    progressBarDiv.addClass("hide");
                    progressBar.css("width", "0%");
                }, 1000);
            }
        });
    }

    function updateIndex(e) {
        $("body").myPrompt({}, function (result) {
            if (typeof result === 'undefined' || result === null || result === "") {
                // doing nothing on empty security key.
                return;
            }
            // getting selected button.
            // data-value is the attribute holding clean update and normal
            // update
            var btn = $("#" + e.target.id),
                data = {
                    cleanUpdate: btn.attr("data-value")
                };
            // performing update
            doUpdate(data, btn, result);
        });
    }


    function filterByName(str, targetDiv) {
        var selected = targetDiv.find("label");
        for (var i = 0; i < selected.length; i++) {
            var selector = selected[i].getElementsByTagName("span")[0];
            if (selector.innerHTML.toLowerCase().lastIndexOf(str.toLowerCase()) === -1) {
                selected[i].setAttribute("class", "hide");
            } else {
                selected[i].setAttribute("class", "span6 show checkbox");
            }
        }
    }

    function printList(fileObj, targetDiv) {
        var keys = Object.keys(fileObj);
        keys = keys.sort();
        for (var i = 0; i < keys.length; i++) {
            targetDiv.append("<label class='span6 show checkbox' title='" + keys[i] + "'>" +
                "<input type='checkbox' class='file-check' value='" + keys[i] + "'>" +
                " <span>" + fileObj[keys[i]] + "</span>" +
                "</label>");
        }
        // adding events for the checkboxes
        var cbFile = $(".file-check");
        // removing previous event
        cbFile.off("change");
        // for toggle select all and select filtered check box
        cbFile.on("change", function () {
            checkSelectAll();
            checkSelectFiltered();
        });
    }

    function checkSelectAll() {
        if ($(".file-check:checked").length === $(".file-check").length) {
            $("#" + ids.cbSelectAll).prop("checked", true);
        } else {
            $("#" + ids.cbSelectAll).prop("checked", false);
        }
    }

    function checkSelectFiltered() {
        var showedBoxes = $(".show");
        if (showedBoxes.find(".file-check:checked").length === showedBoxes.find(".file-check").length) {
            $("#" + ids.cbSelectFiltered).prop("checked", true);
        } else {
            $("#" + ids.cbSelectFiltered).prop("checked", false);
        }
    }

    function printFileFetchError(xhr, targetDiv) {
        targetDiv.html("An error occured:" + xhr.status + "<br />" + xhr.statusText +
            "<br/>" + xhr.responseText);
    }

    function loadFileList(targetDiv) {
        $.ajax({
            url: resumeReader.url.delete,
            success: function (response) {
                targetDiv.empty();
                printList(JSON.parse(response), targetDiv);
            },
            error: function (xhr) {
                printFileFetchError(xhr, targetDiv);
            }
        });
    }

    function loadEmployeeList(targetDiv) {
        $.ajax({
            url: "resumereader/exceldelete",
            success: function (response) {
                targetDiv.empty();
                printList(JSON.parse(response), targetDiv);
            },
            error: function (xhr) {
                printFileFetchError(xhr, targetDiv);
            }
        });
    }

    function getSelectedFiles(targetDiv) {
        var checkBoxes = targetDiv.find(":checkbox"),
            selectedBoxes = [];

        for (var i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].checked) {
                selectedBoxes.push(checkBoxes[i].getAttribute("value"));
            }
        }
        return selectedBoxes;
    }

    function deleteFiles() {
        var deleteModal = $("#" + ids.modalDelete);
        var selectedBoxes = getSelectedFiles(deleteModal.find(".modal-body"));
        if (selectedBoxes.length > 0) {
            var res = confirm("Are you sure you want to delete " + selectedBoxes.length + " file(s)?");
            if (res) {
                deleteModal.myPrompt({showHide: true}, function (res) {
                    $.ajax({
                        type: "post",
                        url: resumeReader.url.delete,
                        data: {
                            filesList: JSON.stringify(selectedBoxes)
                        },
                        beforeSend: function (xhr) {
                            // adding security key in request header
                            xhr.setRequestHeader(resumeReader.urlParams.securityKey, res);
                        },
                        success: function (res) {
                            printSuccessAlert(res);
                        },
                        error: function (xhr) {
                            printErrorAlert(xhr);
                        },
                        complete: function () {
                            deleteModal.modal("hide");
                        }
                    });
                });
            }
        }
    }

    function deleteEmployees() {
        var deleteModal = $("#employeeDeleteModal");
        var selectedBoxes = getSelectedFiles(deleteModal.find(".modal-body"));
        if (selectedBoxes.length > 0) {
            var res = confirm("Are you sure you want to delete " + selectedBoxes.length + " employees?");
            if (res) {
                deleteModal.myPrompt({showHide: true}, function (res) {
                    $.ajax({
                        type: "post",
                        url: "resumereader/exceldelete",
                        data: {
                            employeeList: JSON.stringify(selectedBoxes),
                            accessKey: res
                        },
                        beforeSend: function (xhr) {
                            // adding security key in request header
                            xhr.setRequestHeader(resumeReader.urlParams.securityKey, res);
                        },
                        success: function (res) {
                            printSuccessAlert(res);
                        },
                        error: function (xhr) {
                            printErrorAlert(xhr);
                        },
                        complete: function () {
                            deleteModal.modal("hide");
                        }
                    });
                });
            }
        }
    }

    var DZintialized = false;
    var myDropzone;

    function configDropZone(securityKey) {
        if (!DZintialized) {
            DZintialized = true;
            myDropzone = new Dropzone("#" + ids.dropZone, {
                autoProcessQueue: false,
                acceptedFiles: ".pdf,.doc,.docx",
                addRemoveLinks: true,
                parallelUploads: 10,
                maxFiles: 10,
                // uploadMultiple:true,
                dictInvalidFileType: "Please upload only doc, docx and pdf files"
            });
            $("#" + ids.btnUploadAll).click(function () {
                    myDropzone.processQueue();
                }
            );
            $("#" + ids.modalUpload).on("hidden", function () {
                myDropzone.removeAllFiles();
            });
        } else {
            // removing previous security key
            myDropzone.off("sending");
        }
        // adding security key in the request header for authentication
        myDropzone.on("sending", function (file, xhr, formData) {
            xhr.setRequestHeader(resumeReader.urlParams.securityKey, securityKey);

        });
        myDropzone.on("complete", function (file) {
            // printing error message
            $(file.previewTemplate).find(".dz-error-message").html("<label>" +
                "<strong>Error Code: " + file.xhr.status + "</strong>" +
                "<br>" + file.xhr.statusText + "</label>");

        });
    }

    return {
        loadConfigValues: function () {
            return loadConfigValues();
        },
        updateIndex: function (e) {
            return updateIndex(e);
        },
        updateConfiguration: function () {
            return updateConfiguration();
        },
        loadFileList: function (targetDiv) {
            loadFileList(targetDiv);
        },
        loadEmployeeList: function (targetDiv) {
            loadEmployeeList(targetDiv);
        },
        filterByName: function (str) {
            filterByName(str, $("#" + ids.modalDelete).find(".modal-body"));
            // for hiding and un hiding select all filtered checkbox
            if (str === "") {
                $("#" + ids.lblSelectFiltered).addClass("invisible");
            } else {
                $("#" + ids.lblSelectFiltered).removeClass("invisible");
                checkSelectFiltered();
            }
        },
        checkSelectAll: function () {
            checkSelectAll();
        },
        filterByEmployeeName: function (str) {
            filterByName(str, $("#employeeDeleteModal").find(".modal-body"));
        },
        deleteFiles: function () {
            deleteFiles();
        },
        deleteEmployees: function () {
            deleteEmployees();
        },
        loadUploadBox: function (targetDiv) {
            $(targetDiv).myPrompt({}, function (res) {
                $(targetDiv).modal({
                    "show": true,
                    "backdrop": "static"
                });
                configDropZone(res);
            });
        }
    };
}();