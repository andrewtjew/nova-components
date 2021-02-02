var Alert = (function () {
    function Alert(id) {
        this.id = id;
    }
    Alert.prototype.remove = function (alert) {
        post("/alert/remove", "json", alert);
    };
    return Alert;
}());
function removeAlert(message, id) {
    var alert = new Alert(id);
    confirmDialog(message, alert, alert.remove);
}
