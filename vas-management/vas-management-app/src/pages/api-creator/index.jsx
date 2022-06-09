//lib
import { useEffect } from "react";
import { createUseStyles } from "react-jss";
import { ReactFlowProvider } from "react-flow-renderer"
import { useRecoilState, useRecoilValue } from "recoil";
import { useLocation } from "react-router-dom";
//app
import { selectedTopBarMenuAtom, apiOfApiCreatorAtom } from "../../state/atoms"
import GraphNodePanel from "./graph-panel/GraphNodePanel";
import Graph from "./graph-panel/Graph";
import CodeViewer from "./CodeViewer";

const useStyles = createUseStyles({
    graphEditorRoot: {
        display: 'grid',
        gridTemplateColumns: '10% 90%',
        gap: '1rem',
        height: '88vh',
        padding: '1rem',
    },
})

const initialElements = [
    {
        id: 'start',
        type: 'startNode',
        data: { label: 'Start' },
        position: { x: 25, y: 25 },
    },
    {
        id: 'return',
        type: 'returnNode',
        data: { label: 'Return' },
        position: { x: 425, y: 25 },
    },
];

const ApiCreator = () => {
    const classes = useStyles();
    const location = useLocation();
    const selectedMenu = useRecoilValue(selectedTopBarMenuAtom);
    const [selectedApi, setSelectedApi] = useRecoilState(apiOfApiCreatorAtom);

    useEffect(() => {
        let apiId = Number(new URLSearchParams(location.search).get('ref'));
        setSelectedApi(apiId);
    }, [location])

    const ApiDesigner = () => (
        <div className={classes.graphEditorRoot}>
            <ReactFlowProvider>
                <GraphNodePanel />
                <Graph initialElements={initialElements} />
            </ReactFlowProvider>
        </div>
    )

    return (
        <>
            {selectedMenu.title === "Designer" && <ApiDesigner />}
            {selectedMenu.title === "Code" && <CodeViewer />}
        </>
    )
}

export default ApiCreator;
