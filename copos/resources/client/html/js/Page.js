var IdleWatcher = (function () {
    function IdleWatcher(initialSeconds, mousemoveSeconds, mousedownSeconds, keyboardSeconds) {
        this.mousemoveSeconds = mousemoveSeconds;
        this.mousedownSeconds = mousedownSeconds;
        this.keyboardSeconds = keyboardSeconds;
        this.secondsLeft = initialSeconds;
        this.timer = setInterval(this.check, 1000);
        window.onmousemove = this.resetMousemove;
        window.onmousedown = this.resetMousedown;
        window.onkeydown = this.resetKeyboard;
    }
    IdleWatcher.prototype.resetMousemove = function () {
        if (thisIdleWatcher.secondsLeft < thisIdleWatcher.mousemoveSeconds) {
            thisIdleWatcher.secondsLeft = thisIdleWatcher.mousemoveSeconds;
        }
    };
    IdleWatcher.prototype.resetMousedown = function () {
        if (thisIdleWatcher.secondsLeft < thisIdleWatcher.mousedownSeconds) {
            thisIdleWatcher.secondsLeft = thisIdleWatcher.mousedownSeconds;
        }
    };
    IdleWatcher.prototype.resetKeyboard = function () {
        thisIdleWatcher.secondsLeft = thisIdleWatcher.keyboardSeconds;
    };
    IdleWatcher.prototype.check = function () {
        if (thisIdleWatcher.secondsLeft <= 0) {
            window.location.href = "/alert/status";
        }
        else {
            thisIdleWatcher.secondsLeft--;
        }
    };
    return IdleWatcher;
}());
var thisIdleWatcher;
window.onload = function () {
    thisIdleWatcher = new IdleWatcher(600, 30, 120, 300);
};
