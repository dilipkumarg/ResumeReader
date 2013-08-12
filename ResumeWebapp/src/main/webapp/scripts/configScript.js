/**
 * Created with JetBrains WebStorm.
 * User: dilip
 * Date: 12/8/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */

$(document).ready(function() {
    $.ajax({type:"get",
            url: resumeReader.url.config,
            success : function(response) {
                updatePage(jQuery.parseJSON(response));
            },
            error : function() {

            }
    })
});

function updatePage(values) {
    var ids = resumeReader.ids;
    $("#" + ids.txtResumeDir).val(values.resumeDir);
    $("#" + ids.txtEmployeeList).val(values.txtEmployeeList);
}