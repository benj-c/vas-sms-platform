import AssignNodePropPanel from './property-panels/AssignNodePropPanel'
import CaseDefaultPropsPanel from "./property-panels/CaseDefaultPropsPanel";
import FunctionNodePropPanel from "./property-panels/FunctionNodePropPanel";

export const CORE_NODES = [
    {
        id: 0,
        title: 'assign',
        type: 'customNode',
        icon: 'Variable',
        propsComponent: AssignNodePropPanel,
    },
    {
        id: 1,
        title: 'branch',
        type: 'customNode',
        icon: 'BranchFork2',
        propsComponent: () => <div>Switch</div>,
    },
    {
        id: 2,
        title: 'case',
        type: 'customNode',
        icon: 'Remote',
        propsComponent: CaseDefaultPropsPanel,
    },
    {
        id: 3,
        title: 'default',
        type: 'customNode',
        icon: 'Remote',
        propsComponent: CaseDefaultPropsPanel,
    },
]

export const FUNCTIONS = [
    {
        id: 1,
        title: 'func',
        functionType: 'http',
        props: {
            class: 'rest',
            methods: ["get", "post", "put", "delete", "patch"],
            fields: [
                {
                    name: 'url',
                    type: 'text'
                },
                {
                    name: 'body',
                    type: 'textarea'
                },
            ]
        },
        type: 'customNode',
        icon: 'Remote',
        propsComponent: FunctionNodePropPanel,
    },
    {
        id: 2,
        title: 'func',
        functionType: 'invoke',
        type: 'customNode',
        icon: 'Remote',
        propsComponent: FunctionNodePropPanel,
    },
    {
        id: 3,
        title: 'func',
        functionType: 'email',
        type: 'customNode',
        icon: 'Mail',
        propsComponent: FunctionNodePropPanel,
    },
    {
        id: 4,
        title: 'func',
        functionType: 'sms',
        type: 'customNode',
        icon: 'Message',
        propsComponent: FunctionNodePropPanel,
    },
]
