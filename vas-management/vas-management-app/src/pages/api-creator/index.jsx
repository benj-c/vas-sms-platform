//lib
import { useEffect, useState } from "react";
import { createUseStyles } from "react-jss";
import { ReactFlowProvider } from "react-flow-renderer"
import { useRecoilState, useRecoilValue } from "recoil";
import { useLocation } from "react-router-dom";
//app
import { apiOfApiCreatorAtom, selectedTopBarMenuAtom, apiUpdateEventAtom } from "../../state/atoms"
import GraphNodePanel from "./graph-panel/GraphNodePanel";
import Graph from "./graph-panel/Graph";
import { getApiByApiId } from "../../common/ApiHandler"
import { toJsonGraph } from "./graph-panel/GraphUtil"
import InfoPanel from "./InfoPanel"

const initialElements = {
    nodes: [
        {
            id: 'start',
            type: 'startNode',
            data: { label: 'start' },
            position: { x: 25, y: 225 },
        },
        {
            id: 'return',
            type: 'returnNode',
            data: { label: 'return' },
            position: { x: 1025, y: 225 },
        },
    ],
    edges: []
};

const useStyles = createUseStyles({
    graphEditorRoot: {
        display: 'grid',
        gridTemplateColumns: '10% 90%',
        gap: '0.5rem',
        height: '89vh',
        padding: '1rem',
    },
})

const ApiCreator = () => {
    const classes = useStyles();
    const location = useLocation();
    const [apiLoading, setApiLoading] = useState(false);
    const [selectedApi, setSelectedApi] = useRecoilState(apiOfApiCreatorAtom);
    const menu = useRecoilValue(selectedTopBarMenuAtom)
    const apiUpdateEvent = useRecoilValue(apiUpdateEventAtom)

    useEffect(() => {
        let isApiSubscribed = true;
        if (isApiSubscribed) {
            setSelectedApi(null);
            let urlParams = new URLSearchParams(location.search);
            let apiId = Number(urlParams.get('ref'));
            let commit = urlParams.get('commit');
            loadApi(apiId, commit);
        }

        return () => {
            // cancel the subscription
            isApiSubscribed = false;
        };
    }, [])

    useEffect(() => {
        if (apiUpdateEvent) {
            loadApi(apiUpdateEvent.id, apiUpdateEvent.commitId)
        }
    }, [apiUpdateEvent])

    const loadApi = (id, commit) => {
        setApiLoading(true)
        getApiByApiId(id, commit).then(res => {
            let { nodes, edges } = toJsonGraph(res.data.xml)
            let graphElements = (nodes.length > 0 || edges.length > 0) ? { nodes, edges } : initialElements;
            let d = { ...res.data, graphElements };
            setSelectedApi(d);
        })
            .catch(e => { })
            .finally(() => {
                setApiLoading(false)
            })
    }

    return (
        <>
            {menu.title === "Designer" && (
                <div className={classes.graphEditorRoot}>
                    {selectedApi && selectedApi.graphElements.nodes.length > 0 && (
                        <ReactFlowProvider>
                            <GraphNodePanel />
                            <Graph />
                        </ReactFlowProvider>
                    )}
                </div>
            )}

            {menu.title === "Details" && selectedApi && (
                <InfoPanel inc="1" />
            )}
        </>
    )
}

export default ApiCreator;
