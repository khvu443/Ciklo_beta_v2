// view detail bill
function viewAllBills() {
    var tb = $('#example').DataTable()
    $.ajax(
        {
            url: "/ciklo/bill/viewBill",
            type: 'GET',
            data:
                {
                },
            error: function () {
                console.log("error")
            },
            success: function (data) {
                console.log(data);
                if (data != null) {
                    appendToTable(data, tb);
                }
            }
        }
    )
}

//append data to table for driver and customer
function appendToTable(data, tb) {
    tb.clear().draw()
    for (let i = 0; i < data.length; i++) {
        tb.row.add(
            [
                data[i].bid,
                (data[i].driver.dfirstname + " " + data[i].driver.dlastname),
                data[i].cyclo.reg_No,
                data[i].start_loc,
                data[i].end_loc,
                data[i].distance,
                data[i].dateTrip,
                data[i].total
            ]).draw(false);
    }
}



