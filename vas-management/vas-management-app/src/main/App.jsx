//lib
import React, { useEffect, Suspense } from "react";
import { BrowserRouter as Router, Route, Switch, Redirect } from "react-router-dom";
import { ThemeProvider } from "@fluentui/react";
import { createUseStyles } from 'react-jss';
import { RecoilRoot } from 'recoil';
//app
import { routes } from "./routes";
import { get } from "../common/Storage";
import { isLoggedIn } from "../common/AppUtils"
import useTheme from "../common/hooks/useTheme";
import SideBar from "./SideBar";
import TopBar from "./TopBar";

const useStyles = createUseStyles({
    '@keyframes animateUp': {
        from: {
            transform: 'translateY(2rem)'
        },
        to: {
            transform: 'translateY(0)',
        }
    },
    layout: {
        height: '100vh',
        background: '#26272b'
    },
    container: {
        display: 'grid',
        gridTemplateColumns: '4% 96%'
    },
    pageContainer: {
        overflowY: 'hidden',
        position: 'relative',
    },
    pageWrapper: {
        overflowY: 'auto',
        height: 'calc(100vh - var(--topBarHeight))',
        animation: '$animateUp 0.2s forwards',
    },
})

const App = () => {
    const classes = useStyles();
    const { theme, init } = useTheme();

    useEffect(() => {
        init();
    }, [])

    const resolveComponent = (props, route) => {
        let user = get('user');
        let isPrivateRoute = !route.scope.includes("public");
        let loggedIn = isLoggedIn();
        if (isPrivateRoute) {
            if (loggedIn) {
                //check if user has access to page
                return <route.main {...props} />
            } else {
                return <Redirect to="/login" />
            }
        } else {
            if (loggedIn) {
                //check if user has access to page
                return <Redirect to="/home" />
            } else {
                return <route.main {...props} />
            }
        }
    }

    const renderLoading = () => <div>Loading...</div>

    const MainLayout = () => (
        <main className={`${classes.layout} ${isLoggedIn() ? classes.container : ''}`}>
            {isLoggedIn() && <SideBar />}
            <section className={classes.pageContainer}>
                {isLoggedIn() && <TopBar />}
                <Switch>
                    {routes.map((route, i) => (
                        <Route
                            key={i}
                            path={route.path}
                            exact={route.exact}
                            render={props => (
                                <div className={classes.pageWrapper}>
                                    <Suspense fallback={renderLoading()}>
                                        {resolveComponent(props, route)}
                                    </Suspense>
                                </div>
                            )}
                        />
                    ))}
                    <Redirect to="/404" />
                </Switch>
            </section>
        </main>
    )

    return (
        <RecoilRoot>
            <ThemeProvider theme={theme}>
                <Router>
                    <MainLayout />
                </Router>
            </ThemeProvider>
        </RecoilRoot>
    );
}

export default App;
