import { getIncomers, getOutgoers } from 'react-flow-renderer'

export const validateConnection = (elements, conn) => {
    let sourceEle = elements.filter(e => e.id === conn.source)[0]
    let targetEle = elements.filter(e => e.id === conn.target)[0]
    if (sourceEle && targetEle) {
        let source = sourceEle.data.label, target = targetEle.data.label;
        if (source === 'Branch' && !(target === 'Case' || target === 'Default')) {
            return 'Warn: Branch node can only be connected to a Case/Default node'
        }
        if ((target === 'Case' || target === 'Default') && source !== 'Branch') {
            return 'Warn: Source node of Case/Default must be a Branch'
        }
        //validate conn count
        let ins = getIncomers(sourceEle, elements).length;
        let outs = getOutgoers(sourceEle, elements).length;
        if (source === 'Start' && outs > 0) {
            return "Warn: Start node can only have one output connection"
        }
        if ((source === 'Case' || source === 'Default') && (outs > 0 && ins > 0)) {
            return "Warn: Branch Case/Default nodes can only have one input/output connection"
        }
        if ((source === 'Assign' || source === 'Log' || source.startsWith('Function')) && outs > 0) {
            return "Warn: " + source + " node can only have one output connection"
        }
    }
    return null;
}

export const validateNodeOnDrop = (elements, node) => {
    //check output node exists
    if (node.type === "returnNode" && elements.filter(n => n.type === "returnNode")[0]) {
        return "Return node aleadey exists";
    }
    return null;
};

const createNode = (id, data) => {
    return {
        id: `${id}`,
        type: data.type,
        position: data.position,
        data: {
            label: data.title,
            icon: data.icon,
            functionType: data.functionType,
        },
    };
}

export const getNode = (data) => {
    let id = data.id, nElements = [];
    nElements.push(createNode(id, data));

    if (data.title === 'branch') {
        //create deafault case
        id++;
        let cPos = { x: data.position.x + 150, y: data.position.y - 60 };
        let caseNode = {
            type: 'customNode',
            title: `case`,
            icon: 'Remote',
            position: cPos,
        }
        nElements.push(createNode(id, caseNode))
        //create deafault default
        id++;
        let dPos = { x: cPos.x, y: cPos.y + 130 };
        let dNode = {
            type: 'customNode',
            title: `default`,
            icon: 'Remote',
            position: dPos,
        }
        nElements.push(createNode(id, dNode))
    }
    return nElements;
}

export const toJsonGraph = (xml) => {
    let nodes = [], edges = [], data = [];

    const createNodeByTag = (tag) => {
        let p = tag.getAttribute("pos").split(",");
        return {
            id: tag.getAttribute("id"),
            type: tag.getAttribute("nodeCType"),
            data: {
                label: tag.getAttribute("type"),
                icon: tag.getAttribute("icon"),
                functionType: tag.getElementsByTagName("functionType")[0]?.textContent,
            },
            position: { x: p[0], y: p[1] }
        }
    }

    const getDataObj = (blk) => {
        let d = {
            id: blk.getAttribute("id"),
            variables: []
        }
        let variables = blk.getElementsByTagName('variable');
        for (let i = 0; i < variables.length; i++) {
            d.variables.push({
                name: variables[i].getAttribute("name"),
                value: variables[i].textContent
            })
        }

        if (blk.nodeName === "case") {
            d.expression = blk.getElementsByTagName("expression")[0]?.textContent;
        }
        if (blk.getAttribute("type") === "func") {
            let props = [];
            props.push({
                field: 'functionType',
                value: blk.getElementsByTagName("functionType")[0]?.textContent,
            });
            props.push({
                field: 'method',
                value: blk.getElementsByTagName("method")[0]?.textContent,
            });
            props.push({
                field: 'class',
                value: blk.getElementsByTagName("class")[0]?.textContent
            });
            let params = blk.getElementsByTagName("param")
            for (let i = 0; i < params.length; i++) {
                let param = { name: params[i].getAttribute("name"), value: params[i].textContent };
                props.push({
                    field: param.name,
                    value: param.value
                });
            }
            d.funcProps = props;
        }
        console.log(d)
        return d;
    }

    const getXmlDoc = (str) => {
        try {
            let parser = new DOMParser();
            return parser.parseFromString(str, "text/xml");
        } catch (e) {
            console.log(e)
            return null;
        }
    }

    try {
        let xmlDoc = getXmlDoc(xml);
        if (xmlDoc) {
            let blocks = xmlDoc.getElementsByTagName("block");
            edges = JSON.parse(xmlDoc.getElementsByTagName("conn")[0]?.textContent || "[]");
            for (let i = 0; i < blocks.length; i++) {
                let blk = blocks[i];
                console.log(blk)
                if (blk.getAttribute("nodeCType") == "startNode") {
                    let p = blk.getAttribute("pos").split(",");
                    nodes.push({
                        id: "start",
                        type: blk.getAttribute("nodeCType"),
                        data: {
                            label: "start"
                        },
                        position: { x: p[0], y: p[1] }
                    })
                } else if (blk.getAttribute("type") === "branch") {
                    nodes.push(createNodeByTag(blk));
                    data.push(getDataObj(blk));

                    let cases = blk.getElementsByTagName("case");
                    for (let j = 0; j < cases.length; j++) {
                        nodes.push(createNodeByTag(cases[j]));
                        data.push(getDataObj(cases[j]));
                    }

                    let defaultCase = blk.getElementsByTagName("default");
                    if (defaultCase && defaultCase[0]) {
                        nodes.push(createNodeByTag(defaultCase[0]));
                        data.push(getDataObj(defaultCase[0]));
                    }
                } else {
                    nodes.push(createNodeByTag(blk));
                    data.push(getDataObj(blk));
                }
            }
        }
        return {
            nodes,
            edges,
            data,
        }
    } catch (e) {
        console.log(e)
    }
}
