import { DefaultPalette, Spinner, Stack, Text } from "@fluentui/react"
import { useEffect } from "react";
import { useState } from "react";
import { createUseStyles } from "react-jss";
import { getHealthStats } from "../../common/ApiHandler";

const useStyles = createUseStyles({
    itemStyles: {
        alignItems: 'center',
        background: DefaultPalette.themePrimary,
        color: DefaultPalette.white,
        display: 'flex',
        padding: '1rem 0',
        justifyContent: 'center',
        flexDirection: 'column',
        borderRadius: '0.25rem',
        width: 150,
    }
})

const ServiceStatsPanel = () => {
    const classes = useStyles();
    const [serviceComponents, setServiceComponents] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        // let interval = setInterval(() => loadStats(), (6000))
        // return () => clearInterval(interval)
        loadStats()
    }, [])

    const loadStats = () => {
        setLoading(true);
        getHealthStats().then(res => setServiceComponents(res.data))
            .catch(e => { })
            .finally(() => {
                setLoading(false);
            })
    }

    return (
        <>
            <Stack>
                <span style={{ marginBottom: '0.75rem' }}>Service Status</span>
                <Stack horizontal wrap tokens={{
                    childrenGap: 10,
                }}>
                    {serviceComponents.length > 0 && !loading && serviceComponents.map((svr, key) => (
                        <span className={classes.itemStyles} key={key}>
                            <Text variant="medium" style={{ textTransform: 'capitalize' }}>{svr.name}</Text>
                            <Text variant="mediumPlus" style={{ fontWeight: 600, margin: '0.5rem 0' }}>{svr.status ? 'UP' : 'DOWN'}</Text>
                            <Text variant="small">{svr.status ? 'All Good' : 'Action Needed'}</Text>
                        </span>
                    ))}
                </Stack>

            </Stack>

            {loading && (
                <Spinner label="We're loading statistics, please wait" />
            )}
        </>
    )
}

export default ServiceStatsPanel;
