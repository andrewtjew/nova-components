var ElementResult = (function () {
    function ElementResult() {
    }
    return ElementResult;
}());
var FormResult = (function () {
    function FormResult() {
    }
    return FormResult;
}());
var oldFormResult;
function process(formResult) {
    var success = false;
    if ((formResult.success != null) && (formResult.success != undefined)) {
        success = true;
        var dialog = document.getElementById("dialog").innerHTML = formResult.success;
        $("#dialog").dialog({
            autoOpen: true,
            title: "Success",
            draggable: false,
            modal: true,
            width: 600,
            height: 320,
            resizable: false,
            buttons: [{
                    text: "Close",
                    icons: {
                        primary: "ui-icon-closethick"
                    },
                    click: function () {
                        $(this).dialog("close");
                        processForm(formResult);
                    }
                }]
        }).prev(".ui-dialog-titlebar").css("background", "#040");
    }
    else if ((formResult.error != null) && (formResult.error != undefined)) {
        var dialog = document.getElementById("dialog").innerHTML = formResult.error;
        $("#dialog").dialog({
            autoOpen: true,
            title: "Error",
            draggable: false,
            modal: true,
            width: 600,
            height: 320,
            resizable: false,
            buttons: [{
                    text: "Close",
                    icons: {
                        primary: "ui-icon-closethick"
                    },
                    click: function () {
                        $(this).dialog("close");
                        processForm(formResult);
                    }
                }]
        }).prev(".ui-dialog-titlebar").css("background", "#400");
    }
    else {
        processForm(formResult);
    }
    return success;
}
function processForm(formResult) {
    if ((formResult.location != undefined) && (formResult.location != null)) {
        window.location.href = formResult.location;
    }
    if ((formResult.elementResults != undefined) && (formResult.elementResults != null)) {
        for (var _i = 0, _a = formResult.elementResults; _i < _a.length; _i++) {
            var result = _a[_i];
            var errorElement = document.getElementById(result.id).nextElementSibling;
            errorElement.style.display = "block";
            errorElement.innerHTML = result.text;
        }
    }
}
function confirmDialog(text, obj, action) {
    var dialog = document.getElementById("dialog").innerHTML = text;
    $("#dialog").dialog({
        autoOpen: true,
        title: "Confirm",
        draggable: false,
        modal: true,
        width: 600,
        height: 320,
        resizable: false,
        buttons: [{
                text: "OK",
                className: 'save',
                icons: {
                    primary: "ui-icon-check"
                },
                click: function () {
                    action(obj);
                }
            }, {
                text: "Cancel",
                className: 'cancel',
                icons: {
                    primary: "ui-icon-cancel"
                },
                click: function () {
                    $(this).dialog("close");
                }
            }]
    }).prev(".ui-dialog-titlebar").css("background", "#444");
}
function showDialog(success, text) {
    var dialog = document.getElementById("dialog").innerHTML = text;
    if (success) {
        $("#dialog").dialog({
            autoOpen: true,
            title: "Success",
            draggable: false,
            modal: true,
            width: 600,
            height: 320,
            resizable: false,
            buttons: [{
                    text: "Close",
                    icons: {
                        primary: "ui-icon-closethick"
                    },
                    click: function () {
                        $(this).dialog("close");
                    }
                }]
        }).prev(".ui-dialog-titlebar").css("background", "#040");
    }
    else {
        $("#dialog").dialog({
            autoOpen: true,
            title: "Error",
            draggable: false,
            modal: true,
            width: 600,
            height: 320,
            resizable: false,
            buttons: [{
                    text: "Close",
                    icons: {
                        primary: "ui-icon-closethick"
                    },
                    click: function () {
                        $(this).dialog("close");
                    }
                }]
        }).prev(".ui-dialog-titlebar").css("background", "#400");
    }
}
var RequestSender = (function () {
    function RequestSender() {
    }
    RequestSender.prototype.send = function (url, dataType) {
        var thisRequest = this;
        var request = $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(this),
            dataType: dataType,
            contentType: "application/json",
            async: false,
            success: function (formResult) {
                process(formResult);
            },
            error: function (response) {
                alert("Error: " + response.status);
            }
        });
    };
    return RequestSender;
}());
function post(url, dataType, object) {
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(object),
        dataType: dataType,
        contentType: "application/json",
        async: false,
        success: function (formResult) {
            process(formResult);
        },
        error: function (response) {
            alert("Error: " + response.status);
        }
    });
}
