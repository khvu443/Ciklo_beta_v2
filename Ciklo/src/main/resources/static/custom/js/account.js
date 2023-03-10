//Set value for form update
function passValueUpdate(id, username, fname, lname, phone) {
    $("#id").attr("value", id);
    $("#username").attr("value", username);
    $("#firstname").attr("value", fname);
    $("#lastname").attr("value", lname);
    $("#phone").attr("value", phone);
}

function checkIdToSet(dataPoints) {
    if (dataPoints.authorities[0].authority === "CUSTOMER") {
        passValueUpdate(
            dataPoints.cusId,
            dataPoints.cemail,
            dataPoints.cfirstname,
            dataPoints.clastname,
            dataPoints.cphone,
        )
    } else if (dataPoints.authorities[0].authority === "DRIVER") {
        passValueUpdate(
            dataPoints.driverId,
            dataPoints.demail,
            dataPoints.dfirstname,
            dataPoints.dlastname,
            dataPoints.dphone,
        )
    } else if (dataPoints.authorities[0].authority === "ADMIN") {
        passValueUpdate(
            dataPoints.adId,
            dataPoints.adEmail,
            dataPoints.adFirstname,
            dataPoints.adLastname,
            dataPoints.adPhone,
        )
    }
}

$("#close-update").on("click", function ()
{
    $("#update-notice").empty();
    $("#password").removeClass("bg-danger text-secondary border-danger").val('');
    $("#password2").val('');
    $("#notifyPassword").empty();
    $("#notifyStrength").empty().removeAttr("class").attr("class","form-text");
    $("#progressBar").css("width", "0%");
    $('#bar').attr("aria-valuenow", 0);
})

//update account
$("#btn-confirm-update").on("click", function () {
        if ($("#password").val() === $("#password2").val()) {
            $.ajax(
                {
                    url: 'http://localhost:8080/ciklo/auth/updateAccount',
                    type: 'POST',
                    data:
                        {
                            "id": $("#id").val(),
                            "email": $("#username").val(),
                            "fname": $("#firstname").val(),
                            "lname": $("#lastname").val(),
                            "password": $("#password").val(),
                            "phone": $("#phone").val(),

                        },
                    error: function () {
                        appendNotice("false", "update-notice", "Update");
                    },
                    success: function (data) {
                        appendNotice(data, "update-notice", "Update");
                    }
                }
            )
        }
        else
        {
            appendNotice("false", "update-notice", "Update");
        }

    }
)
