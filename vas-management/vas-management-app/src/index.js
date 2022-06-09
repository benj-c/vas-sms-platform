//lib
import React from 'react';
import ReactDOM from 'react-dom';
import { initializeIcons } from '@fluentui/font-icons-mdl2';
//app
import "./main/app.css"
import App from './main/App';

initializeIcons();

ReactDOM.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
    document.getElementById('root')
);
