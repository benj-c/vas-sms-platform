//lib
import { getSmoothStepPath, getEdgeCenter, getMarkerEnd } from 'react-flow-renderer'
import { useRecoilState } from 'recoil';
import { createUseStyles } from "react-jss";
//app
import { removedEdgeAtom } from '../../../../state/atoms'

const useStyles = createUseStyles({
    edgebutton: {
        background: '#eee',
        border: '1px solid #fff',
        borderRadius: '50%',
        cursor: 'pointer',
        fontSize: '7px',
        fontWeight: 'bold',
        height: '20px',
        width: '20px',
        '&:hover': {
            color: '#fff',
            background: 'rgb(255, 0, 0)',
        },
    },
})

const foreignObjectSize = 40;

const ButtonEdge = ({ id, sourceX, sourceY, targetX, targetY, sourcePosition, targetPosition, style = {}, data, arrowHeadType, markerEndId }) => {
    const edgePath = getSmoothStepPath ({
        sourceX,
        sourceY,
        sourcePosition,
        targetX,
        targetY,
        targetPosition,
    });
    const classes = useStyles();
    const markerEnd = getMarkerEnd(arrowHeadType, markerEndId);
    const [edgeCenterX, edgeCenterY] = getEdgeCenter({
        sourceX,
        sourceY,
        targetX,
        targetY,
    });
    const [removedEdge, setRemovedEdge] = useRecoilState(removedEdgeAtom);

    const onEdgeClick = (evt, id) => {
        evt.stopPropagation();
        if (window.confirm("You are going to remove this connection, are you sure ?")) {
            setRemovedEdge(id);
        }
    }

    return (
        <>
            <path
                id={id}
                style={{ strokeWidth: 3 }}
                className="react-flow__edge-path"
                d={edgePath}
                markerEnd={markerEnd}
            />
            <foreignObject
                width={foreignObjectSize}
                height={foreignObjectSize}
                x={edgeCenterX - foreignObjectSize / 2}
                y={edgeCenterY - foreignObjectSize / 2}
                className="edgebutton-foreignobject"
                requiredExtensions="http://www.w3.org/1999/xhtml"
            >
                <body>
                    <button
                        className={classes.edgebutton}
                        onClick={(event) => onEdgeClick(event, id)}
                        title="Remove this edge"
                    >
                        &#9587;
                    </button>
                </body>
            </foreignObject>
        </>
    );
}

export default ButtonEdge;
