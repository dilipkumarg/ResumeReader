resumeReader.Searcher = function () {
    "use strict";
    function printError(xhr) {
        $("#alertText").html(xhr.responseText);
        var alertBox = $("#alertBox");
        alertBox.removeClass("hide");
        alertBox.addClass("alert-error");
    }

    function printResult(responseText) {
        var resultObj = JSON.parse(responseText),
            resultDiv = $("#" + resumeReader.ids.resultsDiv),
            resultHeaderDiv = resumeReader.ResultHeaderCreator.createResultHeaderDiv(resultObj.searchKey,
                resultObj.totalHits, resultObj.searchDuration),
            resultsList = resumeReader.ListGenerator.createResultsList(resultObj.activeHits, resultObj.inActiveHits,
                resultObj.probableHits);

        resultDiv.empty();

        resultDiv.append(resultHeaderDiv);
        resultDiv.append(resultsList);
    }
    function searchAndPrint(searchQuery) {
        if (searchQuery.trim().length > 0) {
            $.ajax({type: "get",
                url: resumeReader.url.search,
                data: resumeReader.urlParams.searchKey + "=" + searchQuery,
                beforeSend: function() {
                    $('#myModal').off('shown');
                },
                success: function (response) {
                    $("#alertBox").addClass("hide");
                    printResult(response);
                    // for highlighting
                    $('#myModal').on('shown', function () {
                        $('#myModal').find(".modal-body").wrapInTag({
                            word: searchQuery.trim(),
                            tag: '<span>'
                        });
                    });
                },
                error: function (xhr) {
                    $("#" + resumeReader.ids.resultsDiv).empty();
                    printError(xhr);
                }});
        }
    }


    return {
        searchAndPrint: function (searchQuery) {
            return searchAndPrint(searchQuery);
        }
    };
}();
