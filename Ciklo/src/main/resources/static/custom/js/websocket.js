// var stompClient = null;
var privateStompClient = null;

var socket = new SockJS('/ws');
// stompClient = Stomp.over(socket);
// stompClient.connect({}, function (frame) {
//     console.log(frame);
//     stompClient.subscribe('/all/messages', function (result) {
//         showMsg(JSON.parse(result.body));
//     });
// });

// socket = new SockJS('/ws');
// stompClient = Stomp.over(socket);
// stompClient.connect({}, function (frame) {
//     console.log(frame);
//     stompClient.subscribe('/all/noticeRider', function (result) {
//         showMsg(JSON.parse(result.body));
//     });
// });

var socketRider = new SockJS('/ws');
privateStompClient = Stomp.over(socketRider);
privateStompClient.connect({}, function (frame) {
    console.log(frame);
    setTimeout(function () {
            privateStompClient.subscribe('/user/driver', function (result) {
                console.log(result.body)
                showMsg(JSON.parse(result.body));
            });
        }, 500
    );
});

var socketCancel = new SockJS('/ws');
var cancelStompClient = Stomp.over(socketCancel);
cancelStompClient.connect({}, function (frame) {
    console.log(frame);
    setTimeout(function () {
            privateStompClient.subscribe('/user/cancel', function (result) {
                console.log(result.body)
                showMsgCancel(JSON.parse(result.body));
            });
        }, 500
    );
});

var socketDriver = new SockJS('/ws');
privateStompClient = Stomp.over(socketDriver);
privateStompClient.connect({}, function (frame) {
    console.log(frame);
    setTimeout(function () {
        privateStompClient.subscribe('/user/rider', function (result) {
            console.log(result.body)
            showMsgRider(JSON.parse(result.body));
        });
    }, 500)
});

//------------------------------------User send to all driver-----------------------------------------------------------
function sendPrivateMessage() {
    const id = document.getElementById("billid").value
    const begin = document.getElementById('beginConfirm').value;
    const des = document.getElementById('destinationConfirm').value;
    const time = document.getElementById('time').value;
    const distance = document.getElementById('distance').value;
    // const rider = document.getElementById('rider').value;
    const rider = $("#user").text();
    console.log(id, begin, des, time, distance, rider);

    privateStompClient.send("/app/private_driver", {},
        JSON.stringify({
            'id': id,
            'rider': rider,
            'begin': begin,
            'des': des,
            'time': time,
            'distance': distance,
            'cancel': false
        }));
}

//--------------------------Send Cancel Book----------------------------------------------------------------------------

function sendCancelBillToDriver() {

    const begin = $("#beginning").text();
    const des = $("#destination").text();
    const time = $("#duration").text();
    const distance = $("#dis").text();
    const rider = $("#rider").text();
    const id = $("#bid").text();

    cancelStompClient.send("/app/private_cancel", {},
        JSON.stringify({
            'id': id,
            'rider': rider,
            'begin': begin,
            'des': des,
            'time': time,
            'distance': distance,
            'cancel': true
        }));
}

//-------------------------------Driver send to rider-------------------------------------------------------------------
function sendConfirmFromDriver() {


    //decrease number of new notice in driver page
    console.log($(".site-mobile-menu-body .site-nav-wrap .badge").text());
    console.log($(".site-nav .js-clone-nav .badge").text());

    if (($(".site-mobile-menu-body .site-nav-wrap .badge").text()) == null || ($(".site-mobile-menu-body .site-nav-wrap .badge").text()) == 0) {
        $(".site-mobile-menu-body .site-nav-wrap .badge").text("0");
    } else {
        $(".site-mobile-menu-body .site-nav-wrap .badge").text(Math.floor($(".site-mobile-menu-body .site-nav-wrap .badge").text()) - 1);

    }

    if (($(".site-nav .js-clone-nav .badge").text()) == null || ($(".site-nav .js-clone-nav .badge").text()) == 0) {
        $(".site-nav .js-clone-nav .badge").text("0");
    } else {
        $(".site-nav .js-clone-nav .badge").text(Math.floor($(".site-nav .js-clone-nav .badge").text()) - 1);

    }

    if ($("#counter_badge").text() == null || $("#counter_badge").text() == 0) {
        $("#counter_badge").text("0")
    } else {
        $("#counter_badge").text(Math.floor($("#counter_badge").text()) - 1)
    }

    const id = $("#bill_id").text();
    const driver = $("#user").text();
    const begin = document.getElementById('beginCheck').innerText;
    const des = document.getElementById('desCheck').innerText;
    const time = document.getElementById('timeCheck').innerText;
    const distance = document.getElementById('distanceCheck').innerText;
    const rider = document.getElementById('riderCheck').innerText;

    privateStompClient.send("/app/private_rider", {},
        JSON.stringify({
            'id': id,
            'rider': rider,
            'begin': begin,
            'des': des,
            'time': time,
            'distance': distance,
            'driver': driver,
            'cancel': false
        }));
}



