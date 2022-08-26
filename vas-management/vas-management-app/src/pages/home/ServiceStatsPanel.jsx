import { DefaultPalette, Stack, Text } from "@fluentui/react"
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

    useEffect(() => {
        let interval = setInterval(() => loadStats(), (3000))
        return () => clearInterval(interval)
    }, [])

    const loadStats = () => {
        getHealthStats().then(res => setServiceComponents(res.data))
            .catch(e => { })
            .finally(() => { })
    }

    return (
        <Stack>
            <span style={{ marginBottom: '0.75rem' }}>Service Status</span>
            <Stack horizontal tokens={{
                childrenGap: 10,
            }}>
                {serviceComponents.length > 0 && serviceComponents.map((svr, key) => (
                    <span className={classes.itemStyles} key={key}>
                        <Text variant="medium" style={{textTransform: 'capitalize'}}>{svr.name}</Text>
                        <Text variant="mediumPlus" style={{fontWeight: 600, margin: '0.5rem 0'}}>{svr.status ? 'UP' : 'DOWN'}</Text>
                        <Text variant="small">{svr.status ? 'All Good' : 'Action Needed'}</Text>
                    </span>
                ))}
            </Stack>
        </Stack>
    )
}

export default ServiceStatsPanel;
