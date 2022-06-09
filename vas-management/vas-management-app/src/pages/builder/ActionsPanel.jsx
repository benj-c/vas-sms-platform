//lib
import { ActionButton, IconButton, Panel } from "@fluentui/react";
import { useBoolean } from "@fluentui/react-hooks";
import { useEffect, useState } from "react";
import { createUseStyles } from "react-jss";
import { useRecoilState } from "recoil";
//app
import { getActionsByServiceId } from "../../common/ApiHandler";
import { selectedActionAtom } from "../../state/atoms"
import UpdateActionForm from "../../common/forms/UpdateActionForm"
import AddActionForm from "../../common/forms/AddActionForm";

const useStyles = createUseStyles({
    actions: {
        marginTop: '0.5rem',
        position: 'relative',
        borderRadius: '0.35rem',
        '& h2': {
            color: 'var(--themePrimary)',
            fontSize: '1rem',
            marginRight: '1rem',
            borderBottom: '1px solid rgba(255, 255, 255, 0.1)'
        },
        '& ul': {
            display: "grid",
            gridTemplateColumns: 'repeat(4, 1fr)',
            gap: '0.75rem',
            overflowY: 'auto',
            marginTop: '1rem',
            marginRight: '1rem',
            '& li': {
                padding: '1rem',
                borderRadius: '0.35rem',
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                '&:hover i': {
                    visibility: 'visible',
                },
                '& i': {
                    visibility: 'hidden',
                    marginLeft: 'auto',
                    '&:hover': {
                        color: 'var(--themePrimary)'
                    },
                }
            },
        },
    },
})

const ActionsPanel = ({ serviceId }) => {
    const classes = useStyles();
    const [isOpen, { setTrue: openPanel, setFalse: dismissPanel }] = useBoolean(false);
    const [selectedAction, setSelectedAction] = useState(null);
    const [actions, setActions] = useState([]);
    const [selectedActionThisAtom, setSelectedActionAtom] = useRecoilState(selectedActionAtom);
    const [isUpdating, setIsUpdating] = useState(false);

    useEffect(() => {
        loadActions(serviceId);
    }, [serviceId])

    const loadActions = id => {
        getActionsByServiceId(id)
            .then(res => {
                setActions(res.data);
                onActionSelect(res.data[0] || null)
            })
            .catch(e => { })
            .finally(() => { });
    }

    const onActionSelect = item => {
        setSelectedAction(item);
        setSelectedActionAtom(item);
    }

    const onUpdate = () => {
        loadActions(serviceId);
    }

    const showPanel = e => {
        openPanel()
        setIsUpdating(e)
    }

    return (
        <>
            <div className={classes.actions}>
                <h2>
                    Actions
                    <IconButton
                        style={{ float: 'right', bottom: '0.6rem' }}
                        iconProps={{ iconName: 'Add' }}
                        title="Add new action"
                        ariaLabel="Add"
                        onClick={() => showPanel(false)}
                    />
                </h2>
                <ul>
                    {actions.length > 0 && actions.map((act, i) => (
                        <li
                            style={{
                                background: (selectedAction && act.id === selectedAction.id) ? 'rgba(255, 255, 255, 0.1)' : 'rgba(255, 255, 255, 0.03)',
                            }}
                            key={act.id}
                            onClick={() => onActionSelect(act)}>
                            {act.description}
                            <i className="ms-Icon ms-Icon--Settings" aria-hidden="true" onClick={() => showPanel(true)}></i>
                        </li>
                    ))}
                </ul>
            </div>
            <Panel
                isOpen={isOpen}
                onDismiss={dismissPanel}
                headerText={isUpdating ? `Service action settings` : 'Add new service action'}
                closeButtonAriaLabel="Close"
                isFooterAtBottom={true}
            >
                {isUpdating ?
                    <UpdateActionForm
                        {...selectedAction}
                        dismissPanel={dismissPanel}
                        onUpdate={onUpdate}
                    /> :
                    <AddActionForm
                        serviceId={serviceId}
                        dismissPanel={dismissPanel}
                        onUpdate={onUpdate} />
                }
            </Panel>
        </>
    )
}

export default ActionsPanel;
