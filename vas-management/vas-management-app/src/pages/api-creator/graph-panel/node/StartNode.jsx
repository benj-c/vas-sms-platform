//lib
import { memo } from 'react'
import { Handle } from 'react-flow-renderer';
import { createUseStyles } from "react-jss";

const useStyles = createUseStyles({
    handle: {
        background: '#555',
        padding: '0.15rem',
        '&:hover': {
            background: 'var(--themePrimary)'
        }
    },
    nodeInner: {
        fontSize: '1rem',
        display: 'flex',
        alignItems: 'center',
        padding: '0.3rem 1rem',
        margin: '0.5rem 0',
        background: 'rgba(255, 0, 0, 0.6)',
        borderRadius: '0.25rem',
        cursor: 'grab',
        border: '1px solid transparent',
        textTransform: 'uppercase',
        '&:hover': {
            background: 'rgba(255, 0, 0, 0.7)',
            border: '1px solid var(--themePrimary)',
        }
    }
})

const StartNode = ({ data }) => {
    const classes = useStyles();

    return (
        <>
            <Handle
                type="source"
                position="right"
                className={classes.handle}
                style={{ borderRadius: '0.25rem' }}
            />
            <div className={classes.nodeInner}>
                <span>{data.label}</span>
            </div>
        </>
    )
}

export default memo(StartNode);
