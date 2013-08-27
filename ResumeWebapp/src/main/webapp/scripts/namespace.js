var resumeReader = {
    ids: {
        queryTextBox: "queryText",
        resultsDiv: "resultsDiv",
        resultHeaderDiv: "resultHeaderDiv",
        resultsListDiv: "resultsListDiv",
        queryLabel: "queryLabel",
        hitsLabel: "hitsLabel",
        timeTakenLabel: "timeTakenLabel",
        resultList: "resultsList",
        txtResumeDir: "txtResumeDir",
        txtEmployeeList: "txtEmployeeList",
        titleSearchBox: "txtTitleSearch",
        btnResumeToggle: "btn-ResumeToggle",
        btnEmployeeToggle: "btn-EmployeeToggle"
    },
    idsPrefix: {
        itemSummary: "itemSummary",
        activeList: "Active",
        inactiveList: "Inactive",
        probableList: "Probable",
        itemDiv: "itemDiv",
        itemCollapseIcon: "itemCollapseIcon"

    },
    url: {
        search: "resumereader/search",
        view: "resumereader/view",
        config: "resumereader/config",
        update: "resumereader/update"
    },
    urlParams: {
        searchKey: "searchKey",
        resumeDir: "resumeDir",
        employeeFile: "employeeFile",
        securityKey: "accessKey"
    },
    stopWords: ["a", "an", "and",
        "are", "as", "at", "be", "but", "by", "for", "if", "in", "into",
        "is", "it", "no", "not", "of", "on", "or", "such", "that", "the",
        "their", "then", "there", "these", "they", "this", "to", "was",
        "will", "with"],
    ElementCreator: {},
    ListGenerator: {},
    ResultHeaderCreator: {},
    Searcher: {},
    Configuration: {}
};

$(function () {
    // plugin for toggle disabled state
    (function ($) {
        $.fn.toggleDisabled = function () {
            return this.each(function () {
                this.disabled = !this.disabled;
            });
        };
    })(jQuery);
});


function searchQuery() {
    "use strict";
    var query = $("#" + resumeReader.ids.queryTextBox).val().trim();
    // searching for context
    var contextPos = query.indexOf("-");
    var context = "";
    if (contextPos !== -1) {
        context = query.substr(contextPos + 1).trim();
        query = query.substr(0, contextPos).trim();
    }
    resumeReader.Searcher.searchAndPrint(query, context);
    // changing url
    window.location.hash = "q=" + query + ((context !== "") ? "&c=" + context : "");
}

function filterResults() {
    "use strict";
    var query = $("#" + resumeReader.ids.titleSearchBox).val();
    resumeReader.Searcher.filterResults(query);

}

function toggleExpandAll() {
    "use strict";
    if ($('#expandAllIcon').attr("class") == "icon-chevron-down") {
        $('.summaryDiv').slideDown("slow", function () {
            $('.icon-chevron-down').attr("class", "icon-chevron-up");
        });
    } else {
        $('.summaryDiv').slideUp("slow", function () {
            $('.icon-chevron-up').attr("class", "icon-chevron-down");
        });
    }
}
