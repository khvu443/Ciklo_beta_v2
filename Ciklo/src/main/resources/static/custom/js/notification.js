//------------Append notice for adding and update-----------------------------------------------------------------------
function appendNotice(data, name, type) {
    if (data === "true") {
        $("#" + name).empty().append(
            "                        <div class=\"alert alert-success\" role=\"alert\">\n" +
            "                            <h5 class=\"alert-heading mt-2\">" + type + " Success!</h5>\n" +
            "                            <div class=\"progress\" role=\"progressbar\" aria-label=\"Example 1px high\" aria-valuenow=\"25\"\n" +
            "                                 aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"height: 1px\" id=\"bar-pg\">\n" +
            "                                <div class=\"progress-bar bg-success\" style=\"width: 100%\" id=\"value-bar-pg\"></div>\n" +
            "                            </div>\n" +
            "                        </div>"
        );
        countdownProgressbar(name);
    } else {
        console.log("error");
        $("#" + name).empty().append(
            "                        <div class=\"alert alert-danger\" role=\"alert\">\n" +
            "                            <h5 class=\"alert-heading mt-2\">" + type + " Fail!</h5>\n" +
            "                            <div class=\"progress\" role=\"progressbar\" aria-label=\"Example 1px high\" aria-valuenow=\"25\"\n" +
            "                                 aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"height: 1px\" id=\"bar-pg\">\n" +
            "                                <div class=\"progress-bar bg-danger\" style=\"width: 100%\" id=\"value-bar-pg\"></div>\n" +
            "                            </div>\n" +
            "                        </div>"
        );
        countdownProgressbar(name);
    }
}

//--------------Countdown time for alert----------------------------------------------------------
function countdownProgressbar(name) {

    let time = 10;
    var count = setInterval(() => {
        time -= 0.01;
        let progress = (time / 10) * 100;
        if (time > 0) {

            $("#value-bar-pg").css("width", progress + "%");
            $('#bar-pg').attr("aria-valuenow", progress);
        } else {

            clearInterval(count);
            $("#value-bar-pg").css("width", "0%");
            $('#bar-pg').attr("aria-valuenow", "0");
            $("#" + name).empty();

        }
    }, 10)
}