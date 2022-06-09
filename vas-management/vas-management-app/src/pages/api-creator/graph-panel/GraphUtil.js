import { getIncomers, getOutgoers } from 'react-flow-renderer'

export const validateConnection = (elements, conn) => {
    let sourceEle = elements.filter(e => e.id === conn.source)[0]
    let targetEle = elements.filter(e => e.id === conn.target)[0]
    if (sourceEle && targetEle) {
        let source = sourceEle.data.label, target = targetEle.data.label;
        if (source === 'Switch' && !(target === 'Case' || target === 'Default')) {
            return 'Warn: Switch node can only be connected to a Case/Default node'
        }
        if ((target === 'Case' || target === 'Default') && source !== 'Switch') {
            return 'Warn: Source node of Case/Default must be a Switch'
        }
        //validate conn count
        let ins = getIncomers(sourceEle, elements).length;
        let outs = getOutgoers(sourceEle, elements).length;
        console.log(ins, outs)
        if (source === 'Start' && outs > 0) {
            return "Warn: Start node can only have one output connection"
        }
        if ((source === 'Case' || source === 'Default') && (outs > 0 && ins > 0)) {
            return "Warn: Switch Case/Default nodes can only have one input/output connection"
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
        id: `n_${id}`,
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

    if (data.title === 'Switch') {
        //create deafault case
        id += 1;
        let cPos = { x: data.position.x + 150, y: data.position.y - 60 };
        let caseNode = {
            type: 'customNode',
            title: `Case`,
            icon: 'Remote',
            position: cPos,
        }
        nElements.push(createNode(id, caseNode))
        //create deafault default
        id += 1;
        let dPos = { x: cPos.x, y: cPos.y + 130 };
        let dNode = {
            type: 'customNode',
            title: `Default`,
            icon: 'Remote',
            position: dPos,
        }
        nElements.push(createNode(id, dNode))
    }
    return nElements;
}
