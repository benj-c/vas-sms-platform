//lib
import { DefaultPalette } from '@fluentui/react';
import { memo } from 'react'
import { createUseStyles } from "react-jss";
//app


const useStyles = createUseStyles({
    actionPanel: {
        zIndex: '999',
        position: 'absolute',
        right: '50%',
        bottom: '0.5rem',
        '& ul': {
            '& li': {
                display: 'inline',
                background: DefaultPalette.themePrimary,
                margin: '0.25rem',
                padding: '0.15rem 0.25rem',
                cursor: 'pointer',
                borderRadius: '0.25rem',
            }
        }
    }
})

const ActionPanel = ({ onGraphSave, onBuild, onDiscard }) => {
    const classes = useStyles();

    return (
        <div className={classes.actionPanel}>
            <ul>
                {/* <li><i className="ms-Icon ms-Icon--Play"></i></li>
                <li onClick={onBuild}><i className="ms-Icon ms-Icon--Rocket"></i></li>
                <li onClick={onGraphSave}><i className="ms-Icon ms-Icon--Save"></i></li> */}
                <li onClick={onBuild}>Build</li>
                <li onClick={onGraphSave}>Save</li>
                <li onClick={onDiscard}>Discard</li>
            </ul>
        </div>
    )
}

export default memo(ActionPanel);
