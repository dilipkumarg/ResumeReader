resumeReader.Searcher = function () {
    "use strict";
    // variable for holding results
    var resultsObj = {},
        queryObj = {};

    function printError(xhr) {
        $("#alertText").html(xhr.responseText);
        var alertBox = $("#alertBox");
        alertBox.removeClass("hide");
        alertBox.addClass("alert-error");
    }

    function printResult() {
        // displaying query as the context if context is not there.
        var context = ((queryObj.context !== "") ? queryObj.context : queryObj.query),
            resultDiv = $("#" + resumeReader.ids.resultsDiv),
            resultHeaderDiv = resumeReader.ResultHeaderCreator.createResultHeaderDiv(queryObj.query, resultsObj.contextKey,
                resultsObj.totalHits, resultsObj.searchDuration),
            resultsList = resumeReader.ListGenerator.createResultsList(resultsObj, "activeHits");

        resultDiv.empty();

        resultDiv.append(resultHeaderDiv);
        resultDiv.append(resultsList);
        $(".itemHeader").popover({
            trigger: 'hover',
            placement: 'top'
        });
    }

    function filterTitle(list, keyString) {
        var resList = {};
        for (var key in list) {
            if (list.hasOwnProperty(key)) {
                // if keyString not present in the key it returns -1
                if (key.toLowerCase().indexOf(keyString.toLowerCase()) !== -1) {
                    resList[key] = list[key];
                }
            }
        }
        return resList;
    }

    function filterResults(titleQuery) {
        var resultDiv = document.getElementById(resumeReader.ids.resultsDiv),
            resultsListDiv = $("#" + resumeReader.ids.resultsListDiv),
            resultsList = resumeReader.ListGenerator.createResultsList(filterTitle(resultsObj.activeHits, titleQuery),
                filterTitle(resultsObj.inActiveHits, titleQuery),
                filterTitle(resultsObj.probableHits, titleQuery));
        resultDiv.removeChild(document.getElementById(resumeReader.ids.resultsListDiv));
        resultDiv.appendChild(resultsList);
    }

    function searchAndPrint() {
        var searchQuery = queryObj.query + ((queryObj.context !== "") ? " AND " + queryObj.context : "");
        if (searchQuery.trim().length > 0) {
            $.ajax({type: "get",
                url: resumeReader.url.search,
                data: resumeReader.urlParams.searchKey + "=" + searchQuery + "&contextKey=" + queryObj.context,
                beforeSend: function () {
                    // removing previous highlight keyword event
                    $('#myModal').off('shown');
                    // showing progress bar
                    $('#progressBarModal').modal({
                        show: true,
                        keyboard: false,
                        backdrop: 'static'
                    });
                },
                success: function (response) {
                    $("#alertBox").addClass("hide");
                    resultsObj = JSON.parse(response);
                    printResult();
                    // for highlighting
                    $('#myModal').on('shown', function () {
                        var queries = searchQuery.match(/(?:[^\s"]+|"[^"]*")+/g);
                        // this code is for highlighting keywords
                        $('#myModal').find(".modal-body").wrapInTag({
                            words: queries,
                            tag: '<span>',
                            ignoreWords: resumeReader.stopWords
                        });
                    });
                },
                error: function (xhr) {
                    $("#" + resumeReader.ids.resultsDiv).empty();
                    printError(xhr);
                },
                complete: function () {
                    $('#progressBarModal').modal('hide');
                }});
        }
    }


    return {
        searchAndPrint: function (searchQuery, context) {
            queryObj.query = searchQuery.trim();
            queryObj.context = context.trim();
            return searchAndPrint();
        },
        filterResults: function (keyString) {
            return filterResults(keyString);
        }
    };
}();
