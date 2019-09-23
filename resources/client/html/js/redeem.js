var RedeemState = (function () {
    function RedeemState(PIN, balance, redeemed) {
        this.balance = balance;
        this.redeemed = redeemed;
        this.PIN = PIN;
    }
    return RedeemState;
}());
Object.keys = function (obj) {
    var keys = [];
    for (var i in obj) {
        if (obj.hasOwnProperty(i)) {
            keys.push(i);
        }
    }
    return keys;
};
function initializeRedeem(PIN, balance, redeemed) {
    redeemState = new RedeemState(PIN, balance, redeemed);
}
function clearAmount() {
    var amountInput = document.getElementById("amount");
    amountInput.value = "0";
    refreshRedeem();
}
function allCards() {
    var amountInput = document.getElementById("amount");
    amountInput.value = redeemState.balance.toString();
    refreshRedeem();
}
function press(key) {
    var amountInput = document.getElementById("amount");
    var value = null;
    if (amountInput.value == '0') {
        value = key.toString();
    }
    else {
        value = amountInput.value + key.toString();
    }
    amountInput.value = value;
    refreshRedeem();
}
function checkRedeem() {
    if ($('#submit').hasClass('disabled') == false) {
        var amountInput = document.getElementById("amount");
        var amount = Number(amountInput.value);
        if (amount > redeemState.balance) {
            $('#errorMessage').html("Amount entered exceedes available balance of " + redeemState.balance);
            $('#error').modal("show");
        }
        else {
            $('#confirmMessage').html("Redeem  " + amount + " bingo cards?");
            $('#confirm').modal("show");
        }
    }
}
function redeem() {
    var amountInput = document.getElementById("amount");
    var amount = Number(amountInput.value);
    redeemState.amount = amount;
    evalPost("/user/PIN/redeem", "json", redeemState);
}
function refreshRedeem() {
    var amountInput = document.getElementById("amount");
    var amount = Number(amountInput.value);
    if (amount > 0) {
        if (amount <= redeemState.balance) {
            $('#submit').removeClass('disabled');
            var text = "Redeem " + amount + "  cards.<br />";
            text += "Purchased balance: " + (redeemState.balance - amount) + "<br />";
            text += "Redeemed balance: " + (redeemState.redeemed + amount) + "<br />";
            document.getElementById("output").innerHTML = text;
        }
        else {
            $('#submit').addClass('disabled');
            var text = "Number of cards to redeem exceeds available balance of " + redeemState.balance;
            document.getElementById("output").innerHTML = text;
        }
    }
    else {
        document.getElementById("output").innerHTML = "";
        $('#submit').addClass('disabled');
    }
}
