import { useEffect } from "react";
import { createUseStyles } from "react-jss";
import { getRandomColor } from "../../common/AppUtils"

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
        display: 'grid',
        gridTemplateColumns: '20% 80%',
        marginBottom: '0.75rem',
        justifyContent: 'center',
        alignItems: 'center',
        '& span': {
            padding: '0.25rem',
            fontSize: '1.1rem',
            borderRadius: '0.2rem',
            height: '2rem',
            width: '2rem',
            lineHeight: '2rem',
            textAlign: 'center',
            textTransform: 'uppercase',
            // background: getRandomColor(),
            background: 'var(--themePrimary)',
        },
        '& h4': {
            fontSize: '1.1rem',
            fontWeight: '500',
            textTransform: 'capitalize',
            color: '#cccccc',
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
                        <span>{api.name?.substring(0, 2)}</span>
                        <div>
                            <h4>{api.name}</h4>
                            <small>Version: {api.version}</small>
                        </div>
                    </div>
                    <span>{api.description}</span>
                </article>
            )}
        </>
    )
}

export default ApiItem;
