//lib
import { useState } from "react";
import { createUseStyles } from "react-jss";
//app
import AssignNodePropPanel from './property-panels/AssignNodePropPanel'

const CORE_NODES = [
    {
        id: 0,
        title: 'assign',
        type: 'customNode',
        icon: 'Variable',
        propsComponent: AssignNodePropPanel,
    },
    {
        id: 1,
        title: 'branch',
        type: 'customNode',
        icon: 'BranchFork2',
        propsComponent: () => <div>Switch</div>,
    },
    {
        id: 2,
        title: 'case',
        type: 'customNode',
        icon: 'Remote',
        propsComponent: () => <div>Case</div>,
    },
    {
        id: 3,
        title: 'default',
        type: 'customNode',
        icon: 'Remote',
        propsComponent: () => <div>Default</div>,
    },
]

const FUNCTIONS = [
    {
        id: 0,
        title: 'Function:Script',
        type: 'customNode',
        icon: 'JavaScriptLanguage',
        propsComponent: () => <div>Function:Script</div>,
    },
    {
        id: 1,
        title: 'Function:HTTP',
        type: 'customNode',
        icon: 'Remote',
        propsComponent: () => <div>Function:HTTP</div>,
    },
    {
        id: 2,
        title: 'Function:Invoke',
        type: 'customNode',
        icon: 'Remote',
        propsComponent: () => <div>Function:Invoke</div>,
    },
    {
        id: 3,
        title: 'Function:Email',
        type: 'customNode',
        icon: 'Mail',
        propsComponent: () => <div>Function:Email</div>,
    },
]

const useStyles = createUseStyles({
    nodePanel: {
        height: '100%',
        overflowY: 'auto',
        '& ul': {
            paddingRight: '0.25rem',
            marginBottom: '1rem',
            '& h3': {
                textTransform: 'uppercase',
                marginBottom: '0.5rem',
                fontSize: '0.75rem',
                fontWeight: 600,
                borderBottom: '1px solid var(--themePrimary)'
            },
            '& li': {
                padding: '0.3rem',
                margin: '0.5rem 0',
                background: 'rgba(255, 255, 255, 0.1)',
                borderRadius: '0.25rem',
                fontSize: '1rem',
                display: 'flex',
                alignItems: 'center',
                cursor: 'move',
                border: '1px solid transparent',
                '& i': {
                    padding: '0 0.25rem',
                    marginRight: '0.5rem',
                },
                '& span': {
                    borderLeft: '1px solid rgba(255, 255, 255, 0.4)',
                    paddingLeft: '0.5rem',
                },
                '&:hover': {
                    background: 'rgba(255, 255, 255, 0.15)',
                    border: '1px solid var(--themePrimary)',
                }
            }
        }
    },
})

export const getPropComponent = name => {
    if (name) {
        console.log(name)
        if (name == 'func') {
            for (let i = 0; i < FUNCTIONS.length; i++) {
                if (FUNCTIONS[i].title == name) {
                    let c = FUNCTIONS[i].propsComponent;
                    return c;
                }
            }
        } else {
            for (let i = 0; i < CORE_NODES.length; i++) {
                if (CORE_NODES[i].title == name) {
                    let c = CORE_NODES[i].propsComponent;
                    return c;
                }
            }
        }
    }
}

const GraphNodePanel = () => {
    const classes = useStyles();
    const [functionNodes, setFunctionNodes] = useState(FUNCTIONS);

    const onDragStart = (event, nodeType) => {
        event.dataTransfer.setData('application/reactflow', JSON.stringify(nodeType));
        event.dataTransfer.effectAllowed = 'move';
    }

    return (
        <aside className={classes.nodePanel}>
            <ul>
                <h3>core</h3>
                {CORE_NODES.map(node => (
                    <li key={node.id} onDragStart={event => onDragStart(event, node)} draggable>
                        <i className={`ms-Icon ms-Icon--${node.icon}`} aria-hidden="true"></i>
                        <span style={{textTransform: 'capitalize'}}>
                            {node.title}
                        </span>
                    </li>
                ))}
            </ul>
            <ul>
                <h3>functions</h3>
                {functionNodes.map(node => (
                    <li key={node.id} onDragStart={event => onDragStart(event, node)} draggable>
                        <i className={`ms-Icon ms-Icon--${node.icon}`} aria-hidden="true"></i>
                        <span style={{textTransform: 'capitalize'}}>
                            {node.title.split(':')[1]}
                        </span>
                    </li>
                ))}
            </ul>
        </aside>
    )
}

export default GraphNodePanel;
