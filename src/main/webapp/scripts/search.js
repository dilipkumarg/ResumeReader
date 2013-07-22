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
		resultHeaderDiv = createResultHeaderDiv(resultObj.searchQuery, 
			resultObj.totalHits,resultObj.searchDuration),
		resultsList = createResultsList(resultObj.topHits);
		
		resultDiv.appendChild(resultHeaderDiv);
		resultDiv.appendChild(resultsList);
}
function createResultsList(hits) {
	"use strict";
	var resultsList = createDomEle("ul", "resultsList", "","");
	for(var i = 0; i < hits.length; i++) {
		var listEle = createDomEle("li","","resultListElement", hits[i]);
		resultsList.insertBefore(listEle, null);
	}
	return resultsList;
}

function createResultHeaderDiv(searchKey, totalHits, timeTaken) {
	"use strict";
	var headerDiv = createDomEle("div", "resultHeaderDiv","",""),
		queryLabel = createDomEle("label","queryLabel", "", 
			"Search Key:" + searchKey),
		totalHitsLabel = createDomEle("label","hitsLabel", 
			"","Total Hits:" + totalHits),
		timeTakenLabel = createDomEle("label","timeTakenLabel", 
			"", "Search Duration:" + timeTaken + "ms");
	headerDiv.appendChild(queryLabel);
	headerDiv.appendChild(totalHitsLabel);
	headerDiv.appendChild(timeTakenLabel);
	return headerDiv;
}
function createDomEle(type, id, eleClass, value) {
	var domEle = document.createElement(type);
	domEle.id = id;
	domEle.className = eleClass;
	domEle.innerHTML = value;
	return domEle;
}

