var AlertType = (function () {
    function AlertType(application, type) {
        this.application = application;
        this.type = type;
    }
    AlertType.prototype.remove = function (alertType) {
        post("/alertType/remove", "json", alertType);
    };
    return AlertType;
}());
function removeAlertType(application, type, message) {
    var alertType = new AlertType(application, type);
    confirmDialog(message, alertType, alertType.remove);
}
