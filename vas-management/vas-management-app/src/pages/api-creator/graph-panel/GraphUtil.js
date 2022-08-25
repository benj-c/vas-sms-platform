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
    let nodes = [], edges = [];

    const createNodeByTag = (tag, id) => {
        let p = tag.getAttribute("pos").split(",");
        return {
            id: id,
            type: tag.getAttribute("nodeCType"),
            data: {
                label: tag.getAttribute("type")
            },
            position: { x: p[0], y: p[1] }
        }
    }

    const getEdges = () => {

    }

    try {
        let parser = new DOMParser();
        let xmlDoc = parser.parseFromString(xml, "text/xml");
        let blocks = xmlDoc.getElementsByTagName("block");
        edges = JSON.parse(xmlDoc.getElementsByTagName("conn")[0]?.textContent || "[]");
        for (let i = 0; i < blocks.length; i++) {
            let blk = blocks[i];
            if (blk.getAttribute("nodeCType") == "startNode") {
                let p = blk.getAttribute("pos").split(",");
                nodes.push({
                    id: "start",
                    type: blk.getAttribute("nodeCType"),
                    data: {
                        label: "Start"
                    },
                    position: { x: p[0], y: p[1] }
                })
            }

            if (blk.getAttribute("type") === "branch") {
                nodes.push(createNodeByTag(blk, blk.getAttribute("id")));
                let cases = blk.getElementsByTagName("case");
                for (let j = 0; j < cases.length; j++) {
                    nodes.push(createNodeByTag(cases[j], cases[j].getAttribute("id")));
                }
                let defaultCase = blk.getElementsByTagName("default");
                if (defaultCase && defaultCase[0]) {
                    nodes.push(createNodeByTag(defaultCase[0], defaultCase[0].getAttribute("id")));
                }
            } else {
                nodes.push(createNodeByTag(blk, blk.getAttribute("id")));
            }
        }
        return {
            nodes,
            edges,
        }
    } catch (e) {
        console.log(e)
    }
}
