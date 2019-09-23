var BillState = (function () {
    function BillState(cashable, PIN, bingoCardPrice) {
        this.bingoCardPrize = bingoCardPrice;
        this.cashable = cashable;
        this.PIN = PIN;
        this.b1 = 0;
        this.b2 = 0;
        this.b5 = 0;
        this.b10 = 0;
        this.b20 = 0;
        this.b50 = 0;
        this.b100 = 0;
        this.stack = [];
    }
    BillState.prototype.add = function (amount) {
        if (amount == 1) {
            this.b1++;
        }
        else if (amount == 2) {
            this.b2++;
        }
        else if (amount == 5) {
            this.b5++;
        }
        else if (amount == 10) {
            this.b10++;
        }
        else if (amount == 20) {
            this.b20++;
        }
        else if (amount == 50) {
            this.b50++;
        }
        else if (amount == 100) {
            this.b100++;
        }
        this.stack.push(amount);
    };
    BillState.prototype.remove = function (amount) {
        if (amount == 1) {
            this.b1--;
        }
        else if (amount == 2) {
            this.b2--;
        }
        else if (amount == 5) {
            this.b5--;
        }
        else if (amount == 10) {
            this.b10--;
        }
        else if (amount == 20) {
            this.b20--;
        }
        else if (amount == 50) {
            this.b50--;
        }
        else if (amount == 100) {
            this.b100--;
        }
    };
    BillState.prototype.clearBills = function () {
        this.b1 = 0;
        this.b2 = 0;
        this.b5 = 0;
        this.b10 = 0;
        this.b20 = 0;
        this.b50 = 0;
        this.b100 = 0;
        this.stack = [];
    };
    BillState.prototype.undo = function () {
        if (this.stack.length > 0) {
            var amount = this.stack.pop();
            this.remove(amount);
        }
    };
    BillState.prototype.getBillTotal = function () {
        return (this.b1 + this.b2 * 2 + this.b5 * 5 + this.b10 * 10 + this.b20 * 20 + this.b50 * 50 + this.b100 * 100) * 100;
    };
    BillState.prototype.getCashable = function () {
        return this.cashable;
    };
    return BillState;
}());
function initialize(cashable, PIN, bingoCardPrice) {
    billState = new BillState(cashable, PIN, bingoCardPrice);
}
function add(amount) {
    billState.add(amount);
    refresh();
}
function clearBills() {
    if ($('#clearBills').hasClass('disabled') == false) {
        billState.clearBills();
        refresh();
    }
}
function undo() {
    if ($('#undo').hasClass('disabled') == false) {
        billState.undo();
        refresh();
    }
}
function format(denomination, count) {
    if (count == 0) {
        return "";
    }
    return count + " x $" + denomination + " = $" + (denomination * count) + "<br />";
}
function showConfirm() {
    if ($('#submit').hasClass('disabled') == false) {
        var cards = billState.getBillTotal() / billState.bingoCardPrize;
        $('#confirmMessage').html("Purchase " + cards + " bingo cards for $" + billState.getBillTotal() / 100 + "?");
        $('#confirm').modal("show");
    }
}
function purchase() {
    evalPost("/user/PIN/purchase", "json", billState);
}
function refresh() {
    var total = billState.getBillTotal();
    if (total > 0) {
        $('#clearBills').removeClass('disabled');
        $('#undo').removeClass('disabled');
        $('#submit').removeClass('disabled');
        var text = format(100, billState.b100);
        text += format(50, billState.b50);
        text += format(20, billState.b20);
        text += format(10, billState.b10);
        text += format(5, billState.b5);
        text += format(2, billState.b2);
        text += format(1, billState.b1);
        text += "<hr />Total: $" + total / 100;
        document.getElementById("output").innerHTML = text;
    }
    else {
        $('#clearBills').addClass('disabled');
        $('#undo').addClass('disabled');
        $('#submit').addClass('disabled');
        document.getElementById("output").innerHTML = "";
    }
    var cards = total / billState.bingoCardPrize;
    document.getElementById("amount").value = "$" + (total / 100).toString();
    document.getElementById("cards").value = cards.toString();
}
