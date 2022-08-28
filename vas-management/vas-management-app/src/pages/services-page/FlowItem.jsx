//lib
import { Persona, PersonaPresence, PersonaSize } from "@fluentui/react";
import { createUseStyles } from "react-jss";
//app

const useFlowItemStyles = createUseStyles({
    flowItem: {
        padding: '1rem',
        background: 'rgba(255, 255, 255, 0.05)',
        borderRadius: '0.3rem',
        height: '5.8rem',
        cursor: 'pointer',
        '&:hover': {
            background: 'rgba(255, 255, 255, 0.1)',
        },
        '& p': {
            fontSize: '1rem',
            color: '#e2e2e2'
        },
    },
    flowItemHeader: {
        marginBottom: '0.75rem',
        '& span': {
            color: "#fff"
        }
    }
})

const FlowItem = ({ flow, onSelect }) => {
    const classes = useFlowItemStyles();

    return (
        <>
            {flow && (
                <article className={classes.flowItem} onClick={() => onSelect(flow)}>
                    <div className={classes.flowItemHeader}>
                        <Persona
                            initialsColor={`var(--themePrimary)`}
                            size={PersonaSize.size40}
                            text={flow.name}
                            secondaryText={flow.active ? 'Active' : 'Inactive'}
                            presence={flow.active ? PersonaPresence.online : PersonaPresence.busy}
                        />
                    </div>
                    <span>{flow.description}</span>
                </article>
            )}
        </>
    )
}

export default FlowItem;
