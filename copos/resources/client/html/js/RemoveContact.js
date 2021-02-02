var Contact = (function () {
    function Contact(id) {
        this.id = id;
    }
    Contact.prototype.remove = function (alertType) {
        post("/contact/remove", "json", alertType);
    };
    return Contact;
}());
function removeContact(message, id) {
    var contact = new Contact(id);
    confirmDialog(message, contact, contact.remove);
}
