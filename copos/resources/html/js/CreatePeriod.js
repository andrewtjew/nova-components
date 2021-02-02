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
var CreatePeriodRequest = (function (_super) {
    __extends(CreatePeriodRequest, _super);
    function CreatePeriodRequest(contactId, level) {
        var _this = _super.call(this) || this;
        _this.contactId = contactId;
        _this.level = level;
        _this.timeZoneOffset = new Date().getTimezoneOffset();
        _this.start = document.getElementById("start").value;
        _this.duration = Number(document.getElementById("duration").value);
        _this.recurrence = document.getElementById("recurrence").value;
        return _this;
    }
    return CreatePeriodRequest;
}(RequestSender));
function sendCreatePeriodRequest(contactId, level) {
    new CreatePeriodRequest(contactId, level).send("/schedule/period/create", "json");
}
