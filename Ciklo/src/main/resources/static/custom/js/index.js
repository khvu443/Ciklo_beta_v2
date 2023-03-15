function isReload() {
    const pageAccessedByReload = ((window.performance.navigation && window.performance.navigation.type === 1) || window.performance
        .getEntriesByType('navigation')
        .map((nav) => nav.type)
        .includes('reload'));

    return pageAccessedByReload;
}

//---------------------------Info User----------------------------------------------------------------------------------
$(document).ready(function () {

    $.ajax(
        {
            url: '/ciklo/homepage/userInfo',
            type: 'GET',
            data: {},
            error: function () {
                console.log("error");
            },
            success: function (data) {
                if (Object.keys(data).length > 0) {
                    $(".site-mobile-menu-body  #login, .site-nav  #login").attr("hidden", true);

                    $(".site-mobile-menu-body #user").text(data.name);
                    $(".site-nav #user").text(data.name);

                    switch (data.role) {
                        case "DRIVER":
                            $(".site-mobile-menu-body #specific , .site-nav #specific").empty().append(
                                "<hr class=\"hr\"/>\n" +
                                " <a class=\"btn btn-sm border border-0 text-decoration-none\" href=\"/ciklo/driver/\">Driver HomePage</a>"
                            )


                            $("#booking").attr("hidden", "true")
                            break;
                        case "ADMIN":
                            $(".site-mobile-menu-body #specific, .site-nav #specific").empty().append(
                                "<hr class=\"hr\"/>\n" +
                                "<a class=\"btn btn-sm border border-0 text-decoration-none\" href=\"/ciklo/admin/\">Admin\n" +
                                "HomePage</a>"
                            )
                            $("#booking").attr("hidden", "true")
                            break;
                        case "CUSTOMER":
                            $(".site-mobile-menu-body #specific , .site-nav #specific").empty().append(
                                "<hr class=\"hr\"/>\n" +
                                " <button id=\"btn-view-Bill\" class=\"btn btn-sm border border-0\" type=\"button\"\n" +
                                "  data-bs-toggle=\"modal\" onclick='viewAllBills()' data-bs-target=\"#modal-view-bill\">History Invoice\n" +
                                "</button>"
                            )
                            break;
                    }
                } else {
                    $("#booking, .site-mobile-menu-body #specific, .site-nav #specific,.site-nav #dropdown, .site-mobile-menu-body #dropdown,.site-nav #notice, .site-mobile-menu-body #notice,.site-mobile-menu-body #has-login, .site-nav #has-login").attr("hidden", true)
                    $(".site-mobile-menu-body  #login, .site-nav  #login").attr("hidden", false);

                }
            }
        }
    )
})

//----------------------Check if email is exist or not------------------------------------------------------------------
$(document).ready(function () {
    $("#email").on('focusin keyup', function () {
        let email = $('#email').val();
        const regex = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
        if (email.match(regex)) {
            $.ajax(
                {
                    url: '/ciklo/auth/checkEmail',
                    type: 'GET',
                    data:
                        {
                            'email': email,
                        },
                    error: function () {
                        console.log("Error");
                        console.log('not have');
                        $('#notifyEmail').className = '';
                        $('#notifyEmail').addClass("form-text text-success-emphasis");
                        $('#notifyEmail').html('Valid Email');
                    },
                    success: function (data) {

                        const dataPoints = JSON.parse(data);
                        console.log(dataPoints);
                        if (dataPoints != null) {
                            console.log('have');
                            $('#notifyEmail').className = '';
                            $('#notifyEmail').addClass("form-text text-danger-emphasis");
                            $('#notifyEmail').html('Email has already existed');
                        } else {
                            console.log('not have');
                            $('#notifyEmail').className = '';
                            $('#notifyEmail').addClass("form-text text-success-emphasis");
                            $('#notifyEmail').html('Valid Email');
                        }
                    }
                }
            )
        } else {
            $('#notifyEmail').className = '';
            $('#notifyEmail').addClass("form-text text-danger-emphasis");
            $('#notifyEmail').html('Invalid Email');
        }
    })
})

