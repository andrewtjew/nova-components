var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var AddServer = (function (_super) {
    __extends(AddServer, _super);
    function AddServer() {
        var _this = _super.call(this) || this;
        _this.url = document.getElementById("url").value;
        _this.description = document.getElementById("description").value;
        _this.interval = Number(document.getElementById("interval").value);
        return _this;
    }
    return AddServer;
}(RequestSender));
function sendAddServerRequest() {
    var addServer = new AddServer();
    var creator = document.getElementById("creator").value;
    addServer.send("/server/add/post?creator=" + creator, "json");
}
