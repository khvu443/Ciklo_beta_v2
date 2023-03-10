function isReload() {
    const pageAccessedByReload = ((window.performance.navigation && window.performance.navigation.type === 1) || window.performance
        .getEntriesByType('navigation')
        .map((nav) => nav.type)
        .includes('reload'));

    return pageAccessedByReload;
}

function checkReload() {
    console.log("Is reload: " + isReload());
    axios.get('http://localhost:8080/ciklo/auth/refresh',
        {
            params:
                {
                    reload: isReload()
                }
        }).then((response) => {
        console.log("success");
    }, (error) => {
        console.log("error");
    })
}