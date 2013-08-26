var resumeReader = {
    ids : {
        queryTextBox : "queryText",
        resultsDiv : "resultsDiv",
        resultHeaderDiv: "resultHeaderDiv",
        resultsListDiv : "resultsListDiv",
        queryLabel: "queryLabel",
        hitsLabel: "hitsLabel",
        timeTakenLabel: "timeTakenLabel",
        resultList : "resultsList",
        txtResumeDir : "txtResumeDir",
        txtEmployeeList : "txtEmployeeList",
        titleSearchBox : "txtTitleSearch"
    },
    idsPrefix: {
        itemSummary: "itemSummary",
        activeList : "Active",
        inactiveList: "Inactive",
        probableList: "Probable",
        itemDiv: "itemDiv",
        itemCollapseIcon : "itemCollapseIcon"

    },
    url : {
        search : "resumereader/search",
        view : "resumereader/view",
        config : "resumereader/config",
        update : "resumereader/update"
    },
    urlParams : {
        searchKey : "searchKey",
        resumeDir: "resumeDir",
        employeeFile : "employeeFile",
        securityKey : "accessKey"
    },
    ElementCreator : {},
    ListGenerator: {},
    ResultHeaderCreator: {},
    Searcher : {},
    Configuration: {}
};


function searchQuery() {
    "use strict";
    var query = $("#" + resumeReader.ids.queryTextBox).val();
    resumeReader.Searcher.searchAndPrint(query);
    // changing url
    window.location.hash = "q=" + query;
}

function filterResults() {
    "use strict";
    var query = $("#" + resumeReader.ids.titleSearchBox).val();
    resumeReader.Searcher.filterResults(query);

}

function toggleExpandAll() {
    "use strict";
    if($('#expandAllIcon').attr("class") == "icon-chevron-down") {
        $('.summaryDiv').slideDown("slow", function() {
            $('.icon-chevron-down').attr("class", "icon-chevron-up");
        });
    } else {
        $('.summaryDiv').slideUp("slow", function() {
            $('.icon-chevron-up').attr("class", "icon-chevron-down");
        });
    }
}
