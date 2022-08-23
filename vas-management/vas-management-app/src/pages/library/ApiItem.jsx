import { Persona, PersonaPresence, PersonaSize } from "@fluentui/react";
import { createUseStyles } from "react-jss";

const useStyles = createUseStyles({
    apiItem: {
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
    apiItemHeader: {
        marginBottom: '0.75rem',
        '& span': {
            color: "#fff"
        }
    }
})

const ApiItem = ({ api, onSelect }) => {
    const classes = useStyles();

    return (
        <>
            {api && (
                <article className={classes.apiItem} onClick={() => onSelect(api)}>
                    <div className={classes.apiItemHeader}>
                    <Persona
                            initialsColor={`var(--themePrimary)`}
                            size={PersonaSize.size40}
                            text={api.name}
                            secondaryText={`Version: ${api.version}`}
                        />
                    </div>
                    <span>{api.description}</span>
                </article>
            )}
        </>
    )
}

export default ApiItem;
