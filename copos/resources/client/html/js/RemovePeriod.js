var Period = (function () {
    function Period(contactId, level, start, duration, recurrence) {
        this.contactId = contactId;
        this.level = level;
        this.start = start;
        this.duration = duration;
        this.recurrence = recurrence;
    }
    Period.prototype.remove = function (period) {
        post("/period/remove", "json", period);
    };
    return Period;
}());
function removeCoveragePeriod(message, contactId, level, start, duration, recurrence) {
    var period = new Period(contactId, level, start, duration, recurrence);
    confirmDialog(message, period, period.remove);
}
