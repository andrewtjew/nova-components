var PINPad = (function () {
    function PINPad() {
    }
    PINPad.pressDigit = function (key) {
        var inputElement = document.getElementById("PINPadInput");
        if (inputElement.value.length < 10) {
            inputElement.value += key;
        }
    };
    PINPad.pressClear = function () {
        var inputElement = document.getElementById("PINPadInput");
        inputElement.value = "";
    };
    PINPad.pressEnter = function () {
        var formElement = document.getElementById("PINPadForm");
        formElement.submit();
    };
    return PINPad;
}());
