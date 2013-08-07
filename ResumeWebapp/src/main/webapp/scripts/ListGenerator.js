/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 5/8/13
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */

resumeReader.ListGenerator = function () {
    var domEle = resumeReader.ElementCreator,
        ids = resumeReader.ids,
        idsPrefix = resumeReader.idsPrefix;
    "use strict";
    function createResultsList(activeHits, inactiveHits) {
        var resultListContainer = domEle.createDomEle("div", "", "", ""),
            tabContent = domEle.createDomEle("div", "", "tab-content", ""),
            activeList = domEle.createDomEle("div", "active", "tab-pane active", ""),
            inactiveList = domEle.createDomEle("div", "inactive", "tab-pane", ""),
            navigationBar = domEle.createDomEle("ul", "", "nav nav-tabs",
                " <li class='active '> <a class='span4' href='#active' data-toggle='tab'>Active</a> </li>" +
                    "<li><a class='span4' href='#inactive' data-toggle='tab'>Inactive</a></li>");

        // creating active and inactive lists
        activeList.appendChild(createList(activeHits, idsPrefix.activeList));
        inactiveList.appendChild(createList(inactiveHits, idsPrefix.inactiveList));

        tabContent.appendChild(activeList);
        tabContent.appendChild(inactiveList);

        resultListContainer.appendChild(navigationBar);
        resultListContainer.appendChild(tabContent);

        return resultListContainer;
    }

    function createList(hits, idPrefix) {
        "use strict";
        var resultsList = domEle.createDomEle("ul", ids.resultList, "nav nav-tabs nav-stacked", "");
        for (var i = 0; i < hits.length; i++) {
            var listEle = domEle.createDomEle("li", "", "resultListElement", "");
            listEle.appendChild(createListItemHeader(idPrefix + i, hits[i].title, hits[i].filepath));
            listEle.appendChild(createListItemSummaryDiv(idPrefix + i, hits[i].summary));
            resultsList.insertBefore(listEle, null);
        }
        return resultsList;
    }

    function createListItemHeader(id, title, path) {
        title = (title == "" ? "TITLE" : title);
        var titleDiv = domEle.createDomEle("div", idsPrefix.itemDiv + id, "itemHeader", ""),
            titleLabel = domEle.createDomEle("span", "", "titleLabel", title),
            viewLink = domEle.createDomEle("a", "", "offset1 pull-right", "View"),
            expandButton = domEle.createDomEle("a", "", "expandSummary offset1 pull-right",
                "<i id='" + idsPrefix.itemCollapseIcon + id + "' class = 'icon-chevron-down'></i>");
        viewLink.href = resumeReader.url.search + "?filename=" + path;
        viewLink.target = "_blank";
        expandButton.href = "#";
        expandButton.onclick = function (e) {
            e.preventDefault();
            var summaryCollapseIcon = $("#" + idsPrefix.itemCollapseIcon + id);
            $("#itemSummary" + id).slideToggle("slow", function () {
                summaryCollapseIcon.attr("class", (summaryCollapseIcon.attr("class") == "icon-chevron-down"
                        ? "icon-chevron-up" : "icon-chevron-down"))
            });
        };
        titleDiv.appendChild(titleLabel);
        titleDiv.appendChild(expandButton);
        titleDiv.appendChild(viewLink);
        return titleDiv;
    }

    function createListItemSummaryDiv(id, summary) {
        var summaryDiv = domEle.createDomEle("div", idsPrefix.itemSummary + id, "summaryDiv", ""),
            summaryPara = domEle.createDomEle("p", "", "summaryPara", summary);

        summaryDiv.style.display = "none";
        summaryDiv.appendChild(summaryPara);
        return summaryDiv;
    }

    return {
        createResultsList: function (activeHits, inActiveHits) {
            return createResultsList(activeHits, inActiveHits);
        }
    };
}();
