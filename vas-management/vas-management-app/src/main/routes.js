//lib
import { lazy } from "react"
//app
const Home = lazy(() => import(`../pages/home`))
const ApiGallary = lazy(() => import(`../pages/api-page`))
const Library = lazy(() => import(`../pages/services-page`))
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
    {
        id: 2,
        path: "/home",
        name: "Home",
        description: "Home",
        exact: true,
        main: () => <Home />,
        scope: [],
        sideBarProps: {
            pos: "top",
            icon: "HomeSolid",
        },
        menu: []
    },
    {
        id: 3,
        path: "/services",
        name: "Services",
        description: "Service Library",
        exact: true,
        main: () => <Library />,
        scope: [],
        sideBarProps: {
            pos: "top",
            icon: "Library"
        },
        menu: [
            // {
            //     id: 0,
            //     title: "Services",
            // },
            // {
            //     id: 1,
            //     title: "APIs",
            // },
        ]
    },
    {
        id: 4,
        path: "/apis",
        name: "APIs",
        description: "API Gallary",
        exact: true,
        main: () => <ApiGallary />,
        scope: [],
        sideBarProps: {
            pos: "top",
            icon: "Embed"
        },
        menu: []
    },
    {
        id: 5,
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
        id: 6,
        path: "/service",
        name: "Service",
        description: "Service Information",
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
        id: 7,
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
        id: 8,
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
                title: "Details",
            },
            {
                id: 1,
                title: "Design View",
            },
        ]
    },
    {
        id: 9,
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
