//lib
import { useEffect, useCallback, memo, useState } from "react";
import { IconButton, Link, Panel, PanelType, Persona, PersonaPresence, PersonaSize, Text, TooltipHost } from "@fluentui/react";
import { createUseStyles } from "react-jss";
import { ReactFlowProvider } from "react-flow-renderer"
import { useRecoilState, useRecoilValue } from "recoil";
import { useLocation } from "react-router-dom";
//app
import { apiOfApiCreatorAtom, updateTopBarSubTitleAtom, selectedTopBarMenuAtom } from "../../state/atoms"
import GraphNodePanel from "./graph-panel/GraphNodePanel";
import Graph from "./graph-panel/Graph";
import { getApiByApiId } from "../../common/ApiHandler"
import { toJsonGraph } from "./graph-panel/GraphUtil"
import useBoolean from "../../common/hooks/useBoolean";

const initialElements = {
    nodes: [
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
    infoPanelBody: {
        color: '#e7e7e7',
        display: 'grid',
        gridTemplateColumns: '20% 80%',
        marginTop: '1rem',
    }
})

const ApiCreator = () => {
    const classes = useStyles();
    const location = useLocation();
    const [graphElements, setGraphElements] = useState([]);
    const [selectedApi, setSelectedApi] = useRecoilState(apiOfApiCreatorAtom);
    const [topBarSubTitle, setTopBarSubTitle] = useRecoilState(updateTopBarSubTitleAtom);
    const menu = useRecoilValue(selectedTopBarMenuAtom)
    const { value: isInfoPanelOpen, toggle: toggleInfoPanel, } = useBoolean(true);
    const { value: isUpdateInfoPanelOpen, toggle: toggleUpdateInfoPanel, } = useBoolean(false);

    useEffect(() => {
        let urlParams = new URLSearchParams(location.search);
        let apiId = Number(urlParams.get('ref'));
        let version = Number(urlParams.get('version'));
        let isApiSubscribed = true;
        getApiByApiId(apiId).then(res => {
            if (isApiSubscribed) {
                let { nodes, edges } = toJsonGraph(res.data.xml)
                let graphElements = (nodes.length > 0 || edges.length > 0) ? { nodes, edges } : initialElements;
                let d = { ...res.data, graphElements };
                setSelectedApi(d);
                // setTopBarSubTitle(
                //     <>&nbsp;&#x2022;&nbsp;{res.data.name}&nbsp;&#x2022;&nbsp;{`v${res.data.version}`}</>
                // );
            }
        })
            .catch(e => { })
            .finally(() => { })
        return () => {
            // cancel the subscription
            isApiSubscribed = false;
            setTopBarSubTitle(null);
            setSelectedApi(null);
        };
    }, [])

    useEffect(() => {
        if (menu.title === "Info") {
            // toggleInfoPanel();
        }
    }, [menu])

    return (
        <>
            <div className={classes.graphEditorRoot}>
                {selectedApi && selectedApi.graphElements.nodes.length > 0 && (
                    <ReactFlowProvider>
                        <GraphNodePanel />
                        <Graph />
                    </ReactFlowProvider>
                )}
            </div>
            {selectedApi &&
                <Panel
                    isOpen={isInfoPanelOpen}
                    onDismiss={toggleInfoPanel}
                    type={PanelType.smallFluid}
                    closeButtonAriaLabel="Close"
                    headerText="Service Information"
                >
                    <div className={classes.infoPanelBody}>
                        <div style={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            justifyContent: 'center',
                            height: '86vh'
                        }}>
                            <Persona
                                initialsColor={`var(--themePrimary)`}
                                text={selectedApi.name}
                                size={PersonaSize.size56}
                                hidePersonaDetails
                            />
                            <br />
                            <Text variant="xLarge">{selectedApi.name}</Text>
                            <Text variant="mediumPlus">{selectedApi.description}</Text>
                            <br />
                            <TooltipHost content="Edit service details" id={"ss_settings"}>
                                <IconButton
                                    aria-describedby={"ss_settings"}
                                    iconProps={{ iconName: 'Settings' }}
                                    title="Emoji"
                                    ariaLabel="Emoji"
                                    onClick={toggleUpdateInfoPanel}
                                />
                            </TooltipHost>
                        </div>
                        <div></div>
                    </div>
                </Panel>
            }

            {selectedApi &&
                <Panel
                    isOpen={isUpdateInfoPanelOpen}
                    onDismiss={toggleUpdateInfoPanel}
                    closeButtonAriaLabel="Close"
                    headerText="Edit Service Information"
                >
                    <div style={{ color: '#e7e7e7' }}>

                    </div>
                </Panel>
            }
        </>
    )
}

export default ApiCreator;
