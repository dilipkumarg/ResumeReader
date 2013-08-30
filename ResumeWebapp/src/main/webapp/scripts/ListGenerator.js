/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 5/8/13
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */

resumeReader.ListGenerator = function () {
    "use strict";
    var domEle = resumeReader.ElementCreator,
        ids = resumeReader.ids,
        idsPrefix = resumeReader.idsPrefix;

    function createListItemHeader(id, title, path, summary, closestMatch) {
        title = (title === "" ? "TITLE" : title);
        var titleDiv = domEle.createDomEle("div", idsPrefix.itemDiv + id, "itemHeader", ""),
            titleLabel = domEle.createDomEle("span", "", "titleLabel", title),
            viewLink = domEle.createDomEle("a", "", "offset1 pull-right", "<i class='icon-eye-open'></i>"),
            expandButton = domEle.createDomEle("a", "", "expandSummary offset1 pull-right span1",
                "<span class='badge pull-right'> " + summary.length +
                    " <i id='" + idsPrefix.itemCollapseIcon + id + "' class = 'icon-chevron-down'></i>" +
                    "</span>");
        titleDiv.setAttribute('data-toggle', 'popover');
        titleDiv.setAttribute('data-placement', 'bottom');
        titleDiv.setAttribute('data-content', closestMatch);
        titleDiv.setAttribute('data-original-title', 'Related Match!');
        viewLink.href = encodeURI(resumeReader.url.view + "?filename=" + path);
        viewLink.setAttribute('data-toggle', "modal");
        viewLink.setAttribute('title', "View Doc as Plain Text");
        viewLink.setAttribute('data-target', "#myModal");
        expandButton.href = "#";
        expandButton.title = "Expand/Collapse Context";
        expandButton.onclick = function (e) {
            e.preventDefault();
            $("#" + idsPrefix.itemDiv + id).toggleClass("background");
            var summaryCollapseIcon = $("#" + idsPrefix.itemCollapseIcon + id);
            $("#itemSummary" + id).slideToggle("slow", function () {
                summaryCollapseIcon.attr("class", (summaryCollapseIcon.attr("class") === "icon-chevron-down"
                    ? "icon-chevron-up" : "icon-chevron-down"));
            });
        };
        titleDiv.appendChild(titleLabel);
        titleDiv.appendChild(expandButton);
        titleDiv.appendChild(viewLink);
        return titleDiv;
    }

    function createListItemSummaryDiv(id, summary) {
        var summaryDiv = domEle.createDomEle("div", idsPrefix.itemSummary + id, "summaryDiv", ""),
            i = 0;
        for (i = 0; i < summary.length; i++) {
            var summaryPara = domEle.createDomEle("p", "", "summaryPara summaryPara" + i, summary[i]);
            summaryDiv.appendChild(summaryPara);
        }
        summaryDiv.style.display = "none";
        return summaryDiv;
    }

    function createList(hits, idPrefix) {
        var resultsList = domEle.createDomEle("ul", ids.resultList, "nav nav-tabs nav-stacked", ""),
            i = 0;
        var keys = Object.keys(hits);
        keys.sort();
        for (i = 0; i < keys.length; i++) {
            var listEle = domEle.createDomEle("li", "", "resultListElement", "");
            listEle.appendChild(createListItemHeader(idPrefix + i, keys[i], hits[keys[i]].filepath, hits[keys[i]].summary, hits[keys[i]].closematch));
            listEle.appendChild(createListItemSummaryDiv(idPrefix + i, hits[keys[i]].summary));
            resultsList.insertBefore(listEle, null);
        }
        return resultsList;
    }

    function createListNav(hits, showSort) {
        var navigationBar = domEle.createDomEle("ul", "", "nav nav-tabs nav-pills",
            " <li class='active '> <a class='span2' href='#active' data-toggle='tab'>Active (" + Object.keys(hits).length + ")</a> </li>" +
                "<li><a class='span2' href='#probable' data-toggle='tab'>Probable (" + Object.keys(hits).length + ")</a></li>" +
                "<li><a class='span2' href='#inactive' data-toggle='tab'>Inactive (" + Object.keys(hits).length + ")</a></li>" +
                "<li class='pull-right'><a href='javascript:toggleExpandAll();'>" +
                "<span class=badge>" +
                "<i title='Expand/Collapse all' id='expandAllIcon' class='icon-chevron-down'></i>" +
                "</span></a></li>" +
                "<li class='pull-right'></li>");
        return navigationBar;
    }


    function createResultsList(hits, showList, showSort, sortByMatchRate) {
        var resultListContainer = domEle.createDomEle("div", ids.resultsListDiv, "", ""),
            navigationBar = createListNav(hits, false),
            tabContent = domEle.createDomEle("div", "", "", "");

        tabContent.appendChild(createList(hits[showList], idsPrefix.activeList));


        resultListContainer.appendChild(navigationBar);
        resultListContainer.appendChild(tabContent);

        return resultListContainer;
    }

    return {
        createResultsList: function (hits, showList) {
            return createResultsList(hits, showList, false, false);
        }
    };
}();