//----------------------------------Save bill in db--------------------------------------------------------------------
function saveBill(id) {

    const bid = $("#bill_id").text();
    const driver = $("#user").text();
    const begin = document.getElementById('beginCheck').innerText;
    const des = document.getElementById('desCheck').innerText;
    const time = (new Date()).toLocaleString();
    const distance = document.getElementById('distanceCheck').innerText;
    const rider = document.getElementById('riderCheck').innerText;

    const trigger = document.getElementById(id);
    if (trigger) {
        $.ajax(
            {
                url: '/ciklo/bill/saveBill',
                type: 'post',
                data:
                    {
                        'id': bid,
                        'driver': driver,
                        'begin': begin,
                        'des': des,
                        'time': time,
                        'distance': distance,
                        'rider': rider,
                    },
                error: function () {
                    console.log("error");
                },
                success: function (data) {

                    console.log($("#isCancel").text() === 'true');

                    console.log(data);
                    if (data === 'true') {
                        console.log("Save bill")
                        sendConfirmFromDriver();

                        changeStatus(false);
                        changeStatusAgain();

                        uploadData();

                    } else {
                        const now = (new Date()).toLocaleString();
                        $(".notification-drop .item ul  #notices").prepend("<a onclick=\"removeActive(this.id);\" class=\"msg list-group-item list-group-item-action border border-primary-subtle border-opacity-25 border border-2 bg-info bg-opacity-10 activeMsg\" id=" + "'d" + Math.floor(Math.random() * 100) + "'" + ">\n" +
                            "                                        <div class=\"d-flex w-100 justify-content-between\">\n" +
                            "                                            <p class=\"mb-1\">Notice</p>\n" +
                            "                                            <small>" + now + "</small>\n" +
                            "                                        </div>\n" +
                            "                                        <small class=\"mb-1\">The trip has already taken, or you have already taken the trip </small>\n" +
                            "                                    </a>");
                        $(".site-mobile-menu-body .site-nav-wrap .badge").text($(".site-mobile-menu-body .site-nav-wrap .activeMsg").length);
                        $(".site-nav .js-clone-nav .badge").text($(".site-nav .js-clone-nav .activeMsg").length);

                        //-----------------------------------------------------------------------------------------------------------------
                        $("div.dropdown-menu div#notices").prepend("<a onclick=\"removeMsgActive(this.id);\" class=\"bg-success text-white bg-opacity-25 dropdown-item d-flex align-items-center msg activeMsg\" href=\"#\" id=" + "'d" + Math.floor(Math.random() * 100) + "'" + ">\n" +
                            "                                    <div class=\"mr-3\">\n" +
                            "                                        <div class=\"icon-circle bg-success\">\n" +
                            "                                            <i class=\"fa-regular fa-brake-warning\"></i>\n" +
                            "                                        </div>\n" +
                            "                                    </div>\n" +
                            "                                    <div>\n" +
                            "                                        <div class=\"small\">" + now + "</div>\n" +
                            "                                        <span class=\"fw-bold\" id=\"riderCheck\">The trip has already taken</span><br> \n" +
                            "                                    </div>\n" +
                            "                                </a>")
                        $("#counter_badge").text($("div.dropdown-menu div#notices .activeMsg").length);

                    }
                }
            }
        )
    }
}

//----------------------------------Change status driver after accept trip----------------------------------------------
function changeStatus(status) {
    $.ajax(
        {
            url: 'http://localhost:8080/ciklo/driver/change-status',
            type: 'POST',
            data: {
                "status": status
            },
            error: function () {
                console.log("error");
            },
            success: function (data) {
                console.log("success")
            }
        }
    )
}

function changeStatusAgain() {
    const time = document.getElementById('timeCheck').innerText;
    setInterval(function () {
        changeStatus(true)
    }, Math.floor(time.substring(0, time.indexOf("min")) * 60000));
}

