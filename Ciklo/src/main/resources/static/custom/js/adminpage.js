function initTableDriver() {

    return $('#dataTable_driver').DataTable(
        {
            retrieve: true,
            columnDefs: [
                {

                    className: "text-center",
                    'createdCell': function (td, cellData, rowData, row, col) {
                        // this will give each cell an ID
                        $(td).attr('id', 'statusAcc-' + rowData[0]);
                    },
                    targets: 5,
                },
                {
                    className: "text-center",
                    'createdCell': function (td, cellData, rowData, row, col) {
                        console.log(1, rowData)
                        // this will give each cell an ID
                        $(td).attr('id', 'statusDriver-' + rowData[0]);
                    },
                    targets: 4,
                },
                {

                    className: "text-center",
                    'createdCell': function (td, cellData, rowData, row, col) {
                        // this will give each cell an ID
                        if (rowData[5].substring(rowData[5].indexOf(">") + 1, rowData[5].indexOf("</span>")) === "true") {
                            $(td).attr('id', 'removeAcc-' + rowData[0]);
                            $(td).attr('onclick', "removeAcc(this.id)");
                        }
                        if (rowData[5].substring(rowData[5].indexOf(">") + 1, rowData[5].indexOf("</span>")) === "false") {
                            $(td).attr('id', 'recoverAcc-' + rowData[0]);
                            $(td).attr('onclick', "recoverAcc(this.id)");
                        }
                    },
                    targets: 6,
                }
            ]
        }
    ); //table driver
}

function initTableCyclo(data) {
    return $('#dataTable_cyclo').DataTable(
        {
            retrieve: true,
            columnDefs: [
                {
                    className: "text-center",
                    'createdCell': function (td, cellData, rowData, row, col) {
                        // this will give each cell an ID
                        $(td).attr('id', 'driver-cyclo-' + rowData[0]);
                    },
                    targets: 3,
                },
            ]
        }
    ); //table cyclo
}

//----------------------------------------------------------------------------------------------------
$(document).ready(function () {
    console.log("loading data")

    axios.get('/ciklo/admin/user',
        {
            params: {}
        })
        .then((response) => {
            // console.log(response.data);
            $("#user").text(response.data.user);
        }, (error) => {
            console.log("error");
        })
    statisticDataAdmin();

    //-------------Show data when click on nav----------------------------------------------------------
    //show dashboard when click on "Dashboard"
    $("#home").on("click", function () {
        $("#table_bills").attr("hidden", true);
        $("#table_driver").attr("hidden", true);
        $("#table_cyclo").attr("hidden", true);
        $("#dashboard").attr("hidden", false);

        if ($("#drivers").hasClass("active").toString() === 'true') {
            $("#drivers").removeClass("active");
        }
        if ($("#bills").hasClass("active").toString() === 'true') {
            $("#bills").removeClass("active");
        }
        if ($("#cyclos").hasClass("active").toString() === 'true') {
            $("#cyclos").removeClass("active");
        }
        $("#home").addClass("active");
        statisticDataAdmin();

    })

    //show bills data when click on "Bills"
    $("#bills").on("click", function () {

        $("#table_bills").attr("hidden", false);
        $("#table_driver").attr("hidden", true);
        $("#table_cyclo").attr("hidden", true);
        $("#dashboard").attr("hidden", true);

        if ($("#drivers").hasClass("active").toString() === 'true') {
            $("#drivers").removeClass("active");
        }
        if ($("#home").hasClass("active").toString() === 'true') {
            $("#home").removeClass("active");
        }
        if ($("#cyclos").hasClass("active").toString() === 'true') {
            $("#cyclos").removeClass("active");
        }
        $("#bills").addClass("active");
        billsData()
    })

    //show drivers data when click on "Drivers"
    $("#drivers").on("click", function () {
        $("#table_bills").attr("hidden", true);
        $("#table_driver").attr("hidden", false);
        $("#table_cyclo").attr("hidden", true);
        $("#dashboard").attr("hidden", true);

        if ($("#bills").hasClass("active").toString() === 'true') {
            $("#bills").removeClass("active");
        }
        if ($("#home").hasClass("active").toString() === 'true') {
            $("#home").removeClass("active");
        }
        if ($("#cyclos").hasClass("active").toString() === 'true') {
            $("#cyclos").removeClass("active");
        }
        $("#drivers").addClass("active");
        driverData()
    })

    //show cyclo data when click on "cyclos"
    $("#cyclos").on("click", function () {
        $("#table_bills").attr("hidden", true);
        $("#table_driver").attr("hidden", true);
        $("#table_cyclo").attr("hidden", false);
        $("#dashboard").attr("hidden", true);

        if ($("#drivers").hasClass("active").toString() === 'true') {
            $("#drivers").removeClass("active");
        }
        if ($("#home").hasClass("active").toString() === 'true') {
            $("#home").removeClass("active");
        }
        if ($("#bills").hasClass("active").toString() === 'true') {
            $("#bills").removeClass("active");
        }
        $("#cyclos").addClass("active");
        cycloData()
    })

    setInterval(uploadDataAdmin, 7000);

})

