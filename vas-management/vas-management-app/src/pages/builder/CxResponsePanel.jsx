//lib
import { useEffect, useState } from "react";
import { useRecoilValue } from "recoil";
import { DetailsList, DetailsListLayoutMode, IconButton, Panel, SelectionMode } from "@fluentui/react";
import { useBoolean } from "@fluentui/react-hooks";
import { createUseStyles } from "react-jss";
//app
import { selectedActionAtom } from "../../state/atoms"
import { getCxResponsesByApiId } from "../../common/ApiHandler"
import UpdateCxResponse from "../../common/forms/UpdateCxResponseForm";
import CreateCxResponseForm from "../../common/forms/CreateCxResponseForm";

const useStyles = createUseStyles({
    smss: {
        marginTop: '1.5rem',
        position: 'relative',
        borderRadius: '0.35rem',
        '& h2': {
            color: 'var(--themePrimary)',
            fontSize: '1rem',
            marginRight: '1rem',
            borderBottom: '1px solid rgba(255, 255, 255, 0.1)',
            marginBottom: '1rem',
        },
    },
})

const CxResponsePanel = () => {
    const _columns = [
        {
            key: 'resCode',
            name: 'Response Code',
            fieldName: 'resCode',
            minWidth: 100,
            maxWidth: 100,
            isResizable: true
        },
        {
            key: 'resDesc',
            name: 'Response Description',
            fieldName: 'resDesc',
            minWidth: 100,
            maxWidth: 200,
            isResizable: true
        },
        {
            key: 'sms',
            name: 'SMS',
            fieldName: 'sms',
            minWidth: 100,
            maxWidth: 600,
            isResizable: true
        },
        {
            key: 'update',
            name: 'Edit',
            fieldName: 'update',
            minWidth: 30,
            maxWidth: 30,
            onRender: (item) => {
                return (
                    <IconButton
                        iconProps={{ iconName: 'Settings' }}
                        title="Add new SMS"
                        ariaLabel="Add"
                        onClick={() => {
                            setSelectedCxResponse(item);
                            showPanel(true)
                        }}
                    />
                )
            }
        },
    ];
    const classes = useStyles();
    const [responses, setResponses] = useState([]);
    const [isOpen, { setTrue: openPanel, setFalse: dismissPanel }] = useBoolean(false);
    const [isUpdating, setIsUpdating] = useState(false);
    const [selectedCxResponse, setSelectedCxResponse] = useState(null);
    const action = useRecoilValue(selectedActionAtom);

    useEffect(() => {
        if (action) {
            loadCxResponses(action.apiId);
        }
    }, [action])

    const loadCxResponses = id => {
        console.log(id)
        getCxResponsesByApiId(id)
            .then(res => {
                setResponses(res.data);
            })
            .catch(e => { })
            .finally(() => { })
    }

    const onUpdate = () => {
        loadCxResponses(action.apiId);
        setSelectedCxResponse(null);
    }

    const showPanel = e => {
        openPanel()
        setIsUpdating(e)
    }

    return (
        <div className={classes.smss}>
            {action && (
                <>
                    <h2>
                        SMS Responses
                        <IconButton
                            style={{ float: 'right', bottom: '0.6rem' }}
                            iconProps={{ iconName: 'Add' }}
                            title="Add new SMS"
                            ariaLabel="Add"
                            onClick={() => showPanel(false)}
                        />
                    </h2>
                    <div style={{ marginRight: '1rem' }}>
                        <DetailsList
                            compact={true}
                            items={responses}
                            columns={_columns}
                            isHeaderVisible={true}
                            setKey="none"
                            selectionMode={SelectionMode.none}
                            layoutMode={DetailsListLayoutMode.justified}
                        />
                    </div>

                    <Panel
                        isOpen={isOpen}
                        onDismiss={dismissPanel}
                        headerText={isUpdating ? `SMS response settings` : 'Add new response SMS'}
                    >
                        {isUpdating ?
                            <UpdateCxResponse {...selectedCxResponse} dismissPanel={dismissPanel} onUpdate={onUpdate} />
                            :
                            <CreateCxResponseForm dismissPanel={dismissPanel} onUpdate={onUpdate} />
                        }

                    </Panel>
                </>
            )}
        </div>
    )
}

export default CxResponsePanel;
