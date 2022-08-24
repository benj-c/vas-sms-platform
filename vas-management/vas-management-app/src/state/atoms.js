/**
 * Recoil state atoms
 */
import { atom } from 'recoil'

export const selectedTopBarMenuAtom = atom({
    key: 'SelectedTopBarMenu',
    default: 0,
})

export const showSideBarNavAtom = atom({
    key: 'ShowSideBarNav',
    default: 0,
})

export const resetSideBarAtom = atom({
    key: 'ResetSideBar',
    default: false,
})

export const removedEdgeAtom = atom({
    key: 'RemovedEdge',
    default: null,
})

export const processedXmlAtom = atom({
    key: 'ProcessedXmlAtom',
    default: null,
})

export const selectedServiceAtom = atom({
    key: 'SelectedService',
    default: 0,
})

export const selectedActionAtom = atom({
    key: 'SelectedAction',
    default: null,
})
export const apiOfApiCreatorAtom = atom({
    key: 'apiOfApiCreator',
    default: null,
})

export const updateTopBarSubTitleAtom = atom({
    key: 'updateTopBarSubTitle',
    default: null,
})

export const apiUpdateEventAtom = atom({
    key: 'apiUpdateEvent',
    default: null,
})