//---------Data from DB----------------------------------------------------------------------------------------------------
// Statistic data in admin page
function statisticDataAdmin() {

    axios.get('/ciklo/admin/statistic', {
        params: {}
    }).then((response) => {
        console.log(response.data);
        $("#trip-day").empty().text(response.data.today_trips);
        $("#earning-day").empty().text(response.data.total_today_trips + " VND");
        $("#trip-month").empty().text(response.data.month_trips);
        $("#earning-month").empty().text(response.data.total_month_trips + " VND");

    }, (error) => {
        console.log("error");
    })
}

//All Bill in admin page
function billsData() {
    var tb = $("#dataTable").DataTable();//table bill
    $.ajax({
        url: '/ciklo/admin/bill_data', type: 'get', data: {}, error: function () {
            console.log("error");
        }, success: function (data) {
            console.log(data);
            appendToTableBillsAdmin(data, tb);
        }
    })
}

//All data of driver in admin page
function driverData() {
    $.ajax({
        url: '/ciklo/admin/driver_data', type: 'get', data: {}, error: function () {
            console.log("error");
        }, success: function (data) {
            console.log(data);
            var tb_driver = initTableDriver(data);
            // tb_driver.destroy();
            appendToTableDriversAdmin(data, tb_driver);
        }
    })
}

//All data of Cyclo in admin page
function cycloData() {

    $.ajax({
        url: '/ciklo/cyclo/cyclo_data', type: 'get', data: {}, error: function () {
            console.log("error");
        }, success: function (data) {
            console.log(data);
            var tb_cyclo = initTableCyclo(data); // table cyclo
            appendToTableCycloAdmin(data, tb_cyclo);
        }
    })
}

//--------------Append data to table------------------------------------------------------------------------------------
//append data to table bills
function appendToTableBillsAdmin(data, tb) {
    tb.clear().draw();
    for (let i = 0; i < data.length; i++) {
        tb.row.add(
            [
                data[i].bid,
                (data[i].driver.dfirstname + " " + data[i].driver.dlastname),
                (data[i].cus.cfirstname + " " + data[i].cus.clastname),
                data[i].cyclo.regNo,
                data[i].start_loc,
                data[i].end_loc,
                data[i].distance,
                data[i].dateTrip,
                data[i].total + " VND"
            ]).draw(false);
    }
}

//append data to table driver'

function appendToTableDriversAdmin(data, tb) {
    tb.clear().draw();
    for (let i = 0; i < data.length; i++) {
        tb.row.add(
            [
                data[i].driverId,
                (data[i].dfirstname + " " + data[i].dlastname),
                data[i].demail,
                data[i].dphone,
                data[i].dstatus === true ? "<span class='text-success'>Free</span>" : "<span class='text-danger'>Busy</span>",
                data[i].active === true ? "<span hidden>true</span><i class=\"fa-regular fa-circle-check text-success\"></i>" : "<span hidden>false</span><i class=\"fa-regular fa-circle-xmark text-danger\"></i>",
                data[i].active === true ? "<a class='text-decoration-none' id='removeId" + data[i].driverId + "'><i class=\"fa-solid fa-user-slash text-danger\"></i></a>" : "<a class='text-decoration-none' id='recoverId" + data[i].driverId + "'> <i class=\"fa-solid fa-user-check text-success\"></i></a>",
            ]).draw(false);
    }
}

