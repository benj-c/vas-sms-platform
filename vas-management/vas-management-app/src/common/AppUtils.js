import { get, persist } from "./Storage"

const COLORS = ["#55ffff", "#00fdff", "#01f9c6", "#0ff0fc", "#04d9ff"];

//credit: https://stackoverflow.com/questions/376373/pretty-printing-xml-with-javascript/49458964#49458964
export const formatXml = (xml, tab) => {
    var formatted = '', indent = '';
    tab = tab || '\t';
    xml.split(/>\s*</).forEach(function (node) {
        if (node.match(/^\/\w/)) indent = indent.substring(tab.length);
        formatted += indent + '<' + node + '>\r\n';
        if (node.match(/^<?\w[^>]*[^\/]$/)) indent += tab;
    });
    return formatted.substring(1, formatted.length - 3);
}

export const isLoggedIn = () => {
    let user = get('user');
    return user ? true : false;
}

export const logOut = () => {
    persist('user', null);
}

export const randomNumber = max => {
    return Math.floor(Math.random() * max)
}

export const getRandomColor = () => {
    return COLORS[randomNumber(COLORS.length)];
}

export const toJsonGraph = (xml) => {
    let nodes = [], edges = [], data = [];
    try {
        let parser = new DOMParser();
        let xmlDoc = parser.parseFromString(xml, "text/xml");
        let blocks = xmlDoc.getElementsByTagName("block");
        for (let i = 0; i <= blocks.length; i++) {
            console.log(blocks[i])
        }
    } catch (e) {
        console.log(e)
    }
}