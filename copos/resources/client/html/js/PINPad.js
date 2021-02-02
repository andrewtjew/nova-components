var PINPadState = (function () {
    function PINPadState(timeoutLocation, delay) {
        var _this = this;
        console.log("cons:" + window.location.href + "," + timeoutLocation);
        this.delay = delay;
        this.timeoutLocation = timeoutLocation;
        if (timeoutLocation != null) {
            this.timerId = setTimeout(function () {
                window.location.href = _this.timeoutLocation;
            }, delay);
        }
    }
    PINPadState.prototype.setPageTimeout = function () {
        var _this = this;
        if (this.timeoutLocation == null) {
            return;
        }
        clearInterval(this.timerId);
        this.timerId = setTimeout(function () {
            window.location.href = _this.timeoutLocation;
        }, this.delay);
    };
    PINPadState.prototype.pressDigit = function (key) {
        this.setPageTimeout();
        var inputElement = document.getElementById("PINPadInput");
        if (inputElement.value.length < 10) {
            inputElement.value += key;
        }
    };
    PINPadState.prototype.pressClear = function () {
        this.setPageTimeout();
        var inputElement = document.getElementById("PINPadInput");
        inputElement.value = "";
    };
    PINPadState.prototype.pressEnter = function () {
        this.setPageTimeout();
        var formElement = document.getElementById("PINPadForm");
        formElement.submit();
    };
    PINPadState.prototype.pressBackSpace = function () {
        this.setPageTimeout();
        var inputElement = document.getElementById("PINPadInput");
        var length = inputElement.value.length;
        if (length > 0) {
            inputElement.value = inputElement.value.substring(0, length - 1);
        }
    };
    return PINPadState;
}());