//append data to table cyclo
function appendToTableCycloAdmin(data, tb) {
    tb.clear().draw();
    for (let i = 0; i < data.length; i++) {
        console.log(data[i].maintain !== true);
        tb.row.add(
            [
                data[i].id,
                data[i].regNo,
                data[i].driver.dfirstname + " " + data[i].driver.dlastname,
                data[i].maintain === true ?
                    "<a data-bs-toggle= 'tooltip" + "' data-bs-title=\"During Maintenance\" class='text-decoration-none'>" +
                    "       <i class=\"fa-duotone fa-screwdriver-wrench fa-xl\" style=\"--fa-primary-color: #ea5763; --fa-secondary-color: #fa464c;\"></i>" +
                    "</a>"
                    : "<a data-bs-toggle= 'tooltip" + "' data-bs-title=\"During No Maintenance\" class='text-decoration-none'>" +
                    "       <i class=\"fa-duotone fa-octagon-check fa-xl\" style=\"--fa-primary-color: #348312; --fa-secondary-color: limegreen;\"></i>" +
                    "</a>"
            ]).draw(false);
        $("[ data-bs-toggle= 'tooltip']").tooltip();
    }
}

//--------------Ajax for changing status account of driver--------------------------------------------------------------
function removeAcc(id) {
    let no = id.substring(id.indexOf("-") + 1);
    $.ajax(
        {
            url: 'http://localhost:8080/ciklo/admin/delete_driver',
            type: 'Post',
            data:
                {
                    "id": no,
                },
            error: function () {
                console.log("error");
            },
            success: function (data) {
                console.log(no);

                $("#statusAcc-" + no).empty().append("<i class=\"fas fa-spinner fa-pulse\"></i>");
                $('#removeAcc-' + no).empty().append("<i class=\"fas fa-spinner fa-pulse\"></i>");

                setTimeout(function () {
                    $("#statusAcc-" + no)
                        .empty()
                        .append("<i class=\"fa-regular fa-circle-xmark text-danger\"></i>");

                    $('#removeAcc-' + no)
                        .empty()
                        .removeAttr("onclick id")
                        .attr(
                            {
                                onclick: "recoverAcc(this.id)",
                                id: "recoverAcc-" + no
                            })
                        .append(
                            "<a class='text-decoration-none' id='recoverId" + no + "' > <i class=\"fa-solid fa-user-check text-success\"></i></a>"
                        );
                }, 3000)
            }
        }
    )
}

function recoverAcc(id) {
    let no = id.substring(id.indexOf("-") + 1);

    $.ajax(
        {
            url: 'http://localhost:8080/ciklo/admin/recover_driver',
            type: 'Post',
            data:
                {
                    "id": no,
                },
            error: function () {
                console.log("error");
            },
            success: function (data) {
                console.log(data);

                $("#statusAcc-" + no).empty().append("<i class=\"fas fa-spinner fa-pulse\"></i>");
                $('#recoverAcc-' + no).empty().append("<i class=\"fas fa-spinner fa-pulse\"></i>");

                setTimeout(function () {
                    $("#statusAcc-" + no)
                        .empty()
                        .append("<i class=\"fa-regular fa-circle-check text-success\"></i>");
                    $('#recoverAcc-' + no)
                        .empty()
                        .removeAttr("onclick id")
                        .attr(
                            {
                                onclick: " removeAcc(this.id)",
                                id: "removeAcc-" + no
                            }
                        )
                        .append(
                            "<a class='text-decoration-none' id='removeId" + no + "'><i class=\"fa-solid fa-user-slash text-danger\"></i></a>"
                        );
                }, 3000)
            }
        }
    )
}

//--------------Add Driver to DB----------------------------------------------------------------------------------------

$("#dmail").on('focusout', function () {
    $.ajax(
        {
            url: 'http://localhost:8080/ciklo/admin/check_driver',
            type: 'get',
            data: {
                'email': $("#dmail").val()
            },
            error: function () {
            },
            success: function (data) {
                console.log(data);
                if (data[0] != null) {
                    console.log('have');
                    $("#dmail").removeAttr("class").attr("class", "form-control text-light bg-warning p-2 bg-opacity-25")
                    $('#notifyEmail')
                        .removeAttr("class").attr("class", "form-text text-danger-emphasis")
                        .html('<i class="fa-duotone fa-triangle-exclamation fa-lg"\n' +
                            'style="--fa-secondary-color: #FFE033; --fa-primary-color: #1C1C1C"></i> Email has already existed');
                } else {
                    console.log('not have');
                    $("#dmail").removeAttr("class").attr("class", "form-control text-light bg-success p-2 bg-opacity-25")
                    $('#notifyEmail').removeAttr("class").attr("class", "form-text text-success-emphasis");
                    $('#notifyEmail').html('<i class="fa-duotone fa-circle-check fa-lg"\n' +
                        '                   style="--fa-secondary-color: #52CC02; --fa-primary-color: #1C1C1C"></i> Valid Email');
                }
            }
        }
    )
})

