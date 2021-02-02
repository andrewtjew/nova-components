function openAccordions() {
    var elements = document.getElementsByClassName("accordion");
    for (var i = 0; i < elements.length; i++) {
        var element = elements[i];
        openAccordion(element);
    }
}
function openAccordion(element) {
    element.classList.add('active');
    var panel = element.nextElementSibling;
    if (panel.style.maxHeight) {
    }
    else {
        panel.style.maxHeight = panel.scrollHeight + 'px';
    }
}
