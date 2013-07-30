function search() {
	"use strict";
	var searchQuery = document.getElementById("queryText").value;
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			printResult(xmlhttp.responseText);
		} else if(xmlhttp.readyState == 4) {
			document.getElementById("resultsDiv").innerHTML = xmlhttp.responseText;
		}
		
	}
	xmlhttp.open("GET", "resumereader/search?searchKey=" + searchQuery, true);
	xmlhttp.send();
}

function printResult(responseText) {
	"use strict";
	var resultObj = JSON.parse(responseText), 
		resultDiv = document.getElementById("resultsDiv"),
		resultHeaderDiv = createResultHeaderDiv(resultObj.searchKey, 
			resultObj.totalHits,resultObj.searchDuration),
		resultsList = createResultsList(resultObj.topHits);
		
		resultDiv.innerHTML = "";
		
		resultDiv.appendChild(resultHeaderDiv);
		resultDiv.appendChild(resultsList);
}
function createResultsList(hits) {
	"use strict";
	var resultsList = createDomEle("ul", "resultsList", "nav nav-tabs nav-stacked","");
	for(var i = 0; i < hits.length; i++) {
		var listEle = createDomEle("li","","resultListElement", "");
		listEle.appendChild(createListItemHeader(i, hits[i].title, hits[i].filepath));
		listEle.appendChild(createListItemSummaryDiv(i, hits[i].summary));
		resultsList.insertBefore(listEle, null);
	}
	return resultsList;
}

function createListItemHeader(id, title, path) {
	if(title == "") {
		title = "TITLE";
	}
	var titleDiv = createDomEle("div", "itemDiv" + id, "itemHeader","" ),
		titleLabel = createDomEle("span", "", "titleLabel", title),
		viewLink = createDomEle("a", "", "offset1 pull-right", "View"),
		expandButton = createDomEle("a","","offset1 pull-right",
			"<i id='itemCollapseIcon"+ id +"' class = 'icon-chevron-down'></i>");
		//expandIcon = createDomEle("i", "itemCollapseIcon" + id, "icon-chevrom-down", "");
	viewLink.href = "/resumereader/view?filename=" + path;
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


function toggleSummary(itemId) {
	var summaryDiv = document.getElementById(itemId);
	
	if(summaryDiv.style.display == "none") {
		summaryDiv.style.display = "inline-block";
	} else {
		summaryDiv.style.display = "none";
	}	
}


function createResultHeaderDiv(searchKey, totalHits, timeTaken) {
	"use strict";
	var headerDiv = createDomEle("div", "resultHeaderDiv","navbar",""),
		queryLabel = createDomEle("span","queryLabel", "label label-info", 
			"Search Key: " + searchKey),
		totalHitsLabel = createDomEle("span","hitsLabel", 
			"label label-info offset1 pull-right","Total Hits: " + totalHits),
		timeTakenLabel = createDomEle("span","timeTakenLabel", 
			"label label-info pull-right", "Search Duration: " + timeTaken + "ms");
	headerDiv.appendChild(queryLabel);
	headerDiv.appendChild(totalHitsLabel);
	headerDiv.appendChild(timeTakenLabel);
	return headerDiv;
}
function createDomEle(type, id, eleClass, value) {
	"use strict";
	var domEle = document.createElement(type);
	domEle.id = id;
	domEle.className = eleClass;
	domEle.innerHTML = value;
	return domEle;
}

