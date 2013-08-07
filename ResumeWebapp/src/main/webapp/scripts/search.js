resumeReader.Searcher = function () {
    function searchAndPrint(searchQuery) {
        "use strict";
        var ids = resumeReader.ids;

        $.ajax({type: "get",
            url: resumeReader.url.search,
            data: resumeReader.urlParams.searchKey + "=" + searchQuery,
            success: function (response) {
                printResult(response);
            },
            error: function (xhr) {
                $("#" + ids.resultsDiv).text(xhr);
            }});
    }

    function printResult(responseText) {
        "use strict";
        var resultObj = JSON.parse(responseText),
            resultDiv = $("#" + resumeReader.ids.resultsDiv),
            resultHeaderDiv = resumeReader.ResultHeaderCreator.createResultHeaderDiv(resultObj.searchKey,
                resultObj.totalHits, resultObj.searchDuration),
            resultsList = resumeReader.ListGenerator.createResultsList(resultObj.topHits, resultObj.topHits);

        resultDiv.empty();

        resultDiv.append(resultHeaderDiv);
        resultDiv.append(resultsList);
    }

    return {
        searchAndPrint: function (searchQuery) {
            return searchAndPrint(searchQuery);
        }
    };
}();



