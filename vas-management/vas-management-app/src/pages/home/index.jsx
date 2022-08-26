import React, { useEffect, useState, lazy } from "react";
import { createUseStyles } from "react-jss";
import { ActivityItem, DefaultPalette } from "@fluentui/react";

import { getFlows } from "../../common/HttpClient";
import ServiceStatsPanel from "./ServiceStatsPanel";
import UserActionLog from "./UserActionLog";

const useStyles = createUseStyles({
    pageRoot: {
        height: '200vh',
        padding: '1rem',
    },
    sectionHeader: {
        fontSize: '1.25rem',
        marginBottom: '1rem',
    },
})

const Home = props => {
    const classes = useStyles();
    
    return (
        <div className={classes.pageRoot}>
            <div className="ms-Grid" dir="ltr">
                <div className="ms-Grid-row">
                    <div className="ms-Grid-col ms-sm6 ms-md4 ms-lg9">
                        <h2 className={classes.sectionHeader}>Dashboard</h2>
                        <ServiceStatsPanel />
                    </div>
                    <div className="ms-Grid-col ms-sm6 ms-md8 ms-lg3">
                        <UserActionLog />
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Home;
