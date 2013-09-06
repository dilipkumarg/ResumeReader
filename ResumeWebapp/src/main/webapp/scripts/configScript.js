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
        var ids = resumeReader.ids,
            btnUpdate = $("#btnConfigUpdate"),
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
        var progressBarDiv = $("#progressBarDiv"),
            progressBar = $("#progressBar"),
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
            // data-value is the attribute holding clean update and normal update
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
                selected[i].setAttribute("class", "span6");
            }
        }
    }

    function printFileList(fileObj, targetDiv) {
        var keys = Object.keys(fileObj);
        keys = keys.sort();
        for (var i = 0; i < keys.length; i++) {
            targetDiv.append("<label class='span6' title='" + keys[i] + "'>" +
                "<input type='checkbox' class='file-check checkbox' value='" + keys[i] + "'>" +
                " <span>" + fileObj[keys[i]] + "</span>" +
                "</label>");
        }
        // for toggle select all check box
        $(".file-check").on("click", function () {
            if ($(".file-check:checked").length === $(".file-check").length) {
                $("#cbSelectAll").prop("checked", true);
            } else {
                $("#cbSelectAll").prop("checked", false);
            }
        });
        // for checking all on select all button
        $("#cbSelectAll").on("click", function () {
            $(".file-check").prop("checked", this.checked);
        });
    }

    function printFileFetchError(xhr, targetDiv) {
        targetDiv.html("An error occured:" + xhr.status + "<br />" + xhr.statusText +
            "<br/>" + xhr.responseText);
    }

    function loadFileList(targetDiv) {
        $.ajax({
            url: "resumereader/delete",
            success: function (response) {
                targetDiv.empty();
                printFileList(JSON.parse(response), targetDiv);
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
        var deleteModal = $("#fileDeleteModal");
        var selectedBoxes = getSelectedFiles(deleteModal.find(".modal-body"));
        if (selectedBoxes.length > 0) {
            var res = confirm("Are you sure you want to delete " + selectedBoxes.length + " file(s)?");
            if (res) {
                deleteModal.myPrompt({showHide: true}, function (res) {
                    $.ajax({
                        type: "post",
                        url: "resumereader/delete",
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

    var DZintialized = false;
    var myDropzone;

    function configDropZone(securityKey) {
        if (!DZintialized) {
            DZintialized = true;
            myDropzone = new Dropzone("#my-awesome-dropzone", {
                autoProcessQueue: false,
                acceptedFiles: ".pdf,.doc,.docx",
                addRemoveLinks: true,
                parallelUploads: 15,
                maxFiles:15,
                //uploadMultiple:true,
                dictInvalidFileType: "Please upload only doc, docx and pdf files"
            });
            $("#btnUploadAll").click(function () {
                    myDropzone.processQueue();
                }
            );
            $("#fileUploadModal").on("hidden", function () {
                myDropzone.removeAllFiles();
                $("#uploadModalAlert").addClass("hide");
            });
            myDropzone.on("complete", function (file) {
                //console.log(file);
                var alertBox = $("#uploadModalAlert");
                alertBox.removeClass("hide");
                alertBox.find("span").html(file.xhr.response);
            });
        } else {
            // removing previous security key
            myDropzone.off("sending");
        }
        myDropzone.on("sending", function (file, xhr, formData) {
            xhr.setRequestHeader(resumeReader.urlParams.securityKey, securityKey);

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
        filterByName: function (str) {
            filterByName(str, $("#fileDeleteModal").find(".modal-body"));
        },
        deleteFiles: function () {
            deleteFiles();
        },
        loadUploadBox: function (targetDiv) {
            $(targetDiv).myPrompt({}, function (res) {
                $(targetDiv).modal("show");
                configDropZone(res);
            });
        }
    };
}();