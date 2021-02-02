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
var CreateContactRequest = (function (_super) {
    __extends(CreateContactRequest, _super);
    function CreateContactRequest() {
        var _this = _super.call(this) || this;
        _this.id = document.getElementById("id").value;
        _this.email = document.getElementById("email").value;
        _this.smsNumber = document.getElementById("smsNumber").value;
        _this.info = document.getElementById("info").value;
        return _this;
    }
    return CreateContactRequest;
}(RequestSender));
function sendCreateContactRequest() {
    new CreateContactRequest().send("/contact/create", "json");
}
