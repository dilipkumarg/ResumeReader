resumeReader.Searcher = function () {
    function searchAndPrint(searchQuery) {
        "use strict";
        var ids = resumeReader.ids,
            xmlhttp;
        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                printResult(xmlhttp.responseText);
            } else if (xmlhttp.readyState == 4) {
                $(ids.resultsDiv).innerHTML = xmlhttp.responseText;
            }

        }
        xmlhttp.open("GET", resumeReader.url.search + resumeReader.urlParams.searchKey + "=" + searchQuery, true);
        xmlhttp.send();
    }

    function printResult(responseText) {
        "use strict";
        var resultObj = JSON.parse(responseText),
            resultDiv = $(resumeReader.ids.resultsDiv),
            resultHeaderDiv = resumeReader.ResultHeaderCreator.createResultHeaderDiv(resultObj.searchKey,
                resultObj.totalHits, resultObj.searchDuration),
            resultsList = createResultsList(resultObj.topHits);

        resultDiv.innerHTML = "";

        resultDiv.appendChild(resultHeaderDiv);
        resultDiv.appendChild(resultsList);
    }

    return {
        searchAndPrint: function (searchQuery) {
            return searchAndPrint(searchQuery);
        }
    }
}



