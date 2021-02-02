var TestAlertTypeRequest = (function () {
    function TestAlertTypeRequest(application, type) {
        this.application = application;
        this.type = type;
        this.url = document.getElementById("url").value;
        this.message = document.getElementById("message").value;
        this.level = document.getElementById("level").value;
    }
    TestAlertTypeRequest.prototype.send = function () {
        var thisRequest = this;
        var request = $.ajax({
            type: "POST",
            url: "/alert",
            data: JSON.stringify(this),
            dataType: 'text',
            contentType: "application/json",
            async: false,
            success: function (data) {
                showDialog(true, "Test alert successfully sent.");
            },
            error: function () {
                showDialog(false, "Error sending test alert.");
            }
        });
    };
    return TestAlertTypeRequest;
}());
function sendTestAlertTypeRequest(application, type) {
    new TestAlertTypeRequest(application, type).send();
}
