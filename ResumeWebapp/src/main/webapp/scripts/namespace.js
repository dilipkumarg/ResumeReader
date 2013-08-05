var resumeReader = {
    ids : {
        queryTextBox : "queryText",
        resultsDiv : "resultsDiv"
    },
    idsPrefix: {},
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

function $(id) {
    return document.getElementById(id);
}

function searchQuery() {
    var searchQuery = $(resumeReader.ids.queryTextBox).value;
    resumeReader.Searcher.searchAndPrint(searchQuery);
}
function toggleSummary(itemId) {
    var summaryDiv = $(itemId);

    if (summaryDiv.style.display == "none") {
        summaryDiv.style.display = "inline-block";
    } else {
        summaryDiv.style.display = "none";
    }
}