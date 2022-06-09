//lib
import React, { useEffect, useState, lazy, Suspense } from "react";
import { createUseStyles } from "react-jss";
import { useLocation } from "react-router-dom"
import { useRecoilState } from "recoil";
//app
import KeywordPanel from "./KeywordPanel";
import { selectedServiceAtom } from "../../state/atoms"
import CxResponsePanel from "./CxResponsePanel";

const InfoPanel = lazy(() => import("./InfoPanel"))
const ActionsPanel = lazy(() => import("./ActionsPanel"))

const useStyles = createUseStyles({
    '@keyframes animateUp': {
        from: {
            transform: 'translateY(2rem)'
        },
        to: {
            transform: 'translateY(0)',
        }
    },
    builderContainer: {
        padding: '0.5rem 1rem',
        display: 'grid',
        gridTemplateColumns: '25% 75%',
        gap: '1rem',
    },
    propsContainer: {
        height: '90vh',
        overflowY: 'auto',
    }
})

const Builder = () => {
    const classes = useStyles();
    const location = useLocation();
    const [serviceId, setService] = useState(null);
    const [recoilService, setSelectedService] = useRecoilState(selectedServiceAtom);

    useEffect(() => {
        //set visibility of side bar nav item
        // setSideBarNav('Builder');
        let serviceId = Number(new URLSearchParams(location.search).get('ref'));
        setService(serviceId);
        setSelectedService(serviceId)
    }, [serviceId])

    const renderFallback = () => <div>Loading</div>

    return (
        <div className={classes.builderContainer}>
            {serviceId && (
                <Suspense fallback={renderFallback()}>
                    <div>
                        <InfoPanel serviceId={serviceId} />
                    </div>
                    <div className={classes.propsContainer}>
                        <ActionsPanel serviceId={serviceId} />
                        <KeywordPanel />
                        <CxResponsePanel />
                    </div>
                </Suspense>
            )}
        </div>
    )
}

export default Builder;
