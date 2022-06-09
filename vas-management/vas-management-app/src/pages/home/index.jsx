import React, { useEffect, useState, lazy } from "react";
import { withRouter } from "react-router";
import { getFlows } from "../../common/HttpClient";
import { createUseStyles } from "react-jss";
import { ActivityItem } from "@fluentui/react";

const useStyles = createUseStyles({
    pageRoot: {
        height: '200vh',
        padding: '0.5rem',
    },
    pageHeader: {
        position: 'relative',
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
    },
    pageTitle: {
        fontSize: '1.5rem',
        lineHeight: '2rem',
    },
})

const Home = props => {
    const classes = useStyles();
    const [flows, setFlows] = useState([]);
    const [activities, setActivities] = useState([
        {
            key: 1,
            activityDescription: [
                <a key={1} style={{fontWeight: 'bold'}} >
                    Jack Howden
                </a>,
                <span key={2}> renamed </span>,
                <span key={3}>
                    DocumentTitle.docx
                </span>,
            ],
            activityPersonas: [{ imageInitials: 'JH', text: 'Jack Howden' }],
            comments: 'Hello, this is the text of my basic comment!',
            timeStamp: '23m ago',
        }
    ])
    const [isFlowsLoading, toggleFlowLoadingState] = useState(false);

    useEffect(() => {
        // toggleFlowLoadingState(!isFlowsLoading);
        // getFlows().then(res => {
        //     setFlows(res.data.flows);
        // })
        //     .catch(e => console.log(e))
        //     .finally(() => {
        //         toggleFlowLoadingState(!isFlowsLoading);
        //     })
    }, [])

    // const gotoFlow = flow => {
    //     props.history.push(`/builder?ref=${flow.id}`);
    // }

    return (
        <div className={classes.pageRoot}>
            <div className={classes.pageHeader}>
                {/* <h1 className={classes.pageTitle}>Home</h1> */}

            </div>
            <div className="ms-Grid" dir="ltr">
                <div className="ms-Grid-row">
                    <div className="ms-Grid-col ms-sm6 ms-md4 ms-lg9">A</div>
                    <div className="ms-Grid-col ms-sm6 ms-md8 ms-lg3">
                        Activity Log
                        {activities.map((item) => (
                            <ActivityItem {...item} key={item.key} />
                        ))}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default withRouter(Home);
