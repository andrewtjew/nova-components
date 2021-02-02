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
var CreateAlertTypeRequest = (function (_super) {
    __extends(CreateAlertTypeRequest, _super);
    function CreateAlertTypeRequest() {
        var _this = _super.call(this) || this;
        _this.application = document.getElementById("application").value;
        _this.type = document.getElementById("type").value;
        _this.description = document.getElementById("description").value;
        _this.reminderInterval = Number(document.getElementById("reminderInterval").value);
        _this.groupingInterval = Number(document.getElementById("groupingInterval").value);
        return _this;
    }
    return CreateAlertTypeRequest;
}(RequestSender));
function sendCreateAlertTypeRequest() {
    new CreateAlertTypeRequest().send("/alertType/create", "json");
}
