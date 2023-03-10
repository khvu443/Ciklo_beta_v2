//jquery check password and confirm password are the same
$('#password2,#password').on('keyup', function () {
    if ($('#password').val() == $('#password2').val()) {
        $("#notifyPassword").html("<i class=\"fa-regular fa-circle-check\"></i> Password and Confirm password are matching").css('color', 'green');
        $('#register').prop("disabled", false);
    } else {
        $('#register').prop("disabled", true);
        $("#notifyPassword").html("<i class=\"fa-solid fa-xmark\"></i> Password and Confirm password are not matching").css('color', 'red');
    }
})

//Indicator how Strong password
let upperChar = /[A-Z]/, //Leter A-Z
    lowerChar = /[a-z]/, // Letter a-z
    number = /[0-9]/,//number 0 - 9
    specialChar = /[!,@#$%^&*?()-+=~_';:".<>]/; //special character

$('#password').keyup(function () {

    let input = document.getElementById("password").value;
    let indicator = 0;

    if (input.match(upperChar)) {
        document.getElementById('upperIcon').className = '';
        $('#upperIcon').addClass("fa-regular fa-circle-check ")
        indicator += 1;
    } else {
        document.getElementById('upperIcon').className = '';
        $('#upperIcon').addClass("fa-solid fa-circle")
    }

    if (input.match(lowerChar)) {
        document.getElementById('lowIcon').className = '';
        $('#lowIcon').addClass("fa-regular fa-circle-check ")
        indicator += 1;
    } else {
        document.getElementById('lowIcon').className = '';
        $('#lowIcon').addClass("fa-solid fa-circle")
    }

    if (input.match(number)) {
        document.getElementById('numberIcon').className = '';
        $('#numberIcon').addClass("fa-regular fa-circle-check")
        indicator += 1;
    } else {
        document.getElementById('numberIcon').className = '';
        $('#numberIcon').addClass("fa-solid fa-circle")
    }

    if (input.match(specialChar)) {
        document.getElementById('specialIcon').className = '';
        $('#specialIcon').addClass("fa-regular fa-circle-check")
        indicator += 1;
    } else {
        document.getElementById('specialIcon').className = '';
        $('#specialIcon').addClass("fa-solid fa-circle")
    }

    if (input.length >= 8) {
        document.getElementById('lengthIcon').className = '';
        $('#lengthIcon').addClass("fa-regular fa-circle-check")
        indicator += 1;
    } else {
        document.getElementById('lengthIcon').className = '';
        $('#lengthIcon').addClass("fa-solid fa-circle")
    }

    let rmvCls = document.getElementById('password'); // for change class password
    let rmvCls1 = document.getElementById("progressBar");// for change class progress bar
    let rmvCls2 = document.getElementById("notifyStrength");// for change class notify strength password

    switch (indicator) {
        case 0:
            //Remove all class name in tag to make change
            rmvCls.className = '';
            rmvCls1.className = '';
            rmvCls2.className = '';
            //Add class to change color base on how strength
            $('#password').addClass(["form-control"]);

            //Change color and add 20% base on what condition pass
            $('#progressBar').addClass(["progress-bar"]);
            $('#progressBar').css("width", "0%");

            // CHanged for aria-valuenow
            $('#bar').attr("aria-valuenow", 0);

            // make check for the conditional has passed
            $('#notifyStrength').html("");
            break;

        case 1:
            //Remove all class name in tag to make change
            rmvCls.className = '';
            rmvCls1.className = '';
            rmvCls2.className = '';
            //Add class to change color base on how strength
            $('#password').addClass(["form-control", "bg-danger", "border border-3", "border-danger", "text-secondary", "bg-opacity-10"]);

            //Change color and add 20% base on what condition pass
            $('#progressBar').addClass(["progress-bar", "bg-danger"]);
            $('#progressBar').css("width", "20%");

            // CHanged for aria-valuenow
            $('#bar').attr("aria-valuenow", 20);

            // Tick the conditional has pass
            $('#notifyStrength').html("<i class=\"fa-solid fa-circle-info\"></i> Password is weak");
            $('#notifyStrength').addClass("text-danger");
            break;

        case 2:
            //Remove all class name in tag to make change
            rmvCls.className = '';
            rmvCls1.className = '';
            rmvCls2.className = '';
            //Add class to change color base on how strength
            $('#password').addClass(["form-control", "bg-warning", "border border-3", "border-warning", "text-secondary", "bg-opacity-10"]);

            //Change color and add 20% base on what condition pass
            $('#progressBar').addClass(["progress-bar", "bg-warning"]);
            $('#progressBar').css("width", "40%");

            // CHanged for aria-valuenow
            $('#bar').attr("aria-valuenow", 40);

            // Tick the conditional has pass
            $('#notifyStrength').html("<i class=\"fa-solid fa-circle-info\"></i> Password is medium");
            $('#notifyStrength').addClass("text-warning");
            break;

        case 3:
            //Remove all class name in tag to make change
            rmvCls.className = '';
            rmvCls1.className = '';
            rmvCls2.className = '';
            //Add class to change color base on how strength
            $('#password').addClass(["form-control", "bg-warning", "border border-3", "border-warning", "text-secondary", "bg-opacity-10"]);

            //Change color and add 20% base on what condition pass
            $('#progressBar').addClass(["progress-bar", "bg-warning"]);
            $('#progressBar').css("width", "60%");

            // CHanged for aria-valuenow
            $('#bar').attr("aria-valuenow", 60);

            // Tick the conditional has pass
            $('#notifyStrength').html("<i class=\"fa-solid fa-circle-info\"></i> Password is medium");
            $('#notifyStrength').addClass("text-warning");
            break;

        case 4:
            //Remove all class name in tag to make change
            rmvCls.className = '';
            rmvCls1.className = '';
            rmvCls2.className = '';
            //Add class to change color base on how strength
            $('#password').addClass(["form-control", "bg-warning", "border border-3", "border-warning", "text-secondary", "bg-opacity-10"]);

            //Change color and add 20% base on what condition pass
            $('#progressBar').addClass(["progress-bar", "bg-warning"]);
            $('#progressBar').css("width", "80%");

            // CHanged for aria-valuenow
            $('#bar').attr("aria-valuenow", 80);

            // Tick the conditional has pass
            $('#notifyStrength').html("<i class=\"fa-solid fa-circle-info\"></i> Password is medium");
            $('#notifyStrength').addClass("text-warning");
            break;

        case 5:
            //Remove all class name in tag to make change
            rmvCls.className = '';
            rmvCls1.className = '';
            rmvCls2.className = '';
            //Add class to change color base on how strength
            $('#password').addClass(["form-control", "bg-success", "border border-3", "border-success", "text-secondary", "bg-opacity-10"]);

            //Change color and add 20% base on what condition pass
            $('#progressBar').addClass(["progress-bar", "bg-success"]);
            $('#progressBar').css("width", "100%");

            // CHanged for aria-valuenow
            $('#bar').attr("aria-valuenow", 100);

            // Tick the conditional has pass
            $('#notifyStrength').html("<i class=\"fa-solid fa-circle-info\"></i> Password is strong");
            $('#notifyStrength').addClass("text-success");
            break;
    }
})