//Remove active notice
function removeActive(id) {
    console.log(id);
    if (id.includes("d")) {
        $(".site-mobile-menu-body .site-nav-wrap #" + id).removeClass("border-primary-subtle activeMsg bg-info")
            .addClass("border-dark-subtle bg-secondary")
            .prop("onclick", null).off("click");

        $(".site-nav .js-clone-nav #" + id).removeClass("border-primary-subtle activeMsg bg-info")
            .addClass("border-dark-subtle bg-secondary")
            .prop("onclick", null).off("click");

    } else if (id.includes("c")) {
        $(".site-mobile-menu-body .site-nav-wrap #" + id).removeClass("border-primary-subtle activeMsg bg-info")
            .addClass("border-dark-subtle bg-secondary")
            .unbind("click", function () {
                removeActive(id);
            });

        $(".site-nav .js-clone-nav #" + id).removeClass("border-primary-subtle activeMsg bg-info")
            .addClass("border-dark-subtle bg-secondary")
            .unbind("click", function () {
                removeActive(id);
            });
    }

}

function removeMsgActive(id) {
    $(".topbar .dropdown-list #" + id).removeClass("bg-success text-white bg-opacity-25 activeMsg")
        .addClass("text-dark")
        .prop("onclick", null).off("click");
}

//---------------------------------- Msg for driver--------------------------------------------------------------------

function showMsgCancel(message)
{
    const now = (new Date()).toLocaleString();

    $(".notification-drop .item ul  #notices").prepend("<a onclick=\" removeActive(this.id); \" class=\"msg list-group-item list-group-item-action border border-primary-subtle border-opacity-25 border border-2 bg-info bg-opacity-10 activeMsg\" id=" + "'d" + Math.floor(Math.random() * 100) + "'" + ">\n" +
        "                                        <div class=\"d-flex w-100 justify-content-between\">\n" +
        "                                            <p class=\"mb-1\">Cancel Booking</p>\n" +
        "                                            <small>" + now + "</small>\n" +
        "                                        </div>\n" +
        "                                        <small class=\"mb-1\">Booking has been cancel from "+ message.begin + " to " + message.des +"</small>" +
        "                                    </a>");
    $(".site-mobile-menu-body .site-nav-wrap .badge").text($(".site-mobile-menu-body .site-nav-wrap .activeMsg").length);
    $(".site-nav .js-clone-nav .badge").text($(".site-nav .js-clone-nav .activeMsg").length);

    //-----------------------------------------------------------------------------------------------------------------
    $("div.dropdown-menu div#notices").prepend("<a onclick=\" removeMsgActive(this.id);\" class=\"bg-success text-white bg-opacity-25 dropdown-item d-flex align-items-center msg activeMsg\" href=\"#\" id=" + "'d" + Math.floor(Math.random() * 100) + "'" + ">\n" +
        "                                    <div class=\"mr-3\">\n" +
        "                                        <div class=\"icon-circle bg-success\">\n" +
        "                                           <i class=\"fa-sharp fa-regular fa-location-pin-slash\"></i>\n" +
        "                                        </div>\n" +
        "                                    </div>\n" +
        "                                    <div>\n" +
        "                                        <div class=\"small\">" + now + "</div>\n" +
        "                                        <span class=\"fw-bold\" id=\"riderCheck\">" + message.rider + "</span> has cancel the trip from <span class='fw-bold' id=\"beginCheck\">" + message.begin + "</span>" + " to <span class='fw-bold' id=\"desCheck\">" + message.des + "</span> \n" +
        "                                    </div>\n" +
        "                                </a>")
    $("#counter_badge").text($("div.dropdown-menu div#notices .activeMsg").length);
}