$("#btn-add-confirm").on('click', function () {
    if ($("#dpass").val() === $("#dpass-check").val()) {
        $.ajax(
            {
                url: 'http://localhost:8080/ciklo/admin/add_driver',
                type: 'post',
                data:
                    {
                        "email": $("#dmail").val(),
                        "fname": $("#fname").val(),
                        "lname": $("#lname").val(),
                        "pass": $("#dpass").val(),
                        "phone": $("#dphone").val()
                    },
                error: function () {
                    console.log("error");
                },
                success: function (data) {
                    // console.log(data);
                    appendNotice(data, "add-driver-notice", "Add");

                    $("form").trigger("reset");
                    $("#dmail").removeAttr("class").attr("class", "form-control");
                    $('#notifyEmail')
                        .removeAttr("class").attr("class", "form-text")
                        .empty();


                }
            }
        )
    }
})
//----------------------Add new Cyclo to DB-----------------------------------------------------------------------------
$("#btn-add-cyclo").on("click", function () {
    console.log("add new cyclo");
    appendListDriver()
    let reg = generateReg()
    $("#reg_no").val(reg);


    $("#btn-confirm-cyclo").unbind().on("click", function () {
        $.ajax(
            {
                url: 'http://localhost:8080/ciklo/cyclo/add_cyclo',
                type: 'post',
                data: {
                    "reg_no": reg,
                    "demail": $('#select-driver').find(":selected").val()
                },
                error: function () {
                    console.log("error");
                },
                success: function (data) {
                    // console.log(data);
                    // console.log($("#reg_no").val())
                    appendNotice(data, "add-cyclo-notice", "Add");
                }
            }
        )
    })

})

//Append list driver that doesn't have cyclo to ride
function appendListDriver() {
    $.ajax({
        url: "http://localhost:8080/ciklo/cyclo/driver_for_cyclo",
        type: 'get',
        data: {},
        error: function () {
            console.log("error");
        },
        success: function (data) {
            console.log(data);

            $("#select-driver").empty();
            for (let i = 0; i < data.length; i++) {
                $("#select-driver").append($('<option/>',
                    {
                        value: data[i].demail,
                        text: data[i].dfirstname + " " + data[i].dlastname
                    }));
            }
        }
    })
}

//generate auto reg_no for cyclo'
function generateReg() {
    let upperChar = 'ABCDEFGHJKLMNOPQSTUVWXYZ', //Leter A-Z
        lowerChar = 'abcdefghijklmnopqrstuvwxyz', // Letter a-z
        number = '0123456789';//number 0 - 9

    let str = upperChar + lowerChar + number;
    let reg = "";
    for (let i = 0; i < 6; i++) {
        reg += str.charAt(Math.floor(Math.random() * str.length));
    }

    return reg;
}

//--------------Upload data for check if in DB have new change----------------------------------------------------------
function uploadDataAdmin() {
    const statisticAdInterval = setInterval(statisticDataAdmin, 3000);
    const billAdInterval = setInterval(billsData, 3000);
    const driverAdInterval = setInterval(driverData, 3000);
    const cycloAdInterval = setInterval(cycloData, 3000);
    const areaChartInterval = setInterval(chartAreaAd, 3000);
    const barChartAdInterval = setInterval(chartBarAd, 3000);

    setTimeout(function () {
        clearTimeout(statisticAdInterval);
    }, 5000);

    setTimeout(function () {
        clearTimeout(billAdInterval);
    }, 5000);


    setTimeout(function () {
        clearTimeout(driverAdInterval);
    }, 5000);

    setTimeout(function () {
        clearTimeout(cycloAdInterval);
    }, 5000);

    setTimeout(function () {
        clearTimeout(areaChartInterval);
    }, 5000);

    setTimeout(function () {
        clearTimeout(barChartAdInterval);
    }, 5000);
}

//-----------------Update Acc Admin-------------------------------------------------------

$(document).ready(function () {
    $("#btn-update-account").on('click', function () {
        $.ajax({
            url: 'http://localhost:8080/ciklo/auth/updateForm',
            type: 'GET',
            data: {},
            error: function () {
                console.log("error");
            }, success: function (data) {
                if (data != null) {
                    console.log(data);
                    checkIdToSet(data);
                } else {
                    $("#update-notice").append("<div class=\"alert alert-warning\" role=\"alert\">\n" + "  Not found update account. \n" + "</div>")
                }
            }
        })
    })


})


