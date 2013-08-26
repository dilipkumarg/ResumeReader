/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 5/8/13
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */

resumeReader.ResultHeaderCreator = function () {
    "use strict";
    var domEle = resumeReader.ElementCreator,
        ids = resumeReader.ids;

    function createResultHeaderDiv(searchKey, totalHits, timeTaken) {
        var headerDiv = domEle.createDomEle("div", ids.resultHeaderDiv, "navbar row container", ""),
            queryLabel = domEle.createDomEle("span", ids.queryLabel, "label label-info",
                "Search Key: " + searchKey),
            totalHitsLabel = domEle.createDomEle("span", ids.hitsLabel,
                "label label-info offset1", "Total Hits: " + totalHits),
            timeTakenLabel = domEle.createDomEle("span", ids.timeTakenLabel,
                "label label-info offset1", "Search Duration: " + timeTaken + "ms");
        headerDiv.appendChild(queryLabel);
        headerDiv.appendChild(timeTakenLabel);
        headerDiv.appendChild(totalHitsLabel);
        headerDiv.appendChild(createTitleSearchBox());
        return headerDiv;
    }

    function createTitleSearchBox() {
        var searchBox = domEle.createDomEle("input", ids.titleSearchBox, "input-medium search-query pull-right", "");
        searchBox.setAttribute("type", "text");
        searchBox.setAttribute("placeholder", "Search Name");
        searchBox.onkeyup = function (e) {
            filterResults();
        };
        return searchBox;
    }

    return {
        createResultHeaderDiv: function (searchKey, totalHits, timeTaken) {
            return createResultHeaderDiv(searchKey, totalHits, timeTaken);
        }
    };
}();
