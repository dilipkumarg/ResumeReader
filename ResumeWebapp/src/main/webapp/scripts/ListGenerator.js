/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 5/8/13
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */

resumeReader.ListGenerator = function() {
    var domEle = resumeReader.ElementCreator;

    function createResultsList(activeHits, inactiveHits) {
        var resultListContainer = domEle.createDomEle("div","","",""),
            navigationBar = domEle.createDomEle("div","tab","btn-group",""),
            tabContent = domEle.createDomEle("div","","tab-content",""),
            activeList = domEle.createDomEle("div","active","tab-pane active",""),
            inactiveList = domEle.createDomEle("div","inactive","tab-pane","");
        navigationBar["data-toggle"] = "buttons-radio";
        navigationBar.innerHTML = " <a href='#active' class='btn active' data-toggle='tab'>Active</a>" +
            "<a href='#inactive' class='btn' data-toggle='tab'>Inactive</a>";
        activeHits.appendChild(createList(activeHits));
        activeHits.appendChild(createList(inactiveHits));

        tabContent.appendChild(activeList);
        tabContent.appendChild(inactiveList);

        resultListContainer.appendChild(navigationBar);
        resultListContainer.appendChild(tabContent);

        return resultListContainer;
    }

    function createList(hits) {
        "use strict";
        var resultsList = createDomEle("ul", "resultsList", "nav nav-tabs nav-stacked", "");
        for (var i = 0; i < hits.length; i++) {
            var listEle = createDomEle("li", "", "resultListElement", "");
            listEle.appendChild(createListItemHeader(i, hits[i].title, hits[i].filepath));
            listEle.appendChild(createListItemSummaryDiv(i, hits[i].summary));
            resultsList.insertBefore(listEle, null);
        }
        return resultsList;
    }

    function createListItemHeader(id, title, path) {
        if (title == "") {
            title = "TITLE";
        }
        var titleDiv = createDomEle("div", "itemDiv" + id, "itemHeader", ""),
            titleLabel = createDomEle("span", "", "titleLabel", title),
            viewLink = createDomEle("a", "", "offset1 pull-right", "View"),
            expandButton = createDomEle("a", "", "offset1 pull-right",
                "<i id='itemCollapseIcon" + id + "' class = 'icon-chevron-down'></i>");
        //expandIcon = createDomEle("i", "itemCollapseIcon" + id, "icon-chevrom-down", "");
        viewLink.href = "/resumeReader/view?filename=" + path;
        viewLink.target = "_blank";
        expandButton.href = "javascript:toggleSummary('itemSummary" + id + "')";
        //expandButton.appendChild(expandIcon);
        titleDiv.appendChild(titleLabel);
        titleDiv.appendChild(expandButton);
        titleDiv.appendChild(viewLink);
        return titleDiv;
    }
    function createListItemSummaryDiv(id, summary) {
        var summaryDiv = createDomEle("div", "itemSummary" + id, "summaryDiv", ""),
            summaryPara = createDomEle("p", "", "summaryPara", summary);

        summaryDiv.style.display = "none";
        summaryDiv.appendChild(summaryPara);
        return summaryDiv;
    }
}
