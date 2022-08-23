//lib
import { useCallback, useEffect, useRef, useState } from "react";
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
import { updateApi } from "../../../common/ApiHandler"
import CodeViewer from "../CodeViewer";

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

const Graph = () => {
    const classes = useStyles();
    const graphWrapper = useRef(null);
    const handleType = useRef('')

    //state
    const [reactFlowInstance, setReactFlowInstance] = useState(null);
    const [elements, setElements] = useState([]);
    const [graphMsg, setGraphMsg] = useState(null);
    const [selectedNode, setSelectedNode] = useState(null);
    const { value: isPanelOpen, toggle: togglePanel, } = useBoolean(false);
    const { value: isCodeViwerOpen, toggle: toggleCodeViewerPanel, } = useBoolean(false);

    //recoil states
    const removedEdge = useRecoilValue(removedEdgeAtom)
    const [processedXml, setProcessedXml] = useRecoilState(processedXmlAtom);
    const graphApi = useRecoilValue(apiOfApiCreatorAtom)

    useEffect(() => {
        let subs = true;
        if (subs) {
            console.log(graphApi.graphElements.edges)
            setElements((es) => es.concat(graphApi.graphElements.nodes)
                .concat(graphApi.graphElements.edges)
            );
        }
        return () => {
            subs = false;
        }
    }, [graphApi])

    const onLoad = useCallback((rfi) => {
        if (!reactFlowInstance) {
            setReactFlowInstance(rfi);
        }
    }, [reactFlowInstance])

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
        return new XmlWriter(graphApi, nodeArr, edgeArr).write()
    }

    const onGraphSave = () => {
        if (reactFlowInstance) {
            let xml = toXml();
            setProcessedXml(xml)
            let apiData = { ...graphApi, xml: xml };
            updateApi(apiData).then(res => {

            })
                .catch(e => { })
                .finally(() => { })
        }
    }

    const getNextNodeId = () => {
        if (elements.length == 2) return 1;
        let mx = -1;
        for (let i = 0; i < elements.length; i++) {
            if (isNode(elements[i])) {
                let id = isNaN(elements[i].id) ? 0 : elements[i].id;
                console.log({ id, mx })
                mx = Math.max(id, mx)
            }
        }
        mx++;
        console.log({ mx })
        return mx;
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
        console.log({ node, data })
    }

    const getForm = useCallback((node) => {
        let Comp = getPropComponent(node.data.label)
        return <Comp node={node} onNodeDataChange={onNodeDataChange} />
    }, [elements])

    const onGraphBuild = useCallback(() => {
        if (reactFlowInstance) {
            let xml = toXml();
            setProcessedXml(xml)
            toggleCodeViewerPanel();
        }
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
                <ActionPanel onGraphSave={onGraphSave} onBuild={onGraphBuild} />
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

            <Panel
                headerText={`CODE`}
                isOpen={isCodeViwerOpen}
                onDismiss={toggleCodeViewerPanel}
                closeButtonAriaLabel="Close"
                type={PanelType.medium}
            >
                <CodeViewer />
            </Panel>
        </div>
    )
}

export default Graph;
