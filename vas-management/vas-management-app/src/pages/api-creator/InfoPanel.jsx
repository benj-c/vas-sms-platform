import { useEffect, useState } from "react";
import { DefaultButton, DetailsList, DetailsListLayoutMode, FocusTrapCallout, IconButton, Panel, Persona, PersonaSize, PrimaryButton, SelectionMode, Stack, Text, TooltipHost, DefaultPalette } from "@fluentui/react";
import { createUseStyles } from "react-jss";
import { useRecoilState, useRecoilValue } from "recoil";
import { useHistory } from "react-router-dom";

import useBoolean from "../../common/hooks/useBoolean";
import { apiOfApiCreatorAtom, apiUpdateEventAtom, selectedTopBarMenuAtom } from "../../state/atoms"
import UpdateApiForm from "../../common/forms/UpdateApiForm"
import { getApiVersionsByApiId, deployApi } from "../../common/ApiHandler"

const useStyles = createUseStyles({
    infoPanelBody: {
        color: '#e7e7e7',
        display: 'grid',
        gridTemplateColumns: '20% 80%',
        height: '100%',
        padding: '1rem',
        borderRadius: '0.35rem',
    },
    apiList: {
        margin: '1rem 0',
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
        gap: '0.75rem',
        '& li': {
            padding: '0.75rem',
            background: 'rgba(255, 255, 255, 0.03)',
            borderRadius: '0.25rem',
            display: 'flex',
            flexDirection: 'column',
            // margin: '1rem 0',
            '&:hover': {
                background: 'rgba(255, 255, 255, 0.05)',
            }
        }
    },
    infoStatItem: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '0.25rem',
        background: 'rgba(255, 255, 255, 0.1)',
        width: '15%',
        height: '100%',
        borderRadius: '0.25rem',
    }
})

