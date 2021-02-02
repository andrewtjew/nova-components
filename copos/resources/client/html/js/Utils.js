function toggle() {
    var winner = document.getElementById("winner");
    var sequenceElement = document.getElementById("sequenceNumber");
    sequenceElement.required = winner.checked;
}
