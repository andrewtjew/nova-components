function confirmBleed(balance) {
    var amountValue = document.getElementById("amount").value;
    var amount = Number(amountValue);
    var notValidNumber = isNaN(amount);
    if (notValidNumber == true) {
        $('#errorMessage').html("Invalid input: " + amountValue);
        $('#error').modal("show");
        return;
    }
    if (amount <= 0) {
        $('#errorMessage').html("Cannot bleed amount of $" + amount.toFixed(2));
        $('#error').modal("show");
        return;
    }
    if (amount * 100 > balance) {
        $('#errorMessage').html("Cannot bleed more than cash drawer balance of $" + (balance / 100.0).toFixed(2));
        $('#error').modal("show");
        return;
    }
    $('#confirmMessage').html("Bleed $" + amount.toFixed(2) + " from cash drawer?");
    $('#confirm').modal("show");
}
function bleed() {
    var amountValue = document.getElementById("amount").value;
    var amount = Number(amountValue) * 100;
    evalPost("/user/cashDrawer/bleed", "json", amount);
}
function confirmFill(balance) {
    var amountValue = document.getElementById("amount").value;
    var amount = Number(amountValue);
    var notValidNumber = isNaN(amount);
    if (notValidNumber == true) {
        $('#errorMessage').html("Invalid input: " + amountValue);
        $('#error').modal("show");
        return;
    }
    if (amount <= 0) {
        $('#errorMessage').html("Cannot fill  amount of $" + amount.toFixed(2));
        $('#error').modal("show");
        return;
    }
    $('#confirmMessage').html("Fill cash drawer with $" + amount.toFixed(2) + "?");
    $('#confirm').modal("show");
}
function fill() {
    var amountValue = document.getElementById("amount").value;
    var amount = Number(amountValue) * 100;
    evalPost("/user/cashDrawer/fill", "json", amount);
}
function checkCreatePlayer() {
    var canCreate = true;
    $("input:required").each(function (index) {
        var value = $(this).val();
        if (value == null) {
            canCreate = false;
        }
        else if (value.trim().length == 0) {
            canCreate = false;
        }
    });
    if (canCreate) {
        $("#create").removeClass("disabled");
    }
    else {
        $("#create").addClass("disabled");
    }
}
