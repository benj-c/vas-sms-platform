import { useEffect, useState } from "react";
import { createTheme, getTheme } from "@fluentui/react";

import { get, persist } from "../Storage";

//consts
const LS_THEME_KEY = "theme";
const THEME_UPDATE_EVENT = "theme-update";
const SYSTEM_COLOR_SCHEME = "(prefers-color-scheme: dark)";
const THEMES = {
    DARK: "dark",
    LIGHT: "light",
    SYSTEM: "system"
}
//themes
const darkTheme = {
    themePrimary: '#00aff4',
    themeLighterAlt: '#f4fcff',
    themeLighter: '#d5f2fd',
    themeLight: '#b0e7fc',
    themeTertiary: '#64cff9',
    themeSecondary: '#1eb9f6',
    themeDarkAlt: '#009edc',
    themeDark: '#0085ba',
    themeDarker: '#006289',
    neutralLighterAlt: '#1f2023',
    neutralLighter: '#1e1f22',
    neutralLight: '#1d1e21',
    neutralQuaternaryAlt: '#1b1c1f',
    neutralQuaternary: '#1a1b1d',
    neutralTertiaryAlt: '#191a1c',
    neutralTertiary: '#c8c8c8',
    neutralSecondary: '#d0d0d0',
    neutralPrimaryAlt: '#dadada',
    neutralPrimary: '#ffffff',
    neutralDark: '#f4f4f4',
    black: '#f8f8f8',
    white: '#202124',
    tstHard: "#0a9bb6",
    tstLight: "#17525e",
    tstBg: '#ffffff1A'
};
const lightTheme = {
    themePrimary: '#0078d4',
    themeLighterAlt: '#eff6fc',
    themeLighter: '#deecf9',
    themeLight: '#c7e0f4',
    themeTertiary: '#71afe5',
    themeSecondary: '#2b88d8',
    themeDarkAlt: '#106ebe',
    themeDark: '#005a9e',
    themeDarker: '#004578',
    neutralLighterAlt: '#faf9f8',
    neutralLighter: '#f3f2f1',
    neutralLight: '#edebe9',
    neutralQuaternaryAlt: '#e1dfdd',
    neutralQuaternary: '#d0d0d0',
    neutralTertiaryAlt: '#c8c6c4',
    neutralTertiary: '#a19f9d',
    neutralSecondary: '#605e5c',
    neutralPrimaryAlt: '#3b3a39',
    neutralPrimary: '#323130',
    neutralDark: '#201f1e',
    black: '#000000',
    white: '#ffffff',
    tstHard: "#0a9bb6",
    tstLight: "#17525e",
    tstBg: '#e6f2ff'
};

const common_style_props = {
    progressColor: '#009900',
    topBarHeight: '2.5rem',
}

const readSavedThemeType = () => {
    return get(LS_THEME_KEY);
}

const useTheme = () => {
    const [theme, setTheme] = useState(getTheme());

    const toggleTheme = () => {
        let t = readSavedThemeType() === THEMES.DARK ? THEMES.LIGHT : THEMES.DARK;
        persist(LS_THEME_KEY, t);
        window.dispatchEvent(new Event(THEME_UPDATE_EVENT));
    }

    const init = () => {
        let f = readSavedThemeType(LS_THEME_KEY);
        if (!f) {
            let sysTheme = window.matchMedia(SYSTEM_COLOR_SCHEME).matches ? THEMES.DARK : THEMES.LIGHT;
            persist(LS_THEME_KEY, sysTheme);
        }
        window.dispatchEvent(new Event(THEME_UPDATE_EVENT));
    }

    useEffect(() => {
        const storageHandler = (e) => {
            if (e.type === THEME_UPDATE_EVENT) {
                let themeType = readSavedThemeType() === THEMES.DARK ? darkTheme : lightTheme;
                let th = createTheme({ palette: themeType });
                setTheme(th);

                let root = document.querySelector(":root");
                themeType = { ...themeType, ...common_style_props };
                Object.keys(themeType).forEach((k, v) => {
                    root.style.setProperty(`--${k}`, themeType[k]);
                })
            }
        }
        window.addEventListener(THEME_UPDATE_EVENT, storageHandler)
        return () => {
            window.removeEventListener(THEME_UPDATE_EVENT, storageHandler)
        }
    }, []);

    return {
        theme: theme,
        toggleTheme: toggleTheme,
        init: init,
        isDarkTheme: readSavedThemeType() === THEMES.DARK
    };
}

export default useTheme;