//----------------------------Check the required is empty or not--------------------------------------------------------
$(document).ready(function () {
    $('#email').on('focusout keyup', function () {
        if ($("#email").val().length == 0) {
            document.getElementById('email').className = '';
            $('#email').addClass(["form-control", "bg-danger", "border border-3", "border-danger", "text-secondary", "bg-opacity-10"]);
            $('#notifyEmail').addClass("text-danger-emphasis");
            $('#notifyEmail').html("This field can not be empty");
        } else {
            document.getElementById('email').className = '';
            document.getElementById('notifyEmail').className = '';
            $('#email').addClass("form-control");
            $('#notifyEmail').addClass("form-text");
            $('#notifyEmail').empty();
        }
    })

    $('#password').on('focusout keyup', function () {
        if ($("#password").val().length == 0) {
            document.getElementById('password').className = '';
            $('#password').addClass(["form-control", "bg-danger", "border border-3", "border-danger", "text-secondary", "bg-opacity-10"]);
            $('#notifyPasswordCheck').addClass("text-danger-emphasis");
            $('#notifyPasswordCheck').html("This field can not be empty");
        } else {
            document.getElementById('notifyPasswordCheck').className = '';
            $('#notifyPasswordCheck').addClass("form-text");
            $('#notifyPasswordCheck').empty();
        }
    })

    $('#password2').on('focusout keyup', function () {
        if ($("#password2").val().length == 0) {
            document.getElementById('password2').className = '';
            $('#password2').addClass(["form-control", "bg-danger", "border border-3", "border-danger", "text-secondary", "bg-opacity-10"]);
        } else {
            document.getElementById('password2').className = '';
            $('#password2').addClass("form-control");
        }
    })

    $('#phone').on('focusout keyup', function () {
        if ($("#phone").val().length == 0) {
            document.getElementById('username').className = '';
            $('#phone').addClass(["form-control", "bg-danger", "border border-3", "border-danger", "text-secondary", "bg-opacity-10"]);
            $('#notifyPhone').addClass("text-danger-emphasis");
            $('#notifyPhone').html("This field can not be empty");
        } else {
            document.getElementById('phone').className = '';
            document.getElementById('notifyPhone').className = '';
            $('#phone').addClass("form-control");
            $('#notifyPhone').addClass("form-text");
            $('#notifyPhone').empty();
        }
    })

    $('#fname').on('focusout keyup', function () {
        if ($("#fname").val().length == 0) {
            document.getElementById('fname').className = '';
            $('#fname').addClass(["form-control", "bg-danger", "border border-3", "border-danger", "text-secondary", "bg-opacity-10"]);
            $('#notifyFirst').addClass("text-danger-emphasis");
            $('#notifyFirst').html("This field can not be empty");
        } else {
            document.getElementById('fname').className = '';
            document.getElementById('notifyFirst').className = '';
            $('#fname').addClass("form-control");
            $('#notifyFirst').addClass("form-text");
            $('#notifyFirst').empty();
        }
    })

    $('#lname').on('focusout keyup', function () {
        if ($("#lname").val().length == 0) {
            document.getElementById('lname').className = '';
            $('#lname').addClass(["form-control", "bg-danger", "border border-3", "border-danger", "text-secondary", "bg-opacity-10"]);
            $('#notifyLastName').addClass("text-danger-emphasis");
            $('#notifyLastName').html("This field can not be empty");
        } else {
            document.getElementById('lname').className = '';
            document.getElementById('notifyLastName').className = '';
            $('#lname').addClass("form-control");
            $('#notifyLastName').addClass("form-text");
            $('#notifyLastName').empty();
        }
    })
})

$(document).ready(function () {
    $(".notification-drop .item").on('click', function () {
        $(this).find('ul').toggle();
    });
});

//------------------------------form waiting for driver confirm---------------------------------------------------------
$("#confirm").on("click", function () {

    $("#book").removeAttr(" data-bs-target").attr("data-bs-target", "#bill");

    $("#headerInvoice").html("Waiting for driver <i\n" +
        "                        class=\"fa-solid fa-circle-notch fa-spin\"></i>")

    $("#bid").text($("#billid").val())
    $("#driver").empty();
    $("#rider").text($("#user").text());
    let dis = $("#distance").val()
    console.log(dis.substring(0, dis.indexOf("km")) * 10000);
    $("#beginning").html($("#beginConfirm").val());
    $("#destination").html($("#destinationConfirm").val());
    $("#duration").html($("#time").val());
    $("#dis").html(dis);
    $("#cost").html(dis.substring(0, dis.indexOf("km")) * 10000 + " VND");
})

//-----------------------------------generate uuid bill-----------------------------------------------------------------
function generateUUID() { // Public Domain/MIT
    var d = new Date().getTime();//Timestamp
    var d2 = ((typeof performance !== 'undefined') && performance.now && (performance.now() * 1000)) || 0;//Time in microseconds since page-load or 0 if unsupported
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16;//random number between 0 and 16
        if (d > 0) {//Use timestamp until depleted
            r = (d + r) % 16 | 0;
            d = Math.floor(d / 16);
        } else {//Use microseconds since page-load if supported
            r = (d2 + r) % 16 | 0;
            d2 = Math.floor(d2 / 16);
        }
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}

