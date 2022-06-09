//lib
import { MessageBar, MessageBarType } from "@fluentui/react";
import { useRecoilValue } from 'recoil'
//app
import { showSnackBarAtom } from '../state/atoms'

const SnackBar = ({ type, msg }) => {
    const getType = () => {
        if (type === "error") return MessageBarType.error;
    }

    return (
        <div>
            <MessageBar
                messageBarType={getType()}
                isMultiline={false}
                dismissButtonAriaLabel="Close"
            >
                {msg}
            </MessageBar>
        </div>
    )
}

export default SnackBar;
