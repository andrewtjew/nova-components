function post(url, dataType, object) {
    var ret = null;
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(object),
        dataType: dataType,
        contentType: "application/json",
        async: false,
        success: function (result) {
            ret = result;
        },
        error: function (response) {
            alert("Error: " + response.status + ", message=" + response.statusText);
        }
    });
    return ret;
}
function evalPost(url, dataType, object) {
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(object),
        dataType: dataType,
        contentType: "application/json",
        async: false,
        success: function (result) {
            eval(result);
        },
        error: function (response) {
            alert("Error: " + response.status + ", message=" + response.statusText);
        }
    });
}
Object.keys = function (obj) {
    var keys = [];
    for (var i in obj) {
        if (obj.hasOwnProperty(i)) {
            keys.push(i);
        }
    }
    return keys;
};
