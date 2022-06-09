//lib
import { useState, useEffect, Suspense } from "react";
import { createUseStyles } from "react-jss";
import { useHistory } from "react-router";
//app
import FlowItem from "./FlowItem";
import { getServices } from "../../common/ApiHandler"

const useStyles = createUseStyles({
    flowContainerRoot: {
        height: '80vh',
    },
    flowContainer: {
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
        gap: '0.75rem',
        overflowY: 'auto',
    },
    sectionHeader: {
        fontSize: '1.25rem',
        marginBottom: '1rem',
    },
})

const FlowContainer = () => {
    const classes = useStyles();
    const history = useHistory();
    const [flows, setFlows] = useState([]);
    const [loadingFlows, setIsFlowsLoading] = useState(false);

    const FlowItemLoadingFallback = () => <span>Loading...</span>

    useEffect(() => {
        setIsFlowsLoading(true);
        getServices()
            .then(res => {
                setFlows(res.data)
            })
            .catch(e => { })
            .finally(() => {
                setIsFlowsLoading(false);
            });
    }, [])

    const selectFlow = f => {
        history.push(`/builder?ref=${f.id}`)
    }

    return (
        <>
            <h2 className={classes.sectionHeader}>Discover Services</h2>
            <div className={classes.flowContainer}>
                {flows.length > 0 && flows.map((v, i) => (
                    <Suspense fallback={<FlowItemLoadingFallback />} key={i}>
                        <FlowItem flow={v} onSelect={selectFlow} />
                    </Suspense>
                ))}
            </div>
        </>
    )
}

export default FlowContainer;
