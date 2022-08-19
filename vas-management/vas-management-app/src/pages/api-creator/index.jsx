//lib
import { useEffect, useCallback, memo, useState } from "react";
import { createUseStyles } from "react-jss";
import { ReactFlowProvider } from "react-flow-renderer"
import { useRecoilState, useRecoilValue } from "recoil";
import { useLocation } from "react-router-dom";
//app
import { apiOfApiCreatorAtom, updateTopBarSubTitleAtom } from "../../state/atoms"
import GraphNodePanel from "./graph-panel/GraphNodePanel";
import Graph from "./graph-panel/Graph";
import { getApiByApiId } from "../../common/ApiHandler"
import { toJsonGraph } from "../../common/AppUtils"

const initialElements = [
    {
        id: 'start',
        type: 'startNode',
        data: { label: 'Start' },
        position: { x: 25, y: 225 },
    },
    {
        id: 'return',
        type: 'returnNode',
        data: { label: 'Return' },
        position: { x: 1025, y: 225 },
    },
];

const useStyles = createUseStyles({
    graphEditorRoot: {
        display: 'grid',
        gridTemplateColumns: '10% 90%',
        gap: '1rem',
        height: '88vh',
        padding: '1rem',
    },
})

const ApiCreator = () => {
    const classes = useStyles();
    const location = useLocation();
    const [graphElements, setGraphElements] = useState([]);
    const [selectedApi, setSelectedApi] = useRecoilState(apiOfApiCreatorAtom);
    const [topBarSubTitle, setTopBarSubTitle] = useRecoilState(updateTopBarSubTitleAtom);

    useEffect(() => {
        let apiId = Number(new URLSearchParams(location.search).get('ref'));
        let isApiSubscribed = true;
        getApiByApiId(apiId).then(res => {
            if (isApiSubscribed) {
                setSelectedApi(res.data);
                let json = toJsonGraph(res.data.xml)
                setGraphElements(initialElements);
                setTopBarSubTitle(
                    <>&nbsp;&#x2022;&nbsp;{res.data.name}&nbsp;&#x2022;&nbsp;{`v${res.data.version}`}</>
                );
            }
        })
            .catch(e => { })
            .finally(() => { })
        return () => {
            // cancel the subscription
            isApiSubscribed = false;
            setTopBarSubTitle(null);
        };
    }, [])

    return (
        <div className={classes.graphEditorRoot}>
            {graphElements.length > 0 && (
                <ReactFlowProvider>
                    <GraphNodePanel />
                    <Graph graphElements={graphElements} api={selectedApi} />
                </ReactFlowProvider>
            )}
        </div>
    )
}

export default ApiCreator;
