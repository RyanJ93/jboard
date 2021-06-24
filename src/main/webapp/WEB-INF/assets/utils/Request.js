'use strict';

class Request {
    static #prefix = '';

    static #serializePostData(data){
        const properties = [];
        if ( data !== null && typeof data === 'object' ){
            for ( const key in data ){
                if ( data.hasOwnProperty(key) ){
                    properties.push(encodeURIComponent(key) + '=' + encodeURIComponent(data[key]));
                }
            }
        }
        return properties.join('&');
    }

    static #showNetworkErrorMessage(callback, allowRetry){
        const alertModal = document.getElementById('app').__vue__.$root.$refs.app.getAlertModal();
        alertModal.showNetworkErrorMessage(callback, allowRetry);
    }

    static #sendRequest(method, url, data, handleGenericErrors){
        return new Promise((resolve, reject) => {
            const request = new XMLHttpRequest();
            request.open(method, Request.#prefix + url, true);
            request.responseType = 'json';
            request.onreadystatechange = () => {
                if ( request.readyState === XMLHttpRequest.DONE ){
                    if ( request.status === 0 || request.status >= 500 ){
                        if ( handleGenericErrors !== false ){
                            return Request.#showNetworkErrorMessage(() => {
                                const request = Request.#sendRequest(method, url, data, handleGenericErrors);
                                request.then((response) => resolve(response)).catch((ex) => reject(ex));
                            }, true);
                        }else if ( request.state === 0 ){
                            return reject();
                        }
                    }
                    resolve(request.response);
                }
            };
            if ( method === 'POST' ){
                request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                request.send(Request.#serializePostData(data));
            }else{
                request.send();
            }
        });
    }

    static setPrefix(prefix){
        Request.#prefix = typeof prefix === 'string' ? prefix : '';
    }

    static getPrefix(){
        return Request.#prefix;
    }

    static get(url, handleGenericErrors = true){
        return Request.#sendRequest('GET', url, null, handleGenericErrors);
    }

    static post(url, data = null, handleGenericErrors = true){
        return Request.#sendRequest('POST', url, data, handleGenericErrors);
    }
}

export default Request;
