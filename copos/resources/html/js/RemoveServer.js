var RemoveServerRequest = (function () {
    function RemoveServerRequest(url) {
        this.url = url;
    }
    RemoveServerRequest.prototype.remove = function (period) {
        post("/server/remove", "json", period);
    };
    return RemoveServerRequest;
}());
function removeServer(message, url) {
    var request = new RemoveServerRequest(url);
    confirmDialog(message, request, request.remove);
}
