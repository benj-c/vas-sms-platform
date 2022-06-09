//lib
import { memo } from 'react'
import { createUseStyles } from "react-jss";
//app


const useStyles = createUseStyles({
    actionPanel: {
        zIndex: '999',
        position: 'absolute',
        right: '0',
        top: '0',
        '& ul': {
            '& li': {
                margin: '0.5rem',
                '& i': {
                    padding: '0.5rem',
                    background: 'rgba(0, 153, 255, 0.3)',
                    cursor: 'pointer',
                    borderRadius: '100%'
                }
            }
        }
    }
})

const ActionPanel = ({ onGraphSave }) => {
    const classes = useStyles();

    return (
        <div className={classes.actionPanel}>
            <ul>
                <li><i className="ms-Icon ms-Icon--Play"></i></li>
                <li onClick={onGraphSave}><i className="ms-Icon ms-Icon--Save"></i></li>
            </ul>
        </div>
    )
}

export default memo(ActionPanel);