function showMsg(message) {
    console.log("show msg")
    const now = (new Date()).toLocaleString();

    $(".notification-drop .item ul  #notices").prepend("<a onclick=\" removeActive(this.id); saveBill(this.id); \" class=\"msg list-group-item list-group-item-action border border-primary-subtle border-opacity-25 border border-2 bg-info bg-opacity-10 activeMsg\" id=" + "'d" + Math.floor(Math.random() * 100) + "'" + ">\n" +
        "                                        <div class=\"d-flex w-100 justify-content-between\">\n" +
        "                                            <p class=\"mb-1\">Booking</p>\n" +
        "                                            <span hidden id='bill_id'>" + message.id + "</span>" +
        "                                            <span hidden id='isCancel'>" + message.isCancel + "</span>" +
        "                                            <small>" + now + "</small>\n" +
        "                                        </div>\n" +
        "                                        <small class=\"mb-1\"><span class=\"fw-bold\" id=\"riderCheck\">" + message.rider + "</span> has booking the trip in <span class='fw-bold' id=\"beginCheck\">" + message.begin + "</span>" + " to <span class='fw-bold' id=\"desCheck\">" + message.des + "</span> </small><br>\n" +
        "                                        <small class=\"mb-1\">Duration:  <span class=\"fw-bold\" id=\"timeCheck\">" + message.time + "</span> and Distance: <span class='fw-bold' id=\"distanceCheck\">" + message.distance + "</span></small>\n" +
        "                                    </a>");
    $(".site-mobile-menu-body .site-nav-wrap .badge").text($(".site-mobile-menu-body .site-nav-wrap .activeMsg").length);
    $(".site-nav .js-clone-nav .badge").text($(".site-nav .js-clone-nav .activeMsg").length);

    //-----------------------------------------------------------------------------------------------------------------
    $("div.dropdown-menu div#notices").prepend("<a onclick=\" removeMsgActive(this.id); saveBill(this.id);\" class=\"bg-success text-white bg-opacity-25 dropdown-item d-flex align-items-center msg activeMsg\" href=\"#\" id=" + "'d" + Math.floor(Math.random() * 100) + "'" + ">\n" +
        "                                    <div class=\"mr-3\">\n" +
        "                                        <div class=\"icon-circle bg-success\">\n" +
        "                                            <i class=\"fa-solid fa-map-location-dot\"></i>\n" +
        "                                            <span hidden id='bill_id'>" + message.id + "</span>" +
        "                                            <span hidden id='isCancel'>" + message.isCancel + "</span>" +
        "                                        </div>\n" +
        "                                    </div>\n" +
        "                                    <div>\n" +
        "                                        <div class=\"small\">" + now + "</div>\n" +
        "                                        <span class=\"fw-bold\" id=\"riderCheck\">" + message.rider + "</span> has booking the trip in <span class='fw-bold' id=\"beginCheck\">" + message.begin + "</span>" + " to <span class='fw-bold' id=\"desCheck\">" + message.des + "</span><br> \n" +
        "                                        Duration:  <span class=\"fw-bold\" id=\"timeCheck\">" + message.time + "</span> and Distance: <span class='fw-bold' id=\"distanceCheck\">" + message.distance + "</span>" +
        "                                    </div>\n" +
        "                                </a>")
    $("#counter_badge").text($("div.dropdown-menu div#notices .activeMsg").length);
}

//----------------------------------Msg for rider--------------------------------------------------------------------
function showMsgRider(message) {

    const now = (new Date()).toLocaleString();
    $(".notification-drop .item ul  #notices").prepend("  <a data-bs-target=\"#billNotice\" data-bs-toggle=\"modal\" class=\"msg list-group-item list-group-item-action border border-primary-subtle border-opacity-25 border border-2 bg-info bg-opacity-10 activeMsg\" id=" + "'c" + Math.floor(Math.random() * 100) + "'" + " onclick='showDetailInvoice(); removeActive(this.id)'>\n" +
        "    <div class=\"d-flex w-100 justify-content-between\">\n" +
        "      <p class=\"mb-1\">Success Book </p>\n" +
        "    <small id='invoice_id'> Booking Id: " + message.id + "</small>" +
        "    <small>" + now + "</small>\n" +
        "    </div>\n" +
        "    <small class=\"mb-1\"><span class=\"fw-bold\" id=\"riderInvoice\">" + message.driver + "</span> has accepted your book</small><br> \n " +
        "   From <small id='beginInvoice' class=\"fw-bold\"> " + message.begin + "</small> to <p id='desInvoice' class=\"fw-bold\">" + message.des + "</p>" +
        "<p id='timeInvoice' hidden>" + message.time + "</p>" +
        "<p id='distanceInvoice' hidden>" + message.distance + "</p>" +
        "<p id='accepted' hidden>" + message.accept + "</p>" +
        "  </a>");

    $(".site-mobile-menu-body .site-nav-wrap .badge").text($(".site-mobile-menu-body .site-nav-wrap .activeMsg").length);
    $(".site-nav .js-clone-nav .badge").text($(".site-nav .js-clone-nav .activeMsg").length);

    $("#driver").append("<i class=\"fa-solid fa-sync fa-spin\"></i>");
    $("#headerInvoice").empty();
    $("#headerInvoice").append("Driver has accepted your book");

    setTimeout(function () {
        $("#driver").empty();
        $("#driver").text(message.driver)
    }, 5000);

}