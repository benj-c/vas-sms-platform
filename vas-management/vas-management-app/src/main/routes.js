//lib
import { lazy } from "react"
//app
const Home = lazy(() => import(`../pages/home`))
// const Dashboard = lazy(() => import(`../pages/Dashboard`))
const Library = lazy(() => import(`../pages/library`))
const Builder = lazy(() => import(`../pages/builder`))
const Settings = lazy(() => import(`../pages/settings`))
const Login = lazy(() => import(`../pages/login`))
const F404 = lazy(() => import(`../pages/404`))
const NewFlow = lazy(() => import('../pages/newflow'))
const ApiCreator = lazy(() => import('../pages/api-creator'))
const SmsHistory = lazy(() => import('../pages/sms-history'))

let id = 0;
export const routes = [
    {
        id: 0,
        path: "/",
        name: "Login",
        exact: true,
        main: () => <Login />,
        sideBarProps: {
            hidden: true,
        },
        scope: ["public"],
    },
    {
        id: 1,
        path: "/login",
        name: "Login",
        exact: true,
        main: () => <Login />,
        sideBarProps: {
            hidden: true,
        },
        scope: ["public"],
    },
    // {
    //     id: 2,
    //     path: "/home",
    //     name: "Home",
    //     description: "Home",
    //     exact: true,
    //     main: () => <Home />,
    //     scope: [],
    //     sideBarProps: {
    //         pos: "top",
    //         icon: "HomeSolid",
    //     },
    //     menu: []
    // },
    {
        id: 3,
        path: "/library",
        name: "Library",
        description: "Service Library",
        exact: true,
        main: () => <Library />,
        scope: [],
        sideBarProps: {
            pos: "top",
            icon: "Library"
        },
        menu: [
            {
                id: 0,
                title: "Services",
            },
            {
                id: 1,
                title: "APIs",
            },
        ]
    },
    {
        id: 4,
        path: "/service-creator",
        name: "New",
        description: "Service Creator",
        exact: true,
        main: () => <NewFlow />,
        scope: [],
        sideBarProps: {
            pos: "top",
            icon: "Add"
        },
        menu: []
    },
    {
        id: 5,
        path: "/builder",
        name: "Builder",
        description: "Service Builder",
        exact: false,
        main: () => <Builder />,
        scope: [],
        sideBarProps: {
            hidden: true,
            pos: "top",
            icon: "Flow"
        },
        menu: [
        ]
    },
    {
        id: 6,
        path: "/settings",
        name: "Settings",
        description: "Settings",
        exact: true,
        main: () => <Settings />,
        scope: [],
        sideBarProps: {
            pos: "bottom",
            icon: "Settings",
        },
        menu: [
            {
                id: 0,
                title: "Hello",
            },
            {
                id: 1,
                title: "World",
            },
        ]
    },
    {
        id: 7,
        path: "/api-creator",
        name: "api creator",
        description: "API Creator",
        exact: true,
        main: () => <ApiCreator />,
        scope: [],
        sideBarProps: {
            hidden: true,
            pos: "top",
            icon: "Add"
        },
        menu: [
            {
                id: 0,
                title: "Designer",
            },
            {
                id: 1,
                title: "Code",
            },
        ]
    },
    {
        id: 8,
        path: "/sms-history",
        name: "History",
        description: "SMS History Viewer",
        exact: true,
        main: () => <SmsHistory />,
        scope: [],
        sideBarProps: {
            pos: "top",
            icon: "Message"
        },
        menu: []
    },
    {
        id: 999,
        path: "/404",
        name: "404",
        main: () => <F404 />,
        scope: ["public"],
        sideBarProps: {
            hidden: true,
        },
    },
];
