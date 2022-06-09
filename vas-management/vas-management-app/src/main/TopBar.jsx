//lib
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { Callout, IconButton, Persona, PersonaPresence, PersonaSize, PrimaryButton } from "@fluentui/react";
import { createUseStyles } from 'react-jss';
//app
import { routes } from "./routes";
import { get } from "../common/Storage";
import { logOut } from "../common/AppUtils"
import TopBarMenu from "./TopBarMenu";
import Search from './Search';
import useTheme from "../common/hooks/useTheme"

const useStyles = createUseStyles({
    topBar: {
        background: '#1f2024',
        display: 'flex',
        alignItems: 'center',
        height: 'var(--topBarHeight)',
    },
    left: {
        '& i': {
            fontSize: '0.6rem',
            margin: '0 0.5rem'
        },
    },
    profile: {
        margin: '0 1rem 0 1rem',
        '& span': {
            textTransform: 'uppercase',
            cursor: 'pointer',
        },
        '& img': {
            height: '1.5rem',
            width: 'auto',
            borderRadius: '100%',
            cursor: 'pointer',
        },
        '& #user_detail_co': {
            cursor: 'pointer',
        }
    },
    user_detail_co: {
        padding: '0.75rem',
        border: '1px solid #777'
    },
    submitBtn: {
        marginTop: '1rem',
    },
})

const TopBar = (props) => {
    const classes = useStyles();
    const location = useLocation();
    const [currentNav, setCurrentNav] = useState();
    const [user, setUser] = useState(get('user'));
    const [isCalloutVisible, toggleIsCalloutVisible] = useState(false);
    const { theme, toggleTheme, isDarkTheme } = useTheme();

    //get nav object from routes array and put it in currentNav state var
    //this changes when location is changed
    useEffect(() => {
        let cn = routes.filter(r => r.path === location.pathname && !r.scope.includes('public'))[0];
        setCurrentNav(cn);
    }, [location])

    const showHideCallout = () => toggleIsCalloutVisible(!isCalloutVisible);

    const logOutUser = () => {
        logOut();
        window.location.reload();
    }

    return (
        <div className={classes.topBar}>
            {currentNav && (
                <>
                    <div className={classes.left}>
                        <span>{currentNav.description}</span>
                        <TopBarMenu menu={currentNav.menu} />
                    </div>
                    <Search />
                    <div className={classes.profile}>
                        {user && (
                            <Persona
                                id={'user_detail_co'}
                                text={user.name}
                                size={PersonaSize.size24}
                                onClick={showHideCallout}
                                hidePersonaDetails={true}
                            />
                        )}
                    </div>
                </>
            )}
            {isCalloutVisible && (
                <Callout
                    className={classes.user_detail_co}
                    gapSpace={0}
                    target={`#user_detail_co`}
                    onDismiss={showHideCallout}
                    setInitialFocus
                >
                    <Persona
                        // imageInitials={user.name.toUpperCase()}
                        text={user.name}
                        secondaryText={user.email}
                        size={PersonaSize.size56}
                        hidePersonaDetails={false}
                        presence={PersonaPresence.none}
                        imageAlt={user.name}
                    />
                    <PrimaryButton text="Sign Out" onClick={logOutUser} className={classes.submitBtn} />

                    <IconButton
                        iconProps={{ iconName: isDarkTheme ? "Sunny" : "ClearNight" }}
                        title={`Switch to ${isDarkTheme ? "light" : "dark"} theme`}
                        ariaLabel="Switch theme"
                        onClick={toggleTheme}
                    />
                </Callout>
            )}
        </div>
    )
}

export default TopBar;
