/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 14/8/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
$.fn.wrapInTag = function (opts) {

    var o = $.extend({
        words: [],
        tag: '<strong>',
        css: {
            color: "blue",
            backgroundColor: "yellow",
            "font-weight": "bold",
            "text-decoration": "underline"
        }
    }, opts);

    return this.each(function () {
        var html = $(this).html();
        for (var i = 0; i < o.words.length; i = i+1) {
            var str = o.words[i].replace(/"/g, ""),// deleting double quotes from word
            // regular expression for selecting desired string.
                re = new RegExp("([/\\s>])(" + str + "(\\([/\\w]+\\)){0,1}([-_.]*[/\\d]*)*)(([/\\W]){0,1}[/\\s<])", "gim");
            html = html.replace(re, '$1' + o.tag.replace('>', ' class="highlight"> ') + '$2' + o.tag.replace('<', '</') + '$5 ');
        }
        $(this).html(html);
        $(this).find("span.highlight").css(o.css);
    });
};

