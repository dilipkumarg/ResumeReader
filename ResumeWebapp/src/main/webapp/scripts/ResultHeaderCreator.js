/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 5/8/13
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */

resumeReader.ResultHeaderCreator = function() {
    var domEle = resumeReader.ElementCreator;
    function createResultHeaderDiv(searchKey, totalHits, timeTaken) {
        "use strict";
        var headerDiv = domEle.createDomEle("div", "resultHeaderDiv", "navbar", ""),
            queryLabel = domEle.createDomEle("span", "queryLabel", "label label-info",
                "Search Key: " + searchKey),
            totalHitsLabel = domEle.createDomEle("span", "hitsLabel",
                "label label-info offset1 pull-right", "Total Hits: " + totalHits),
            timeTakenLabel = domEle.createDomEle("span", "timeTakenLabel",
                "label label-info pull-right", "Search Duration: " + timeTaken + "ms");
        headerDiv.appendChild(queryLabel);
        headerDiv.appendChild(totalHitsLabel);
        headerDiv.appendChild(timeTakenLabel);
        return headerDiv;
    }
    return {
        createResultHeaderDiv : function(searchKey, totalHits, timeTaken) {
            return createResultHeaderDiv(searchKey, totalHits, timeTaken);
        }
    }
}
