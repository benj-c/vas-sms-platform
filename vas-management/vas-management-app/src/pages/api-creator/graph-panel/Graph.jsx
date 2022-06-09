//lib
import { memo, useCallback, useEffect, useRef, useState } from "react";
import { Panel, PanelType } from "@fluentui/react";
import ReactFlow, { Background, Controls, addEdge, removeElements, updateEdge, isNode } from "react-flow-renderer"
import { createUseStyles } from "react-jss";
import { useRecoilValue, useRecoilState } from 'recoil'
//app
import ActionPanel from './ActionPanel'
import ButtonEdge from "./node/ButtonEdge";
import CustomNode from "./node/CustomNode";
import StartNode from "./node/StartNode";
import ReturnNode from "./node/ReturnNode";
import { removedEdgeAtom, processedXmlAtom, apiOfApiCreatorAtom } from '../../../state/atoms'
import { validateConnection, validateNodeOnDrop, getNode } from './GraphUtil'
import XmlWriter from './XmlWriter'
import useBoolean from "../../../common/hooks/useBoolean";
import { getPropComponent } from './GraphNodePanel'
import { getApiByApiId } from "../../../common/ApiHandler"

const useStyles = createUseStyles({
    graphPanel: {
        height: '100%',
        border: '1px dashed rgba(255, 255, 255, 0.4)',
    },
    stats: {
        fontFamily: 'monospace',
        paddingTop: '0.25rem',
        color: 'yellow',
    },
    graphStats: {
        float: 'right',
        paddingRight: '1rem',
    }
})

const edgeTypes = {
    buttonedge: ButtonEdge,
}

const nodeTypes = {
    customNode: CustomNode,
    startNode: StartNode,
    returnNode: ReturnNode,
}

const Graph = ({ initialElements, flow }) => {
    const classes = useStyles();
    const graphWrapper = useRef(null);
    const [reactFlowInstance, setReactFlowInstance] = useState(null);
    const [elements, setElements] = useState([]);
    const [graphMsg, setGraphMsg] = useState(null);
    const [selectedNode, setSelectedNode] = useState(null);
    const handleType = useRef('')
    const { value: isPanelOpen, toggle: togglePanel, } = useBoolean(false);
    const [currentApi, setCurrentApi] = useState(null);
    //recoil states
    const removedEdge = useRecoilValue(removedEdgeAtom)
    const [processedXml, setProcessedXml] = useRecoilState(processedXmlAtom);
    const apiId = useRecoilValue(apiOfApiCreatorAtom)

    useEffect(() => {
        if (apiId !== 0) {
            getApiByApiId(apiId).then(res => {
                setCurrentApi(res.data);
            })
                .catch(e => { })
                .finally(() => { })
        }

    }, [apiId])


    const onLoad = useCallback((rfi) => {
        if (!reactFlowInstance) {
            setReactFlowInstance(rfi)
            setElements(initialElements)
            console.log('Flow Loaded');
        }
    }, [reactFlowInstance])

    const onConnect = useCallback((params) => {
        console.log('onConnect', params);
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
        console.log('onElementsRemove', elementsToRemove);
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

    const onGraphSave = useCallback(() => {
        if (reactFlowInstance) {
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
            console.log(currentApi)
            let xml = new XmlWriter(currentApi, nodeArr, edgeArr).write()
            setProcessedXml(xml)
        }
    }, [currentApi])

    const getNextNodeId = () => {
        if (elements.length == 2) return 1;
        let mx = -1;
        for (let i = 0; i < elements.length; i++) {
            if (isNode(elements[i]) && elements[i].id.includes('_')) {
                let id = Number(elements[i].id.split('_')[1])
                if (id > mx) {
                    mx = id;
                }
            }
        }
        return mx + 1;
    }

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
        } else {
            setGraphMsg(validated);
        }
    };

    const onEdgeUpdate = (oldEdge, newConnection) => {
        setElements((els) => updateEdge(oldEdge, { ...newConnection, type: 'buttonedge' }, els))
    };

    const onNodeDoubleClick = useCallback((evt, node) => {
        if (node.type !== 'startNode' && node.type !== 'returnNode') {
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

    const dismissPanel = useCallback(() => {
        togglePanel()
        setSelectedNode(null)
    }, [reactFlowInstance])

    const onNodeDataChange = data => {
        let node = elements.filter(n => n.id == data.id)[0]
        console.log(data)
        console.log(node)
    }

    const getForm = useCallback((node) => {
        let Comp = getPropComponent(node.data.label)
        return <Comp node={node} onNodeDataChange={onNodeDataChange} />
    }, [elements])

    return (
        <div className={classes.graphPanel} ref={graphWrapper}>
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
                <ActionPanel onGraphSave={onGraphSave} />
                <Controls />
                <Background color="#aaa" gap={16} />
            </ReactFlow>
            <div className={classes.stats}>
                <p>
                    {graphMsg && <span className={classes.graphMsg}>{graphMsg}</span>}
                    <span className={classes.graphStats}>Nodes: {elements.length}</span>
                </p>
            </div>

            {selectedNode && (
                <Panel
                    headerText={`Properties | Node: ${selectedNode.data.label} ( ${selectedNode.id} )`}
                    isOpen={isPanelOpen}
                    onDismiss={dismissPanel}
                    closeButtonAriaLabel="Close"
                    type={PanelType.medium}
                >
                    {getForm(selectedNode)}
                </Panel>
            )}
        </div>
    )
}

export default memo(Graph);
