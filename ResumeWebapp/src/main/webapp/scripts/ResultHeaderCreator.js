/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 5/8/13
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */

resumeReader.ResultHeaderCreator = function () {
    "use strict";
    var domEle = resumeReader.ElementCreator;

    function createResultHeaderDiv(searchKey, totalHits, timeTaken) {
        var ids = resumeReader.ids,
            headerDiv = domEle.createDomEle("div", ids.resultHeaderDiv, "navbar", ""),
            queryLabel = domEle.createDomEle("span", ids.queryLabel, "label label-info",
                "Search Key: " + searchKey),
            totalHitsLabel = domEle.createDomEle("span", ids.hitsLabel,
                "label label-info offset1 pull-right", "Total Hits: " + totalHits),
            timeTakenLabel = domEle.createDomEle("span", ids.timeTakenLabel,
                "label label-info pull-right", "Search Duration: " + timeTaken + "ms");
        headerDiv.appendChild(queryLabel);
        headerDiv.appendChild(totalHitsLabel);
        headerDiv.appendChild(timeTakenLabel);
        return headerDiv;
    }

    return {
        createResultHeaderDiv: function (searchKey, totalHits, timeTaken) {
            return createResultHeaderDiv(searchKey, totalHits, timeTaken);
        }
    };
}();
