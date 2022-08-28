//lib
import { ActionButton, IconButton, Panel, TooltipHost } from "@fluentui/react";
import { useBoolean } from "@fluentui/react-hooks";
import { memo, useEffect, useRef, useState } from "react";
import { createUseStyles } from "react-jss";
import { useHistory } from "react-router-dom";
//app
import { getServiceById } from '../../common/ApiHandler'
import { options } from "../../common/HttpClient"
import UpdateServiceForm from "../../common/forms/UpdateServiceForm";

const useStyles = createUseStyles({
    serviceDetail: {
        position: 'relative',
        background: 'rgba(255, 255, 255, 0.03)',
        display: 'flex',
        alignItems: 'center',
        textAlign: 'center',
        flexDirection: 'column',
        padding: '1rem',
        borderRadius: '0.35rem',
        height: '85vh',
        '& h2': {
            textTransform: 'uppercase',
            height: '4rem',
            width: '4rem',
            lineHeight: '4.1rem',
            borderRadius: '100%',
            background: 'var(--themePrimary)',
            marginBottom: '1rem',
            marginTop: '2rem',
            fontSize: '1.2rem',
            fontWeight: '600'
        },
        '& h3': {
            marginBottom: '0.5rem',
            fontSize: '1.2rem',
        },
        '& p': {
            marginBottom: '0.5rem',
            color: '#e7e7e7',
        }
    },
    verPill: {
        padding: '0 0.2rem',
        '& i': {
            color: 'var(--themePrimary)',
        },
        '& span': {
            marginLeft: '1rem',
            textDecoration: 'underline',
            cursor: 'pointer',
            '&:hover': {
                textDecorationColor: 'var(--themePrimary)',
                color: 'var(--themePrimary)'
            }
        }
    },
})

const InfoPanel = ({ serviceId }) => {
    const classes = useStyles();
    const history = useHistory();
    const fileDownloadRef = useRef();
    const [isOpen, { setTrue: openPanel, setFalse: dismissPanel }] = useBoolean(false);
    const [service, setService] = useState(null);

    useEffect(() => {
        loadService(serviceId);
    }, [serviceId])

    const loadService = (serviceId) => {
        getServiceById(serviceId).then(res => {
            setService(res.data);
        })
            .catch(e => {
                console.log(e)
            })
            .finally(() => { });
    }

    const exportXml = () => {
        let url = `${options.apiEndpoint}/service/${serviceId}/download`;
        let ops = {
            method: "GET",
            headers: {
                "Authorization": options.apiKey,
            },
        };
        fetch(url, ops)
            .then(res => {
                return res.blob();
            })
            .then((blob) => {
                const href = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = href;
                link.setAttribute('download', `${service.name}-${service.id}.xml`);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            })
            .catch(e => {
                console.log(e)
            })
            .finally(() => { });
    }

    const onUpdate = (serviceId) => {
        loadService(serviceId);
    }

    return (
        <>
            {service && (
                <>
                    <div className={classes.serviceDetail}>
                        <h2>{service.name.substring(0, 2)}</h2>
                        <h3>{service.name}</h3>
                        <p>{service.description}</p>
                        <span className={classes.verPill}>
                            {service.active ? 'Active' : 'Inactive'}
                        </span>

                        <div style={{ position: "absolute", bottom: '0.5rem' }}>
                            <br />
                            <br />
                            <span>SMS to send if the service is disabled</span>
                            <br />
                            <span style={{ textDecoration: 'underline' }}>{service.disabledSms}</span>
                            <br />
                            <br />
                            <span>Published On <b>{service.createdDate}</b> by <b>CRM</b></span>
                            <br />
                            <ActionButton
                                iconProps={{ iconName: 'ChromeBack' }}
                                style={{ marginTop: '2rem' }}
                                onClick={() => history.push("/services")}
                            >
                                Services
                            </ActionButton>
                            <TooltipHost content="Service settings" id={"ss_settings"}>
                                <IconButton
                                    aria-describedby={"ss_settings"}
                                    iconProps={{ iconName: 'Settings' }}
                                    title="Emoji"
                                    ariaLabel="Emoji"
                                    onClick={openPanel}
                                />
                            </TooltipHost>
                            <TooltipHost content="Export" id={"ss_share"}>
                                <IconButton
                                    aria-describedby={"ss_share"}
                                    iconProps={{ iconName: 'Share' }}
                                    title="Emoji"
                                    ariaLabel="Emoji"
                                    onClick={exportXml}
                                />
                            </TooltipHost>
                        </div>
                        <a ref={fileDownloadRef} style={{display: 'none'}} />
                    </div>
                    <Panel
                        isOpen={isOpen}
                        onDismiss={dismissPanel}
                        headerText="Update service details"
                        closeButtonAriaLabel="Close"
                        isFooterAtBottom={true}
                    >
                        <UpdateServiceForm {...service} dismissPanel={dismissPanel} onUpdate={onUpdate} />
                    </Panel>
                </>
            )}

        </>
    )
}

export default memo(InfoPanel);
