var Keyboard;
(function (Keyboard) {
    var State = (function () {
        function State(timeoutLocation, delay) {
            var _this = this;
            this.shift = false;
            this.caps = false;
            this.firstShift = false;
            this.updateKeys();
            this.delay = delay;
            this.timeoutLocation = timeoutLocation;
            if (timeoutLocation != null) {
                this.timerId = setTimeout(function () {
                    window.location.href = _this.timeoutLocation;
                }, delay);
            }
        }
        State.prototype.setPageTimeout = function () {
            var _this = this;
            if (this.timeoutLocation == null) {
                return;
            }
            clearInterval(this.timerId);
            this.timerId = setTimeout(function () {
                window.location.href = _this.timeoutLocation;
            }, this.delay);
        };
        State.prototype.updateKeys = function () {
            var _this = this;
            this.setPageTimeout();
            {
                var keys = document.getElementsByName("letter");
                keys.forEach(function (element) {
                    var button = element;
                    if (_this.shift != _this.caps) {
                        button.value = button.value.toUpperCase();
                    }
                    else {
                        button.value = button.value.toLowerCase();
                    }
                    button.innerText = button.value;
                });
            }
            {
                var keys = document.getElementsByName("shift");
                keys.forEach(function (element) {
                    var button = element;
                    if (_this.shift) {
                        button.classList.add("button-shift-down");
                        button.classList.remove("button-shift");
                    }
                    else {
                        button.classList.add("button-shift");
                        button.classList.remove("button-shift-down");
                    }
                });
            }
            {
                var keys = document.getElementsByName("caps");
                keys.forEach(function (element) {
                    var button = element;
                    if (_this.caps) {
                        button.classList.add("button-shift-down");
                        button.classList.remove("button-shift");
                    }
                    else {
                        button.classList.add("button-shift");
                        button.classList.remove("button-shift-down");
                    }
                });
            }
        };
        return State;
    }());
    Keyboard.State = State;
    function show() {
        Keyboard.state.shift = true;
        Keyboard.state.updateKeys();
    }
    Keyboard.show = show;
    function handleKey(event) {
        var focusElement = document.activeElement;
        if (focusElement == null) {
            return;
        }
        var element = event.target;
        if (element.value.length > 1) {
            var index = Keyboard.state.shift ? 1 : 0;
            focusElement.value += element.value.charAt(index);
        }
        else {
            focusElement.value += element.value;
        }
        Keyboard.state.shift = false;
        Keyboard.state.updateKeys();
    }
    Keyboard.handleKey = handleKey;
    function handleShift(event) {
        var element = event.target;
        Keyboard.state.shift = !Keyboard.state.shift;
        Keyboard.state.updateKeys();
    }
    Keyboard.handleShift = handleShift;
    function findNextTabStop(el) {
        var universe = document.querySelectorAll('input, button, select, textarea, a[href]');
        var list = Array.prototype.filter.call(universe, function (item) { return item.tabIndex >= "0"; });
        var index = list.indexOf(el);
        return list[index + 1] || list[0];
    }
    function handleEnter(event) {
        var focusElement = document.activeElement;
        if (focusElement == null) {
            return;
        }
        var nextEl = findNextTabStop(focusElement);
        var inputElement = nextEl;
        if (inputElement != null) {
            inputElement.focus();
            if (inputElement.attributes.getNamedItem("firstCap") != null) {
                Keyboard.state.shift = inputElement.value.length == 0;
            }
            else {
                if (inputElement.value.length == 0) {
                    Keyboard.state.shift = false;
                }
            }
            Keyboard.state.updateKeys();
        }
    }
    Keyboard.handleEnter = handleEnter;
    function handleCaps(event) {
        var element = event.target;
        Keyboard.state.caps = !Keyboard.state.caps;
        Keyboard.state.updateKeys();
    }
    Keyboard.handleCaps = handleCaps;
    function handleBackSpace(event) {
        var focusElement = document.activeElement;
        if (focusElement == null) {
            return;
        }
        var length = focusElement.value.length;
        if (length > 0) {
            focusElement.value = focusElement.value.substr(0, length - 1);
        }
        if (focusElement.attributes.getNamedItem("firstCap") != null) {
            Keyboard.state.shift = focusElement.value.length == 0;
        }
        else {
            if (focusElement.value.length == 0) {
                Keyboard.state.shift = false;
            }
        }
        Keyboard.state.updateKeys();
    }
    Keyboard.handleBackSpace = handleBackSpace;
    function inputClick(event) {
        var element = event.target;
        element.value = element.value + " ";
        handleBackSpace(event);
    }
    Keyboard.inputClick = inputClick;
    function mousedown(event) {
        event.preventDefault();
        var element = event.target;
        element.value = element.value;
    }
    Keyboard.mousedown = mousedown;
    function test(event) {
        console.log(event.type + ": key:" + event.key + ", code:" + event.code + ",charCode:" + event.charCode + ",char:" + event.char + ",target:" + event.target);
    }
    Keyboard.test = test;
    function keydown(event) {
        console.log(event.type + ": key:" + event.key + ", code:" + event.code + ",charCode:" + event.charCode + ",char:" + event.char + ",target:" + event.target);
    }
    Keyboard.keydown = keydown;
    function keypress(event) {
        console.log(event.type + ": key:" + event.key + ", code:" + event.code + ",charCode:" + event.charCode + ",char:" + event.char + ",target:" + event.target);
    }
    Keyboard.keypress = keypress;
    function input(event) {
        console.log(event.type + ": data:" + event.data + ", which:" + event.which + ",target:" + event.target);
    }
    Keyboard.input = input;
    function keyup(event) {
        console.log(event.type + ": key:" + event.key + ", code:" + event.code + ",charCode:" + event.charCode + ",char:" + event.char + ",target:" + event.target);
    }
    Keyboard.keyup = keyup;
    function change(event) {
        console.log(event.type + ",target:" + event.target + ",bubbles:" + event.bubbles);
    }
    Keyboard.change = change;
})(Keyboard || (Keyboard = {}));