//------------------------------Book trip-------------------------------------------------------------------------------
$(document).ready(function () {
    $("#book").on("click", function () {

        let id = generateUUID();
        // console.log(id);
        $("#rider-form").val($("#user").text());
        $("#time").val('');
        $("#distance").val('');
        $("#beginConfirm").val('');
        $("#destinationConfirm").val('');

        $("#billid").val(id);
        $("#mapbox-directions-origin-input .mapboxgl-ctrl-geocoder input").val($("#begin").val()).on("keyup focusout", function () {
            $("#beginConfirm").val($("#mapbox-directions-origin-input .mapboxgl-ctrl-geocoder input").val());
        });
        $("#mapbox-directions-destination-input .mapboxgl-ctrl-geocoder input").val($("#destination").val()).on("keyup focusout", function () {
            $("#destinationConfirm").val($("#mapbox-directions-destination-input .mapboxgl-ctrl-geocoder input").val())

            let time = $(".mapbox-directions-route-summary h1").text();
            let distance = $(".mapbox-directions-route-summary span").text();

            // console.log("mil -> km: " + (distance.substring(0, distance.indexOf("mi")) * 1.609) + "km");

            $("#time").val(time);
            $("#distance").val((Math.round((distance.substring(0, distance.indexOf("mi")) * 1.609) * 100) / 100) + "km");

        });
    })
});

//-----------------Change Back to book when someone accept--------------------------------------------------------------

let end = setInterval(function () {
    if ($("#driver").text() !== '') {
        console.log("change")
        let interval = setInterval($("#book").removeAttr("data-bs-target").attr("data-bs-target", "#myModal"), 3000);
        $("#cancel").hide();
        setTimeout(function () {
                clearInterval(interval)
                clearInterval(end)
            }, 2000
        )
    }
    else
    {
        $("#cancel").show();
    }
}, 3000)


//------------------Open notice of customer to see detail invoice-------------------------------------------------------
function showDetailInvoice() {
    //decrease number of new notice in rider page
    console.log($(".site-mobile-menu-body .badge").text());
    console.log($(".site-nav .badge").text());

    if (($(".site-mobile-menu-body .badge").text()) == null || ($(".site-mobile-menu-body .badge").text()) == 0) {
        $(".site-mobile-menu-body .badge").text("0");
    } else {
        $(".site-mobile-menu-body .badge").text(Math.floor($(".site-mobile-menu-body .badge").text()) - 1);

    }

    if (($(".site-nav .badge").text()) == null || ($(".site-nav .badge").text()) == 0) {
        $(".site-nav .badge").text("0");
    } else {
        $(".site-nav .badge").text(Math.floor($(".site-nav .badge").text()) - 1);

    }

    $("#rider-detail").text($("#user").text())
    $("#bill_id").text($("#invoice_id").text());
    $("#beginNotice").text($("#beginInvoice").text());
    $("#destinationNotice").text($("#desInvoice").text());
    $("#durationNotice").text($("#timeInvoice").text());
    $("#disNotice").text($("#distanceInvoice").text());
    $("#driverNotice").text($("#riderInvoice").text());
    $("#costNotice").text($("#distanceInvoice").text().substring(0, $("#distanceInvoice").text().indexOf("km")) * 10000 + " VND");
}

//------------Update account in homepage--------------------------------------------------------------------------------
$(document).ready(function () {
    //set info update to form update
    $(".site-nav .js-clone-nav #btn-update-account").on('click', function () {
        console.log("form update")
        $.ajax(
            {
                url: 'http://localhost:8080/ciklo/auth/updateForm',
                type: 'GET',
                data: {},
                error: function () {
                    console.log("error");
                },
                success: function (data) {
                    if (data != null) {
                        // console.log(data.authorities[0].authority);
                        checkIdToSet(data);
                    } else {
                        $("#header-update-form").append(
                            "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                            "  Not found update account. \n" +
                            "</div>"
                        )
                    }
                }
            }
        )
    })

    $(".site-mobile-menu-body .site-nav-wrap #btn-update-account").on('click', function () {
        console.log("form update")
        $.ajax(
            {
                url: 'http://localhost:8080/ciklo/auth/updateForm',
                type: 'GET',
                data: {},
                error: function () {
                    console.log("error");
                },
                success: function (data) {
                    if (data != null) {
                        checkIdToSet(data);
                    } else {
                        $("#update-notice").append(
                            "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                            "  Not found update account. \n" +
                            "</div>"
                        )
                    }
                }
            }
        )
    })

})