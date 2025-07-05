function callCoinApi() {
    fetch('/coindesk/callApi')
        .then(response => {
            if (!response.ok) {
                throw new Error('error');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            return fetch('/coindesk', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}