const InfoPanel = () => {
    const classes = useStyles();
    const graphApi = useRecoilValue(apiOfApiCreatorAtom)
    const { value: isUpdateInfoPanelOpen, toggle: toggleUpdateInfoPanel, } = useBoolean(false);

    const ApiVersionsPanel = () => {
        const history = useHistory();
        const { value: isCalloutVisible, toggle: toggleIsCalloutVisible, } = useBoolean(false);
        const [selectedApiVersion, setSelectedApiVersion] = useState(null);

        const onMoreClick = (item) => {
            setSelectedApiVersion(item);
            toggleIsCalloutVisible()
        }

        const _columns = [
            {
                key: 'version',
                name: 'Version',
                fieldName: 'version',
                minWidth: 50,
                maxWidth: 50,
                isResizable: false,
            },
            {
                key: 'commitMessage',
                name: 'Commit Message',
                fieldName: 'commitMessage',
                minWidth: 400,
                maxWidth: 400,
                isResizable: false,
            },
            {
                key: 'commitId',
                name: 'Commit ID',
                fieldName: 'commitId',
                minWidth: 300,
                maxWidth: 300,
                isResizable: true,
            },
            {
                key: 'isActive',
                name: 'Status',
                fieldName: 'isActive',
                minWidth: 50,
                maxWidth: 50,
                isResizable: false,
                onRender: (item) => {
                    return (
                        <span style={{
                            color: item.isActive ? 'var(--themePrimary)' : '',
                            fontWeight: item.isActive ? 600 : 200
                        }}>
                            {item.isActive ? 'Active' : 'Inactive'}
                        </span>
                    )
                }
            },
            {
                key: 'action',
                name: '',
                minWidth: 50,
                maxWidth: 50,
                isResizable: false,
                onRender: (item) => {
                    return (
                        <IconButton id={`btn_${item.id}`} iconProps={{ iconName: 'Settings' }} aria-label="More" onClick={() => onMoreClick(item)} />
                    )
                }
            },
        ];
        const [apis, setApis] = useState([]);
        const [apisLoading, setApisLoading] = useState(false);
        const apiUpdateEvent = useRecoilValue(apiUpdateEventAtom)


        useEffect(() => {
            let subs = true;
            if (subs) {
                loadApis();
            }
            return () => {
                subs = false;
            }
        }, [])

        useEffect(() => {
            if (apiUpdateEvent) {
                loadApis();
            }
        }, [apiUpdateEvent])

        const loadApis = () => {
            setApisLoading(true);
            getApiVersionsByApiId(graphApi.id).then(res => {
                setApis(res.data);
            }).catch(e => {

            }).finally(() => {
                setApisLoading(true);
            })
        }

        const onDeploy = () => {
            deployApi(selectedApiVersion.apiId, selectedApiVersion.commitId).then(res => {
                toggleIsCalloutVisible();
                loadApis();
                history.push(`/api-creator?ref=${selectedApiVersion.apiId}&commit=${selectedApiVersion.commitId}`)
            }).catch(e => {

            }).finally(() => {
                setApisLoading(true);
            })
        }

        const onCheckout = () => {
            history.push(`/api-creator?ref=${selectedApiVersion.apiId}&commit=${selectedApiVersion.commitId}`)
        }

        return (
            <div style={{
                height: '72vh',
                overflowY: 'auto',
            }}>
                <Text variant="medium" style={{ borderBottom: '1px solid var(--themePrimary)', paddingBottom: '0.25rem' }}>API Change History</Text>
                <div style={{ marginTop: '1rem' }}>
                    <DetailsList
                        compact={true}
                        items={apis}
                        columns={_columns}
                        isHeaderVisible={true}
                        setKey="none"
                        selectionMode={SelectionMode.none}
                        layoutMode={DetailsListLayoutMode.justified}
                    />
                </div>

                {isCalloutVisible && (
                    <FocusTrapCallout
                        role="alertdialog"
                        gapSpace={0}
                        target={`#btn_${selectedApiVersion.id}`}
                        onDismiss={toggleIsCalloutVisible}
                    >
                        <div style={{ color: '#e7e7e7', padding: '1rem', border: '1px solid var(--themePrimary)' }}>
                            <Text block variant="mediumPlus">API Actions</Text>
                            <br />
                            <Text block variant="small">Selected API: v{selectedApiVersion.version}</Text>
                            <br />
                            <Stack gap={8} horizontal>
                                <PrimaryButton onClick={onDeploy} iconProps={{ iconName: 'Rocket' }}>Deploy</PrimaryButton>
                                <PrimaryButton onClick={onCheckout} iconProps={{ iconName: 'Down' }}>Checkout</PrimaryButton>
                                <DefaultButton onClick={toggleIsCalloutVisible} iconProps={{ iconName: 'Cancel' }}>Dismiss</DefaultButton>
                            </Stack>
                        </div>
                    </FocusTrapCallout>
                )}
            </div>
        )
    }

    const ApiStatsPanel = () => {
        return (
            <>
                <Text variant="medium" style={{ borderBottom: '1px solid var(--themePrimary)', paddingBottom: '0.25rem' }}>
                    API Insights - v{graphApi.version}
                </Text>
                <Stack
                    horizontal
                    tokens={{
                        childrenGap: 10,
                    }}
                    style={{
                        display: 'flex',
                        margin: '1rem 0 1.5rem 0',
                        height: '4rem'
                    }}
                >
                    <div className={classes.infoStatItem}>
                        <Text variant="large" style={{ color: 'var(--themePrimary)' }}>
                            {(graphApi.totalRequestsCount == 0 ? 0 : (graphApi.avgResTime / graphApi.totalRequestsCount) || 0).toFixed(2)} ms
                        </Text>
                        <Text variant="small">Response Time</Text>
                    </div>
                    <div className={classes.infoStatItem}>
                        <Text variant="large" style={{ color: 'var(--themePrimary)' }}>
                            {graphApi.totalRequestsCount}
                        </Text>
                        <Text variant="small">Requests</Text>
                    </div>
                    <div className={classes.infoStatItem}>
                        <Text variant="large" style={{ color: 'var(--themePrimary)' }}>
                            {graphApi.errorCount || 0}
                        </Text>
                        <Text variant="small">Error Count</Text>
                    </div>
                </Stack>
            </>
        )
    }

    return (
        <>
            <div className={classes.infoPanelBody}>
                <div style={{
                    background: 'rgba(255, 255, 255, 0.03)',
                }}>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        // justifyContent: 'center',
                        position: 'relative',
                        top: '15%',
                        textAlign: 'center',
                    }}>
                        <Persona
                            initialsColor={`var(--themePrimary)`}
                            text={graphApi.name}
                            size={PersonaSize.size56}
                            hidePersonaDetails
                        />
                        <br />
                        <Text variant="xLarge">{graphApi.name}</Text>
                        <br />
                        <Text variant="mediumPlus">{graphApi.description}</Text>
                        <br />
                        <Text variant="small">v{graphApi.version}</Text>
                        {/* <br /> */}
                        {/* <Text variant="small">v{graphApi.version} - {graphApi.isActive ? 'Active' : 'Inactive'}</Text> */}
                        <br />
                        <TooltipHost content="Edit service details" id={"ss_settings"}>
                            <IconButton
                                aria-describedby={"ss_settings"}
                                iconProps={{ iconName: 'Settings' }}
                                title="Emoji"
                                ariaLabel="Emoji"
                                onClick={toggleUpdateInfoPanel}
                            />
                        </TooltipHost>
                    </div>
                    <div style={{
                        textAlign: 'center',
                        position: 'relative',
                        top: '25%',
                    }}>
                        <Text variant="small">Commit ID</Text>
                        <br />
                        <Text variant="medium">{graphApi.commitId}</Text>
                        <br />
                        <br />
                        <Text variant="small">Commit Message</Text>
                        <br />
                        <Text variant="medium">{graphApi.commitMessage}</Text>
                    </div>
                </div>
                <div style={{ padding: '0 1rem' }}>
                    <ApiStatsPanel />
                    <ApiVersionsPanel />
                </div>
            </div>

            {graphApi &&
                <Panel
                    isOpen={isUpdateInfoPanelOpen}
                    onDismiss={toggleUpdateInfoPanel}
                    closeButtonAriaLabel="Close"
                    headerText="Edit Service Information"
                >
                    <div style={{ color: '#e7e7e7' }}>
                        <UpdateApiForm dismissPanel={toggleUpdateInfoPanel} api={graphApi} />
                    </div>
                </Panel>
            }
        </>
    )
}

export default InfoPanel;
