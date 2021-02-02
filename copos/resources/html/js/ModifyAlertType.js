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
var ModifyAlertTypeRequest = (function (_super) {
    __extends(ModifyAlertTypeRequest, _super);
    function ModifyAlertTypeRequest(application, type) {
        var _this = _super.call(this) || this;
        _this.application = application;
        _this.type = type;
        _this.reminderInterval = Number(document.getElementById("reminderInterval").value);
        _this.groupingInterval = Number(document.getElementById("groupingInterval").value);
        return _this;
    }
    return ModifyAlertTypeRequest;
}(RequestSender));
function sendModifyAlertTypeRequest(application, type) {
    var request = new ModifyAlertTypeRequest(application, type);
    request.send("/alertType/modify", "json");
}
