/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 5/8/13
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
resumeReader.ElementCreator = function() {
    function createDomEle(type, id, eleClass, value) {
        "use strict";
        var domEle = document.createElement(type);
        domEle.id = id;
        domEle.className = eleClass;
        domEle.innerHTML = value;
        return domEle;
    }

    return {
        createDomEle : function(type, id, eleClass, value) {
            return createDomEle(type, id, eleClass, value);
        }
    }
}
