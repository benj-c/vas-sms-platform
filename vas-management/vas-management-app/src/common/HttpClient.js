import { get } from "../common/Storage"
//http methods
export const HttpMethod = {
    GET: "get",
    POST: "post",
    PUT: "put",
    DELETE: "delete",
}
//http request options
export const options = {
    apiEndpoint: process.env.REACT_APP_API_BASE || "http://localhost:8405",
    apiKey: `Bearer ${get('token')}`,
}
//perform http request
const _fetch = async ({ url, method, body }) => {
    url = `${options.apiEndpoint}${url}`;
    let request = {};
    if (HttpMethod.GET === method) {
        request = new Request(url, {
            method: HttpMethod.GET,
            headers: {
                "Authorization": `Bearer ${get('token')}`
            }
        });
    } else if (HttpMethod.PUT === method || HttpMethod.PATCH === method || HttpMethod.POST === method) {
        request = new Request(url, {
            method: method,
            headers: {
                "Authorization": options.apiKey,
                "Content-Type": "application/json"
            },
            body: body,
        });
    }
    
    let fetchResult = undefined;
    try {
        fetchResult = await fetch(request);
    } catch (e) {
        //handle errors (ex:connection errors)
        const responseError = {
            responseErr: false,
            error: e
        }
        let error = new Error();
        error = { ...error, ...responseError };
        throw error;
    }

    const result = await fetchResult.json();
    //send json on success
    if (fetchResult.ok) {
        return result;
    }
    //handle response errors
    const responseError = {
        responseErr: true,
        error: result.error
    }
    let error = new Error();
    error = { ...error, ...responseError };
    throw error;
}

export default _fetch;
