const STORAGE_KEY = "ef.cfg";

export const persist = (key, value) => {
    let cfg = JSON.parse(localStorage.getItem(STORAGE_KEY)) || {};
    cfg[key] = value;
    localStorage.setItem(STORAGE_KEY, JSON.stringify(cfg));
}

export const get = (key) => {
    let cfg = JSON.parse(localStorage.getItem(STORAGE_KEY))
    return cfg ? cfg[key] : null;
}
