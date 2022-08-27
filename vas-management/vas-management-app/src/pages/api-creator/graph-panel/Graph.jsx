//lib
import { useCallback, useEffect, useRef, useState } from "react";
import { Panel, PanelType, Text } from "@fluentui/react";
import ReactFlow, { Background, Controls, addEdge, removeElements, updateEdge, isNode } from "react-flow-renderer"
import { useRecoilValue, useRecoilState } from 'recoil'
import { useHistory } from "react-router-dom";
//app
import ActionPanel from './ActionPanel'
import ButtonEdge from "./node/ButtonEdge";
import CustomNode from "./node/CustomNode";
import StartNode from "./node/StartNode";
import ReturnNode from "./node/ReturnNode";
import { removedEdgeAtom, processedXmlAtom, apiOfApiCreatorAtom, apiUpdateEventAtom } from '../../../state/atoms'
import { validateConnection, validateNodeOnDrop, getNode } from './GraphUtil'
import XmlWriter from './XmlWriter'
import useBoolean from "../../../common/hooks/useBoolean";
import { getPropComponent } from './GraphNodePanel'
import { updateApi } from "../../../common/ApiHandler"
import CodeViewer from "../CodeViewer";
import SaveApiChangeForm from "../../../common/forms/SaveApiChangeForm";

const edgeTypes = {
    buttonedge: ButtonEdge,
}

const nodeTypes = {
    customNode: CustomNode,
    startNode: StartNode,
    returnNode: ReturnNode,
}

