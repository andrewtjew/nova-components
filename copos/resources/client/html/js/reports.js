function runSummaryReport() {
    var range = document.getElementById("picker").value;
    window.location.assign("/report/summary?range=" + range);
}
function runPlayerReport() {
    var range = document.getElementById("picker").value;
    var PIN = document.getElementById("PIN").value;
    window.location.assign("/report/player?range=" + range + "&PIN=" + PIN);
}
function runCashierReport() {
    var cashier;
    var range = document.getElementById("picker").value;
    var select = document.getElementById("cashier");
    if (select == null) {
        var hidden = document.getElementById("hidden");
        cashier = hidden.value;
    }
    else {
        cashier = select.options[select.selectedIndex].value;
    }
    window.location.assign("/report/cashier?range=" + range + "&cashier=" + cashier);
}
