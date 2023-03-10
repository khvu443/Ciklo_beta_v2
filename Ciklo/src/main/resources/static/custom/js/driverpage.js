var tb = $("#dataTable").DataTable();

$(document).ready(function () {
    console.log("loading data")
    statisticData()
    axios.get('/ciklo/driver/statistic', {
        params: {}
    }).then((response) => {
        $("#user").text(response.data.name);
    })

    axios.get('/ciklo/cyclo/cyclo_driver', {
        params: {}
    }).then((response) => {
        console.log(response.data);
        $('#reg-no').val(response.data.regNo)
        $('#status-cyclo').empty().append(
            response.data.maintain === true ?
                "                        <a data-bs-toggle='tooltip' data-bs-title=\"During Maintenance\"\n" +
                "                           class='text-decoration-none status' onclick='changeStatusCyclo()'>\n" +
                "                            <i class=\"fa-duotone fa-screwdriver-wrench\"\n" +
                "                               style=\"--fa-primary-color: #ea5763; --fa-secondary-color: #fa464c;\"></i>\n" +
                "                        </a>"
                :
                "                        <a data-bs-toggle='tooltip' data-bs-title=\"During No Maintenance\"\n" +
                "                           class='text-decoration-none status' onclick='changeStatusCyclo()'>\n" +
                "                            <i class=\"fa-duotone fa-octagon-check\"\n" +
                "                               style=\"--fa-primary-color: #348312; --fa-secondary-color: limegreen;\"></i>\n" +
                "                        </a>"
        );
    })

    $("#home").on("click", function () {
        $("#table_bills").attr("hidden", true);
        $("#dashboard").attr("hidden", false);
        $("#trips").removeClass("active")
        $("#home").addClass("active");

    })


    $("#trips").on("click", function () {
        $("#dashboard").attr("hidden", true);
        $("#table_bills").attr("hidden", false);
        $("#home").removeClass("active")
        $("#trips").addClass("active");
        billsData();

    })

    setInterval(uploadData, 5000);
})

function statisticData() {
    axios.get('/ciklo/driver/statistic', {
        params: {}
    }).then((response) => {
        // console.log(response.data);
        $("#trip-day").empty().text(response.data.today_trips);
        $("#earning-day").empty().text(response.data.total_today_trips + " VND");
        $("#trip-month").empty().text(response.data.month_trips);
        $("#earning-month").empty().text(response.data.total_month_trips + " VND");

    }, (error) => {
        console.log("error");
    })
}

function billsData() {
    $.ajax({
        url: '/ciklo/driver/list_bills', type: 'get', data: {}, error: function () {
            console.log("error");
        }, success: function (data) {
            appendToTable(data, tb);
        }
    })
}

//----------------Update account---------------------------------------------------------
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
                    checkIdToSet(data);
                } else {
                    $("#header-update-form").append("<div class=\"alert alert-warning\" role=\"alert\">\n" + "  Not found update account. \n" + "</div>")
                }
            }
        })
    })
})

//-----------------Start Upload data again and then stop----------------------------------------------------------------
function uploadData() {
    const statisticInterval = setInterval(statisticData, 3000);
    const billInterval = setInterval(billsData, 3000);
    const areaInterval = setInterval(areaDemo, 3000);
    const barInterval = setInterval(barDemo, 3000);

    //Set time out upload data to page
    setTimeout(function () {
        clearInterval(statisticInterval);
    }, 5000)

    setTimeout(function () {
        clearInterval(billInterval);
    }, 5000)

    setTimeout(function () {
        clearInterval(areaInterval);
    }, 5000)

    setTimeout(function () {
        clearInterval(barInterval);
    }, 5000)
}

//------------------------Change status cyclo---------------------------------------------------------------------------

function changeStatusCyclo() {
    const reg = $("#reg-no").val();

    $.ajax(
        {
            url: '',
            type: 'post',
            data: {
                "reg": reg,
            },
            error: function () {
                console.log("error");
            },
            success: function (data) {
                console.log(data);
            }
        }
    )
}

//---------------------Change Status of Cyclo---------------------------------------------------------------------------
function changeStatusCyclo() {
    const reg = $("#reg-no").val();
    $.ajax(
        {
            url: 'http://localhost:8080/ciklo/cyclo/status_cyclo',
            type: 'POST',
            data:
                {
                    "reg": reg,
                },
            error: function () {
                console.log("error");
            },
            success: function (data) {
                // console.log(data);
                if (data.isSuccess === "true") {
                    if (data.maintain !== "true") {
                        $('#status-cyclo').empty().append(
                            "                        <a data-bs-toggle='tooltip' data-bs-title=\"During Maintenance\"\n" +
                            "                           class='text-decoration-none status' onclick='changeStatusCyclo()'>\n" +
                            "                            <i class=\"fa-duotone fa-screwdriver-wrench\"\n" +
                            "                               style=\"--fa-primary-color: #ea5763; --fa-secondary-color: #fa464c;\"></i>\n" +
                            "                        </a>"
                        );
                    } else {
                        $('#status-cyclo').empty().append(
                            "                        <a data-bs-toggle='tooltip' data-bs-title=\"During No Maintenance\"\n" +
                            "                           class='text-decoration-none status' onclick='changeStatusCyclo()'>\n" +
                            "                            <i class=\"fa-duotone fa-octagon-check\"\n" +
                            "                               style=\"--fa-primary-color: #348312; --fa-secondary-color: limegreen;\"></i>\n" +
                            "                        </a>"
                        );
                    }
                }


            }
        }
    )
}