let nodeData = [];
const Graph = () => {
    const graphWrapper = useRef(null);
    const handleType = useRef('')
    const history = useHistory();

    //state
    const [reactFlowInstance, setReactFlowInstance] = useState(null);
    const [elements, setElements] = useState([]);
    const [graphMsg, setGraphMsg] = useState(null);
    const [selectedNode, setSelectedNode] = useState(null);
    const [apiToBeSaved, setApiToBeSaved] = useState(null);
    const { value: isPanelOpen, toggle: togglePanel, } = useBoolean(false);
    const { value: isCodeViwerOpen, toggle: toggleCodeViewerPanel, } = useBoolean(false);
    const { value: isSaveOpen, toggle: toggleSavePanel, } = useBoolean(false);

    //recoil states
    const removedEdge = useRecoilValue(removedEdgeAtom)
    const [processedXml, setProcessedXml] = useRecoilState(processedXmlAtom);
    const graphApi = useRecoilValue(apiOfApiCreatorAtom)

    useEffect(() => {
        let elemns = [].concat(graphApi.graphElements.nodes).concat(graphApi.graphElements.edges)
        setElements(elemns);
        nodeData = graphApi.graphElements.data.length > 0 ? graphApi.graphElements.data : graphApi.graphElements.nodes.map(e => {
            return {
                id: e.id
            }
        })
    }, [graphApi])

    const onLoad = (rfi) => {
        setReactFlowInstance(rfi);
    }

    const onConnect = useCallback((params) => {
        if (handleType.current !== 'target') {
            let validated = validateConnection(elements, params)
            if (!validated) {
                setElements((els) => addEdge({ ...params, type: 'buttonedge' }, els))
            } else {
                setGraphMsg(validated);
            }
        } else {
            setGraphMsg('Warn: Target->Source isn\'t a valid connection');
        }
    }, [elements]);

    const onConnectStart = (evt, node) => {
        handleType.current = node.handleType;
    }

    const onElementsRemove = useCallback((elementsToRemove) => {
        setElements((els) => removeElements(elementsToRemove, els))
    }, []);

    useEffect(() => {
        if (removedEdge) {
            onElementsRemove(elements.filter(ele => ele.id === removedEdge))
        }
    }, [removedEdge])

    const onDragOver = (event) => {
        event.preventDefault();
        event.dataTransfer.dropEffect = 'move';
    };

    const onDrop = (event) => {
        event.preventDefault();
        const node = JSON.parse(event.dataTransfer.getData('application/reactflow'));
        node.id = getNextNodeId();
        let validated = validateNodeOnDrop(elements, node);
        if (!validated) {
            const reactFlowBounds = graphWrapper.current.getBoundingClientRect();
            const position = {
                x: event.clientX - reactFlowBounds.left,
                y: event.clientY - reactFlowBounds.top,
            };
            node.position = position;
            let nElements = getNode(node);
            setElements((es) => es.concat(nElements));

            nodeData = nodeData.concat(nElements.map(e => {
                return {
                    id: e.id
                }
            }))
        } else {
            setGraphMsg(validated);
        }
    };

    const onEdgeUpdate = (oldEdge, newConnection) => {
        setElements((els) => updateEdge(oldEdge, { ...newConnection, type: 'buttonedge' }, els))
    };

    const onNodeDoubleClick = useCallback((evt, node) => {
        if (node.type !== 'startNode' && node.type !== 'returnNode' && node.data.label !== 'branch') {
            setSelectedNode(node)
            togglePanel()
        }
    }, [reactFlowInstance])

    const onElementClick = useCallback((evt, ele) => {
        setGraphMsg(isNode(ele) ? `Node: ${ele.data.label} [${ele.id}]` : `Connection: [${ele.id}]`);
    }, [reactFlowInstance])

    const onPaneClick = useCallback(e => {
        setGraphMsg(null)
    }, [reactFlowInstance])

    const onNodeDataChange = props => {
        console.log(props)
        // nodeData.filter(d => d.id == props.id)[0]?.props = props.data

        // for (let i = 0; i < nodeData.length; i++) {
        //     if (nodeData[i].id == props.id) {
        //         nodeData[i].props = props.data;
        //     }
        // }

        nodeData = nodeData.map(d => {
            if (d.id == props.id) {
                return { id: d.id, props: props.data }
            }
            return d;
        })
        console.log(nodeData)
    }

    const dismissPanel = useCallback(() => {
        togglePanel()
        setSelectedNode(null)
    }, [reactFlowInstance])

    const toXml = () => {
        let _flow = reactFlowInstance.toObject();
        let nodeArr = [], edgeArr = [];
        for (let i = 0; i < _flow.elements.length; i++) {
            let e = _flow.elements[i]
            if (isNode(e)) {
                nodeArr.push(e);
            } else {
                edgeArr.push(e);
            }
        }
        return new XmlWriter(graphApi, nodeArr, edgeArr, nodeData).write()
    }

    const onGraphSave = () => {
        if (reactFlowInstance) {
            let xml = toXml();
            setProcessedXml(xml)
            let apiData = { ...graphApi, xml: xml };
            delete apiData.graphElements;
            setApiToBeSaved(apiData);
            toggleSavePanel();
        }
    }

    const onDiscard = () => {
        history.push(`/api-creator?ref=${graphApi.id}&commit=${graphApi.commitId}`)
    }

    const getNextNodeId = () => {
        if (elements.length == 2) return 1;
        let mx = -1;
        for (let i = 0; i < elements.length; i++) {
            if (isNode(elements[i])) {
                let id = isNaN(elements[i].id) ? 0 : elements[i].id;
                mx = Math.max(id, mx)
            }
        }
        mx++;
        return mx;
    }

    const getForm = useCallback((node) => {
        let Comp = getPropComponent(node.data.label)
        let d = nodeData.filter(n => n.id == node.id)[0];
        // console.log(nodeData)
        // console.log(node.id)
        // console.log(d)
        return <Comp node={node} data={d} onNodeDataChange={onNodeDataChange} />
    }, [elements, nodeData])

    const onGraphBuild = () => {
        if (reactFlowInstance) {
            let xml = toXml();
            setProcessedXml(xml)
            toggleCodeViewerPanel();
        }
    }

    const getNodeCount = () => {
        return elements.filter(e => isNode(e)).length;
    }

    const ApiDetail = () => (
        <div style={{
            paddingTop: '0.25rem',
            position: 'absolute',
            top: '0rem',
            left: '0.5rem',
            display: 'flex',
            gap: '0.25rem',
            alignItems: 'center'
        }}>
            <Text variant="small">[ {graphApi.name}, </Text>
            <Text variant="small">v{graphApi.version} ]</Text>
            <Text variant="small">{graphApi.commitId}</Text>
        </div>
    )

    const GraphStat = () => (
        <div style={{
            fontFamily: 'monospace',
            paddingTop: '0.25rem',
            color: 'yellow',
            position: 'absolute',
            top: '0rem',
            right: '0.5rem',
            textAlign: 'right'
        }}>
            <small>
                Nodes: {getNodeCount()}
                <br />
                {graphMsg && <>{graphMsg}</>}
            </small>
        </div>
    )

    return (
        <div
            ref={graphWrapper}
            style={{
                height: '100%',
                border: '1px dashed rgba(255, 255, 255, 0.4)',
                position: 'relative'
            }}>
            <ReactFlow
                elements={elements}
                edgeTypes={edgeTypes}
                nodeTypes={nodeTypes}
                deleteKeyCode={'Delete'}
                onConnect={onConnect}
                onConnectStart={onConnectStart}
                onElementsRemove={onElementsRemove}
                onEdgeUpdate={onEdgeUpdate}
                onLoad={onLoad}
                onDrop={onDrop}
                onDragOver={onDragOver}
                onNodeDoubleClick={onNodeDoubleClick}
                onElementClick={onElementClick}
                onPaneClick={onPaneClick}
            >
                <ActionPanel 
                onGraphSave={onGraphSave} 
                onBuild={onGraphBuild} 
                onDiscard={onDiscard}
                />
                <Controls />
                <Background color="#aaa" gap={16} />
            </ReactFlow>

            <ApiDetail />
            <GraphStat />

            {selectedNode && (
                <Panel
                    headerText={`Properties, ${selectedNode.data.label} Node`}
                    isOpen={isPanelOpen}
                    onDismiss={dismissPanel}
                    closeButtonAriaLabel="Close"
                    type={PanelType.medium}
                >
                    {getForm(selectedNode)}
                </Panel>
            )}

            <Panel
                headerText={`CODE`}
                isOpen={isCodeViwerOpen}
                onDismiss={toggleCodeViewerPanel}
                closeButtonAriaLabel="Close"
                type={PanelType.medium}
            >
                <CodeViewer />
            </Panel>


            <Panel
                headerText={`Save API`}
                isOpen={isSaveOpen}
                onDismiss={toggleSavePanel}
                closeButtonAriaLabel="Close"
                type={PanelType.medium}
            >
                <SaveApiChangeForm dismissPanel={toggleSavePanel} api={apiToBeSaved} />
            </Panel>
        </div>
    )
}

export default Graph;
