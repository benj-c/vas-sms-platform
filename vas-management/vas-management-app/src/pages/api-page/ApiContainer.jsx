//lib
import { useState, useEffect, Suspense } from "react";
import { Spinner } from "@fluentui/react";
import { createUseStyles } from "react-jss";
import { useHistory } from "react-router";
//app
import ApiItem from "./ApiItem";
import { getAllApis } from "../../common/ApiHandler"

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

const ApiContainer = () => {
    const classes = useStyles();
    const history = useHistory();
    const [apis, setApis] = useState([]);
    const [loadingApis, setIsApisLoading] = useState(false);

    const ApiItemLoadingFallback = () => <span>Loading...</span>

    useEffect(() => {
        let subscription = true;
        if (subscription) {
            setIsApisLoading(true);
            getAllApis()
                .then(res => {
                    setApis(res.data)
                })
                .catch(e => { })
                .finally(() => {
                    setIsApisLoading(false);
                });
        }
        return () => {
            subscription = false;
        }
    }, [])

    const selectApi = f => {
        history.push(`/api-creator?ref=${f.id}&commit=${f.commitId}`)
    }

    return (
        <>
            <h2 className={classes.sectionHeader}>Browse APIs</h2>
            {loadingApis ?
                <Spinner label="Please wait, we're loading APIs..." /> :
                <div className={classes.flowContainer}>
                    {apis.length > 0 && apis.map((v, i) => (
                        <Suspense fallback={<ApiItemLoadingFallback />} key={i}>
                            <ApiItem api={v} onSelect={selectApi} />
                        </Suspense>
                    ))}
                </div>
            }
        </>
    )
}

export default ApiContainer;
