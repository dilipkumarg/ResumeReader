var resumeReader = {
    ids : {
        queryTextBox : "queryText",
        resultsDiv : "resultsDiv",
        resultHeaderDiv: "resultHeaderDiv",
        queryLabel: "queryLabel",
        hitsLabel: "hitsLabel",
        timeTakenLabel: "timeTakenLabel",
        resultList : "resultsList",
        txtResumeDir : "txtResumeDir",
        txtEmployeeList : "txtEmployeeList"
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
