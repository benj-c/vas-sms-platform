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
    pageContainer: {
        overflowY: 'hidden',
        position: 'relative',
    },
    pageWrapper: {
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
                return <Redirect to="/library" />
            } else {
                return <route.main {...props} />
            }
        }
    }

    const renderLoading = () => <div>Loading...</div>

    const MainLayout = (props) => (
        <main
            style={{
                height: '100vh',
                background: '#26272b',
                display: 'grid',
                gridTemplateColumns: isLoggedIn() ? '4% 96%' : '100%'
            }}
        >
            {isLoggedIn() && props.sidebar}
            <section className={classes.pageContainer}>
                {isLoggedIn() && props.topbar}
                {props.main}
            </section>
        </main>
    )

    return (
        <RecoilRoot>
            <ThemeProvider theme={theme}>
                <Router>
                    <MainLayout
                        sidebar={<SideBar />}
                        topbar={<TopBar />}
                        main={
                            <Switch>
                                {routes.map((route, i) => (
                                    <Route
                                        key={i}
                                        path={route.path}
                                        exact={route.exact}
                                        render={props => (
                                            <div style={{
                                                overflowY: isLoggedIn() ? 'hidden' : 'hidden',
                                            }}
                                                className={classes.pageWrapper}
                                            >
                                                <Suspense fallback={renderLoading()}>
                                                    {resolveComponent(props, route)}
                                                </Suspense>
                                            </div>
                                        )}
                                    />
                                ))}
                                <Redirect to="/404" />
                            </Switch>
                        }
                    />
                </Router>
            </ThemeProvider>
        </RecoilRoot>
    );
}

export default App;
