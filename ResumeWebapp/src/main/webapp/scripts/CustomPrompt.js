$.fn.myPrompt = function (opts, callbackfn) {
    var o = $.extend({
        header: "<h3>Authentication Required!</h3>",
        headerClass: "modal-header",
        inputBox: "<input type='password' id='txtPwd' placeholder='Enter Security Key here!' class='span3'>",
        inputBoxId: "#txtPwd",
        hint: "<span class='help-block'>This key is used to authenticate you as a authenticated user!</span>",
        bodyClass: "modal-body",
        footerClass: "modal-footer",
        showHide: false
    }, opts);

    return this.each(function () {
        var selectedModal = $(this),
            promptModal = $("<div id='promptModal' class='modal hide fade' tabindex='-1' role='dialog' " +
                "aria-labelledby='myModalLabel' aria-hidden='true'></div>").appendTo("body");
        if (o.showHide) {
            selectedModal.css({display: "none"});
        }
        promptModal.empty();
        promptModal.append($("<div class='" + o.headerClass + "'>" + o.header + "</div>"));
        promptModal.append($("<div class='" + o.bodyClass + "'>" +
            o.inputBox +
            o.hint +
            "</div>"));
        promptModal.append($("<div class='" + o.footerClass + "'>" +
            "<button id='btnCancel' type='button' class='btn'>Cancel</button>" +
            "<button id='btnOk' class='btn btn-primary'>Ok</button> " +
            "</div>"));

        promptModal.modal({
            show: true,
            keyboard: false,
            backdrop: "static"
        });

        promptModal.on("shown", function () {
            $(o.inputBoxId).focus();
        });

        $(o.inputBoxId).on("keyup", function (e) {
            if (e.keyCode == 13) {
                doOk();
            }
        });
        function doOk() {
            var val = $(o.inputBoxId).val();
            if (o.showHide) {
                selectedModal.css({display: "block"});
            }
            promptModal.modal("hide");
            promptModal.remove();
            // calling call back function
            if (typeof callbackfn == "function") {
                callbackfn.call(this, val);
            }
        }

        $("#btnOk").click(function () {
            doOk();
        });
        $("#btnCancel").click(function () {
            if (o.showHide) {
                selectedModal.css({display: "block"});
            }
            promptModal.modal("hide");
            promptModal.remove();
        });
    });
};


