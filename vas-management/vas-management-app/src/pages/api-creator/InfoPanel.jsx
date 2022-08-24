import { IconButton, Panel, Persona, PersonaSize, Text, TooltipHost } from "@fluentui/react";
import { createUseStyles } from "react-jss";
import { useRecoilValue } from "recoil";

import useBoolean from "../../common/hooks/useBoolean";
import { apiOfApiCreatorAtom } from "../../state/atoms"
import UpdateApiForm from "../../common/forms/UpdateApiForm"

const useStyles = createUseStyles({
    infoPanelBody: {
        color: '#e7e7e7',
        display: 'grid',
        gridTemplateColumns: '20% 80%',
        height: '89vh',
        padding: '1rem',
        borderRadius: '0.35rem',
    }
})

const InfoPanel = ({ }) => {
    const classes = useStyles();
    const graphApi = useRecoilValue(apiOfApiCreatorAtom)
    const { value: isUpdateInfoPanelOpen, toggle: toggleUpdateInfoPanel, } = useBoolean(false);

    return (
        <>
            <div className={classes.infoPanelBody}>
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    background: 'rgba(255, 255, 255, 0.03)',
                }}>
                    <Persona
                        initialsColor={`var(--themePrimary)`}
                        text={graphApi.name}
                        size={PersonaSize.size56}
                        hidePersonaDetails
                    />
                    <br />
                    <Text variant="xLarge">{graphApi.name}</Text>
                    <Text variant="mediumPlus">{graphApi.description}</Text>
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
                <div></div>
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