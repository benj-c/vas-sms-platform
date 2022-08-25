//lib
import { memo } from 'react'
import { Handle } from 'react-flow-renderer';
import { createUseStyles } from "react-jss";
import { useRecoilState } from 'recoil'
//app

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
        flexDirection: 'row',
        position: 'relative',
        padding: '0.3rem',
        margin: '0.5rem 0',
        background: 'rgba(255, 255, 255, 0.1)',
        borderRadius: '0.25rem',
        cursor: 'move',
        border: '1px solid transparent',
        '&:hover': {
            background: 'rgba(255, 255, 255, 0.15)',
            border: '1px solid var(--themePrimary)',
        },
        '& i': {
            padding: '0 0.5rem',
            marginRight: '0.2rem',
        },
        '& span': {
            borderLeft: '1px solid #e7e7e7',
            paddingLeft: '0.5rem',
            paddingRight: '0.5rem',
            textTransform: 'capitalize',
        },
    },
})

const CustomNode = ({ id, data }) => {
    const classes = useStyles();

    return (
        <>
            <div className={classes.nodeInner}>
                <i className={`ms-Icon ms-Icon--${data.icon}`} aria-hidden="true"></i>
                <span>{data.label.includes(':') ? data.label.split(':')[1] : data.label}</span>
                {/* <br />
                <small>{id}</small> */}
            </div>
            <Handle
                id="target"
                type="target"
                position="left"
                className={classes.handle}
                style={{ borderRadius: '0.25rem' }}
            />
            <Handle
                id="source"
                type="source"
                position="right"
                className={classes.handle}
                style={{ borderRadius: '0.25rem' }}
            />
        </>
    )
}

export default memo(CustomNode);
