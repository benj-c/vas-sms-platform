import _fetch, { HttpMethod } from "./HttpClient";

//api calls
export const login = async (data) => {
    let req = {
        url: `/authenticate`,
        method: HttpMethod.POST,
        body: JSON.stringify(data),
    }
    return await _fetch(req);
}

//
export const getServices = async () => {
    let url = '/service';
    let req = {
        url: url,
        method: HttpMethod.GET,
    }
    return await _fetch(req);
}

export const getServiceById = async (sRef) => {
    let req = {
        url: `/service/${sRef}`,
        method: HttpMethod.GET,
    }
    return await _fetch(req);
}

export const updateService = async (data) => {
    return await _fetch({
        url: `/service`,
        method: HttpMethod.PUT,
        body: JSON.stringify(data),
    });
}

export const updateAction = async (data) => {
    return await _fetch({
        url: `/action`,
        method: HttpMethod.PUT,
        body: JSON.stringify(data),
    });
}
export const createAction = async (data) => {
    return await _fetch({
        url: `/action`,
        method: HttpMethod.POST,
        body: JSON.stringify(data),
    });
}

export const getActionsByServiceId = async (id) => {
    return await _fetch({
        url: `/service/${id}/action`,
        method: HttpMethod.GET,
    });
}

export const updateKeyword = async (data) => {
    return await _fetch({
        url: `/keyword`,
        method: HttpMethod.PUT,
        body: JSON.stringify(data),
    });
}
export const getKeywordByActionsId = async (id) => {
    return await _fetch({
        url: `/action/${id}/keyword`,
        method: HttpMethod.GET,
    });
}
export const getApis = async (id) => {
    return await _fetch({
        url: `/api`,
        method: HttpMethod.GET,
    });
}
export const getAllApis = async () => {
    return await _fetch({
        url: `/api`,
        method: HttpMethod.GET,
    });
}
export const getApiByApiId = async (id) => {
    return await _fetch({
        url: `/api/${id}`,
        method: HttpMethod.GET,
    });
}
export const createKeyword = async (data) => {
    return await _fetch({
        url: `/keyword`,
        method: HttpMethod.POST,
        body: JSON.stringify(data),
    });
}
export const createService = async (data) => {
    return await _fetch({
        url: `/service`,
        method: HttpMethod.POST,
        body: JSON.stringify(data),
    });
}
export const createApi = async (data) => {
    return await _fetch({
        url: `/api`,
        method: HttpMethod.POST,
        body: JSON.stringify(data),
    });
}
export const updateApi = async (data) => {
    return await _fetch({
        url: `/api`,
        method: HttpMethod.PUT,
        body: JSON.stringify(data),
    });
}
export const getCxResponsesByApiId = async (api_id) => {
    return await _fetch({
        url: `/api/${api_id}/cx-responses`,
        method: HttpMethod.GET,
    });
}
export const createCxResponse = async (data) => {
    return await _fetch({
        url: `/cx-response`,
        method: HttpMethod.POST,
        body: JSON.stringify(data),
    });
}
export const updateCxResponse = async (data) => {
    return await _fetch({
        url: `/cx-response`,
        method: HttpMethod.PUT,
        body: JSON.stringify(data),
    });
}
export const getExportXml = async (sId) => {
    return await _fetch({
        url: `/service/${sId}/download`,
        method: HttpMethod.GET,
    });
}
export const getSmsHistoryByMsisdn = async (msisdn) => {
    return await _fetch({
        url: `/history?msisdn=${msisdn}`,
        method: HttpMethod.GET,
    });
}
