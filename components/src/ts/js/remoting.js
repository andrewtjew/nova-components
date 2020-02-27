var org;
(function (org) {
    var nova;
    (function (nova) {
        var html;
        (function (html) {
            var remoting;
            (function (remoting) {
                class HtmlResult {
                }
                class ModalResult {
                }
                class ValResult {
                }
                class RemoveClassResult {
                }
                class AddClassResult {
                }
                class PropResult {
                }
                class ClearTimerCommand {
                }
                class Response {
                }
                function get(pathAndQuery, result = null) {
                    call("GET", pathAndQuery, result);
                }
                remoting.get = get;
                function post(pathAndQuery, result = null) {
                    call("POST", pathAndQuery, null);
                }
                remoting.post = post;
                function scheduleGet(pathAndQuery, timerName, interval) {
                    scheduleRun("GET", pathAndQuery, timerName, interval);
                }
                remoting.scheduleGet = scheduleGet;
                function schedulePost(pathAndQuery, timerName, interval) {
                    scheduleRun("POST", pathAndQuery, timerName, interval);
                }
                remoting.schedulePost = schedulePost;
                var timers = [];
                function scheduleRun(type, pathAndQuery, timerName, interval) {
                    clearTimer[timerName];
                    var timerId = window.setInterval(function () { call(type, pathAndQuery, null); }, interval);
                    timers[timerName] = timerId;
                }
                function clearTimer(timerName) {
                    var timerId = timers[timerName];
                    if (timerId != null) {
                        window.clearInterval(timerId);
                    }
                    timers[timerName] = null;
                }
                function call(type, pathAndQuery, result) {
                    $.ajax({ url: pathAndQuery,
                        type: type,
                        dataType: "json",
                        cache: false,
                        success: function (response) {
                            if (response.clearTimerCommands != null) {
                                response.clearTimerCommands.forEach(item => {
                                    clearTimer(item.timerName);
                                });
                            }
                            if (response.htmlResults != null) {
                                response.htmlResults.forEach(item => {
                                    $("#" + item.id).html(item.html);
                                });
                            }
                            if (response.appendResults != null) {
                                response.appendResults.forEach(item => {
                                    var template = document.createElement("template");
                                    template.innerHTML = item.html;
                                });
                            }
                            if (response.modalResults != null) {
                                response.modalResults.forEach(item => {
                                    $("#" + item.id).modal(item.option);
                                });
                            }
                            if (response.valResults != null) {
                                response.valResults.forEach(item => {
                                    $("#" + item.id).val(item.val);
                                });
                            }
                            if (response.removeClassResults != null) {
                                response.removeClassResults.forEach(item => {
                                    $("#" + item.id).removeClass(item.class_);
                                });
                            }
                            if (response.addClassResults != null) {
                                response.addClassResults.forEach(item => {
                                    $("#" + item.id).addClass(item.class_);
                                });
                            }
                            if (response.propResults != null) {
                                response.propResults.forEach(item => {
                                    if (item.value != null) {
                                        $("#" + item.id).prop(item.prop, item.value);
                                    }
                                    else {
                                        $("#" + item.id).prop(item.prop);
                                    }
                                });
                            }
                            if (response.script != null) {
                                try {
                                    eval(response.script);
                                }
                                catch (ex) {
                                }
                            }
                            if (response.location != null) {
                                document.location.href = response.location;
                            }
                            if (result != null) {
                                if (response.result != null) {
                                    result(JSON.parse(response.result));
                                }
                                else {
                                    result(null);
                                }
                            }
                        },
                        error: function (xhr) {
                            alert("Error: " + xhr.status + " " + xhr.statusText);
                        }
                    });
                }
                remoting.call = call;
            })(remoting = html.remoting || (html.remoting = {}));
        })(html = nova.html || (nova.html = {}));
    })(nova = org.nova || (org.nova = {}));
})(org || (org = {}));
export class CallBuilder {
    constructor(path) {
        this.path = path;
        this.connector = path.indexOf("?") ? "&" : "?";
    }
    inputValue(id) {
        var input = document.getElementById(id);
        this.stringValue(input.name, input.value);
    }
    stringValue(name, value) {
        this.path = this.path + this.connector + name + "=" + encodeURI(value);
        this.connector = "&";
    }
    numberValue(name, value) {
        this.stringValue(name, value.toString());
    }
    pathAndQuery() {
        return this.path;
    }
    post() {
        org.nova.html.remoting.post(this.path);
    }
    get() {
        org.nova.html.remoting.get(this.path);
    }
}
