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
		if (xmlhttp.readyState == 4) {// && xmlhttp.status == 200) {
			printResult(xmlhttp.responseText);
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
		var listEleAnchor = createDomEle("a","", "", hits[i]),
		    listEle = createDomEle("li","","resultListElement", "");
		listEleAnchor.href = "/resumereader/download?filename=" + hits[i];
		listEle.appendChild(listEleAnchor);
		resultsList.insertBefore(listEle, null);
	}
	return resultsList;
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

