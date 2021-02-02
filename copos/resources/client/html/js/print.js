function printReceipt(receiptUrl, fontSize, confirm) {
    var result = post(receiptUrl, "json", null);
    var lines = JSON.parse(result);
    window.external.PrintReceipt(result, fontSize);
    if (confirm != null) {
        $(confirm).modal("show");
    }
}
function printDocument(documentUrl, confirm) {
    var result = post(documentUrl, "json", null);
    window.external.PrintDocument(result);
    if (confirm != null) {
        $(confirm).modal("show");
    }
}
