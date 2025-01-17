class XmlWriter {
    constructor(currentApi, nodes, edges, data) {
        // console.log({nodes})
        console.log({ data })
        this.flow = currentApi;
        this.nodes = nodes;
        this.edges = edges;
        this.data = data;
    }
    getNodeById(id) {
        return this.nodes.filter(e => e.id === id)[0];
    }
    getEdgeBySourceId(id) {
        return this.edges.filter(e => e.source === id)[0]
    }
    getPos(node) {
        return `${node.position.x},${node.position.y}`;
    }
    getDataObj(id) {
        return this.data.filter(e => e.id === id)[0];
    }
    getHeaderXml() {
        let xml = `<name>${this.flow.name}</name>`;
        xml += `<version>${this.flow.version}</version>`;
        return xml;
    }
    getNodeInitPart(node) {
        let lbl = node.data.label.startsWith("Function") ? "func" : node.data.label;
        return `<block id="${node.id}" type="${lbl}" nodeCType="${node.type}" pos="${this.getPos(node)}" icon="${node.data.icon}">`
    }
    getNodeNextPart(next) {
        return `<next-node>${next === "return" ? "END" : next}</next-node>`;
    }
    getVariableXml(variables) {
        let xml = "";
        if (variables.length > 0) {
            for (let i = 0; i < variables.length; i++) {
                let v = variables[i];
                xml += `<variable name="${v.name}">${v.value}</variable>`
            }
        }
        return xml;
    }
    getStartXml(node, next) {
        let xml = `<block id="0" type="assign" nodeCType="${node.type}" pos="${this.getPos(node)}">`;
        xml += this.getNodeNextPart(next)
        xml += '</block>'
        return xml;
    }
    getAssignXml(node, next) {
        let xml = this.getNodeInitPart(node);
        let dt = this.getDataObj(node.id);
        xml += this.getVariableXml(dt?.variables || []);
        xml += this.getNodeNextPart(next)
        xml += `</block>`
        return xml;
    }
    getFunctionXml(node, next) {
        let dt = this.getDataObj(node.id);
        let xml = this.getNodeInitPart(node);
        if (dt.funcProps) {
            for (let i = 0; i < dt.funcProps.length; i++) {
                let field = dt.funcProps[i];
                if (field.field === 'method') {
                    xml += `<method>${field.value}</method>`;
                } else if (field.field === 'class') {
                    xml += `<class>${field.value}</class>`;
                } else if (field.field === 'functionType') {
                    xml += `<functionType>${field.value}</functionType>`;
                } else {
                    xml += `<param name="${field.field}">${field.value}</param>`;
                }
            }
        }
        xml += this.getNodeNextPart(next);
        xml += `</block>`;
        return xml;
    }
    getSwitchXml() {
        let xml = ``;
        let switches = this.nodes.filter(f => f.data.label === 'branch')
        for (let i = 0; i < switches.length; i++) {
            xml += this.getNodeInitPart(switches[i])
            let sEdges = this.edges.filter(f => f.source == switches[i].id)
            for (let j = 0; j < sEdges.length; j++) {
                let nd = this.getNodeById(sEdges[j].target)
                let ed = this.getEdgeBySourceId(sEdges[j].target)
                let dt = this.getDataObj(nd.id);
                if (nd.data.label === 'case') {
                    xml += `<case id="${nd.id}" type="case" nodeCType="customNode" pos="${this.getPos(nd)}">`;
                    xml += `<expression>${dt?.expression}</expression>`
                    xml += this.getVariableXml(dt?.variables || []);
                    xml += this.getNodeNextPart(ed.target)
                    xml += `</case>`
                } else {
                    xml += `<default id="${nd.id}" type="default" nodeCType="customNode" pos="${this.getPos(nd)}">`
                    xml += this.getVariableXml(dt?.variables || []);
                    xml += this.getNodeNextPart(ed.target)
                    xml += `</default>`
                }
            }
            xml += '</block>'
        }
        return xml;
    }
    getReturnXml(node) {
        let xml = this.getNodeInitPart(node);
        xml += '<outputparams name="ses">-1</outputparams>';
        xml += '</block>';
        return xml;
    }
    write() {
        let xml = '<?xml version="1.0" encoding="UTF-8"?>';
        xml += `<service name='${this.flow.name}'>`;

        for (let i = 0; i < this.edges.length; i++) {
            let ed = this.edges[i];
            let node = this.getNodeById(ed.source)
            if (node.data.label === 'func') {
                xml += this.getFunctionXml(node, ed.target);
            } else {
                switch (node.data.label) {
                    case 'start': {
                        xml += this.getStartXml(node, ed.target);
                        break;
                    }
                    case 'assign': {
                        xml += this.getAssignXml(node, ed.target);
                        break;
                    }
                    default:
                        break;
                }
            }
        }
        xml += this.getSwitchXml();
        xml += this.getReturnXml(this.nodes.filter(n => n.id === "return")[0]);
        xml += `<conn>${JSON.stringify(this.edges)}</conn>`
        xml += "</service>"
        return xml;
    }
}

export default XmlWriter;

// let nodes = [{ "id": "start", "type": "startNode", "data": { "label": "Start" }, "position": { "x": 25, "y": 25 } }, { "id": "return", "type": "returnNode", "data": { "label": "Return" }, "position": { "x": 1029, "y": 384 } }, { "id": "n_1", "type": "customNode", "position": { "x": 116.453125, "y": 197 }, "data": { "label": "Log", "icon": "CodeEdit" } }, { "id": "n_2", "type": "customNode", "position": { "x": 314.453125, "y": 201 }, "data": { "label": "Log", "icon": "CodeEdit" } }, { "id": "n_3", "type": "customNode", "position": { "x": 559.453125, "y": 277 }, "data": { "label": "Switch", "icon": "BranchFork2" } }, { "id": "n_4", "type": "customNode", "position": { "x": 709.453125, "y": 217 }, "data": { "label": "Case", "icon": "Remote" } }, { "id": "n_5", "type": "customNode", "position": { "x": 709.453125, "y": 347 }, "data": { "label": "Default", "icon": "Remote" } }, { "id": "n_7", "type": "customNode", "position": { "x": 891.453125, "y": 169 }, "data": { "label": "Log", "icon": "CodeEdit" } }];
// let edges = [{ "source": "n_3", "sourceHandle": "source", "target": "n_5", "targetHandle": "target", "type": "buttonedge", "id": "reactflow__edge-n_3source-n_5target" }, { "source": "n_3", "sourceHandle": "source", "target": "n_4", "targetHandle": "target", "type": "buttonedge", "id": "e_2" }, { "source": "n_2", "sourceHandle": "source", "target": "n_3", "targetHandle": "target", "type": "buttonedge", "id": "e_3" }, { "source": "start", "sourceHandle": null, "target": "n_1", "targetHandle": "target", "type": "buttonedge", "id": "e_4" }, { "source": "n_1", "sourceHandle": "source", "target": "n_2", "targetHandle": "target", "type": "buttonedge", "id": "e_5" }, { "source": "n_4", "sourceHandle": "source", "target": "n_7", "targetHandle": "target", "type": "buttonedge", "id": "e_6" }, { "source": "n_7", "sourceHandle": "source", "target": "return", "targetHandle": null, "type": "buttonedge", "id": "e_7" }, { "source": "n_5", "sourceHandle": "source", "target": "return", "targetHandle": null, "type": "buttonedge", "id": "e_8" }]

// let xml = new XmlWriter({ name: 'test', version: 'v1.0.0' }, nodes, edges).write()
// console.log(xml)
