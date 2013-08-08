var resumeReader = {
    ids : {
        queryTextBox : "queryText",
        resultsDiv : "resultsDiv",
        resultHeaderDiv: "resultHeaderDiv",
        queryLabel: "queryLabel",
        hitsLabel: "hitsLabel",
        timeTakenLabel: "timeTakenLabel",
        resultList : "resultsList"
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
        view : "resumereader/view"
    },
    urlParams : {
        searchKey : "searchKey"
    },
    ElementCreator : {},
    ListGenerator: {},
    ResultHeaderCreator: {},
    Searcher : {}
};


function searchQuery() {
    var searchQuery = $("#" + resumeReader.ids.queryTextBox).val();
    resumeReader.Searcher.searchAndPrint(searchQuery);
}
function toggleSummary(itemId) {
    /*var summaryDiv = $("#" + itemId);
    if (summaryDiv.css("display") == "none") {
        summaryDiv.css({"display":"inline-block"});
    } else {
        summaryDiv.css({"display" : "none"});
    }*/
    $("#" + itemId).slideToggle("slow");
}